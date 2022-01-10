package com.roadstar.customerr.app.module.ui.adapter;

/**
 * @Developer android
 * @Company android
 **/


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.models.card_model.CardDetails;

import java.util.ArrayList;

public class PaymentListAdapter extends ArrayAdapter<CardDetails>{

    int vg;

    public  ArrayList<CardDetails> list;

    Context context;

    public PaymentListAdapter(Context context, int vg, ArrayList<CardDetails> list){

        super(context,vg,list);

        this.context=context;

        this.vg=vg;

        this.list=list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        ImageView paymentTypeImg =(ImageView) itemView.findViewById(R.id.paymentTypeImg);

        TextView cardNumber =(TextView) itemView.findViewById(R.id.cardNumber);

        ImageView tickImg= (ImageView) itemView.findViewById(R.id.img_tick);

        try {
           if(list.get(position).getBrand().equalsIgnoreCase("MASTERCARD")){
               paymentTypeImg.setImageResource(R.drawable.ic_visa); // TODO: 10/1/2020 change the card images
           }else if(list.get(position).getBrand().equalsIgnoreCase("MASTRO")){
               paymentTypeImg.setImageResource(R.drawable.ic_visa);
           }else if(list.get(position).getBrand().equalsIgnoreCase("Visa")){
               paymentTypeImg.setImageResource(R.drawable.ic_visa);
           }
           cardNumber.setText("xxxx - xxxx - xxxx - "+list.get(position).getLast_four());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }
}
