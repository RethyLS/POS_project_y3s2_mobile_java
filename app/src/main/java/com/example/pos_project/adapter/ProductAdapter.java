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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductCategory, tvProductPrice, 
                        tvProductQuantity, tvProductDescription;
        private Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            tvProductDescription = itemView.findViewById(R.id.tv_product_description);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductCategory.setText(product.getCategory());
            tvProductPrice.setText(String.format("$%.2f", product.getPrice()));
            tvProductQuantity.setText("Qty: " + product.getQuantity());
            
            if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                tvProductDescription.setText(product.getDescription());
                tvProductDescription.setVisibility(View.VISIBLE);
            } else {
                tvProductDescription.setVisibility(View.GONE);
            }

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(product);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(product);
                }
            });

            // Set quantity color based on stock level
            if (product.getQuantity() <= 5) {
                tvProductQuantity.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
            } else if (product.getQuantity() <= 10) {
                tvProductQuantity.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
            } else {
                tvProductQuantity.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
            }
        }
    }
}