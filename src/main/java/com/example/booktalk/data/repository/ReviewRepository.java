package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.dao.ReviewDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Review;
import java.util.List;

/**
 * Repository for review operations
 */
public class ReviewRepository {
    private AppDbHelper dbHelper;
    private ReviewDao reviewDao;
    
    public ReviewRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.reviewDao = new ReviewDao(db);
    }
    
    /**
     * Upsert a review (create or update)
     */
    public void upsertReview(Review review) {
        reviewDao.upsertReview(review);
    }
    
    /**
     * Get all reviews for a book
     */
    public List<Review> getReviewsForBook(String bookId) {
        return reviewDao.getReviewsForBook(bookId);
    }
    
    /**
     * Get user's review for a book
     */
    public Review getUserReview(int userId, String bookId) {
        return reviewDao.getUserReview(userId, bookId);
    }
    
    /**
     * Get average rating for a book
     */
    public double getAverageRating(String bookId) {
        return reviewDao.getAverageRating(bookId);
    }
}

