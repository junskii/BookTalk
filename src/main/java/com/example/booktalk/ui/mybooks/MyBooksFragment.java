package com.example.booktalk.ui.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktalk.R;
import com.example.booktalk.controller.MyBooksController;
import com.example.booktalk.data.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * My Books Fragment showing user's want-to-read books (MVC)
 */
public class MyBooksFragment extends Fragment {
    private RecyclerView rvBooks;
    private MyBooksController myBooksController;
    private MyBooksAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_books, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        myBooksController = new MyBooksController(requireContext());
        
        rvBooks = view.findViewById(R.id.rvBooks);
        rvBooks.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new MyBooksAdapter(new ArrayList<>());
        rvBooks.setAdapter(adapter);
        
        loadMyBooks();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadMyBooks();
    }
    
    private void loadMyBooks() {
        myBooksController.loadMyBooks(new MyBooksController.Callback() {
            @Override
            public void onBooksLoaded(List<Book> books) {
                adapter.updateBooks(books);
            }
            
            @Override
            public void onError(String message) {
                // Show error
            }
        });
    }
}

