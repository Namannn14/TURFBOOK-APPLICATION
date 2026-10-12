package com.example.turfbook;

import com.example.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {

    private final Context context;
    private List<Venue> venueList;

    public VenueAdapter(Context context, List<Venue> venueList) {
        this.context = context;
        this.venueList = venueList;
    }

    public void updateList(List<Venue> newList) {
        this.venueList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_venue, parent, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        Venue venue = venueList.get(position);

        holder.tvVenueName.setText(venue.getName());
        holder.tvVenueLocation.setText(venue.getLocation());
        holder.tvVenueSport.setText(venue.getSport());
        holder.tvVenueRating.setText(String.format("%.1f", venue.getRating()));
        holder.tvVenuePrice.setText(String.format("₹%.0f/hour", venue.getPrice()));

        // Resolve drawable resource
        int resId = context.getResources().getIdentifier(
                venue.getImage(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivVenueImage.setImageResource(resId);
        } else {
            holder.ivVenueImage.setImageResource(R.drawable.img_turf_generic);
        }

        View.OnClickListener clickListener = v -> {
            Intent intent = new Intent(context, VenueDetailsActivity.class);
            intent.putExtra("venue_id", venue.getId());
            context.startActivity(intent);
        };

        holder.btnViewDetails.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return venueList != null ? venueList.size() : 0;
    }

    public static class VenueViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVenueImage;
        TextView tvVenueName;
        TextView tvVenueLocation;
        TextView tvVenueSport;
        TextView tvVenueRating;
        TextView tvVenuePrice;
        MaterialButton btnViewDetails;

        public VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVenueImage = itemView.findViewById(R.id.ivVenueImage);
            tvVenueName = itemView.findViewById(R.id.tvVenueName);
            tvVenueLocation = itemView.findViewById(R.id.tvVenueLocation);
            tvVenueSport = itemView.findViewById(R.id.tvVenueSport);
            tvVenueRating = itemView.findViewById(R.id.tvVenueRating);
            tvVenuePrice = itemView.findViewById(R.id.tvVenuePrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
