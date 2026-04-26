package com.example.thriftbooks.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.thriftbooks.api.ApiService;
import com.example.thriftbooks.model.Book;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {
    private static BookRepository instance;
    private ApiService apiService;

    private BookRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mock.thriftbooks.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public LiveData<List<Book>> getFeaturedBooks() {
        MutableLiveData<List<Book>> data = new MutableLiveData<>();
        
        // Using reliable public CDN book covers for the demo
        List<Book> dummyBooks = new ArrayList<>();
        dummyBooks.add(new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", 4.19, 12.99, 
            "https://covers.openlibrary.org/b/id/8432047-L.jpg", 4.5f, 1250, "Used - Like New"));
        dummyBooks.add(new Book("2", "1984", "George Orwell", 5.49, 15.00, 
            "https://www.penguin.co.uk/_next/image?url=https%3A%2F%2Fcdn.penguin.co.uk%2Fdam-assets%2Fbooks%2F9780141036144%2F9780141036144-jacket-large.jpg&w=614&q=100", 4.8f, 3200, "Used - Very Good"));
        dummyBooks.add(new Book("3", "To Kill a Mockingbird", "Harper Lee", 4.79, 14.99, 
            "https://images.squarespace-cdn.com/content/v1/5fa57aacf5b0a90a76b0d7cc/1620985843093-9UTAERL56BB8AHMOY2H1/Harper+Lee+-+To+Kill+A+Mockingbird.jpg", 4.9f, 5400, "Used - Good"));
        dummyBooks.add(new Book("4", "The Catcher in the Rye", "J.D. Salinger", 4.25, 10.99, 
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/The_Catcher_in_the_Rye_%281951%2C_first_edition_cover%29.jpg/250px-The_Catcher_in_the_Rye_%281951%2C_first_edition_cover%29.jpg", 4.2f, 2100, "New"));
        dummyBooks.add(new Book("5", "The Hobbit", "J.R.R. Tolkien", 6.99, 18.00, 
            "https://m.media-amazon.com/images/I/81mCE+uclxL._UF1000,1000_QL80_.jpg", 4.7f, 4500, "Used - Like New"));
        
        data.setValue(dummyBooks);
        return data;
    }
}
