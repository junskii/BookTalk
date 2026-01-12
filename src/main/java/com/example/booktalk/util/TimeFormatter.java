package com.example.booktalk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility to format time for reviews (today, yesterday, or date)
 */
public class TimeFormatter {
    
    /**
     * Format timestamp to "today HH:mm", "yesterday", or date
     * @param timestamp Timestamp in milliseconds
     * @return Formatted string
     */
    public static String formatReviewTime(long timestamp) {
        if (timestamp <= 0) {
            return "";
        }
        
        Calendar now = Calendar.getInstance();
        Calendar reviewTime = Calendar.getInstance();
        reviewTime.setTimeInMillis(timestamp);
        
        // Check if same day
        if (now.get(Calendar.YEAR) == reviewTime.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == reviewTime.get(Calendar.DAY_OF_YEAR)) {
            // Today - show time
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return "today " + timeFormat.format(new Date(timestamp));
        }
        
        // Check if yesterday
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (reviewTime.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
            reviewTime.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "yesterday";
        }
        
        // Otherwise show date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
}

