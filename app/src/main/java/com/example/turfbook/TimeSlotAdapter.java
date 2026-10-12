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

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.SlotViewHolder> {

    public interface OnSlotSelectedListener {
        void onSlotSelected(TimeSlot slot);
    }

    private final Context context;
    private final List<TimeSlot> slotList;
    private final OnSlotSelectedListener listener;

    public TimeSlotAdapter(Context context, List<TimeSlot> slotList, OnSlotSelectedListener listener) {
        this.context = context;
        this.slotList = slotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        TimeSlot slot = slotList.get(position);
        holder.tvSlotTime.setText(slot.getSlotText());

        if (slot.isBooked()) {
            holder.cardTimeSlot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.slot_booked_bg));
            holder.cardTimeSlot.setStrokeColor(ContextCompat.getColor(context, R.color.border));
            holder.cardTimeSlot.setStrokeWidth(1);
            holder.tvSlotTime.setTextColor(ContextCompat.getColor(context, R.color.slot_booked_text));
            holder.tvSlotStatus.setText("Booked");
            holder.tvSlotStatus.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled));
            holder.itemView.setClickable(false);
            holder.itemView.setOnClickListener(null);
        } else if (slot.isSelected()) {
            holder.cardTimeSlot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary));
            holder.cardTimeSlot.setStrokeColor(ContextCompat.getColor(context, R.color.accent));
            holder.cardTimeSlot.setStrokeWidth(4);
            holder.tvSlotTime.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvSlotStatus.setText("Selected");
            holder.tvSlotStatus.setTextColor(ContextCompat.getColor(context, R.color.accent_light));
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSlotSelected(slot);
            });
        } else {
            holder.cardTimeSlot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface));
            holder.cardTimeSlot.setStrokeColor(ContextCompat.getColor(context, R.color.slot_available_border));
            holder.cardTimeSlot.setStrokeWidth(2);
            holder.tvSlotTime.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            holder.tvSlotStatus.setText("Available");
            holder.tvSlotStatus.setTextColor(ContextCompat.getColor(context, R.color.status_confirmed));
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onSlotSelected(slot);
            });
        }
    }

    @Override
    public int getItemCount() {
        return slotList != null ? slotList.size() : 0;
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardTimeSlot;
        TextView tvSlotTime;
        TextView tvSlotStatus;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTimeSlot = itemView.findViewById(R.id.cardTimeSlot);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);
            tvSlotStatus = itemView.findViewById(R.id.tvSlotStatus);
        }
    }
}
