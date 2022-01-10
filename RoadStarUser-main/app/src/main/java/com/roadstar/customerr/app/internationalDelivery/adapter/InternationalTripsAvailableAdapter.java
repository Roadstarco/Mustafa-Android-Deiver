package com.roadstar.customerr.app.internationalDelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.internationalDelivery.InternationalTripDetailsActivity;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;

import java.text.MessageFormat;
import java.util.List;

public class InternationalTripsAvailableAdapter extends
    RecyclerView.Adapter<InternationalTripsAvailableAdapter.ViewHolder> {

    private final Context context;
    public static List<AvailAbleTripsModel> available_trips;

    public static AvailAbleTripsModel getSingle_item(int position){
        return available_trips.get(position);
    }
    public InternationalTripsAvailableAdapter(Context context, List<AvailAbleTripsModel> contacts) {
        available_trips = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.available_trips_item_layout, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvailAbleTripsModel item = available_trips.get(position);

        holder.startDate.setText(item.getCreated_at());
        holder.provider_id.setText(MessageFormat.format("{0}{1}", R.string.provider_id, item.getProvider_id().toString()));
        holder.src.setText(item.getTripfrom());
        holder.des.setText(item.getTripto());

        holder.main_layout.setOnClickListener(v -> {
            Intent tripDetailActivity = new Intent(context, InternationalTripDetailsActivity.class);
            tripDetailActivity.putExtra("trip_id",item.getId());
            tripDetailActivity.putExtra("pos",position);
            context.startActivity(tripDetailActivity);
        });

    }

    @Override
    public int getItemCount() {
        return available_trips.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView startDate,provider_id,src,des;
        public CardView main_layout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            main_layout = itemView.findViewById(R.id.main_layout);
            startDate = (TextView) itemView.findViewById(R.id.tv_tripDateTime);
            provider_id = (TextView) itemView.findViewById(R.id.booking_id);
            src = (TextView) itemView.findViewById(R.id.tv_trip_start_dest);
            des = (TextView) itemView.findViewById(R.id.tv_trip_end_dest);

        }
    }
}