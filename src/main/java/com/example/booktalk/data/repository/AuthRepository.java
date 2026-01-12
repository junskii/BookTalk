package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.dao.UserDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.User;

/**
 * Repository for authentication operations
 */
public class AuthRepository {
    private AppDbHelper dbHelper;
    private UserDao userDao;
    
    public AuthRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.userDao = new UserDao(db);
    }
    
    /**
     * Register a new user
     */
    public User register(String name, String username, String password, String avatarPath) {
        // Check if username already exists
        User existing = userDao.getUserByUsername(username);
        if (existing != null) {
            return null; // Username already taken
        }
        
        User user = new User(name, username, password);
        user.setAvatarUri(avatarPath);
        long userId = userDao.insertUser(user);
        if (userId > 0) {
            user.setUserId((int) userId);
            return user;
        }
        return null;
    }
    
    /**
     * Login with username and password
     */
    public User login(String username, String password) {
        User user = userDao.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(int userId, String name, String username) {
        // If username is being changed, check if new username is available
        User currentUser = userDao.getUserById(userId);
        if (currentUser != null && !currentUser.getUsername().equals(username)) {
            User existing = userDao.getUserByUsername(username);
            if (existing != null) {
                return false; // Username already taken
            }
        }
        
        return userDao.updateUser(userId, name, username);
    }
}

