package com.example.turfbook;

import com.example.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    public interface OnRemoveFavoriteListener {
        void onRemoveFavorite(Venue venue);
    }

    private final Context context;
    private List<Venue> venueList;
    private final OnRemoveFavoriteListener removeListener;

    public FavoriteAdapter(Context context, List<Venue> venueList, OnRemoveFavoriteListener removeListener) {
        this.context = context;
        this.venueList = venueList;
        this.removeListener = removeListener;
    }

    public void updateList(List<Venue> newList) {
        this.venueList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Venue venue = venueList.get(position);

        holder.tvFavVenueName.setText(venue.getName());
        holder.tvFavVenueLocation.setText(venue.getLocation());
        holder.tvFavVenueSport.setText(venue.getSport());
        holder.tvFavVenueRating.setText(String.format("%.1f", venue.getRating()));
        holder.tvFavVenuePrice.setText(String.format("₹%.0f/hr", venue.getPrice()));

        int resId = context.getResources().getIdentifier(
                venue.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivFavVenueImage.setImageResource(resId);
        } else {
            holder.ivFavVenueImage.setImageResource(R.drawable.img_turf_generic);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VenueDetailsActivity.class);
            intent.putExtra("venue_id", venue.getId());
            context.startActivity(intent);
        });

        holder.btnRemoveFavorite.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemoveFavorite(venue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return venueList != null ? venueList.size() : 0;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFavVenueImage;
        TextView tvFavVenueSport;
        TextView tvFavVenueName;
        TextView tvFavVenueLocation;
        TextView tvFavVenuePrice;
        TextView tvFavVenueRating;
        ImageButton btnRemoveFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFavVenueImage = itemView.findViewById(R.id.ivFavVenueImage);
            tvFavVenueSport = itemView.findViewById(R.id.tvFavVenueSport);
            tvFavVenueName = itemView.findViewById(R.id.tvFavVenueName);
            tvFavVenueLocation = itemView.findViewById(R.id.tvFavVenueLocation);
            tvFavVenuePrice = itemView.findViewById(R.id.tvFavVenuePrice);
            tvFavVenueRating = itemView.findViewById(R.id.tvFavVenueRating);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
}
