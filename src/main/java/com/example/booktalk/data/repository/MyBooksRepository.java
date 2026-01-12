package com.example.booktalk.data.repository;

import android.database.sqlite.SQLiteDatabase;
import com.example.booktalk.data.dao.MyBooksDao;
import com.example.booktalk.data.db.AppDbHelper;
import com.example.booktalk.data.model.Book;
import java.util.List;

/**
 * Repository for My Books operations
 */
public class MyBooksRepository {
    private AppDbHelper dbHelper;
    private MyBooksDao myBooksDao;
    
    public MyBooksRepository(AppDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        this.myBooksDao = new MyBooksDao(db);
    }
    
    /**
     * Toggle want to read status for a book
     */
    public boolean toggleWantToRead(int userId, String bookId) {
        boolean isInMyBooks = myBooksDao.isInMyBooks(userId, bookId);
        
        if (isInMyBooks) {
            // Remove from my books
            myBooksDao.removeFromMyBooks(userId, bookId);
            return false;
        } else {
            // Add to my books
            myBooksDao.addToMyBooks(userId, bookId);
            return true;
        }
    }
    
    /**
     * Check if book is in user's want to read list
     */
    public boolean isWantToRead(int userId, String bookId) {
        return myBooksDao.isInMyBooks(userId, bookId);
    }
    
    /**
     * Get all books for a user
     */
    public List<Book> getMyBooks(int userId) {
        return myBooksDao.getMyBooks(userId);
    }
}

