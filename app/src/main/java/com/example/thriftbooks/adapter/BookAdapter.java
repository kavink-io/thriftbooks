package com.example.thriftbooks.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.thriftbooks.databinding.ItemBookBinding;
import com.example.thriftbooks.model.Book;
import java.util.Locale;

public class BookAdapter extends ListAdapter<Book, BookAdapter.BookViewHolder> {
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public void setOnBookClickListener(OnBookClickListener listener) {
        this.listener = listener;
    }

    public BookAdapter() {
        super(new DiffUtil.ItemCallback<Book>() {
            @Override
            public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                       oldItem.getPrice() == newItem.getPrice();
            }
        });
    }

    public void setBooks(java.util.List<Book> books) {
        submitList(books);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookBinding binding;

        public BookViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onBookClick(getItem(position));
                }
            });
        }

        public void bind(Book book) {
            binding.bookTitle.setText(book.getTitle());
            binding.bookAuthor.setText(book.getAuthor());
            // Changed $ to ₹
            binding.bookPrice.setText(String.format(Locale.US, "₹%.2f", book.getPrice() * 83)); // Approximation
            binding.bookRating.setRating(book.getRating());
            
            if (book.getOriginalPrice() > book.getPrice()) {
                binding.originalPrice.setVisibility(View.VISIBLE);
                binding.originalPrice.setText(String.format(Locale.US, "₹%.2f", book.getOriginalPrice() * 83));
                binding.originalPrice.setPaintFlags(binding.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                binding.originalPrice.setVisibility(View.GONE);
            }

            Glide.with(binding.bookImage.getContext())
                    .load(book.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.bookImage);
        }
    }
}
