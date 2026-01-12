package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for My Books operations
 */
public class MyBooksDao {
    private SQLiteDatabase db;
    
    public MyBooksDao(SQLiteDatabase db) {
        this.db = db;
    }
    
    /**
     * Add book to user's want to read list
     */
    public long addToMyBooks(int userId, String bookId) {
        ContentValues values = new ContentValues();
        values.put(DbContract.MyBooks.COLUMN_USER_ID, userId);
        values.put(DbContract.MyBooks.COLUMN_BOOK_ID, bookId);
        values.put(DbContract.MyBooks.COLUMN_STATUS, "WANT_TO_READ");
        values.put(DbContract.MyBooks.COLUMN_CREATED_AT, System.currentTimeMillis());
        
        return db.insertWithOnConflict(
                DbContract.MyBooks.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Remove book from user's want to read list
     */
    public int removeFromMyBooks(int userId, String bookId) {
        String whereClause = DbContract.MyBooks.COLUMN_USER_ID + " = ? AND " +
                DbContract.MyBooks.COLUMN_BOOK_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId), bookId};
        
        return db.delete(DbContract.MyBooks.TABLE_NAME, whereClause, whereArgs);
    }
    
    /**
     * Check if book is in user's want to read list
     */
    public boolean isInMyBooks(int userId, String bookId) {
        String selection = DbContract.MyBooks.COLUMN_USER_ID + " = ? AND " +
                DbContract.MyBooks.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId), bookId};
        
        Cursor cursor = db.query(
                DbContract.MyBooks.TABLE_NAME,
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
        return exists;
    }
    
    /**
     * Get all books for a user (want to read)
     */
    public List<Book> getMyBooks(int userId) {
        String sql = "SELECT b.* FROM " + DbContract.Books.TABLE_NAME + " b " +
                "INNER JOIN " + DbContract.MyBooks.TABLE_NAME + " mb " +
                "ON b." + DbContract.Books.COLUMN_BOOK_ID + " = mb." + DbContract.MyBooks.COLUMN_BOOK_ID + " " +
                "WHERE mb." + DbContract.MyBooks.COLUMN_USER_ID + " = ? " +
                "ORDER BY mb." + DbContract.MyBooks.COLUMN_CREATED_AT + " DESC";
        
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        
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
        return book;
    }
}

