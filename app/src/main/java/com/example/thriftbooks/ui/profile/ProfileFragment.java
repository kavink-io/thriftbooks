package com.example.thriftbooks.ui.profile;

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
import com.example.thriftbooks.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String currentEmail = sharedPref.getString("currentEmail", "");
            String name = sharedPref.getString("name_" + currentEmail, "User");
            
            binding.tvUserName.setText("Hello, " + name);
            binding.tvUserEmail.setText(currentEmail);
            binding.tvUserEmail.setVisibility(View.VISIBLE);
            binding.btnLogin.setText("LOGOUT");
            
            binding.btnLogin.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.putString("currentEmail", null);
                editor.apply();
                Navigation.findNavController(v).navigate(R.id.homeFragment);
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            });
        } else {
            binding.tvUserName.setText("Welcome to ThriftBooks");
            binding.tvUserEmail.setVisibility(View.GONE);
            binding.btnLogin.setText("Sign In / Register");

            binding.btnLogin.setOnClickListener(v -> 
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_loginFragment)
            );
        }

        binding.btnMyOrders.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_ordersFragment)
        );

        binding.btnWishlist.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_wishlistFragment)
        );

        binding.btnAccountSettings.setOnClickListener(v -> {
            if (isLoggedIn) {
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_accountSettingsFragment);
            } else {
                Toast.makeText(getContext(), "Please login to access settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
