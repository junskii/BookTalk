package com.example.booktalk.controller;

import android.content.Context;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.repository.MyBooksRepository;
import com.example.booktalk.util.Prefs;
import java.util.List;

/**
 * Controller for My Books (MVC)
 */
public class MyBooksController {
    private MyBooksRepository myBooksRepository;
    private Prefs prefs;
    
    public interface Callback {
        void onBooksLoaded(List<Book> books);
        void onError(String message);
    }
    
    public MyBooksController(Context context) {
        AppDbHelper dbHelper = new AppDbHelper(context);
        this.myBooksRepository = new MyBooksRepository(dbHelper);
        this.prefs = new Prefs(context);
    }
    
    public void loadMyBooks(Callback callback) {
        int userId = prefs.getCurrentUserId();
        if (userId == -1) {
            callback.onError("Please login first");
            return;
        }
        
        List<Book> books = myBooksRepository.getMyBooks(userId);
        callback.onBooksLoaded(books);
    }
}

