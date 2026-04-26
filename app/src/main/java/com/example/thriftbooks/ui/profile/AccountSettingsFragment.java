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
import com.example.thriftbooks.databinding.FragmentAccountSettingsBinding;

public class AccountSettingsFragment extends Fragment {

    private FragmentAccountSettingsBinding binding;
    private SharedPreferences sharedPref;
    private String currentEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        currentEmail = sharedPref.getString("currentEmail", "");

        loadUserData();

        binding.btnUpdateProfile.setOnClickListener(v -> updateProfile());
        binding.btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void loadUserData() {
        if (!currentEmail.isEmpty()) {
            String name = sharedPref.getString("name_" + currentEmail, "");
            binding.etSettingsName.setText(name);
            binding.etSettingsEmail.setText(currentEmail);
        }
    }

    private void updateProfile() {
        String newName = binding.etSettingsName.getText().toString().trim();
        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name_" + currentEmail, newName);
        editor.apply();

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void changePassword() {
        String newPassword = binding.etNewPassword.getText().toString().trim();
        if (newPassword.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password_" + currentEmail, newPassword);
        editor.apply();

        binding.etNewPassword.setText("");
        Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
