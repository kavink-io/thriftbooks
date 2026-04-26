package com.example.thriftbooks.api;

import com.example.thriftbooks.model.Book;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("books/featured")
    Call<List<Book>> getFeaturedBooks();

    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("q") String query);

    @GET("books/category")
    Call<List<Book>> getBooksByCategory(@Query("category") String category);
}
