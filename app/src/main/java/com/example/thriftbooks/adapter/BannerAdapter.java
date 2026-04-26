package com.example.thriftbooks.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.thriftbooks.databinding.ItemBannerBinding;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<String> bannerUrls;

    public BannerAdapter(List<String> bannerUrls) {
        this.bannerUrls = bannerUrls;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBannerBinding binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Glide.with(holder.binding.bannerImage.getContext())
                .load(bannerUrls.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(android.R.drawable.progress_horizontal)
                .error(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(holder.binding.bannerImage);
    }

    @Override
    public int getItemCount() {
        return bannerUrls.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        final ItemBannerBinding binding;
        BannerViewHolder(ItemBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
