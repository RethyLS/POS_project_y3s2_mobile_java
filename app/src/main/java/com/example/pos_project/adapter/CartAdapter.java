package com.example.pos_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pos_project.R;
import com.example.pos_project.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onIncreaseQuantity(CartItem item, int position);
        void onDecreaseQuantity(CartItem item, int position);
        void onRemoveItem(CartItem item, int position);
    }

    public CartAdapter(List<CartItem> cartItems, OnCartItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName, tvUnitPrice, tvQuantity, tvTotalPrice;
        private Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_cart_product_image);
            tvProductName = itemView.findViewById(R.id.tv_cart_product_name);
            tvUnitPrice = itemView.findViewById(R.id.tv_cart_unit_price);
            tvQuantity = itemView.findViewById(R.id.tv_cart_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_cart_total_price);
            btnIncrease = itemView.findViewById(R.id.btn_increase_quantity);
            btnDecrease = itemView.findViewById(R.id.btn_decrease_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove_item);
        }

        public void bind(CartItem item, int position) {
            tvProductName.setText(item.getProductName());
            tvUnitPrice.setText(String.format("$%.2f each", item.getUnitPrice()));
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            tvTotalPrice.setText(String.format("$%.2f", item.getTotalPrice()));

            // Load product image
            String baseUrl = "http://10.0.2.2:8000/storage/";
            String imageUrl = item.getProductImage();
            
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // If image starts with http, use it directly, otherwise prepend base URL
                String fullImageUrl = imageUrl.startsWith("http") ? imageUrl : baseUrl + imageUrl;
                
                Glide.with(itemView.getContext())
                    .load(fullImageUrl)
                    .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .centerCrop())
                    .into(ivProductImage);
            } else {
                // Set placeholder if no image
                ivProductImage.setImageResource(R.drawable.ic_image_placeholder);
            }

            btnIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncreaseQuantity(item, position);
                }
            });

            btnDecrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDecreaseQuantity(item, position);
                }
            });

            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(item, position);
                }
            });

            // Disable decrease button if quantity is 1
            btnDecrease.setEnabled(item.getQuantity() > 1);
        }
    }
}