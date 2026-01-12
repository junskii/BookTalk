package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Review;
import com.example.booktalk.data.repository.BooksRepository;
import com.example.booktalk.data.repository.MyBooksRepository;
import com.example.booktalk.data.repository.ReviewRepository;
import com.example.booktalk.util.Prefs;
import java.util.List;

/**
 * Controller for Book Detail (MVC)
 */
public class BookDetailController {
    private BooksRepository booksRepository;
    private MyBooksRepository myBooksRepository;
    private ReviewRepository reviewRepository;
    private Prefs prefs;
    private String bookId;
    private Book currentBook;
    
    public interface BookCallback {
        void onBookLoaded(Book book);
        void onError(String error);
    }
    
    public interface WantToReadCallback {
        void onWantToReadUpdated(boolean isWantToRead);
        void onError(String error);
    }
    
    public interface ReviewCallback {
        void onReviewsLoaded(List<Review> reviews);
        void onReviewSubmitted();
        void onError(String error);
    }
    
    public BookDetailController(Context context, String bookId) {
        this.bookId = bookId;
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.booksRepository = new BooksRepository(dbHelper);
        this.myBooksRepository = new MyBooksRepository(dbHelper);
        this.reviewRepository = new ReviewRepository(dbHelper);
        this.prefs = new Prefs(context);
    }
    
    public void loadBook(String bookId, BookCallback callback) {
        this.bookId = bookId;
        booksRepository.getBookByIdAsync(bookId, new BooksRepository.BookCallback() {
            @Override
            public void onBookLoaded(Book book) {
                currentBook = book;
                if (book != null) {
                    callback.onBookLoaded(book);
                    
                    int userId = prefs.getCurrentUserId();
                    if (userId != -1) {
                        boolean isWantToRead = myBooksRepository.isWantToRead(userId, bookId);
                        // This will be handled separately via getWantToReadStatus
                    }
                } else {
                    callback.onError("Book not found");
                }
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error != null ? error : "Failed to load book");
            }
        });
    }
    
    public void getWantToReadStatus(String bookId, WantToReadCallback callback) {
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            callback.onError("Please login first");
            return;
        }
        
        boolean isWantToRead = myBooksRepository.isWantToRead(userId, bookId);
        callback.onWantToReadUpdated(isWantToRead);
    }
    
    public void toggleWantToRead(String bookId, WantToReadCallback callback) {
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            callback.onError("Please login first");
            return;
        }
        
        boolean isWantToRead = myBooksRepository.toggleWantToRead(userId, bookId);
        callback.onWantToReadUpdated(isWantToRead);
    }
    
    public void loadReviews(String bookId, ReviewCallback callback) {
        List<Review> reviews = reviewRepository.getReviewsForBook(bookId);
        callback.onReviewsLoaded(reviews);
    }
    
    public void submitReview(String bookId, int rating, String reviewText, ReviewCallback callback) {
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            callback.onError("Please login first");
            return;
        }
        
        if (rating < 1 || rating > 5) {
            callback.onError("Rating must be between 1 and 5");
            return;
        }
        
        if (reviewText == null || reviewText.trim().isEmpty()) {
            callback.onError("Review text cannot be empty");
            return;
        }
        
        // Check if user already has a review for this book
        Review existingReview = reviewRepository.getUserReview(userId, bookId);
        
        Review review;
        if (existingReview != null) {
            // Update existing review
            review = existingReview;
            review.setRating(rating);
            review.setReviewText(reviewText.trim());
            review.setUpdatedAt(System.currentTimeMillis());
        } else {
            // Create new review
            review = new Review();
            review.setUserId(userId);
            review.setBookId(bookId);
            review.setRating(rating);
            review.setReviewText(reviewText.trim());
            long now = System.currentTimeMillis();
            review.setCreatedAt(now);
            review.setUpdatedAt(now);
        }
        
        reviewRepository.upsertReview(review);
        callback.onReviewSubmitted();
    }
    
    public Review getUserReview(String bookId) {
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            return null;
        }
        return reviewRepository.getUserReview(userId, bookId);
    }
    
    public Book getCurrentBook() {
        return currentBook;
    }
}

