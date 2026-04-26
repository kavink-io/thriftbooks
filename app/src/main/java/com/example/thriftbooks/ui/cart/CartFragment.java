package com.example.thriftbooks.ui.cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.thriftbooks.R;
import com.example.thriftbooks.adapter.CartAdapter;
import com.example.thriftbooks.databinding.FragmentCartBinding;
import com.example.thriftbooks.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private String cartKey;
    private SharedPreferences sharedPref;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            binding.emptyCartView.setVisibility(View.VISIBLE);
            binding.checkoutSection.setVisibility(View.GONE);
            return;
        }

        String currentEmail = sharedPref.getString("currentEmail", "");
        cartKey = "cart_" + currentEmail;

        setupCartList();
        loadUserCart();
        
        View.OnClickListener checkoutListener = v -> 
            Navigation.findNavController(v).navigate(R.id.action_cartFragment_to_checkoutFragment);
            
        binding.btnCheckout.setOnClickListener(checkoutListener);
        binding.btnBuyNow.setOnClickListener(checkoutListener);
        
        binding.btnShopNow.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.homeFragment)
        );
    }

    private void setupCartList() {
        adapter = new CartAdapter();
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        adapter.setOnCartItemChangeListener(new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                item.setQuantity(newQuantity);
                saveCart();
                updateTotals();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRemoved(CartItem item) {
                cartItems.remove(item);
                saveCart();
                adapter.setCartItems(cartItems);
                updateTotals();
            }
        });
    }

    private void loadUserCart() {
        String jsonCart = sharedPref.getString(cartKey, null);
        if (jsonCart != null) {
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            cartItems = gson.fromJson(jsonCart, type);
        } else {
            cartItems = new ArrayList<>();
        }
        
        adapter.setCartItems(cartItems);
        updateTotals();
    }

    private void saveCart() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(cartKey, gson.toJson(cartItems));
        editor.apply();
    }

    private void updateTotals() {
        if (cartItems.isEmpty()) {
            binding.emptyCartView.setVisibility(View.VISIBLE);
            binding.checkoutSection.setVisibility(View.GONE);
        } else {
            binding.emptyCartView.setVisibility(View.GONE);
            binding.checkoutSection.setVisibility(View.VISIBLE);
            
            double subtotal = 0;
            for (CartItem item : cartItems) {
                subtotal += item.getTotalPrice();
            }
            binding.tvSubtotal.setText(String.format(Locale.US, "₹%.2f", subtotal * 83));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
