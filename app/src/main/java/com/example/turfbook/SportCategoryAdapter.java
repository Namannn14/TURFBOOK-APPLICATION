package com.example.turfbook;

import com.example.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SportCategoryAdapter extends RecyclerView.Adapter<SportCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(SportCategory category);
    }

    private final Context context;
    private final List<SportCategory> categoryList;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public SportCategoryAdapter(Context context, List<SportCategory> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sport_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SportCategory category = categoryList.get(position);
        holder.tvSportName.setText(category.getName());
        holder.ivSportIcon.setImageResource(category.getIconResId());

        boolean isSelected = (position == selectedPosition);
        if (isSelected) {
            holder.cardSportIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary));
            holder.cardSportIcon.setStrokeColor(ContextCompat.getColor(context, R.color.accent));
            holder.ivSportIcon.setColorFilter(ContextCompat.getColor(context, R.color.white));
            holder.tvSportName.setTextColor(ContextCompat.getColor(context, R.color.primary));
        } else {
            holder.cardSportIcon.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface));
            holder.cardSportIcon.setStrokeColor(ContextCompat.getColor(context, R.color.border));
            holder.ivSportIcon.setColorFilter(ContextCompat.getColor(context, R.color.primary));
            holder.tvSportName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPos);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardSportIcon;
        ImageView ivSportIcon;
        TextView tvSportName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSportIcon = itemView.findViewById(R.id.cardSportIcon);
            ivSportIcon = itemView.findViewById(R.id.ivSportIcon);
            tvSportName = itemView.findViewById(R.id.tvSportName);
        }
    }
}
