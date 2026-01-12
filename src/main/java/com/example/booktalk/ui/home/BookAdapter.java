package com.example.booktalk.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.booktalk.R;
import com.example.booktalk.data.model.Book;
import java.util.List;

/**
 * Adapter for horizontal book list
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private OnBookClickListener listener;
    
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }
    
    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }
    
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_cover, parent, false);
        return new BookViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }
    
    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }
    
    class BookViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookClick(books.get(position));
                }
            });
        }
        
        public void bind(Book book) {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(book.getCoverUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(ivCover);
            } else {
                ivCover.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}

