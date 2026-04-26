package com.example.thriftbooks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.thriftbooks.R;
import com.example.thriftbooks.adapter.BannerAdapter;
import com.example.thriftbooks.adapter.BookAdapter;
import com.example.thriftbooks.adapter.CategoryAdapter;
import com.example.thriftbooks.databinding.FragmentHomeBinding;
import com.example.thriftbooks.model.Category;
import com.example.thriftbooks.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private BookAdapter featuredAdapter;
    private BookAdapter bestSellersAdapter;
    private BookAdapter dealsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupBanner();
        setupCategories();
        setupRecyclerViews();
        observeViewModel();
    }

    private void setupBanner() {
        // Using more stable high-resolution promo banners from Unsplash for testing
        List<String> bannerUrls = Arrays.asList(
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?q=80&w=1000&auto=format&fit=crop",
                "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?q=80&w=1000&auto=format&fit=crop"
        );
        BannerAdapter bannerAdapter = new BannerAdapter(bannerUrls);
        binding.bannerViewPager.setAdapter(bannerAdapter);
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1", "Fiction", R.drawable.ic_fiction));
        categories.add(new Category("2", "Kids", R.drawable.ic_kids));
        categories.add(new Category("3", "ThriftDeals", R.drawable.ic_deals));
        categories.add(new Category("4", "Romance", R.drawable.ic_romance));
        categories.add(new Category("5", "Sci-Fi", R.drawable.ic_scifi));
        categories.add(new Category("6", "History", R.drawable.ic_history));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRecyclerView.setAdapter(categoryAdapter);
        
        categoryAdapter.setOnCategoryClickListener(category -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", category.getName());
            
            // Navigate with options that allow returning to Home tab naturally
            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false)
                    .build();
            
            Navigation.findNavController(requireView()).navigate(R.id.searchFragment, bundle, navOptions);
        });
    }

    private void setupRecyclerViews() {
        featuredAdapter = new BookAdapter();
        binding.featuredRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.featuredRecyclerView.setAdapter(featuredAdapter);

        bestSellersAdapter = new BookAdapter();
        binding.bestSellersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.bestSellersRecyclerView.setAdapter(bestSellersAdapter);

        dealsAdapter = new BookAdapter();
        binding.dealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.dealsRecyclerView.setAdapter(dealsAdapter);

        BookAdapter.OnBookClickListener listener = book -> {
            Bundle bundle = new Bundle();
            bundle.putString("bookId", book.getId());
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle);
        };

        featuredAdapter.setOnBookClickListener(listener);
        bestSellersAdapter.setOnBookClickListener(listener);
        dealsAdapter.setOnBookClickListener(listener);
    }

    private void observeViewModel() {
        viewModel.getFeaturedBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                featuredAdapter.setBooks(books);
                bestSellersAdapter.setBooks(books);
                dealsAdapter.setBooks(books);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
