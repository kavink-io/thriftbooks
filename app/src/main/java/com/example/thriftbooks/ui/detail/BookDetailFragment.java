package com.example.thriftbooks.ui.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.thriftbooks.R;
import com.example.thriftbooks.databinding.FragmentBookDetailBinding;
import com.example.thriftbooks.model.Book;
import com.example.thriftbooks.model.CartItem;
import com.example.thriftbooks.viewmodel.HomeViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookDetailFragment extends Fragment {

    private FragmentBookDetailBinding binding;
    private Book currentBook;
    private HomeViewModel viewModel;
    private boolean isWishlisted = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        
        if (getArguments() != null) {
            String bookId = getArguments().getString("bookId");
            if (bookId != null) {
                observeBookDetails(bookId);
            }
        }
    }

    private void observeBookDetails(String bookId) {
        viewModel.getFeaturedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                for (Book book : books) {
                    if (book.getId().equals(bookId)) {
                        currentBook = book;
                        checkIfWishlisted();
                        displayBookData();
                        break;
                    }
                }
            }
        });
    }

    private void checkIfWishlisted() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        if (currentEmail.isEmpty()) return;

        String wishlistKey = "wishlist_" + currentEmail;
        String jsonWishlist = sharedPref.getString(wishlistKey, null);
        if (jsonWishlist != null) {
            List<Book> wishlistItems = new Gson().fromJson(jsonWishlist, new TypeToken<List<Book>>(){}.getType());
            for (Book book : wishlistItems) {
                if (book.getId().equals(currentBook.getId())) {
                    isWishlisted = true;
                    updateWishlistIcon(true);
                    break;
                }
            }
        }
    }

    private void displayBookData() {
        if (currentBook == null) return;

        binding.detailTitle.setText(currentBook.getTitle());
        binding.detailAuthor.setText("by " + currentBook.getAuthor());
        binding.detailPrice.setText(String.format(Locale.US, "₹%.2f", currentBook.getPrice() * 83));
        binding.detailRating.setRating(currentBook.getRating());
        
        binding.detailDescription.setText(currentBook.getTitle() + " by " + currentBook.getAuthor() + 
            " is a highly acclaimed book available at ThriftBooks.");

        Glide.with(this)
                .load(currentBook.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.detailImage);

        binding.addToCartButton.setOnClickListener(v -> addToCart());
        binding.btnAddToWishlist.setOnClickListener(v -> toggleWishlist());
    }

    private void updateWishlistIcon(boolean active) {
        if (active) {
            binding.btnAddToWishlist.setImageTintList(ColorStateList.valueOf(Color.RED));
        } else {
            binding.btnAddToWishlist.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }

    private void toggleWishlist() {
        if (currentBook == null) return;
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        if (!sharedPref.getBoolean("isLoggedIn", false)) {
            Toast.makeText(getContext(), "Please login to manage wishlist", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = sharedPref.getString("currentEmail", "");
        String wishlistKey = "wishlist_" + currentEmail;
        Gson gson = new Gson();
        String jsonWishlist = sharedPref.getString(wishlistKey, null);
        List<Book> wishlistItems = jsonWishlist == null ? new ArrayList<>() : gson.fromJson(jsonWishlist, new TypeToken<List<Book>>(){}.getType());

        if (isWishlisted) {
            // Remove from wishlist
            wishlistItems.removeIf(book -> book.getId().equals(currentBook.getId()));
            isWishlisted = false;
            updateWishlistIcon(false);
            Toast.makeText(getContext(), "Removed from Wishlist", Toast.LENGTH_SHORT).show();
        } else {
            // Add to wishlist
            wishlistItems.add(currentBook);
            isWishlisted = true;
            updateWishlistIcon(true);
            Toast.makeText(getContext(), "Added to Wishlist!", Toast.LENGTH_SHORT).show();
        }

        sharedPref.edit().putString(wishlistKey, gson.toJson(wishlistItems)).apply();
    }

    private void addToCart() {
        if (currentBook == null) return;
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        if (!sharedPref.getBoolean("isLoggedIn", false)) {
            Toast.makeText(getContext(), "Please login to add items to cart", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = sharedPref.getString("currentEmail", "");
        String cartKey = "cart_" + currentEmail;
        Gson gson = new Gson();
        String jsonCart = sharedPref.getString(cartKey, null);
        List<CartItem> cartItems = jsonCart == null ? new ArrayList<>() : gson.fromJson(jsonCart, new TypeToken<List<CartItem>>(){}.getType());

        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getBook().getId().equals(currentBook.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        if (!found) cartItems.add(new CartItem(currentBook, 1));

        sharedPref.edit().putString(cartKey, gson.toJson(cartItems)).apply();
        Toast.makeText(getContext(), "Added to your cart!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
