package com.example.booktalk.ui.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.booktalk.R;
import com.example.booktalk.controller.BookDetailController;
import com.example.booktalk.data.model.Book;
import com.example.booktalk.data.model.Review;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Book Detail Activity with collapsing toolbar (MVC)
 */
public class BookDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private ImageView ivCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private MaterialButton btnWantToRead;
    private TextView tvDescription;
    private RecyclerView rvReviews;
    private TextView tvToolbarTitle;
    private TextView tvToolbarAuthor;
    private View toolbarHeader;
    private LinearLayout ratingStarsContainer;
    private ImageView star1, star2, star3, star4, star5;
    private EditText etReview;
    private MaterialButton btnSubmitReview;
    private int selectedRating = 0;
    
    private BookDetailController bookDetailController;
    private ReviewAdapter reviewAdapter;
    private String bookId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        
        // Enable edge-to-edge and handle system bars
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        bookId = getIntent().getStringExtra("book_id");
        if (bookId == null) {
            finish();
            return;
        }
        
        bookDetailController = new BookDetailController(this, bookId);
        
        setupViews();
        setupCollapsingToolbar();
        
        loadBook();
        loadReviews();
        loadUserReview();
    }
    
    private void loadBook() {
        bookDetailController.loadBook(bookId, new BookDetailController.BookCallback() {
            @Override
            public void onBookLoaded(Book book) {
                showBook(book);
                bookDetailController.getWantToReadStatus(bookId, new BookDetailController.WantToReadCallback() {
                    @Override
                    public void onWantToReadUpdated(boolean isWantToRead) {
                        updateWantToReadButton(isWantToRead);
                    }
                    
                    @Override
                    public void onError(String error) {
                        // Ignore error for want to read status
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                android.widget.Toast.makeText(BookDetailActivity.this, error, android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadReviews() {
        bookDetailController.loadReviews(bookId, new BookDetailController.ReviewCallback() {
            @Override
            public void onReviewsLoaded(List<Review> reviews) {
                showReviews(reviews);
            }
            
            @Override
            public void onReviewSubmitted() {
                // Not used here
            }
            
            @Override
            public void onError(String error) {
                // Ignore error
            }
        });
    }
    
    private void loadUserReview() {
        Review userReview = bookDetailController.getUserReview(bookId);
        if (userReview != null) {
            updateRatingStars(userReview.getRating());
            etReview.setText(userReview.getReviewText());
        }
    }
    
    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        ivCover = findViewById(R.id.ivCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        btnWantToRead = findViewById(R.id.btnWantToRead);
        tvDescription = findViewById(R.id.tvDescription);
        rvReviews = findViewById(R.id.rvReviews);
        toolbarHeader = findViewById(R.id.toolbarHeader);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarAuthor = findViewById(R.id.tvToolbarAuthor);
        ratingStarsContainer = findViewById(R.id.ratingStarsContainer);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        etReview = findViewById(R.id.etReview);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        // Set initial navigation icon color to green (expanded state)
        updateNavigationIconColor(false); // false = expanded, green
        
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Handle system bars for toolbar (add padding top for status bar)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top + 8, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });
        
        btnWantToRead.setOnClickListener(v -> {
            bookDetailController.toggleWantToRead(bookId, new BookDetailController.WantToReadCallback() {
                @Override
                public void onWantToReadUpdated(boolean isWantToRead) {
                    updateWantToReadButton(isWantToRead);
                }
                
                @Override
                public void onError(String error) {
                    android.widget.Toast.makeText(BookDetailActivity.this, error, android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
        
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(new ArrayList<>());
        rvReviews.setAdapter(reviewAdapter);
        
        // Initially hide toolbar header
        toolbarHeader.setAlpha(0f);
        toolbarHeader.setVisibility(View.GONE);
        
        // Setup rating stars
        setupRatingStars();
        
        // Setup submit review button
        btnSubmitReview.setOnClickListener(v -> {
            String reviewText = etReview.getText().toString().trim();
            if (selectedRating == 0) {
                android.widget.Toast.makeText(this, "Please select a rating", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if (reviewText.isEmpty()) {
                android.widget.Toast.makeText(this, "Please write a review", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            bookDetailController.submitReview(bookId, selectedRating, reviewText, new BookDetailController.ReviewCallback() {
                @Override
                public void onReviewsLoaded(List<Review> reviews) {
                    // Not used here
                }
                
                @Override
                public void onReviewSubmitted() {
                    onReviewSubmittedSuccess();
                }
                
                @Override
                public void onError(String error) {
                    android.widget.Toast.makeText(BookDetailActivity.this, error, android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    
    private void setupRatingStars() {
        View.OnClickListener starClickListener = v -> {
            int rating = 0;
            if (v == star1) rating = 1;
            else if (v == star2) rating = 2;
            else if (v == star3) rating = 3;
            else if (v == star4) rating = 4;
            else if (v == star5) rating = 5;
            
            // Update UI immediately
            updateRatingStars(rating);
        };
        
        star1.setOnClickListener(starClickListener);
        star2.setOnClickListener(starClickListener);
        star3.setOnClickListener(starClickListener);
        star4.setOnClickListener(starClickListener);
        star5.setOnClickListener(starClickListener);
        
        // Ensure stars are clickable
        star1.setClickable(true);
        star2.setClickable(true);
        star3.setClickable(true);
        star4.setClickable(true);
        star5.setClickable(true);
    }
    
    public void updateRatingStars(int rating) {
        selectedRating = rating;
        star1.setImageResource(rating >= 1 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star2.setImageResource(rating >= 2 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star3.setImageResource(rating >= 3 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star4.setImageResource(rating >= 4 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        star5.setImageResource(rating >= 5 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }
    
    private void updateNavigationIconColor(boolean isCollapsed) {
        if (toolbar.getNavigationIcon() != null) {
            int color = isCollapsed 
                ? androidx.core.content.ContextCompat.getColor(this, R.color.white)  // White when collapsed (green header)
                : androidx.core.content.ContextCompat.getColor(this, R.color.color_primary); // Green when expanded
            
            // Get a mutable copy of the drawable to avoid affecting the original
            android.graphics.drawable.Drawable drawable = toolbar.getNavigationIcon();
            if (drawable != null) {
                drawable = androidx.core.graphics.drawable.DrawableCompat.wrap(drawable.mutate());
                androidx.core.graphics.drawable.DrawableCompat.setTint(drawable, color);
                toolbar.setNavigationIcon(drawable);
            }
        }
    }
    
    private void setupCollapsingToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                
                if (scrollRange + verticalOffset == 0) {
                    // Collapsed - ensure toolbar is visible with background
                    if (!isShow) {
                        isShow = true;
                        toolbarHeader.setVisibility(View.VISIBLE);
                        toolbarHeader.animate().alpha(1f).setDuration(200).start();
                    }
                    // Ensure toolbar background is visible when collapsed
                    toolbar.setBackgroundColor(getResources().getColor(R.color.color_primary));
                    // Change navigation icon to white for contrast
                    updateNavigationIconColor(true);
                } else {
                    // Expanded
                    if (isShow) {
                        isShow = false;
                        toolbarHeader.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                            toolbarHeader.setVisibility(View.GONE);
                        }).start();
                    }
                    // Make toolbar transparent when expanded
                    toolbar.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                    // Change navigation icon to green
                    updateNavigationIconColor(false);
                }
            }
        });
    }
    
    private void showBook(Book book) {
        if (book == null) return;
        
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvToolbarTitle.setText(book.getTitle());
        tvToolbarAuthor.setText(book.getAuthor());
        
        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            tvDescription.setText(book.getDescription());
        } else {
            tvDescription.setText("No description available");
        }
        
        if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            Glide.with(this)
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .fitCenter()
                    .into(ivCover);
        } else {
            ivCover.setImageResource(R.drawable.ic_launcher_background);
        }
    }
    
    private void showReviews(List<Review> reviews) {
        reviewAdapter.updateReviews(reviews);
    }
    
    private void onReviewSubmittedSuccess() {
        // Clear form
        selectedRating = 0;
        updateRatingStars(0);
        etReview.setText("");
        
        // Reload reviews to show the new one
        loadReviews();
        
        android.widget.Toast.makeText(this, "Review submitted successfully", android.widget.Toast.LENGTH_SHORT).show();
    }
    
    private void updateWantToReadButton(boolean isWantToRead) {
        // Keep text as "Want to Read" but change appearance based on state
        if (isWantToRead) {
            // Book is in My Books - make button appear pressed/disabled (pudar)
            btnWantToRead.setAlpha(0.5f);
            // Keep enabled so user can click to remove
        } else {
            // Book is not in My Books - make button normal
            btnWantToRead.setAlpha(1.0f);
        }
    }
    
}

