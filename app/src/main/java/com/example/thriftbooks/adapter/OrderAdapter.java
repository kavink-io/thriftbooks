package com.example.thriftbooks.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.thriftbooks.databinding.ItemOrderBinding;
import com.example.thriftbooks.model.CartItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<CartItem> orderItems = new ArrayList<>();
    private String orderDate;

    public void setOrderData(List<CartItem> items, String date) {
        this.orderItems = items;
        this.orderDate = date;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartItem item) {
            binding.tvOrderDate.setText("Ordered on: " + orderDate);
            binding.orderBookTitle.setText(item.getBook().getTitle());
            binding.orderBookQty.setText("Qty: " + item.getQuantity());
            binding.orderBookPrice.setText(String.format(Locale.US, "₹%.2f", item.getTotalPrice() * 83));
            
            Glide.with(binding.orderBookImage.getContext())
                    .load(item.getBook().getImageUrl())
                    .into(binding.orderBookImage);
        }
    }
}
