package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.Review;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for review operations
 */
public class ReviewDao {
    private SQLiteDatabase db;
    
    public ReviewDao(SQLiteDatabase db) {
        this.db = db;
    }
    
    /**
     * Insert or update a review (upsert)
     */
    public long upsertReview(Review review) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Reviews.COLUMN_USER_ID, review.getUserId());
        values.put(DbContract.Reviews.COLUMN_BOOK_ID, review.getBookId());
        values.put(DbContract.Reviews.COLUMN_RATING, review.getRating());
        values.put(DbContract.Reviews.COLUMN_REVIEW_TEXT, review.getReviewText());
        
        // Check if review exists
        String selection = DbContract.Reviews.COLUMN_USER_ID + " = ? AND " +
                DbContract.Reviews.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = {String.valueOf(review.getUserId()), review.getBookId()};
        
        Cursor cursor = db.query(
                DbContract.Reviews.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        
        if (exists) {
            // Update existing review
            values.put(DbContract.Reviews.COLUMN_UPDATED_AT, System.currentTimeMillis());
            db.update(DbContract.Reviews.TABLE_NAME, values, selection, selectionArgs);
            return 1;
        } else {
            // Insert new review
            values.put(DbContract.Reviews.COLUMN_CREATED_AT, System.currentTimeMillis());
            values.put(DbContract.Reviews.COLUMN_UPDATED_AT, System.currentTimeMillis());
            return db.insert(DbContract.Reviews.TABLE_NAME, null, values);
        }
    }
    
    /**
     * Get all reviews for a book (with user info)
     */
    public List<Review> getReviewsForBook(String bookId) {
        String sql = "SELECT r.*, u." + DbContract.Users.COLUMN_NAME + " as user_name, " +
                "u." + DbContract.Users.COLUMN_AVATAR_URI + " as user_avatar_uri " +
                "FROM " + DbContract.Reviews.TABLE_NAME + " r " +
                "INNER JOIN " + DbContract.Users.TABLE_NAME + " u " +
                "ON r." + DbContract.Reviews.COLUMN_USER_ID + " = u." + DbContract.Users.COLUMN_USER_ID + " " +
                "WHERE r." + DbContract.Reviews.COLUMN_BOOK_ID + " = ? " +
                "ORDER BY r." + DbContract.Reviews.COLUMN_CREATED_AT + " DESC";
        
        Cursor cursor = db.rawQuery(sql, new String[]{bookId});
        
        List<Review> reviews = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                reviews.add(cursorToReview(cursor));
            }
            cursor.close();
        }
        return reviews;
    }
    
    /**
     * Get user's review for a book
     */
    public Review getUserReview(int userId, String bookId) {
        String selection = DbContract.Reviews.COLUMN_USER_ID + " = ? AND " +
                DbContract.Reviews.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId), bookId};
        
        Cursor cursor = db.query(
                DbContract.Reviews.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Review review = null;
        if (cursor != null && cursor.moveToFirst()) {
            review = cursorToReview(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return review;
    }
    
    /**
     * Get average rating for a book
     */
    public double getAverageRating(String bookId) {
        String sql = "SELECT AVG(" + DbContract.Reviews.COLUMN_RATING + ") as avg_rating " +
                "FROM " + DbContract.Reviews.TABLE_NAME + " " +
                "WHERE " + DbContract.Reviews.COLUMN_BOOK_ID + " = ?";
        
        Cursor cursor = db.rawQuery(sql, new String[]{bookId});
        
        double avgRating = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            int avgIndex = cursor.getColumnIndex("avg_rating");
            if (!cursor.isNull(avgIndex)) {
                avgRating = cursor.getDouble(avgIndex);
            }
            cursor.close();
        }
        return avgRating;
    }
    
    /**
     * Convert cursor to Review object
     */
    private Review cursorToReview(Cursor cursor) {
        Review review = new Review();
        review.setReviewId(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_REVIEW_ID)));
        review.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_USER_ID)));
        review.setBookId(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_BOOK_ID)));
        review.setRating(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_RATING)));
        review.setReviewText(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_REVIEW_TEXT)));
        review.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_CREATED_AT)));
        review.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Reviews.COLUMN_UPDATED_AT)));
        
        // Joined user data
        int userNameIndex = cursor.getColumnIndex("user_name");
        if (userNameIndex >= 0 && !cursor.isNull(userNameIndex)) {
            review.setUserName(cursor.getString(userNameIndex));
        }
        int avatarIndex = cursor.getColumnIndex("user_avatar_uri");
        if (avatarIndex >= 0 && !cursor.isNull(avatarIndex)) {
            review.setUserAvatarUri(cursor.getString(avatarIndex));
        }
        
        return review;
    }
}

