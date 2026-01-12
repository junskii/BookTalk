package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.repository.SearchRepository;
import java.util.List;

/**
 * Controller for Search (MVC)
 */
public class SearchController {
    private SearchRepository searchRepository;
    
    public interface Callback {
        void onSearchResults(List<Book> books);
        void onError(String error);
    }
    
    public SearchController(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.searchRepository = new SearchRepository(dbHelper);
    }
    
    public void search(String query, Callback callback) {
        searchRepository.searchBooksAsync(query, new SearchRepository.SearchCallback() {
            @Override
            public void onSearchResults(List<Book> books) {
                callback.onSearchResults(books);
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}

