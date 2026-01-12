package com.example.booktalk.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit service interface for Google Books API
 */
public interface BooksApiService {
    
    String BASE_URL = "https://www.googleapis.com/books/v1/";
    
    /**
     * Search books by query
     * @param query Search query
     * @param maxResults Maximum number of results (default 40)
     * @param printType Filter by print type (books)
     * @param orderBy Sort order: "relevance" (most popular first) or "newest"
     * @param langRestrict Language restriction (e.g., "en" for English)
     * @param country Country code (e.g., "US" for United States)
     * @param startIndex Starting index for pagination (0-based)
     * @param key API key
     */
    @GET("volumes")
    Call<BooksResponse> searchBooks(
            @Query("q") String query,
            @Query("maxResults") int maxResults,
            @Query("printType") String printType,
            @Query("orderBy") String orderBy,
            @Query("langRestrict") String langRestrict,
            @Query("country") String country,
            @Query("startIndex") Integer startIndex,
            @Query("key") String key
    );
    
    /**
     * Get book details by volume ID
     * @param volumeId Google Books volume ID
     * @param country Country code (e.g., "US" for United States)
     * @param key API key
     */
    @GET("volumes/{volumeId}")
    Call<VolumeResponse> getBookDetails(
            @Path("volumeId") String volumeId,
            @Query("country") String country,
            @Query("key") String key
    );
}

