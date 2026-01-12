package com.example.booktalk.data.remote;

import com.example.booktalk.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

/**
 * Retrofit client singleton
 */
public class RetrofitClient {
    private static RetrofitClient instance;
    private BooksApiService apiService;
    
    private RetrofitClient() {
        // Setup logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // Setup OkHttp client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        // Setup Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BooksApiService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        apiService = retrofit.create(BooksApiService.class);
    }
    
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    
    public BooksApiService getApiService() {
        return apiService;
    }
    
    public static String getApiKey() {
        return BuildConfig.GOOGLE_BOOKS_API_KEY;
    }
}

