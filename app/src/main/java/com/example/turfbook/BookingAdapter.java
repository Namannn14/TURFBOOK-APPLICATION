package com.example.turfbook;

import com.example.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    public interface OnBookingCancelListener {
        void onCancelBooking(Booking booking);
    }

    private final Context context;
    private List<Booking> bookingList;
    private final OnBookingCancelListener cancelListener;

    public BookingAdapter(Context context, List<Booking> bookingList, OnBookingCancelListener cancelListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.cancelListener = cancelListener;
    }

    public void updateList(List<Booking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingId.setText("ID: " + booking.getBookingId());
        holder.tvBookingVenueName.setText(booking.getVenueName());
        holder.tvBookingLocation.setText(booking.getVenueLocation());
        holder.tvBookingDate.setText(booking.getDate());
        holder.tvBookingTime.setText(booking.getTimeSlot());
        holder.tvBookingAmount.setText(String.format("₹%.0f", booking.getAmount()));
        holder.tvBookingStatus.setText(booking.getStatus());

        if ("Confirmed".equalsIgnoreCase(booking.getStatus())) {
            holder.tvBookingStatus.setBackgroundResource(R.drawable.bg_badge_confirmed);
            holder.tvBookingStatus.setTextColor(ContextCompat.getColor(context, R.color.status_confirmed));
            holder.btnCancelBooking.setVisibility(View.VISIBLE);
        } else {
            holder.tvBookingStatus.setBackgroundResource(R.drawable.bg_badge_cancelled);
            holder.tvBookingStatus.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled));
            holder.btnCancelBooking.setVisibility(View.GONE);
        }

        holder.btnCancelBooking.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelBooking(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId;
        TextView tvBookingStatus;
        TextView tvBookingVenueName;
        TextView tvBookingLocation;
        TextView tvBookingDate;
        TextView tvBookingTime;
        TextView tvBookingAmount;
        MaterialButton btnCancelBooking;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvBookingVenueName = itemView.findViewById(R.id.tvBookingVenueName);
            tvBookingLocation = itemView.findViewById(R.id.tvBookingLocation);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
            tvBookingAmount = itemView.findViewById(R.id.tvBookingAmount);
            btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
