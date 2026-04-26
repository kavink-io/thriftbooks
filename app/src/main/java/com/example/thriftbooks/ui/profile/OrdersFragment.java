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
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.thriftbooks.R;
import com.example.thriftbooks.adapter.OrderAdapter;
import com.example.thriftbooks.databinding.FragmentOrdersBinding;
import com.example.thriftbooks.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        loadOrders();

        binding.btnShopNow.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.homeFragment)
        );
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter();
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ordersRecyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentEmail = sharedPref.getString("currentEmail", "");
        String ordersKey = "orders_" + currentEmail;

        Gson gson = new Gson();
        String jsonOrders = sharedPref.getString(ordersKey, null);

        if (jsonOrders == null) {
            showEmptyView();
        } else {
            Type type = new TypeToken<List<List<CartItem>>>() {}.getType();
            List<List<CartItem>> allOrders = gson.fromJson(jsonOrders, type);

            if (allOrders.isEmpty()) {
                showEmptyView();
            } else {
                binding.emptyOrdersContainer.setVisibility(View.GONE);
                binding.ordersRecyclerView.setVisibility(View.VISIBLE);
                
                // For this display, we'll flatten the list or just show the latest order items
                // In a full app, we'd have a nested RecyclerView or Order summary cards
                List<CartItem> flatList = new ArrayList<>();
                String lastDate = sharedPref.getString("order_date_" + currentEmail + "_" + (allOrders.size() - 1), "Recent");
                
                for (List<CartItem> order : allOrders) {
                    flatList.addAll(order);
                }
                
                adapter.setOrderData(flatList, lastDate);
            }
        }
    }

    private void showEmptyView() {
        binding.emptyOrdersContainer.setVisibility(View.VISIBLE);
        binding.ordersRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
