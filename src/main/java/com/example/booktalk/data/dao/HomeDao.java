package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Category;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for home category operations
 */
public class HomeDao {
    private SQLiteDatabase db;
    private BookDao bookDao;
    
    public HomeDao(SQLiteDatabase db) {
        this.db = db;
        this.bookDao = new BookDao(db);
    }
    
    /**
     * Insert or update category
     */
    public long upsertCategory(Category category) {
        ContentValues values = new ContentValues();
        if (category.getCategoryId() > 0) {
            values.put(DbContract.HomeCategory.COLUMN_CATEGORY_ID, category.getCategoryId());
        }
        values.put(DbContract.HomeCategory.COLUMN_NAME, category.getName());
        values.put(DbContract.HomeCategory.COLUMN_QUERY_HINT, category.getQueryHint());
        values.put(DbContract.HomeCategory.COLUMN_FETCHED_AT, category.getFetchedAt());
        
        return db.insertWithOnConflict(
                DbContract.HomeCategory.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        Cursor cursor = db.query(
                DbContract.HomeCategory.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbContract.HomeCategory.COLUMN_CATEGORY_ID + " ASC"
        );
        
        List<Category> categories = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                categories.add(cursorToCategory(cursor));
            }
            cursor.close();
        }
        return categories;
    }
    
    /**
     * Get category by ID
     */
    public Category getCategoryById(int categoryId) {
        String selection = DbContract.HomeCategory.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        
        Cursor cursor = db.query(
                DbContract.HomeCategory.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Category category = null;
        if (cursor != null && cursor.moveToFirst()) {
            category = cursorToCategory(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return category;
    }
    
    /**
     * Update category fetched_at timestamp
     */
    public void updateCategoryFetchedAt(int categoryId, long fetchedAt) {
        ContentValues values = new ContentValues();
        values.put(DbContract.HomeCategory.COLUMN_FETCHED_AT, fetchedAt);
        
        String whereClause = DbContract.HomeCategory.COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = {String.valueOf(categoryId)};
        
        db.update(DbContract.HomeCategory.TABLE_NAME, values, whereClause, whereArgs);
    }
    
    /**
     * Insert category-book relationship
     */
    public void insertCategoryBook(int categoryId, String bookId, int sortOrder) {
        ContentValues values = new ContentValues();
        values.put(DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID, categoryId);
        values.put(DbContract.HomeCategoryBooks.COLUMN_BOOK_ID, bookId);
        values.put(DbContract.HomeCategoryBooks.COLUMN_SORT_ORDER, sortOrder);
        
        db.insertWithOnConflict(
                DbContract.HomeCategoryBooks.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }
    
    /**
     * Delete all books for a category (before re-inserting)
     */
    public void deleteCategoryBooks(int categoryId) {
        String whereClause = DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = {String.valueOf(categoryId)};
        db.delete(DbContract.HomeCategoryBooks.TABLE_NAME, whereClause, whereArgs);
    }
    
    /**
     * Get books for a category (ordered by sort_order)
     */
    public List<Book> getCategoryBooks(int categoryId) {
        String sql = "SELECT b.* FROM " + DbContract.Books.TABLE_NAME + " b " +
                "INNER JOIN " + DbContract.HomeCategoryBooks.TABLE_NAME + " hcb " +
                "ON b." + DbContract.Books.COLUMN_BOOK_ID + " = hcb." + DbContract.HomeCategoryBooks.COLUMN_BOOK_ID + " " +
                "WHERE hcb." + DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + " = ? " +
                "ORDER BY hcb." + DbContract.HomeCategoryBooks.COLUMN_SORT_ORDER + " ASC";
        
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId)});
        
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
    
    /**
     * Convert cursor to Category object
     */
    private Category cursorToCategory(Cursor cursor) {
        Category category = new Category();
        category.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.HomeCategory.COLUMN_CATEGORY_ID)));
        category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.HomeCategory.COLUMN_NAME)));
        category.setQueryHint(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.HomeCategory.COLUMN_QUERY_HINT)));
        category.setFetchedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.HomeCategory.COLUMN_FETCHED_AT)));
        return category;
    }
}

