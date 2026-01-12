package com.example.booktalk.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktalk.R;
import com.example.booktalk.controller.SearchController;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.ui.mybooks.MyBooksAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Search Fragment (MVC)
 */
public class SearchFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView rvResults;
    private SearchController searchController;
    private MyBooksAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        searchController = new SearchController(requireContext());
        
        etSearch = view.findViewById(R.id.etSearch);
        rvResults = view.findViewById(R.id.rvResults);
        rvResults.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        
        adapter = new MyBooksAdapter(new ArrayList<>());
        rvResults.setAdapter(adapter);
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    searchController.search(query, new SearchController.Callback() {
                        @Override
                        public void onSearchResults(List<Book> books) {
                            adapter.updateBooks(books);
                        }
                        
                        @Override
                        public void onError(String error) {
                            // Show error
                        }
                    });
                } else {
                    adapter.updateBooks(new ArrayList<>());
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}

