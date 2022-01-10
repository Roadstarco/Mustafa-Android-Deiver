package com.roadstar.customerr.app.module.ui.booking_history;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.BaseItem;
import com.roadstar.customerr.app.data.models.Booking;
import com.roadstar.customerr.app.data.models.InternationalHistoryModel;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewAdapter;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewHolder;
import com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends BaseRecyclerViewAdapter {

    Context context;

    public BookingHistoryAdapter(Context context, List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
        this.context = context;
    }


    @Override
    public BaseRecyclerViewHolder createSpecificViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ArchiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false));
    }

    @Override
    public void bindSpecificViewHolder(@NonNull BaseRecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
        //Booking booking = (Booking) getItemAt(position);
        //ArchiveHolder archiveHolder = (ArchiveHolder) holder;
        ((ArchiveHolder) holder).setItemData(position);

    }


    private class ArchiveHolder extends BaseRecyclerViewHolder {

        View view;

        public ArchiveHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return ArchiveHolder.this;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null) {
                getItemClickListener().onRecyclerViewItemClick(ArchiveHolder.this);
            }

        }

        public void setItemData(int pos) {

            BaseItem item = getItemAt(pos);
            if (item.getItemType() == 0){

                Booking booking = (Booking)getItemAt(pos);
                findTextViewById(view, R.id.tv_trip_start_dest).setText(booking.getSAddress());
                findTextViewById(view, R.id.tv_trip_end_dest).setText(booking.getDAddress());
                if (booking.getPayment() != null)
                    findTextViewById(view, R.id.tripAmount).setText("$"+booking.getPayment().getFixed());
                else
                    findTextViewById(view, R.id.tripAmount).setText("payment Pending");

                findTextViewById(view, R.id.tv_tripDateTime).setText(booking.getStartedAt());
                findTextViewById(view, R.id.booking_id).setText(booking.getBookingId());

                if (booking.getServiceType() != null)
                    findTextViewById(view, R.id.car_name).setText(", "+booking.getServiceType().getName());
                else
                    findTextViewById(view, R.id.car_name).setText(", By Road");

                if(booking.getSLatitude()+""!=null && booking.getSLongitude()+""!=null && booking.getDLatitude()+""!=null && booking.getDLongitude()+""!=null){
                    findTextViewById(view, R.id.tv_trip_start_dest).setText(getAddress(booking.getSLatitude(),booking.getSLongitude()));
                    findTextViewById(view, R.id.tv_trip_end_dest).setText(getAddress(booking.getDLatitude(),booking.getDLongitude()));
                }

            }else if (item.getItemType() == 1){

                InternationalHistoryModel internationalHistoryModel = (InternationalHistoryModel)getItemAt(pos);
                findTextViewById(view, R.id.tv_trip_start_dest).setText(internationalHistoryModel.getTripfrom());
                findTextViewById(view, R.id.tv_trip_end_dest).setText(internationalHistoryModel.getTripto());
                findTextViewById(view, R.id.tripAmount).setText("$"+internationalHistoryModel.getTrip_request().getAmount());
                findTextViewById(view, R.id.tv_tripDateTime).setText(internationalHistoryModel.getArrival_date());
                findTextViewById(view, R.id.booking_id).setText(String.valueOf(internationalHistoryModel.getId()));
                findTextViewById(view, R.id.car_name).setText(", "+internationalHistoryModel.getService_type());

            }
           // findVizImageById(view, R.id.iv_car).setImage(booking.getServiceType().getImage());
        }
    }

    private String getAddress(Double latitude,Double longitude){
        Geocoder geocoder;
        String address = "";
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0);
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }

        return address;
    }


}
