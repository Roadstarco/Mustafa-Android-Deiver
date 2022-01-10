package com.roadstar.customerr.app.module.ui.your_package.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.module.ui.your_package.DetailOfPackage;
import com.roadstar.customerr.app.module.ui.your_package.ListOfBidsOnTrip;

import java.util.List;

public class InternationalUserTripsAdapter extends
    RecyclerView.Adapter<InternationalUserTripsAdapter.ViewHolder> {

    private final Context context;
    public static List<AvailAbleTripsModel> available_trips;

    public static AvailAbleTripsModel getSingle_item(int position){
        return available_trips.get(position);
    }
    public InternationalUserTripsAdapter(Context context, List<AvailAbleTripsModel> contacts) {
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
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvailAbleTripsModel singleItem = available_trips.get(position);

        holder.startDate.setText(singleItem.getCreated_at());
        if(!singleItem.getProvider_id().equals("0") && singleItem.getProvider_id().toString().length()>0) {
            holder.provider_id.setText(String.format("Provider ID :%s", singleItem.getProvider_id().toString()));
            holder.provider_id.setVisibility(View.VISIBLE);
        }
        else
            holder.provider_id.setVisibility(View.GONE);
        holder.src.setText(singleItem.getTripfrom());
        holder.des.setText(singleItem.getTripto());
        //holder.tripAmount.setText(singleItem.getAmount());

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleItem.getCreated_by().equals("user")){
                    Intent tripDetailActivity = new Intent(context, ListOfBidsOnTrip.class);
                    tripDetailActivity.putExtra("trip_id",singleItem.getId());
                    tripDetailActivity.putExtra("trip_pos",position);
                    context.startActivity(tripDetailActivity);
                }else {
                    Intent tripDetailActivity = new Intent(context, DetailOfPackage.class);

                    tripDetailActivity.putExtra("trip_id",singleItem.getId());
                    tripDetailActivity.putExtra("pos",position);
                    context.startActivity(tripDetailActivity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return available_trips.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView startDate,provider_id,src,des,tripAmount;
        public Button messageButton;
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
            tripAmount = itemView.findViewById(R.id.tripAmount);

        }
    }
}