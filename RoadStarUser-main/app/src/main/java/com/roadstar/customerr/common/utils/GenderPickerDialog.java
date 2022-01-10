package com.roadstar.customerr.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.roadstar.customerr.R;

/**
 * Created by JunaidAhmed on 5/18/2018.
 */
public class GenderPickerDialog extends BottomSheetDialog implements View.OnClickListener {

    private Context context;
    private OnGenderItemClickListener onGenderItemClickListener;
    private BottomSheetBehavior<View> mBehavior;

    public GenderPickerDialog(@NonNull Context context, OnGenderItemClickListener onPickerItemClickListener) {
        super(context);
        this.context = context;
        this.onGenderItemClickListener = onPickerItemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = View.inflate(getContext(),R.layout.dialog_gender_picker, null);
        setContentView(contentView);
    }

    private void setTransparentBackground() {
        View contentView = View.inflate(getContext(),R.layout.dialog_gender_picker, null);
        setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = GenderPickerDialog.this;

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.setCanceledOnTouchOutside(true);
        setTransparentBackground();
        TextView textViewMale = dialog.findViewById(R.id.tv_male);
        TextView textViewFemale = dialog.findViewById(R.id.tv_female);
        TextView textViewCancel = dialog.findViewById(R.id.cancel__gender_dialog_tv);
        textViewMale.setOnClickListener(this);
        textViewFemale.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        onGenderItemClickListener.onGenderItemClick(view);
    }

    public interface OnGenderItemClickListener {
        void onGenderItemClick(View view);
    }
}
