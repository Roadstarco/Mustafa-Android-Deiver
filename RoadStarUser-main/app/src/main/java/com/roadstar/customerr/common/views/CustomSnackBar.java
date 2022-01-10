package com.roadstar.customerr.common.views;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import androidx.core.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roadstar.customerr.R;


public class CustomSnackBar extends BaseTransientBottomBar<CustomSnackBar> {

    /**
     * Constructor for the transient bottom bar.
     *
     * @param parent   The parent for this transient bottom bar.
     * @param content  The content view for this transient bottom bar.
     * @param callback The content view callback for this transient bottom bar.
     */
    private CustomSnackBar(ViewGroup parent, View content, ContentViewCallback callback) {
        super(parent, content, callback);

    }

    public static CustomSnackBar make(@NonNull ViewGroup parent, @Duration int duration) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View content = inflater.inflate(R.layout.custom_snack_bar, parent, false);
        final ContentViewCallback viewCallback = new ContentViewCallback(content);
        final CustomSnackBar customSnackBar = new CustomSnackBar(parent, content, viewCallback);

        customSnackBar.getView().setPadding(0, 0, 0, 0);
        customSnackBar.setDuration(duration);
        return customSnackBar;
    }

    public CustomSnackBar setText(CharSequence text) {
        TextView textView = getView().findViewById(R.id.snackbar_text);
        textView.setText(text);
        return this;
    }


    private static class ContentViewCallback implements BaseTransientBottomBar.ContentViewCallback {

        private View content;

        public ContentViewCallback(View content) {
            this.content = content;
        }

        @Override
        public void animateContentIn(int delay, int duration) {
            ViewCompat.setScaleY(content, 0f);
            ViewCompat.animate(content).scaleY(1f).setDuration(duration).setStartDelay(delay);
        }

        @Override
        public void animateContentOut(int delay, int duration) {
            ViewCompat.setScaleY(content, 1f);
            ViewCompat.animate(content).scaleY(0f).setDuration(duration).setStartDelay(delay);
        }
    }
}