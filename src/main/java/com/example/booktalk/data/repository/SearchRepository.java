package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import com.example.booktalk.data.dao.BookDao;
import com.example.booktalk.data.dao.SearchDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
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
 * Repository for search operations
 */
public class SearchRepository {
    private static final long CACHE_TTL = 7 * 24 * 60 * 60 * 1000L; // 7 days in milliseconds
    
    private AppDbHelper dbHelper;
    private SearchDao searchDao;
    private BookDao bookDao;
    private BooksApiService apiService;
    private ExecutorService executorService;
    private Handler mainHandler;
    
    public interface SearchCallback {
        void onSearchResults(List<Book> books);
        void onError(String error);
    }
    
    public SearchRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.searchDao = new SearchDao(db);
        this.bookDao = new BookDao(db);
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Search books (offline-first, async)
     */
    public void searchBooksAsync(String query, SearchCallback callback) {
        if (query == null || query.trim().isEmpty()) {
            if (callback != null) {
                mainHandler.post(() -> callback.onSearchResults(new java.util.ArrayList<>()));
            }
            return;
        }
        
        String normalizedQuery = query.trim().toLowerCase();
        String originalQuery = query.trim();
        
        executorService.execute(() -> {
            // Check cache first
            long cachedFetchedAt = searchDao.getSearchCacheFetchedAt(normalizedQuery);
            long now = System.currentTimeMillis();
            boolean isCacheFresh = cachedFetchedAt > 0 && (now - cachedFetchedAt) < CACHE_TTL;
            
            if (isCacheFresh) {
                // Return cached results
                List<Book> cachedBooks = searchDao.getCachedSearchResults(normalizedQuery);
                if (callback != null) {
                    mainHandler.post(() -> callback.onSearchResults(cachedBooks));
                }
                return;
            }
            
            // Fetch from API
            try {
                String apiKey = RetrofitClient.getApiKey();
                
                // Step 1: Try primary strict query (intitle:)
                String primaryQuery = QueryBuilder.buildSearchPrimaryQuery(originalQuery);
                android.util.Log.d("SearchRepository", "Original query: " + originalQuery);
                android.util.Log.d("SearchRepository", "Primary query (intitle:): " + primaryQuery);
                
                Call<BooksResponse> call = apiService.searchBooks(
                    primaryQuery,
                    QueryBuilder.getDefaultMaxResults(),
                    QueryBuilder.getDefaultPrintType(),
                    QueryBuilder.getDefaultOrderBy(),
                    QueryBuilder.getDefaultLang(),
                    QueryBuilder.getDefaultCountry(), // "US"
                    null, // startIndex
                    apiKey
                );
                Response<BooksResponse> response = call.execute();
                
                List<Book> books = new java.util.ArrayList<>();
                
                // Check if primary query returned good results
                if (response.isSuccessful() && response.body() != null) {
                    BooksResponse booksResponse = response.body();
                    if (booksResponse.getItems() != null && !booksResponse.getItems().isEmpty()) {
                        books = BookMapper.toBookList(booksResponse.getItems());
                        android.util.Log.d("SearchRepository", "Primary query returned " + books.size() + " results");
                    }
                }
                
                // Step 2: Fallback if primary query returned too few results
                // MIN_RESULTS threshold: if less than 10, try fallback
                final int MIN_RESULTS = 10;
                if (books.size() < MIN_RESULTS) {
                    android.util.Log.d("SearchRepository", "Primary query returned too few results, trying fallback");
                    String fallbackQuery = QueryBuilder.buildSearchFallbackQuery(originalQuery);
                    android.util.Log.d("SearchRepository", "Fallback query: " + fallbackQuery);
                    
                    call = apiService.searchBooks(
                        fallbackQuery,
                        QueryBuilder.getDefaultMaxResults(),
                        QueryBuilder.getDefaultPrintType(),
                        QueryBuilder.getDefaultOrderBy(),
                        QueryBuilder.getDefaultLang(),
                        QueryBuilder.getDefaultCountry(), // "US"
                        null,
                        apiKey
                    );
                    response = call.execute();
                    
                    if (response.isSuccessful() && response.body() != null) {
                        BooksResponse booksResponse = response.body();
                        if (booksResponse.getItems() != null && !booksResponse.getItems().isEmpty()) {
                            books = BookMapper.toBookList(booksResponse.getItems());
                            android.util.Log.d("SearchRepository", "Fallback query returned " + books.size() + " results");
                        }
                    }
                }
                
                // Step 3: Apply strict completeness filtering and ranking by ratings
                if (!books.isEmpty()) {
                    android.util.Log.d("SearchRepository", "Initial results: " + books.size() + " books");
                    
                    // Step 3a: Immediate filter - drop items missing title/author/thumbnail
                    List<Book> candidatesWithBasicData = new java.util.ArrayList<>();
                    for (Book book : books) {
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
                    
                    android.util.Log.d("SearchRepository", "After basic filter (title/author/thumbnail): " + candidatesWithBasicData.size() + " books");
                    
                    // Step 3b: Rank by ratings BEFORE fetching details (to limit detail API calls)
                    List<Book> rankedCandidates = BookQualityFilter.rankBooksByRatings(candidatesWithBasicData);
                    
                    // Step 3c: Fetch details for books missing description (limit to top K=30 candidates for search)
                    int maxDetailCalls = 30; // More for search since user is actively searching
                    int detailCallsMade = 0;
                    int targetCompleteBooks = 20; // Show top 20 complete books
                    
                    List<Book> completeBooks = new java.util.ArrayList<>();
                    
                    for (Book book : rankedCandidates) {
                        // If we already have enough complete books, stop
                        if (completeBooks.size() >= targetCompleteBooks) {
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
                                android.util.Log.w("SearchRepository", "Failed to fetch detail for " + book.getBookId() + ": " + e.getMessage());
                            }
                        }
                        
                        // Step 3d: Apply STRICT completeness filter (all 4 fields required)
                        if (BookQualityFilter.isCompleteBook(book)) {
                            completeBooks.add(book);
                        }
                    }
                    
                    android.util.Log.d("SearchRepository", "Complete books found: " + completeBooks.size() + " (made " + detailCallsMade + " detail API calls)");
                    
                    // Step 3e: Final ranking by ratings (in case fetching details changed order)
                    List<Book> finalRankedBooks = BookQualityFilter.rankBooksByRatings(completeBooks);
                    
                    // Step 3f: Take top 20 complete books (ranked by ratings)
                    int topN = 20;
                    List<Book> topBooks = finalRankedBooks.size() > topN 
                        ? finalRankedBooks.subList(0, topN) 
                        : finalRankedBooks;
                    
                    android.util.Log.d("SearchRepository", "Final top " + topBooks.size() + " complete books (ranked by ratings)");
                    
                    // Make final for lambda
                    final List<Book> finalBooks = topBooks;
                        
                    // Upsert complete books to cache (with description)
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    BookDao bookDao = new BookDao(db);
                    SearchDao searchDao = new SearchDao(db);
                    
                    for (Book book : finalBooks) {
                        bookDao.upsertBook(book);
                    }
                    
                    // Clear old search cache
                    searchDao.deleteSearchCacheBooks(normalizedQuery);
                    
                    // Insert new search cache with sort_order (ranked by ratings)
                    searchDao.upsertSearchCache(normalizedQuery, now);
                    for (int i = 0; i < finalBooks.size(); i++) {
                        searchDao.insertSearchCacheBook(normalizedQuery, finalBooks.get(i).getBookId(), i);
                    }
                    
                    if (callback != null) {
                        mainHandler.post(() -> callback.onSearchResults(finalBooks));
                    }
                    return;
                }
                
                // If no results, return cached (even if stale)
                List<Book> cachedBooks = searchDao.getCachedSearchResults(normalizedQuery);
                if (callback != null) {
                    mainHandler.post(() -> callback.onSearchResults(cachedBooks));
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Return cached results even if stale on error
                List<Book> cachedBooks = searchDao.getCachedSearchResults(normalizedQuery);
                if (callback != null) {
                    mainHandler.post(() -> {
                        if (cachedBooks.isEmpty()) {
                            callback.onError("Search failed: " + e.getMessage());
                        } else {
                            callback.onSearchResults(cachedBooks);
                        }
                    });
                }
            }
        });
    }
    
    /**
     * Synchronous search (for backward compatibility, but should not be used on main thread)
     */
    @Deprecated
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        String normalizedQuery = query.trim().toLowerCase();
        List<Book> cachedBooks = searchDao.getCachedSearchResults(normalizedQuery);
        return cachedBooks;
    }
}

