package com.roadstar.customerr.app.module.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.BaseItem;
import com.roadstar.customerr.app.data.models.Service;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewAdapter;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewHolder;
import com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener;

import java.util.List;

public class ServicesAdapter extends BaseRecyclerViewAdapter {

    Context context;

    public ServicesAdapter(Context context, List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
        this.context = context;
    }


    @Override
    public BaseRecyclerViewHolder createSpecificViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PickupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false));
    }

    @Override
    public void bindSpecificViewHolder(@NonNull BaseRecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
       Service service = (Service) getItemAt(position);
        PickupHolder pickupHolder = (PickupHolder) holder;

        pickupHolder.serviceNameTv.setText(service.getName());
//        String url = "https://emernox.com/public/uploads/dc50b1e69e5edab65149f112aa315d3ae5b815e4.png";
//        pickupHolder.vehicleImg.setImage(url);

        Glide.with(context).load(service.getImage()).into(pickupHolder.vehicleImg);

    }

    private class PickupHolder extends BaseRecyclerViewHolder {

        public TextView serviceNameTv ;
        ImageView vehicleImg;

        public PickupHolder(View view) {
            super(view);
            serviceNameTv = view.findViewById(R.id.tv_service_name);
            vehicleImg = view.findViewById(R.id.iv_vehicle);

            view.setOnClickListener(this);
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return PickupHolder.this;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null) {
                getItemClickListener().onRecyclerViewItemClick(PickupHolder.this);
            }

        }
    }


}
