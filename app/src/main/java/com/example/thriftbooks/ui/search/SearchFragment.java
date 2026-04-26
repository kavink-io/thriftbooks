package com.example.thriftbooks.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.thriftbooks.R;
import com.example.thriftbooks.adapter.BookAdapter;
import com.example.thriftbooks.databinding.FragmentSearchBinding;
import com.example.thriftbooks.model.Book;
import com.example.thriftbooks.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private HomeViewModel viewModel;
    private BookAdapter searchAdapter;
    private List<Book> originalList = new ArrayList<>();
    private List<Book> currentList = new ArrayList<>();
    private String searchQuery;
    private String categoryFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        if (getArguments() != null) {
            searchQuery = getArguments().getString("searchQuery");
            categoryFilter = getArguments().getString("category");
        }

        setupRecyclerView();
        observeViewModel();
        setupButtons();
    }

    private void setupRecyclerView() {
        searchAdapter = new BookAdapter();
        binding.searchResultRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.searchResultRecyclerView.setAdapter(searchAdapter);

        // Click listener to show details
        searchAdapter.setOnBookClickListener(book -> {
            Bundle bundle = new Bundle();
            bundle.putString("bookId", book.getId());
            Navigation.findNavController(requireView()).navigate(R.id.bookDetailFragment, bundle);
        });
    }

    private void observeViewModel() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        viewModel.getFeaturedBooks().observe(getViewLifecycleOwner(), books -> {
            binding.searchProgressBar.setVisibility(View.GONE);
            if (books != null) {
                originalList = books;
                applySearchAndFilter(null);
            }
        });
    }

    private void setupButtons() {
        binding.btnSort.setOnClickListener(v -> showSortDialog());
        binding.btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    private void showSortDialog() {
        if (currentList == null || currentList.isEmpty()) return;

        String[] options = {"Price: Low to High", "Price: High to Low", "Customer Rating"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Sort By")
                .setItems(options, (dialog, which) -> {
                    List<Book> sortedList = new ArrayList<>(currentList);
                    if (which == 0) {
                        Collections.sort(sortedList, (b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()));
                    } else if (which == 1) {
                        Collections.sort(sortedList, (b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()));
                    } else if (which == 2) {
                        Collections.sort(sortedList, (b1, b2) -> Float.compare(b2.getRating(), b1.getRating()));
                    }
                    currentList = sortedList;
                    searchAdapter.setBooks(new ArrayList<>(currentList));
                })
                .show();
    }

    private void showFilterDialog() {
        String[] options = {"All Conditions", "New", "Used - Like New", "Used - Very Good"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Filter By Condition")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        applySearchAndFilter(null);
                    } else {
                        applySearchAndFilter(options[which]);
                    }
                })
                .show();
    }

    private void applySearchAndFilter(String condition) {
        List<Book> filteredList = new ArrayList<>();
        for (Book book : originalList) {
            boolean matchesSearch = searchQuery == null || 
                book.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) || 
                book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase());
            
            boolean matchesCondition = condition == null || book.getCondition().equals(condition);
            
            if (matchesSearch && matchesCondition) {
                filteredList.add(book);
            }
        }

        currentList = filteredList;
        if (currentList.isEmpty()) {
            binding.emptyResultText.setVisibility(View.VISIBLE);
            searchAdapter.setBooks(new ArrayList<>());
        } else {
            binding.emptyResultText.setVisibility(View.GONE);
            searchAdapter.setBooks(new ArrayList<>(currentList));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
