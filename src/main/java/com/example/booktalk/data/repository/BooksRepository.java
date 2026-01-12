package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import com.example.booktalk.data.dao.BookDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.remote.BookMapper;
import com.example.booktalk.data.remote.RetrofitClient;
import com.example.booktalk.data.remote.BooksApiService;
import com.example.booktalk.data.remote.VolumeResponse;
import retrofit2.Call;
import retrofit2.Response;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for book operations
 */
public class BooksRepository {
    private AppDbHelper dbHelper;
    private BookDao bookDao;
    private BooksApiService apiService;
    private ExecutorService executorService;
    private Handler mainHandler;
    
    public interface BookCallback {
        void onBookLoaded(Book book);
        void onError(String error);
    }
    
    public BooksRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.bookDao = new BookDao(db);
        this.apiService = RetrofitClient.getInstance().getApiService();
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }
    
    /**
     * Get book by ID (offline-first: check cache first, then API if description missing) - ASYNC
     */
    public void getBookByIdAsync(String bookId, BookCallback callback) {
        executorService.execute(() -> {
            // Always read from cache first
            Book book = bookDao.getBookById(bookId);
            
            // Update last opened timestamp
            if (book != null) {
                bookDao.updateLastOpenedAt(bookId);
            }
            
            // Return cached book immediately if available
            if (book != null) {
                final Book finalBook = book;
                if (callback != null) {
                    mainHandler.post(() -> callback.onBookLoaded(finalBook));
                }
                
                // If description is missing, fetch from API in background
                if (book.getDescription() == null || book.getDescription().isEmpty()) {
                    fetchBookDetailsAsync(bookId, callback);
                }
            } else {
                // Book not in cache, try to fetch from API
                fetchBookDetailsAsync(bookId, callback);
            }
        });
    }
    
    /**
     * Fetch book details from API (async)
     */
    private void fetchBookDetailsAsync(String bookId, BookCallback callback) {
        executorService.execute(() -> {
            try {
                            Call<VolumeResponse> call = apiService.getBookDetails(
                                bookId, 
                                com.example.booktalk.data.remote.QueryBuilder.getDefaultCountry(), 
                                RetrofitClient.getApiKey()
                            );
                Response<VolumeResponse> response = call.execute();
                
                if (response.isSuccessful() && response.body() != null) {
                    VolumeResponse volumeResponse = response.body();
                    if (volumeResponse.getVolumeInfo() != null) {
                        // Convert to Book model
                        Book book = BookMapper.toBook(
                                new com.example.booktalk.data.remote.VolumeItem() {{
                                    setId(volumeResponse.getId());
                                    setVolumeInfo(volumeResponse.getVolumeInfo());
                                }}
                        );
                        
                        if (book != null) {
                            // Upsert full book to cache
                            bookDao.upsertBook(book);
                            
                            // Update last opened timestamp
                            bookDao.updateLastOpenedAt(bookId);
                            
                            // Return the book
                            final Book finalBook = book;
                            if (callback != null) {
                                mainHandler.post(() -> callback.onBookLoaded(finalBook));
                            }
                        } else {
                            if (callback != null) {
                                mainHandler.post(() -> callback.onError("Failed to parse book data"));
                            }
                        }
                    } else {
                        if (callback != null) {
                            mainHandler.post(() -> callback.onError("Book not found"));
                        }
                    }
                } else {
                    if (callback != null) {
                        mainHandler.post(() -> callback.onError("Failed to fetch book details"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    mainHandler.post(() -> callback.onError("Network error: " + e.getMessage()));
                }
            }
        });
    }
    
    /**
     * Upsert book to cache
     */
    public void upsertBook(Book book) {
        bookDao.upsertBook(book);
    }
}

