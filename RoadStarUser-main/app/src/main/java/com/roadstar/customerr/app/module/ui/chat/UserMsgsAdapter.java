package com.roadstar.customerr.app.module.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.module.ui.chat.model.User;


import java.util.ArrayList;

public class UserMsgsAdapter extends RecyclerView.Adapter<UserMsgsAdapter.BlockedFamilyMembersViewHolder> {
    ArrayList<User> familyMembersModelList = new ArrayList<>();
    Context context;
    OnItemClick onItemClick;


    public UserMsgsAdapter(Context context, ArrayList<User> familyMembersModelList, OnItemClick onItemClick) {
        this.context = context;
        this.familyMembersModelList = familyMembersModelList;
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    public BlockedFamilyMembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_messages_row_layout, parent, false);
        return new BlockedFamilyMembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedFamilyMembersViewHolder holder, int position) {
        holder.textViewName.setText(familyMembersModelList.get(position).name);
        if (familyMembersModelList.get(position).message.text!=null){

            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.chatImageview.setVisibility(View.GONE);
            holder.textViewMsg.setText(familyMembersModelList.get(position).message.text);

        }else {
            holder.relativeLayout.setVisibility(View.GONE);

        }


//        if (familyMembersModelList.get(position).type != null && familyMembersModelList.get(position).type.equals("1")) {
////            holder.imageViewMenu.setVisibility(View.GONE);
//
//            if (familyMembersModelList.get(position).message.status.equals("0"))
//                holder.textViewCount.setVisibility(View.VISIBLE);
//            else holder.textViewCount.setVisibility(View.GONE);
//
            if (familyMembersModelList.get(position).avata != null && familyMembersModelList.get(position).avata.length() > 0)
                Glide.with(context).load(familyMembersModelList.get(position).avata).into(holder.imageViewProfile);
            else Glide.with(context).load(R.drawable.ic_user_name).into(holder.imageViewProfile);
//        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return familyMembersModelList.size();
    }

    public class BlockedFamilyMembersViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProfile, imageViewMenu, chatImageview;
        private TextView textViewName, textViewMsg;
        private ConstraintLayout constraintLayout;
        private TextView textViewCount;
        private RelativeLayout relativeLayout;


        public BlockedFamilyMembersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tvName);
            textViewMsg = itemView.findViewById(R.id.tvMsg);
            relativeLayout = itemView.findViewById(R.id.rel_chat);
            imageViewProfile = itemView.findViewById(R.id.imgProfile);
            chatImageview = itemView.findViewById(R.id.chat_image);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            textViewCount = itemView.findViewById(R.id.tvCount);
        }
    }

    public interface OnItemClick{
        public void onClick(View v,int p);
    }
}
