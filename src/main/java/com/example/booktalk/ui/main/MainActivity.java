package com.example.booktalk.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.booktalk.R;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.repository.HomeRepository;
import com.example.booktalk.ui.auth.LoginActivity;
import com.example.booktalk.ui.home.HomeFragment;
import com.example.booktalk.ui.mybooks.MyBooksFragment;
import com.example.booktalk.ui.search.SearchFragment;
import com.example.booktalk.ui.settings.SettingsFragment;
import com.example.booktalk.util.Prefs;

/**
 * Main Activity with Custom Pill-shaped Bottom Navigation
 */
public class MainActivity extends AppCompatActivity {
    private LinearLayout navItemHome, navItemMyBooks, navItemSearch, navItemSettings;
    private ImageView iconHome, iconMyBooks, iconSearch, iconSettings;
    private TextView labelHome, labelMyBooks, labelSearch, labelSettings;
    private Prefs prefs;
    private int selectedNavId = R.id.navItemHome;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prefs = new Prefs(this);
        
        // Check if user is logged in
        if (!prefs.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        
        // Seed categories on first launch
        AppDbHelper dbHelper = new AppDbHelper(this);
        HomeRepository homeRepository = new HomeRepository(dbHelper);
        homeRepository.seedCategories();
        
        // Setup custom bottom navigation
        setupCustomBottomNav();
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            setSelectedNavItem(R.id.navItemHome);
        }
    }
    
    private void setupCustomBottomNav() {
        navItemHome = findViewById(R.id.navItemHome);
        navItemMyBooks = findViewById(R.id.navItemMyBooks);
        navItemSearch = findViewById(R.id.navItemSearch);
        navItemSettings = findViewById(R.id.navItemSettings);
        
        iconHome = findViewById(R.id.iconHome);
        iconMyBooks = findViewById(R.id.iconMyBooks);
        iconSearch = findViewById(R.id.iconSearch);
        iconSettings = findViewById(R.id.iconSettings);
        
        labelHome = findViewById(R.id.labelHome);
        labelMyBooks = findViewById(R.id.labelMyBooks);
        labelSearch = findViewById(R.id.labelSearch);
        labelSettings = findViewById(R.id.labelSettings);
        
        navItemHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            setSelectedNavItem(R.id.navItemHome);
        });
        
        navItemMyBooks.setOnClickListener(v -> {
            loadFragment(new MyBooksFragment());
            setSelectedNavItem(R.id.navItemMyBooks);
        });
        
        navItemSearch.setOnClickListener(v -> {
            loadFragment(new SearchFragment());
            setSelectedNavItem(R.id.navItemSearch);
        });
        
        navItemSettings.setOnClickListener(v -> {
            loadFragment(new SettingsFragment());
            setSelectedNavItem(R.id.navItemSettings);
        });
    }
    
    private void setSelectedNavItem(int navItemId) {
        // Clear all backgrounds
        navItemHome.setBackground(null);
        navItemMyBooks.setBackground(null);
        navItemSearch.setBackground(null);
        navItemSettings.setBackground(null);
        
        // Reset all icon tints to gray (unselected)
        int grayColor = ContextCompat.getColor(this, android.R.color.darker_gray);
        iconHome.setColorFilter(grayColor, android.graphics.PorterDuff.Mode.SRC_IN);
        iconMyBooks.setColorFilter(grayColor, android.graphics.PorterDuff.Mode.SRC_IN);
        iconSearch.setColorFilter(grayColor, android.graphics.PorterDuff.Mode.SRC_IN);
        iconSettings.setColorFilter(grayColor, android.graphics.PorterDuff.Mode.SRC_IN);
        
        labelHome.setTextColor(grayColor);
        labelMyBooks.setTextColor(grayColor);
        labelSearch.setTextColor(grayColor);
        labelSettings.setTextColor(grayColor);
        
        // Set selected background and tint
        selectedNavId = navItemId;
        int primaryColor = ContextCompat.getColor(this, R.color.color_primary);
        
        if (navItemId == R.id.navItemHome) {
            navItemHome.setBackgroundResource(R.drawable.bg_nav_selected);
            iconHome.setColorFilter(primaryColor, android.graphics.PorterDuff.Mode.SRC_IN);
            labelHome.setTextColor(primaryColor);
        } else if (navItemId == R.id.navItemMyBooks) {
            navItemMyBooks.setBackgroundResource(R.drawable.bg_nav_selected);
            iconMyBooks.setColorFilter(primaryColor, android.graphics.PorterDuff.Mode.SRC_IN);
            labelMyBooks.setTextColor(primaryColor);
        } else if (navItemId == R.id.navItemSearch) {
            navItemSearch.setBackgroundResource(R.drawable.bg_nav_selected);
            iconSearch.setColorFilter(primaryColor, android.graphics.PorterDuff.Mode.SRC_IN);
            labelSearch.setTextColor(primaryColor);
        } else if (navItemId == R.id.navItemSettings) {
            navItemSettings.setBackgroundResource(R.drawable.bg_nav_selected);
            iconSettings.setColorFilter(primaryColor, android.graphics.PorterDuff.Mode.SRC_IN);
            labelSettings.setTextColor(primaryColor);
        }
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

