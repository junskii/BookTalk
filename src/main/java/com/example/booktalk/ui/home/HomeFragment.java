package com.example.booktalk.ui.home;

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
import com.example.booktalk.controller.HomeController;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Category;
import com.example.booktalk.ui.detail.BookDetailActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Home Fragment showing categories with horizontal book lists (MVC)
 */
public class HomeFragment extends Fragment {
    private RecyclerView rvCategories;
    private HomeController homeController;
    private CategoryAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        homeController = new HomeController(requireContext());
        
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        adapter = new CategoryAdapter(new ArrayList<>(), book -> {
            Intent intent = new Intent(requireContext(), BookDetailActivity.class);
            intent.putExtra("book_id", book.getBookId());
            startActivity(intent);
        });
        rvCategories.setAdapter(adapter);
        
        homeController.loadCategories(new HomeController.Callback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                adapter.updateCategories(categories);
            }
            
            @Override
            public void onError(String message) {
                // Show error toast or snackbar
            }
        });
    }
}

