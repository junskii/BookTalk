package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.User;
import com.example.booktalk.data.repository.AuthRepository;
import com.example.booktalk.util.Prefs;

/**
 * Controller for Settings (MVC)
 */
public class SettingsController {
    private AuthRepository authRepository;
    private Prefs prefs;
    
    public interface UserInfoCallback {
        void onUserInfoLoaded(String name, String username, String avatarUri);
        void onError(String message);
    }
    
    public interface UpdateCallback {
        void onSuccess(String message);
        void onError(String message);
    }
    
    public interface LogoutCallback {
        void onLogout();
    }
    
    public SettingsController(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.authRepository = new AuthRepository(dbHelper);
        this.prefs = new Prefs(context);
    }
    
    public void loadUserInfo(UserInfoCallback callback) {
        int userId = prefs.getCurrentUserId();
        if (userId != -1) {
            User user = authRepository.getUserById(userId);
            if (user != null) {
                callback.onUserInfoLoaded(user.getName(), user.getUsername(), user.getAvatarUri());
            } else {
                callback.onError("User not found");
            }
        } else {
            callback.onError("User not logged in");
        }
    }
    
    public void updateUser(String name, String username, UpdateCallback callback) {
        if (name == null || name.trim().isEmpty()) {
            callback.onError("Name cannot be empty");
            return;
        }
        
        if (username == null || username.trim().isEmpty()) {
            callback.onError("Username cannot be empty");
            return;
        }
        
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            callback.onError("User not logged in");
            return;
        }
        
        boolean success = authRepository.updateUser(userId, name.trim(), username.trim());
        if (success) {
            callback.onSuccess("Profile updated successfully");
        } else {
            callback.onError("Username already taken");
        }
    }
    
    public void logout(LogoutCallback callback) {
        prefs.clearCurrentUserId();
        callback.onLogout();
    }
}

