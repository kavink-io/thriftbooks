package com.example.thriftbooks.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.thriftbooks.databinding.ItemCartBinding;
import com.example.thriftbooks.model.CartItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems = new ArrayList<>();
    private OnCartItemChangeListener listener;

    public interface OnCartItemChangeListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public abstract static class CartViewHolder extends RecyclerView.ViewHolder {
        public CartViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
        }
        public abstract void bind(CartItem item);
    }

    class BookViewHolder extends CartViewHolder {
        private final ItemCartBinding binding;

        public BookViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void bind(CartItem item) {
            binding.cartProductTitle.setText(item.getBook().getTitle());
            binding.cartProductAuthor.setText(item.getBook().getAuthor());
            // Changed to Rupee
            binding.cartProductPrice.setText(String.format(Locale.US, "₹%.2f", item.getBook().getPrice() * 83));
            binding.tvQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(binding.cartProductImage.getContext())
                    .load(item.getBook().getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.cartProductImage);

            binding.btnPlus.setOnClickListener(v -> {
                if (listener != null) listener.onQuantityChanged(item, item.getQuantity() + 1);
            });

            binding.btnMinus.setOnClickListener(v -> {
                if (listener != null) {
                    if (item.getQuantity() > 1) {
                        listener.onQuantityChanged(item, item.getQuantity() - 1);
                    } else {
                        // If quantity is 1 and "-" is clicked, remove the item
                        listener.onItemRemoved(item);
                    }
                }
            });

            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onItemRemoved(item);
            });
        }
    }
}
