package com.example.booktalk.data.db;

/**
 * Database contract defining table names and column names
 */
public class DbContract {
    
    // Database name and version
    public static final String DATABASE_NAME = "booktalk.db";
    public static final int DATABASE_VERSION = 2;
    
    // Table: users
    public static final class Users {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_AVATAR_URI = "avatar_uri";
        public static final String COLUMN_CREATED_AT = "created_at";
    }
    
    // Table: books
    public static final class Books {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_COVER_URL = "cover_url";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_FETCHED_AT = "fetched_at";
        public static final String COLUMN_LAST_OPENED_AT = "last_opened_at";
        public static final String COLUMN_PUBLISHED_DATE = "published_date";
        public static final String COLUMN_RATINGS_COUNT = "ratings_count";
        public static final String COLUMN_AVERAGE_RATING = "average_rating";
    }
    
    // Table: home_category
    public static final class HomeCategory {
        public static final String TABLE_NAME = "home_category";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUERY_HINT = "query_hint";
        public static final String COLUMN_FETCHED_AT = "fetched_at";
    }
    
    // Table: home_category_books
    public static final class HomeCategoryBooks {
        public static final String TABLE_NAME = "home_category_books";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_SORT_ORDER = "sort_order";
    }
    
    // Table: my_books
    public static final class MyBooks {
        public static final String TABLE_NAME = "my_books";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_CREATED_AT = "created_at";
    }
    
    // Table: reviews
    public static final class Reviews {
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_REVIEW_TEXT = "review_text";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
    }
    
    // Table: search_cache
    public static final class SearchCache {
        public static final String TABLE_NAME = "search_cache";
        public static final String COLUMN_QUERY = "query";
        public static final String COLUMN_FETCHED_AT = "fetched_at";
    }
    
    // Table: search_cache_books
    public static final class SearchCacheBooks {
        public static final String TABLE_NAME = "search_cache_books";
        public static final String COLUMN_QUERY = "query";
        public static final String COLUMN_BOOK_ID = "book_id";
        public static final String COLUMN_SORT_ORDER = "sort_order";
    }
}

