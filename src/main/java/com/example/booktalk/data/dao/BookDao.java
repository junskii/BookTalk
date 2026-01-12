package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for book operations
 */
public class BookDao {
    private SQLiteDatabase db;
    
    public BookDao(SQLiteDatabase db) {
        this.db = db;
    }
    
    /**
     * Insert or update a book (upsert)
     */
    public long upsertBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Books.COLUMN_BOOK_ID, book.getBookId());
        values.put(DbContract.Books.COLUMN_TITLE, book.getTitle());
        values.put(DbContract.Books.COLUMN_AUTHOR, book.getAuthor());
        values.put(DbContract.Books.COLUMN_COVER_URL, book.getCoverUrl());
        values.put(DbContract.Books.COLUMN_DESCRIPTION, book.getDescription());
        values.put(DbContract.Books.COLUMN_FETCHED_AT, book.getFetchedAt());
        values.put(DbContract.Books.COLUMN_PUBLISHED_DATE, book.getPublishedDate());
        if (book.getRatingsCount() != null) {
            values.put(DbContract.Books.COLUMN_RATINGS_COUNT, book.getRatingsCount());
        }
        if (book.getAverageRating() != null) {
            values.put(DbContract.Books.COLUMN_AVERAGE_RATING, book.getAverageRating());
        }
        
        // Use INSERT OR REPLACE for upsert
        return db.insertWithOnConflict(
                DbContract.Books.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Get book by ID
     */
    public Book getBookById(String bookId) {
        String selection = DbContract.Books.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = {bookId};
        
        Cursor cursor = db.query(
                DbContract.Books.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Book book = null;
        if (cursor != null && cursor.moveToFirst()) {
            book = cursorToBook(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return book;
    }
    
    /**
     * Update book's last opened timestamp
     */
    public void updateLastOpenedAt(String bookId) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Books.COLUMN_LAST_OPENED_AT, System.currentTimeMillis());
        
        String whereClause = DbContract.Books.COLUMN_BOOK_ID + " = ?";
        String[] whereArgs = {bookId};
        
        db.update(DbContract.Books.TABLE_NAME, values, whereClause, whereArgs);
    }
    
    /**
     * Update book description
     */
    public void updateBookDescription(String bookId, String description) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Books.COLUMN_DESCRIPTION, description);
        values.put(DbContract.Books.COLUMN_FETCHED_AT, System.currentTimeMillis());
        
        String whereClause = DbContract.Books.COLUMN_BOOK_ID + " = ?";
        String[] whereArgs = {bookId};
        
        db.update(DbContract.Books.TABLE_NAME, values, whereClause, whereArgs);
    }
    
    /**
     * Get books by IDs
     */
    public List<Book> getBooksByIds(List<String> bookIds) {
        if (bookIds == null || bookIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        String placeholders = "";
        for (int i = 0; i < bookIds.size(); i++) {
            if (i > 0) placeholders += ",";
            placeholders += "?";
        }
        
        String selection = DbContract.Books.COLUMN_BOOK_ID + " IN (" + placeholders + ")";
        String[] selectionArgs = bookIds.toArray(new String[0]);
        
        Cursor cursor = db.query(
                DbContract.Books.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        List<Book> books = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                books.add(cursorToBook(cursor));
            }
            cursor.close();
        }
        return books;
    }
    
    /**
     * Convert cursor to Book object
     */
    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setBookId(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Books.COLUMN_BOOK_ID)));
        book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Books.COLUMN_TITLE)));
        int authorIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_AUTHOR);
        if (!cursor.isNull(authorIndex)) {
            book.setAuthor(cursor.getString(authorIndex));
        }
        int coverIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_COVER_URL);
        if (!cursor.isNull(coverIndex)) {
            book.setCoverUrl(cursor.getString(coverIndex));
        }
        int descIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_DESCRIPTION);
        if (!cursor.isNull(descIndex)) {
            book.setDescription(cursor.getString(descIndex));
        }
        book.setFetchedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Books.COLUMN_FETCHED_AT)));
        book.setLastOpenedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Books.COLUMN_LAST_OPENED_AT)));
        
        // New fields (may not exist in old database versions)
        int pubDateIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_PUBLISHED_DATE);
        if (pubDateIndex >= 0 && !cursor.isNull(pubDateIndex)) {
            book.setPublishedDate(cursor.getString(pubDateIndex));
        }
        int ratingsCountIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_RATINGS_COUNT);
        if (ratingsCountIndex >= 0 && !cursor.isNull(ratingsCountIndex)) {
            book.setRatingsCount(cursor.getInt(ratingsCountIndex));
        }
        int avgRatingIndex = cursor.getColumnIndex(DbContract.Books.COLUMN_AVERAGE_RATING);
        if (avgRatingIndex >= 0 && !cursor.isNull(avgRatingIndex)) {
            book.setAverageRating(cursor.getDouble(avgRatingIndex));
        }
        
        return book;
    }
}

