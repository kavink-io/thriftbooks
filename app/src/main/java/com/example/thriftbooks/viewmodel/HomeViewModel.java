package com.example.thriftbooks.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.thriftbooks.model.Book;
import com.example.thriftbooks.repository.BookRepository;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final BookRepository repository;
    private LiveData<List<Book>> featuredBooks;

    public HomeViewModel() {
        repository = BookRepository.getInstance();
    }

    public LiveData<List<Book>> getFeaturedBooks() {
        if (featuredBooks == null) {
            featuredBooks = repository.getFeaturedBooks();
        }
        return featuredBooks;
    }
}
