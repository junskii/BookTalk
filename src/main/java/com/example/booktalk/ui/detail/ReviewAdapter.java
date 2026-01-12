package com.example.booktalk.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktalk.R;
import com.example.booktalk.data.model.Review;
import com.example.booktalk.util.TimeFormatter;
import java.util.List;

/**
 * Adapter for review list
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    
    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public void updateReviews(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }
    
    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }
    
    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatar;
        private TextView tvUsername;
        private ImageView star1, star2, star3, star4, star5;
        private TextView tvTime;
        private TextView tvReviewText;
        
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
        }
        
        public void bind(Review review) {
            tvUsername.setText(review.getUserName() != null ? review.getUserName() : "Anonymous");
            
            // Update rating stars (golden stars)
            int rating = review.getRating();
            star1.setImageResource(rating >= 1 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            star2.setImageResource(rating >= 2 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            star3.setImageResource(rating >= 3 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            star4.setImageResource(rating >= 4 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            star5.setImageResource(rating >= 5 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            
            // Format and display time
            String timeText = TimeFormatter.formatReviewTime(review.getCreatedAt());
            tvTime.setText(timeText);
            
            tvReviewText.setText(review.getReviewText());
            
            // Set default avatar (you can use Glide here if you have avatar URLs)
            ivAvatar.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}

