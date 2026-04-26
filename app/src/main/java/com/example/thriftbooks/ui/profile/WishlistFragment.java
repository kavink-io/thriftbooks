package com.example.thriftbooks.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.thriftbooks.R;
import com.example.thriftbooks.adapter.BookAdapter;
import com.example.thriftbooks.databinding.FragmentWishlistBinding;
import com.example.thriftbooks.model.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private FragmentWishlistBinding binding;
    private BookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        loadWishlist();

        binding.btnWishlistShopNow.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.homeFragment)
        );
    }

    private void setupRecyclerView() {
        adapter = new BookAdapter();
        binding.wishlistRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.wishlistRecyclerView.setAdapter(adapter);

        adapter.setOnBookClickListener(book -> {
            Bundle bundle = new Bundle();
            bundle.putString("bookId", book.getId());
            Navigation.findNavController(requireView()).navigate(R.id.bookDetailFragment, bundle);
        });
    }

    private void loadWishlist() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        String wishlistKey = "wishlist_" + currentEmail;

        Gson gson = new Gson();
        String jsonWishlist = sharedPref.getString(wishlistKey, null);

        if (jsonWishlist == null) {
            showEmptyView();
        } else {
            Type type = new TypeToken<List<Book>>() {}.getType();
            List<Book> wishlistItems = gson.fromJson(jsonWishlist, type);

            if (wishlistItems == null || wishlistItems.isEmpty()) {
                showEmptyView();
            } else {
                binding.emptyWishlistContainer.setVisibility(View.GONE);
                binding.wishlistRecyclerView.setVisibility(View.VISIBLE);
                adapter.setBooks(wishlistItems);
            }
        }
    }

    private void showEmptyView() {
        binding.emptyWishlistContainer.setVisibility(View.VISIBLE);
        binding.wishlistRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
