package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for search cache operations
 */
public class SearchDao {
    private SQLiteDatabase db;
    
    public SearchDao(SQLiteDatabase db) {
        this.db = db;
    }
    
    /**
     * Insert or update search cache entry
     */
    public void upsertSearchCache(String query, long fetchedAt) {
        ContentValues values = new ContentValues();
        values.put(DbContract.SearchCache.COLUMN_QUERY, query);
        values.put(DbContract.SearchCache.COLUMN_FETCHED_AT, fetchedAt);
        
        db.insertWithOnConflict(
                DbContract.SearchCache.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Get search cache fetched_at timestamp
     */
    public long getSearchCacheFetchedAt(String query) {
        String selection = DbContract.SearchCache.COLUMN_QUERY + " = ?";
        String[] selectionArgs = {query};
        
        Cursor cursor = db.query(
                DbContract.SearchCache.TABLE_NAME,
                new String[]{DbContract.SearchCache.COLUMN_FETCHED_AT},
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        long fetchedAt = 0;
        if (cursor != null && cursor.moveToFirst()) {
            fetchedAt = cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.SearchCache.COLUMN_FETCHED_AT));
            cursor.close();
        }
        return fetchedAt;
    }
    
    /**
     * Insert search cache book
     */
    public void insertSearchCacheBook(String query, String bookId, int sortOrder) {
        ContentValues values = new ContentValues();
        values.put(DbContract.SearchCacheBooks.COLUMN_QUERY, query);
        values.put(DbContract.SearchCacheBooks.COLUMN_BOOK_ID, bookId);
        values.put(DbContract.SearchCacheBooks.COLUMN_SORT_ORDER, sortOrder);
        
        db.insertWithOnConflict(
                DbContract.SearchCacheBooks.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Delete all books for a search query (before re-inserting)
     */
    public void deleteSearchCacheBooks(String query) {
        String whereClause = DbContract.SearchCacheBooks.COLUMN_QUERY + " = ?";
        String[] whereArgs = {query};
        db.delete(DbContract.SearchCacheBooks.TABLE_NAME, whereClause, whereArgs);
    }
    
    /**
     * Get cached search results
     */
    public List<Book> getCachedSearchResults(String query) {
        String sql = "SELECT b.* FROM " + DbContract.Books.TABLE_NAME + " b " +
                "INNER JOIN " + DbContract.SearchCacheBooks.TABLE_NAME + " scb " +
                "ON b." + DbContract.Books.COLUMN_BOOK_ID + " = scb." + DbContract.SearchCacheBooks.COLUMN_BOOK_ID + " " +
                "WHERE scb." + DbContract.SearchCacheBooks.COLUMN_QUERY + " = ? " +
                "ORDER BY scb." + DbContract.SearchCacheBooks.COLUMN_SORT_ORDER + " ASC";
        
        Cursor cursor = db.rawQuery(sql, new String[]{query});
        
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

