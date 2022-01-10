package com.roadstar.customerr.common.views;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseDialog extends BottomSheetDialog {


    public BaseDialog(@NonNull Context context) {
        super(context);
    }


    public Button findButtonById(@IdRes int id) {
        return (Button) findViewById(id);
    }

    public ImageView findImageViewById(@IdRes int id) {
        return (ImageView) findViewById(id);
    }

    public EditText findFieldById(@IdRes int id) {
        return (EditText) findViewById(id);
    }

    public TextView findTexViewById(@IdRes int id) {
        return (TextView) findViewById(id);
    }


    public void showSnackBar(String message, int duration) {
//        ViewGroup view = findViewById(R.id.bottom_sheet_container);
//        if (view == null)
//            return;
//        CustomSnackBar customSnackbar = CustomSnackBar.make(view, duration);
//        customSnackbar.setText(message);
//        customSnackbar.show();
    }

    public void showSnackBar(String message) {
        showSnackBar(message, Snackbar.LENGTH_LONG);
    }
}