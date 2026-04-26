package com.example.thriftbooks.ui.checkout;

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
import com.example.thriftbooks.R;
import com.example.thriftbooks.databinding.FragmentCheckoutBinding;
import com.example.thriftbooks.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutFragment extends Fragment {

    private FragmentCheckoutBinding binding;
    private List<CartItem> currentCartItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        calculateTotal();

        binding.btnPlaceOrder.setOnClickListener(v -> {
            if (binding.rgUpi.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            saveOrder();
            Toast.makeText(getContext(), "Payment Successful! Order Placed.", Toast.LENGTH_LONG).show();
            clearCart();
            Navigation.findNavController(v).navigate(R.id.homeFragment);
        });
    }

    private void calculateTotal() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        String cartKey = "cart_" + currentEmail;
        
        String jsonCart = sharedPref.getString(cartKey, null);
        if (jsonCart != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            currentCartItems = gson.fromJson(jsonCart, type);
            
            double total = 0;
            for (CartItem item : currentCartItems) {
                total += item.getTotalPrice();
            }
            binding.tvCheckoutTotal.setText(String.format(Locale.US, "₹%.2f", total * 83));
        }
    }

    private void saveOrder() {
        if (currentCartItems.isEmpty()) return;

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        String ordersKey = "orders_" + currentEmail;

        Gson gson = new Gson();
        String jsonOrders = sharedPref.getString(ordersKey, null);
        List<List<CartItem>> allOrders;

        if (jsonOrders == null) {
            allOrders = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<List<CartItem>>>() {}.getType();
            allOrders = gson.fromJson(jsonOrders, type);
        }

        // Add current order date prefix to items or handle as separate objects
        // For simplicity, we store the list of items as one order
        allOrders.add(new ArrayList<>(currentCartItems));

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ordersKey, gson.toJson(allOrders));
        
        // Also save a simple string for the date of each order (indexed)
        String dateKey = "order_date_" + currentEmail + "_" + (allOrders.size() - 1);
        String currentDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        editor.putString(dateKey, currentDate);
        
        editor.apply();
    }

    private void clearCart() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("cart_" + currentEmail);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
