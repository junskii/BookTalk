package com.example.booktalk.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper for BookTalk database
 * Manages database creation and version management
 */
public class AppDbHelper extends SQLiteOpenHelper {
    
    public AppDbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        db.execSQL("CREATE TABLE " + DbContract.Users.TABLE_NAME + " (" +
                DbContract.Users.COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.Users.COLUMN_NAME + " TEXT NOT NULL, " +
                DbContract.Users.COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                DbContract.Users.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                DbContract.Users.COLUMN_AVATAR_URI + " TEXT NULL, " +
                DbContract.Users.COLUMN_CREATED_AT + " INTEGER NOT NULL" +
                ")");
        
        // Create books table
        db.execSQL("CREATE TABLE " + DbContract.Books.TABLE_NAME + " (" +
                DbContract.Books.COLUMN_BOOK_ID + " TEXT PRIMARY KEY, " +
                DbContract.Books.COLUMN_TITLE + " TEXT NOT NULL, " +
                DbContract.Books.COLUMN_AUTHOR + " TEXT, " +
                DbContract.Books.COLUMN_COVER_URL + " TEXT, " +
                DbContract.Books.COLUMN_DESCRIPTION + " TEXT, " +
                DbContract.Books.COLUMN_FETCHED_AT + " INTEGER NOT NULL, " +
                DbContract.Books.COLUMN_LAST_OPENED_AT + " INTEGER NOT NULL DEFAULT 0, " +
                DbContract.Books.COLUMN_PUBLISHED_DATE + " TEXT, " +
                DbContract.Books.COLUMN_RATINGS_COUNT + " INTEGER, " +
                DbContract.Books.COLUMN_AVERAGE_RATING + " REAL" +
                ")");
        
        // Create home_category table
        db.execSQL("CREATE TABLE " + DbContract.HomeCategory.TABLE_NAME + " (" +
                DbContract.HomeCategory.COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.HomeCategory.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DbContract.HomeCategory.COLUMN_QUERY_HINT + " TEXT NOT NULL, " +
                DbContract.HomeCategory.COLUMN_FETCHED_AT + " INTEGER NOT NULL" +
                ")");
        
        // Create home_category_books table
        db.execSQL("CREATE TABLE " + DbContract.HomeCategoryBooks.TABLE_NAME + " (" +
                DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
                DbContract.HomeCategoryBooks.COLUMN_BOOK_ID + " TEXT NOT NULL, " +
                DbContract.HomeCategoryBooks.COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                "PRIMARY KEY(" + DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + ", " +
                DbContract.HomeCategoryBooks.COLUMN_BOOK_ID + "), " +
                "FOREIGN KEY(" + DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + ") REFERENCES " +
                DbContract.HomeCategory.TABLE_NAME + "(" + DbContract.HomeCategory.COLUMN_CATEGORY_ID + "), " +
                "FOREIGN KEY(" + DbContract.HomeCategoryBooks.COLUMN_BOOK_ID + ") REFERENCES " +
                DbContract.Books.TABLE_NAME + "(" + DbContract.Books.COLUMN_BOOK_ID + ")" +
                ")");
        
        // Create my_books table
        db.execSQL("CREATE TABLE " + DbContract.MyBooks.TABLE_NAME + " (" +
                DbContract.MyBooks.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                DbContract.MyBooks.COLUMN_BOOK_ID + " TEXT NOT NULL, " +
                DbContract.MyBooks.COLUMN_STATUS + " TEXT NOT NULL DEFAULT 'WANT_TO_READ', " +
                DbContract.MyBooks.COLUMN_CREATED_AT + " INTEGER NOT NULL, " +
                "PRIMARY KEY(" + DbContract.MyBooks.COLUMN_USER_ID + ", " +
                DbContract.MyBooks.COLUMN_BOOK_ID + "), " +
                "FOREIGN KEY(" + DbContract.MyBooks.COLUMN_USER_ID + ") REFERENCES " +
                DbContract.Users.TABLE_NAME + "(" + DbContract.Users.COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + DbContract.MyBooks.COLUMN_BOOK_ID + ") REFERENCES " +
                DbContract.Books.TABLE_NAME + "(" + DbContract.Books.COLUMN_BOOK_ID + ")" +
                ")");
        
        // Create reviews table
        db.execSQL("CREATE TABLE " + DbContract.Reviews.TABLE_NAME + " (" +
                DbContract.Reviews.COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbContract.Reviews.COLUMN_USER_ID + " INTEGER NOT NULL, " +
                DbContract.Reviews.COLUMN_BOOK_ID + " TEXT NOT NULL, " +
                DbContract.Reviews.COLUMN_RATING + " INTEGER NOT NULL CHECK(" +
                DbContract.Reviews.COLUMN_RATING + " BETWEEN 1 AND 5), " +
                DbContract.Reviews.COLUMN_REVIEW_TEXT + " TEXT NOT NULL, " +
                DbContract.Reviews.COLUMN_CREATED_AT + " INTEGER NOT NULL, " +
                DbContract.Reviews.COLUMN_UPDATED_AT + " INTEGER NOT NULL, " +
                "UNIQUE(" + DbContract.Reviews.COLUMN_USER_ID + ", " +
                DbContract.Reviews.COLUMN_BOOK_ID + "), " +
                "FOREIGN KEY(" + DbContract.Reviews.COLUMN_USER_ID + ") REFERENCES " +
                DbContract.Users.TABLE_NAME + "(" + DbContract.Users.COLUMN_USER_ID + "), " +
                "FOREIGN KEY(" + DbContract.Reviews.COLUMN_BOOK_ID + ") REFERENCES " +
                DbContract.Books.TABLE_NAME + "(" + DbContract.Books.COLUMN_BOOK_ID + ")" +
                ")");
        
        // Create search_cache table
        db.execSQL("CREATE TABLE " + DbContract.SearchCache.TABLE_NAME + " (" +
                DbContract.SearchCache.COLUMN_QUERY + " TEXT PRIMARY KEY, " +
                DbContract.SearchCache.COLUMN_FETCHED_AT + " INTEGER NOT NULL" +
                ")");
        
        // Create search_cache_books table
        db.execSQL("CREATE TABLE " + DbContract.SearchCacheBooks.TABLE_NAME + " (" +
                DbContract.SearchCacheBooks.COLUMN_QUERY + " TEXT NOT NULL, " +
                DbContract.SearchCacheBooks.COLUMN_BOOK_ID + " TEXT NOT NULL, " +
                DbContract.SearchCacheBooks.COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                "PRIMARY KEY(" + DbContract.SearchCacheBooks.COLUMN_QUERY + ", " +
                DbContract.SearchCacheBooks.COLUMN_BOOK_ID + "), " +
                "FOREIGN KEY(" + DbContract.SearchCacheBooks.COLUMN_QUERY + ") REFERENCES " +
                DbContract.SearchCache.TABLE_NAME + "(" + DbContract.SearchCache.COLUMN_QUERY + "), " +
                "FOREIGN KEY(" + DbContract.SearchCacheBooks.COLUMN_BOOK_ID + ") REFERENCES " +
                DbContract.Books.TABLE_NAME + "(" + DbContract.Books.COLUMN_BOOK_ID + ")" +
                ")");
        
        // Create indexes
        db.execSQL("CREATE INDEX idx_books_title ON " + DbContract.Books.TABLE_NAME +
                "(" + DbContract.Books.COLUMN_TITLE + ")");
        db.execSQL("CREATE INDEX idx_home_category_books ON " + DbContract.HomeCategoryBooks.TABLE_NAME +
                "(" + DbContract.HomeCategoryBooks.COLUMN_CATEGORY_ID + ", " +
                DbContract.HomeCategoryBooks.COLUMN_SORT_ORDER + ")");
        db.execSQL("CREATE INDEX idx_reviews_book ON " + DbContract.Reviews.TABLE_NAME +
                "(" + DbContract.Reviews.COLUMN_BOOK_ID + ")");
        db.execSQL("CREATE INDEX idx_mybooks_user ON " + DbContract.MyBooks.TABLE_NAME +
                "(" + DbContract.MyBooks.COLUMN_USER_ID + ")");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new columns for version 2
            db.execSQL("ALTER TABLE " + DbContract.Books.TABLE_NAME + 
                " ADD COLUMN " + DbContract.Books.COLUMN_PUBLISHED_DATE + " TEXT");
            db.execSQL("ALTER TABLE " + DbContract.Books.TABLE_NAME + 
                " ADD COLUMN " + DbContract.Books.COLUMN_RATINGS_COUNT + " INTEGER");
            db.execSQL("ALTER TABLE " + DbContract.Books.TABLE_NAME + 
                " ADD COLUMN " + DbContract.Books.COLUMN_AVERAGE_RATING + " REAL");
        }
        
        // For major version changes, drop and recreate (for development)
        if (oldVersion < newVersion && newVersion >= 2) {
            // Keep existing upgrade logic for major changes
        }
    }
}

