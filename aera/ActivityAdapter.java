package com.example.aera;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aera.ActivityEntry;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<ActivityEntry> list;

    public ActivityAdapter(List<ActivityEntry> list) {
        this.list = list;
    }

    public void updateList(List<ActivityEntry> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityEntry entry = list.get(position);
        holder.tvSubtype.setText(entry.getSubType());
        holder.tvType.setText(entry.getType());
        holder.tvCo2.setText(String.format(Locale.getDefault(), "%.1f kg", entry.getCo2e()));

        int iconRes = R.drawable.ic_transport;
        switch (entry.getType()) {
            case "Transport": iconRes = R.drawable.ic_transport; break;
            case "Energy": iconRes = R.drawable.ic_energy; break;
            case "Food": iconRes = R.drawable.ic_food; break;
            case "Shopping": iconRes = R.drawable.ic_shopping; break;
            case "Waste": iconRes = R.drawable.ic_waste; break;
        }
        holder.ivIcon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvSubtype, tvType, tvCo2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_activity_icon);
            tvSubtype = itemView.findViewById(R.id.tv_activity_subtype);
            tvType = itemView.findViewById(R.id.tv_activity_type);
            tvCo2 = itemView.findViewById(R.id.tv_activity_co2);
        }
    }
}