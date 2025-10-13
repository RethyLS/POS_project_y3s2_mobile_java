package com.example.pos_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0; // Default to "All" category

    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.tvCategory.setText(category);
        
        // Highlight selected category
        if (position == selectedPosition) {
            holder.tvCategory.setBackgroundResource(R.drawable.category_selected_background);
            holder.tvCategory.setTextColor(holder.itemView.getContext().getColor(android.R.color.white));
        } else {
            holder.tvCategory.setBackgroundResource(R.drawable.category_background);
            holder.tvCategory.setTextColor(holder.itemView.getContext().getColor(R.color.text_primary));
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category, position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}