package com.example.booktalk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility to parse published dates from Google Books API
 * Supports formats: YYYY, YYYY-MM, YYYY-MM-DD
 */
public class DateParser {
    
    private static final String[] DATE_FORMATS = {
        "yyyy-MM-dd",
        "yyyy-MM",
        "yyyy"
    };
    
    /**
     * Parse published date string to Date object
     * @param dateString Date string from API (can be YYYY, YYYY-MM, or YYYY-MM-DD)
     * @return Date object, or null if parsing fails
     */
    public static Date parsePublishedDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = dateString.trim();
        
        // Try each format
        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                sdf.setLenient(false);
                return sdf.parse(trimmed);
            } catch (ParseException e) {
                // Try next format
            }
        }
        
        return null;
    }
    
    /**
     * Get year from published date string
     * @param dateString Date string from API
     * @return Year as integer, or -1 if parsing fails
     */
    public static int getYear(String dateString) {
        Date date = parsePublishedDate(dateString);
        if (date == null) {
            return -1;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * Check if date is within last N years
     * @param dateString Date string from API
     * @param years Number of years to check
     * @return true if date is within last N years, false otherwise
     */
    public static boolean isWithinLastYears(String dateString, int years) {
        int year = getYear(dateString);
        if (year == -1) {
            return false;
        }
        
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int cutoffYear = currentYear - years;
        
        return year >= cutoffYear;
    }
}

