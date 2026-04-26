package com.example.thriftbooks;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.thriftbooks.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import java.util.HashSet;
import java.util.Set;

/**
 * Main Activity hosting the native navigation architecture and global search.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            
            // Link Navigation Drawer with NavController
            NavigationUI.setupWithNavController(binding.navigationView, navController);

            // Manual setup for Bottom Navigation to handle re-selection
            binding.bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                
                // Use NavOptions to clear the backstack when switching tabs
                NavOptions options = new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                        .build();
                
                navController.navigate(itemId, null, options);
                return true;
            });

            // Defining all main tabs as top-level destinations
            Set<Integer> topLevelDestinations = new HashSet<>();
            topLevelDestinations.add(R.id.homeFragment);
            topLevelDestinations.add(R.id.searchFragment);
            topLevelDestinations.add(R.id.cartFragment);
            topLevelDestinations.add(R.id.profileFragment);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Show Menu icon for main tabs, Back arrow for sub-screens
                if (topLevelDestinations.contains(destination.getId())) {
                    binding.menuIcon.setImageResource(R.drawable.ic_menu);
                    binding.menuIcon.setOnClickListener(v -> binding.drawerLayout.open());
                } else {
                    binding.menuIcon.setImageResource(R.drawable.ic_back);
                    binding.menuIcon.setOnClickListener(v -> navController.navigateUp());
                }
                
                // Show/Hide Bottom Nav for specific screens
                if (destination.getId() == R.id.checkoutFragment || destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                    binding.bottomNav.setVisibility(View.GONE);
                } else {
                    binding.bottomNav.setVisibility(View.VISIBLE);
                }
            });
        }

        // Setup Global Search
        setupGlobalSearch();

        // Cart Icon Global Click
        binding.cartIcon.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.cartFragment);
            }
        });
    }

    private void setupGlobalSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty() && navController != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("searchQuery", query);
                    navController.navigate(R.id.searchFragment, bundle);
                    binding.searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
