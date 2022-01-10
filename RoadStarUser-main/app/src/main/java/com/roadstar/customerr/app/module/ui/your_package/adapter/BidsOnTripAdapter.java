package com.roadstar.customerr.app.module.ui.your_package.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.AcceptBidModel;
import com.roadstar.customerr.app.module.ui.your_package.DetailOfPackage;
import com.roadstar.customerr.app.module.ui.your_package.ListOfBidsOnTrip;
import com.roadstar.customerr.app.module.ui.your_package.model.AllBidsOnTripModel;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

public class BidsOnTripAdapter extends
    RecyclerView.Adapter<BidsOnTripAdapter.ViewHolder> {

    private final Context context;
    public static List<AllBidsOnTripModel> allBids;

    public static AllBidsOnTripModel getSingle_item(int position){
        return allBids.get(position);
    }
    public BidsOnTripAdapter(Context context, List<AllBidsOnTripModel> contacts) {
        allBids = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.all_bids_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllBidsOnTripModel singleItem = allBids.get(position);

        holder.startDate.setText(singleItem.getCreated_at());
        holder.provider_id.setText("Provider ID :"+singleItem.getProvider_id().toString());
        holder.src.setText("First Name: "+singleItem.getFirst_name());
        holder.des.setText("Last Name: "+singleItem.getLast_name());
        if(singleItem.getCounter_amount()!=null && singleItem.getCounter_amount()>0)
            holder.price.setText("$"+singleItem.getCounter_amount());
        else
            holder.price.setText("$"+singleItem.getAmount());

        if (singleItem.getStatus().equals( "Approved")){
            holder.buttonContainer.setVisibility(View.GONE);
            holder.viewStatus.setVisibility(View.VISIBLE);
        }else {
            holder.buttonContainer.setVisibility(View.VISIBLE);
            holder.viewStatus.setVisibility(View.GONE);
        }

        if (!singleItem.getStatus().equals( "Approved") && singleItem.getIs_counter() == 1){
            String status  = String.valueOf(context.getString(R.string.your_counter_offer_off)+singleItem.getCounter_amount()+context.getString(R.string.has_been_sent));
            holder.counterOfferStatus.setText(status);
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        }

        holder.viewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tripDetailActivity = new Intent(context, DetailOfPackage.class);
                int trip_id;

                if (((ListOfBidsOnTrip)context).getIntent().hasExtra("trip_id"))
                     trip_id = ((ListOfBidsOnTrip)context).getIntent().getIntExtra("trip_id",-1);
                else
                     trip_id = -1;

                tripDetailActivity.putExtra("trip_id",trip_id);
                tripDetailActivity.putExtra("pos",((ListOfBidsOnTrip)context).selectedTripPos);
                context.startActivity(tripDetailActivity);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignRider(singleItem.getTrip_id(),singleItem.getId());
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcounterOfferDialog(singleItem.getId());
            }
        });

    }

    private void addcounterOfferDialog(Integer bid_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.counter_bid_layout, null, false);

        EditText amount = dialogView.findViewById(R.id.et_bid_amount);
        ProgressBar progressBarInDialog = dialogView.findViewById(R.id.progressBarInDialog);
        AppCompatButton sendButton = dialogView.findViewById(R.id.btn_accept);
        AppCompatButton cancel = dialogView.findViewById(R.id.cancel);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(amount.getText())){
                    amount.setError("Add bid amount");
                }else {
                    callAddCounterAPI(progressBarInDialog,amount,bid_id);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void callAddCounterAPI(ProgressBar progressBarInDialog, EditText amount, Integer bid_id) {
        progressBarInDialog.setVisibility(View.VISIBLE);
        AcceptBidModel model = new AcceptBidModel();
        //model.setTrip_id(trip_id);
        model.setBid_id(bid_id);
        model.setCounter_amount(amount.getText().toString());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = service.sendCounterOffer(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",model/*, BASE_URL, "application/json"*/);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBarInDialog.setVisibility(View.GONE);
                if (response.code() == 200){
                    Toast.makeText(context, "counter offer send !", Toast.LENGTH_SHORT).show();
                    ((ListOfBidsOnTrip)context).finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBarInDialog.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void assignRider(Integer trip_id,Integer id) {

        ((ListOfBidsOnTrip)context).progressDoalog.setVisibility(View.VISIBLE);

        AcceptBidModel model = new AcceptBidModel();
        model.setTrip_id(trip_id);
        model.setBid_id(id);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = service.acceptBid(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",model/*, BASE_URL, "application/json"*/);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ((ListOfBidsOnTrip)context).progressDoalog.setVisibility(View.GONE);
                if (response.code() == 200){
                    Toast.makeText(context, "Rider Assigned", Toast.LENGTH_SHORT).show();
                    ((ListOfBidsOnTrip)context).finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ((ListOfBidsOnTrip)context).progressDoalog.setVisibility(View.GONE);
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allBids.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView startDate,provider_id,src,des,price,counterOfferStatus;
        public Button messageButton;
        public CardView main_layout;
        public LinearLayout buttonContainer;
        public AppCompatButton accept,reject,viewStatus;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            buttonContainer = itemView.findViewById(R.id.buttonContainer);
            main_layout = itemView.findViewById(R.id.main_layout);
            startDate = (TextView) itemView.findViewById(R.id.tv_tripDateTime);
            price = (TextView) itemView.findViewById(R.id.tripAmount);
            provider_id = (TextView) itemView.findViewById(R.id.booking_id);
            counterOfferStatus = (TextView) itemView.findViewById(R.id.counterOfferStatus);
            src = (TextView) itemView.findViewById(R.id.tv_trip_start_dest);
            des = (TextView) itemView.findViewById(R.id.tv_trip_end_dest);
            accept =  itemView.findViewById(R.id.accept_bid);
            reject =  itemView.findViewById(R.id.btn_cancel_req);
            viewStatus =  itemView.findViewById(R.id.view_status);

        }
    }
}