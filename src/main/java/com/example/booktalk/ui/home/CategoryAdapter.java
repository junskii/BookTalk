package com.example.booktalk.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktalk.R;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Category;
import java.util.List;

/**
 * Adapter for category list (vertical)
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private OnBookClickListener bookClickListener;
    
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
    
    public CategoryAdapter(List<Category> categories, OnBookClickListener bookClickListener) {
        this.categories = categories;
        this.bookClickListener = bookClickListener;
    }
    
    public void updateCategories(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }
    
    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
    
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private RecyclerView rvBooks;
        
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            rvBooks = itemView.findViewById(R.id.rvBooks);
            
            // Enable nested scrolling for horizontal RecyclerView
            rvBooks.setNestedScrollingEnabled(false);
            
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            rvBooks.setLayoutManager(layoutManager);
        }
        
        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
            
            // Convert CategoryAdapter.OnBookClickListener to BookAdapter.OnBookClickListener
            BookAdapter.OnBookClickListener bookAdapterListener = book -> {
                if (bookClickListener != null) {
                    bookClickListener.onBookClick(book);
                }
            };
            
            BookAdapter bookAdapter = new BookAdapter(
                    category.getBooks() != null ? category.getBooks() : new java.util.ArrayList<>(),
                    bookAdapterListener
            );
            rvBooks.setAdapter(bookAdapter);
        }
    }
}

