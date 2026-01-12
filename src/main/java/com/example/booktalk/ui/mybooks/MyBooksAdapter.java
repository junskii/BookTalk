package com.example.booktalk.ui.mybooks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.booktalk.R;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.ui.detail.BookDetailActivity;
import com.example.booktalk.util.DateParser;
import java.util.List;

/**
 * Adapter for My Books list
 */
public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.BookViewHolder> {
    private List<Book> books;
    
    public MyBooksAdapter(List<Book> books) {
        this.books = books;
    }
    
    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list, parent, false);
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
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvYear;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvYear = itemView.findViewById(R.id.tvYear);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Book book = books.get(position);
                    Intent intent = new Intent(itemView.getContext(), BookDetailActivity.class);
                    intent.putExtra("book_id", book.getBookId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
        
        public void bind(Book book) {
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(book.getAuthor() != null ? book.getAuthor() : "Unknown Author");
            
            // Display year from publishedDate
            if (book.getPublishedDate() != null && !book.getPublishedDate().isEmpty()) {
                int year = DateParser.getYear(book.getPublishedDate());
                if (year != -1) {
                    tvYear.setText(String.valueOf(year));
                } else {
                    tvYear.setText("");
                }
            } else {
                tvYear.setText("");
            }
            
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(book.getCoverUrl())
                        .placeholder(R.drawable.ic_launcher_background)
                        .fitCenter()
                        .into(ivCover);
            } else {
                ivCover.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}

