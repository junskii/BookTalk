package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Category;
import com.example.booktalk.data.repository.HomeRepository;
import java.util.List;

/**
 * Controller for Home (MVC)
 */
public class HomeController {
    private HomeRepository homeRepository;
    
    public interface Callback {
        void onCategoriesLoaded(List<Category> categories);
        void onError(String message);
    }
    
    public HomeController(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.homeRepository = new HomeRepository(dbHelper);
    }
    
    public void loadCategories(Callback callback) {
        // Load from cache first (synchronous)
        try {
            List<Category> categories = homeRepository.getCategories();
            callback.onCategoriesLoaded(categories);
        } catch (Exception e) {
            callback.onError("Failed to load categories: " + e.getMessage());
        }
        
        // Then fetch from API async and update
        homeRepository.getCategoriesAsync(categories -> {
            callback.onCategoriesLoaded(categories);
        });
    }
}

