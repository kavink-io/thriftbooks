package com.example.thriftbooks.ui.auth;

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
import com.example.thriftbooks.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            String inputEmail = binding.etEmail.getText().toString().trim();
            String inputPassword = binding.etPassword.getText().toString().trim();

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve stored user data from SharedPreferences
            // Using a specific key for the user like "user_" + email
            SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String storedPassword = sharedPref.getString("password_" + inputEmail, null);

            if (storedPassword != null && storedPassword.equals(inputPassword)) {
                
                // Mark as logged in and save current user
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("currentEmail", inputEmail);
                editor.apply();

                Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            } else {
                Toast.makeText(getContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCreateAccount.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
