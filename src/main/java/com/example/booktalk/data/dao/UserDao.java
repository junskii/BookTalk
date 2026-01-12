package com.example.booktalk.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.db.DbContract;
import com.example.booktalk.data.model.User;

/**
 * DAO for user operations
 */
public class UserDao {
    private SQLiteDatabase db;
    
    public UserDao(SQLiteDatabase db) {
        this.db = db;
    }
    
    /**
     * Insert a new user
     */
    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Users.COLUMN_NAME, user.getName());
        values.put(DbContract.Users.COLUMN_USERNAME, user.getUsername());
        values.put(DbContract.Users.COLUMN_PASSWORD, user.getPassword());
        values.put(DbContract.Users.COLUMN_AVATAR_URI, user.getAvatarUri());
        values.put(DbContract.Users.COLUMN_CREATED_AT, user.getCreatedAt());
        
        return db.insert(DbContract.Users.TABLE_NAME, null, values);
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        String selection = DbContract.Users.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = db.query(
                DbContract.Users.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        String selection = DbContract.Users.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(
                DbContract.Users.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(int userId, String name, String username) {
        ContentValues values = new ContentValues();
        values.put(DbContract.Users.COLUMN_NAME, name);
        values.put(DbContract.Users.COLUMN_USERNAME, username);
        
        String whereClause = DbContract.Users.COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(userId)};
        
        int rowsAffected = db.update(DbContract.Users.TABLE_NAME, values, whereClause, whereArgs);
        return rowsAffected > 0;
    }
    
    /**
     * Convert cursor to User object
     */
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Users.COLUMN_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Users.COLUMN_NAME)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Users.COLUMN_USERNAME)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Users.COLUMN_PASSWORD)));
        int avatarIndex = cursor.getColumnIndex(DbContract.Users.COLUMN_AVATAR_URI);
        if (!cursor.isNull(avatarIndex)) {
            user.setAvatarUri(cursor.getString(avatarIndex));
        }
        user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Users.COLUMN_CREATED_AT)));
        return user;
    }
}

