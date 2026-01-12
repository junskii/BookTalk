package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import com.example.booktalk.data.dao.BookDao;
import com.example.booktalk.data.dao.HomeDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Category;
import com.example.booktalk.data.remote.BookMapper;
import com.example.booktalk.data.remote.BookQualityFilter;
import com.example.booktalk.data.remote.BooksApiService;
import com.example.booktalk.data.remote.BooksResponse;
import com.example.booktalk.data.remote.QueryBuilder;
import com.example.booktalk.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for home category operations
 */
public class HomeRepository {
    private static final long CACHE_TTL = 7 * 24 * 60 * 60 * 1000L; // 7 days in milliseconds
    
    private AppDbHelper dbHelper;
    private HomeDao homeDao;
    private BookDao bookDao;
    private BooksApiService apiService;
    private ExecutorService executorService;
    private Handler mainHandler;
    
    public interface CategoriesUpdateCallback {
        void onCategoriesUpdated(List<Category> categories);
    }
    
    public HomeRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.homeDao = new HomeDao(db);
        this.bookDao = new BookDao(db);
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Get all categories with books (offline-first)
     */
    public List<Category> getCategories() {
        List<Category> categories = homeDao.getAllCategories();
        
        // Load books for each category
        for (Category category : categories) {
            List<Book> books = homeDao.getCategoryBooks(category.getCategoryId());
            category.setBooks(books);
            
            // Check if cache is stale or empty
            long now = System.currentTimeMillis();
            boolean isStale = (now - category.getFetchedAt()) > CACHE_TTL;
            boolean isEmpty = books == null || books.isEmpty();
            
            if (isStale || isEmpty) {
                // Fetch from API in background
                fetchCategoryBooksAsync(category, null);
            }
        }
        
        return categories;
    }
    
    /**
     * Get categories with callback for async updates
     */
    public void getCategoriesAsync(CategoriesUpdateCallback callback) {
        executorService.execute(() -> {
            List<Category> categories = homeDao.getAllCategories();
            
            // Load books for each category from cache first
            for (Category category : categories) {
                List<Book> books = homeDao.getCategoryBooks(category.getCategoryId());
                category.setBooks(books);
            }
            
            // Return initial cached data immediately
            if (callback != null) {
                mainHandler.post(() -> callback.onCategoriesUpdated(categories));
            }
            
            // Then fetch fresh data for each category that needs it
            for (Category category : categories) {
                long now = System.currentTimeMillis();
                boolean isStale = (now - category.getFetchedAt()) > CACHE_TTL;
                boolean isEmpty = category.getBooks() == null || category.getBooks().isEmpty();
                
                if (isStale || isEmpty) {
                    // Fetch from API in background (sequential to avoid race conditions)
                    fetchCategoryBooksAsync(category, callback);
                }
            }
        });
    }
    
    /**
     * Fetch books for a category from API (async)
     * STRICT REQUIREMENT: Only show books with complete data (title, author, thumbnail, description)
     * RANKING: Order by highest-rated books (ratingsCount DESC, averageRating DESC)
     */
    private void fetchCategoryBooksAsync(Category category, CategoriesUpdateCallback callback) {
        executorService.execute(() -> {
            try {
                int categoryId = category.getCategoryId();
                String categoryName = category.getName();
                String queryHint = category.getQueryHint();
                String apiKey = RetrofitClient.getApiKey();
                
                android.util.Log.d("HomeRepository", "=== Fetching for category: " + categoryName + " (ID: " + categoryId + ") ===");
                
                // Build query using QueryBuilder
                String query = QueryBuilder.buildHomeCategoryQuery(queryHint);
                android.util.Log.d("HomeRepository", "Query: " + query);
                
                // STEP 1: Fetch enough candidates (40 books, optionally 2 pages)
                List<Book> allBooks = new java.util.ArrayList<>();
                
                // Fetch first page (40 results)
                Call<BooksResponse> call = apiService.searchBooks(
                    query,
                    QueryBuilder.getDefaultMaxResults(), // 40
                    QueryBuilder.getDefaultPrintType(),  // "books"
                    QueryBuilder.getDefaultOrderBy(),    // "relevance"
                    QueryBuilder.getDefaultLang(),       // "en"
                    QueryBuilder.getDefaultCountry(),    // "US"
                    null, // startIndex = 0 (first page)
                    apiKey
                );
                Response<BooksResponse> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    BooksResponse booksResponse = response.body();
                    if (booksResponse.getItems() != null && !booksResponse.getItems().isEmpty()) {
                        List<Book> books = BookMapper.toBookList(booksResponse.getItems());
                        allBooks.addAll(books);
                        android.util.Log.d("HomeRepository", "Fetched " + books.size() + " books from first page");
                    }
                }
                
                // Optional: Fetch second page if needed (startIndex=40)
                if (allBooks.size() < 40) {
                    try {
                        Call<BooksResponse> call2 = apiService.searchBooks(
                            query,
                            QueryBuilder.getDefaultMaxResults(),
                            QueryBuilder.getDefaultPrintType(),
                            QueryBuilder.getDefaultOrderBy(),
                            QueryBuilder.getDefaultLang(),
                            QueryBuilder.getDefaultCountry(), // "US"
                            40, // startIndex = 40 (second page)
                            apiKey
                        );
                        Response<BooksResponse> response2 = call2.execute();
                        if (response2.isSuccessful() && response2.body() != null) {
                            BooksResponse booksResponse2 = response2.body();
                            if (booksResponse2.getItems() != null && !booksResponse2.getItems().isEmpty()) {
                                List<Book> books2 = BookMapper.toBookList(booksResponse2.getItems());
                                allBooks.addAll(books2);
                                android.util.Log.d("HomeRepository", "Fetched " + books2.size() + " books from second page");
                            }
                        }
                    } catch (IOException e) {
                        android.util.Log.w("HomeRepository", "Failed to fetch second page: " + e.getMessage());
                    }
                }
                
                android.util.Log.d("HomeRepository", "Total fetched: " + allBooks.size() + " books");
                
                // STEP 2: Immediate filter - drop items missing title/author/thumbnail
                List<Book> candidatesWithBasicData = new java.util.ArrayList<>();
                for (Book book : allBooks) {
                    // Must have title
                    if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                        continue;
                    }
                    // Must have author
                    if (book.getAuthor() == null || book.getAuthor().trim().isEmpty() || 
                        book.getAuthor().equals("Unknown Author")) {
                        continue;
                    }
                    // Must have thumbnail
                    if (book.getCoverUrl() == null || book.getCoverUrl().trim().isEmpty()) {
                        continue;
                    }
                    candidatesWithBasicData.add(book);
                }
                
                android.util.Log.d("HomeRepository", "After basic filter (title/author/thumbnail): " + candidatesWithBasicData.size() + " books");
                
                // STEP 3: Rank by ratings BEFORE fetching details (to limit detail API calls)
                List<Book> rankedCandidates = BookQualityFilter.rankBooksByRatings(candidatesWithBasicData);
                
                // STEP 4: Fetch details for books missing description (limit to top K=20 candidates)
                int maxDetailCalls = 20; // Limit to avoid too many network calls
                int detailCallsMade = 0;
                int completeBooksFound = 0;
                int targetCompleteBooks = 12; // Need 12 complete books per category
                
                List<Book> completeBooks = new java.util.ArrayList<>();
                
                for (Book book : rankedCandidates) {
                    // If we already have enough complete books, stop
                    if (completeBooksFound >= targetCompleteBooks) {
                        break;
                    }
                    
                    // Check if description is missing
                    boolean needsDescription = (book.getDescription() == null || book.getDescription().trim().isEmpty());
                    
                    if (needsDescription && detailCallsMade < maxDetailCalls) {
                        // Fetch detail API to get description
                        try {
                            Call<com.example.booktalk.data.remote.VolumeResponse> detailCall = 
                                apiService.getBookDetails(book.getBookId(), QueryBuilder.getDefaultCountry(), apiKey);
                            Response<com.example.booktalk.data.remote.VolumeResponse> detailResponse = detailCall.execute();
                            
                            if (detailResponse.isSuccessful() && detailResponse.body() != null) {
                                com.example.booktalk.data.remote.VolumeResponse volumeResponse = detailResponse.body();
                                if (volumeResponse.getVolumeInfo() != null) {
                                    String description = volumeResponse.getVolumeInfo().getDescription();
                                    if (description != null && !description.trim().isEmpty()) {
                                        book.setDescription(description);
                                    }
                                }
                            }
                            detailCallsMade++;
                        } catch (IOException e) {
                            android.util.Log.w("HomeRepository", "Failed to fetch detail for " + book.getBookId() + ": " + e.getMessage());
                        }
                    }
                    
                    // STEP 5: Apply STRICT completeness filter (all 4 fields required)
                    if (BookQualityFilter.isCompleteBook(book)) {
                        completeBooks.add(book);
                        completeBooksFound++;
                    }
                }
                
                android.util.Log.d("HomeRepository", "Complete books found: " + completeBooks.size() + " (made " + detailCallsMade + " detail API calls)");
                
                // STEP 6: Final ranking by ratings (in case fetching details changed order)
                List<Book> finalRankedBooks = BookQualityFilter.rankBooksByRatings(completeBooks);
                
                // STEP 7: Take top N = 12 complete books
                int topN = 12;
                List<Book> topBooks = finalRankedBooks.size() > topN 
                    ? finalRankedBooks.subList(0, topN) 
                    : finalRankedBooks;
                
                android.util.Log.d("HomeRepository", "Final top " + topBooks.size() + " complete books for " + categoryName);
                
                if (!topBooks.isEmpty()) {
                    // STEP 8: Cache into SQLite
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    BookDao bookDao = new BookDao(db);
                    HomeDao homeDao = new HomeDao(db);
                    
                    // Upsert all complete books (with description)
                    for (Book book : topBooks) {
                        bookDao.upsertBook(book);
                    }
                    android.util.Log.d("HomeRepository", "Saved " + topBooks.size() + " complete books to database");
                    
                    // Clear old category books FIRST before inserting new ones
                    homeDao.deleteCategoryBooks(categoryId);
                    android.util.Log.d("HomeRepository", "Cleared old books for category " + categoryName);
                    
                    // Insert new category books with sort_order (0..N-1)
                    for (int i = 0; i < topBooks.size(); i++) {
                        Book book = topBooks.get(i);
                        homeDao.insertCategoryBook(categoryId, book.getBookId(), i);
                    }
                    
                    // Update fetched_at timestamp
                    long now = System.currentTimeMillis();
                    homeDao.updateCategoryFetchedAt(categoryId, now);
                    category.setFetchedAt(now);
                    category.setBooks(topBooks);
                    
                    // Update UI on main thread - reload all categories to ensure consistency
                    if (callback != null) {
                        // Reload all categories from database to get latest data
                        List<Category> updatedCategories = homeDao.getAllCategories();
                        for (Category cat : updatedCategories) {
                            // Reload books for each category from database
                            List<Book> catBooks = homeDao.getCategoryBooks(cat.getCategoryId());
                            cat.setBooks(catBooks);
                            android.util.Log.d("HomeRepository", "Final check - Category " + cat.getName() + " (ID: " + cat.getCategoryId() + ") has " + (catBooks != null ? catBooks.size() : 0) + " books");
                        }
                        mainHandler.post(() -> callback.onCategoriesUpdated(updatedCategories));
                    }
                } else {
                    android.util.Log.d("HomeRepository", "No complete books found for " + categoryName);
                }
            } catch (IOException e) {
                android.util.Log.e("HomeRepository", "Error fetching books for " + category.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Seed initial categories (call once on first launch)
     */
    public void seedCategories() {
        List<Category> existing = homeDao.getAllCategories();
        if (!existing.isEmpty()) {
            return; // Already seeded
        }
        
        // Use subject queries (Google Books API standard)
        Category romance = new Category("Romance", "subject:romance");
        romance.setFetchedAt(0);
        
        Category scifi = new Category("Science Fiction", "subject:science fiction");
        scifi.setFetchedAt(0);
        
        Category nonfiction = new Category("Non-fiction", "subject:nonfiction");
        nonfiction.setFetchedAt(0);
        
        Category selfhelp = new Category("Self Development", "subject:self-help");
        selfhelp.setFetchedAt(0);
        
        homeDao.upsertCategory(romance);
        homeDao.upsertCategory(scifi);
        homeDao.upsertCategory(nonfiction);
        homeDao.upsertCategory(selfhelp);
        
        android.util.Log.d("HomeRepository", "Seeded 4 categories");
    }
}

