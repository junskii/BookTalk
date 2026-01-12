package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.User;
import com.example.booktalk.data.repository.AuthRepository;
import com.example.booktalk.util.Prefs;

/**
 * Controller for Authentication (MVC)
 */
public class AuthController {
    private AuthRepository authRepository;
    private Prefs prefs;
    
    public interface Callback {
        void onSuccess();
        void onError(String message);
        void onNavigateToMain();
    }
    
    public AuthController(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.authRepository = new AuthRepository(dbHelper);
        this.prefs = new Prefs(context);
    }
    
    public void login(String username, String password, Callback callback) {
        if (username == null || username.trim().isEmpty()) {
            callback.onError("Username cannot be empty");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            callback.onError("Password cannot be empty");
            return;
        }
        
        User user = authRepository.login(username.trim(), password);
        if (user != null) {
            prefs.setCurrentUserId(user.getUserId());
            callback.onNavigateToMain();
        } else {
            callback.onError("Invalid username or password");
        }
    }
    
    public void register(String name, String username, String password, String avatarPath, Callback callback) {
        if (name == null || name.trim().isEmpty()) {
            callback.onError("Name cannot be empty");
            return;
        }
        
        if (username == null || username.trim().isEmpty()) {
            callback.onError("Username cannot be empty");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            callback.onError("Password cannot be empty");
            return;
        }
        
        User user = authRepository.register(name.trim(), username.trim(), password, avatarPath);
        if (user != null) {
            prefs.setCurrentUserId(user.getUserId());
            callback.onSuccess();
            callback.onNavigateToMain();
        } else {
            callback.onError("Username already taken");
        }
    }
}

