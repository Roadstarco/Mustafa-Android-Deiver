package com.roadstar.customerr.app.module.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.roadstar.customerr.R;

public class ReqAcceptDialog extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    /* Its a good practice to create callback interface within the fragment that host an activity must implement.
    As you can see below we have created OnInputListener interface. Also you must override on onAttach handler to
    obtain a reference to the host activity, confirming that it implement the required interface.
     */

    public interface OnInputListener {
        void onReqResponse(Boolean policyAccept);
    }

    public OnInputListener onInputListener;

    private TextView mActionOk ;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_req_accept, container, false);


        mActionOk = view.findViewById(R.id.action_ok);




        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onInputListener.onReqResponse(true);
                getDialog().dismiss();

            }
        });

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }

}
