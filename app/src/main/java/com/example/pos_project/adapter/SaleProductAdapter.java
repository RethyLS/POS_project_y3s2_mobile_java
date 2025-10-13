package com.example.pos_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;
import com.example.pos_project.model.Product;

import java.util.List;

public class SaleProductAdapter extends RecyclerView.Adapter<SaleProductAdapter.SaleProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(Product product);
    }

    public SaleProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaleProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sale_product, parent, false);
        return new SaleProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class SaleProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductPrice, tvStock;
        private Button btnAddToCart;

        public SaleProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvStock = itemView.findViewById(R.id.tv_stock);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
            tvStock.setText("Stock: " + product.getQuantity());

            // Enable/disable add to cart based on stock
            btnAddToCart.setEnabled(product.getQuantity() > 0);
            if (product.getQuantity() == 0) {
                btnAddToCart.setText("Out of Stock");
                btnAddToCart.setBackgroundResource(android.R.color.darker_gray);
            } else {
                btnAddToCart.setText("Add to Cart");
                btnAddToCart.setBackgroundResource(R.drawable.button_primary);
            }

            btnAddToCart.setOnClickListener(v -> {
                if (listener != null && product.getQuantity() > 0) {
                    listener.onAddToCartClick(product);
                }
            });

            // Set stock quantity color
            if (product.getQuantity() <= 5) {
                tvStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            } else if (product.getQuantity() <= 10) {
                tvStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
            } else {
                tvStock.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            }
        }
    }
}