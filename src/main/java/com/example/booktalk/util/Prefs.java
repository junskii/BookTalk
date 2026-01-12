package com.example.booktalk.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences utility for storing current user ID
 */
public class Prefs {
    private static final String PREFS_NAME = "booktalk_prefs";
    private static final String KEY_CURRENT_USER_ID = "current_user_id";
    
    private SharedPreferences prefs;
    
    public Prefs(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void setCurrentUserId(int userId) {
        prefs.edit().putInt(KEY_CURRENT_USER_ID, userId).apply();
    }
    
    public int getCurrentUserId() {
        return prefs.getInt(KEY_CURRENT_USER_ID, -1);
    }
    
    public void clearCurrentUserId() {
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply();
    }
    
    public boolean isLoggedIn() {
        return getCurrentUserId() != -1;
    }
}

