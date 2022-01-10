package com.roadstar.customerr.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.OnContentChangeListener;
import com.roadstar.customerr.app.business.OnNetworkTaskListener;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.utils.Logger;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.roadstar.customerr.common.utils.ViewUtils;
import com.roadstar.customerr.common.views.VizImageView;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

/***
 * This Fragment contains {@link WeakReference} of {@link OnContentChangeListener}.
 * Which we are using for Activity and fragment communication.
 */
@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public abstract class BaseFragment extends Fragment implements OnNetworkTaskListener {
    public Dialog dialog;

    // region CONTENT_CHANGE_LISTENER
    private WeakReference<OnContentChangeListener> reference = null;

    final protected OnContentChangeListener getReference() {
        return reference != null ? reference.get() : null;
    }
    // endregion

    // region ABSTRACT

    /**
     * @return Title of given fragment
     */
    abstract public String getTitle();
    // endregion

    // region LIFE_CYCLE
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            reference = new WeakReference<>((OnContentChangeListener) context);
        } catch (ClassCastException e) {
            Logger.caughtException(e);
            throw new ClassCastException(context.toString() + " must implement OnContentChangeListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.dialog = getProgressDialog(false);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.error("onLowMemory", String.format("%s is running on low memory", getLogTag()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.clear();
        reference = null;
        Logger.info("onDestroy", getLogTag());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // endregion

    //region VIEW_HELPERS
    public View findViewById(int resourceId) {
        return getView().findViewById(resourceId);
    }

    public TextView findTextViewById(int resourceId) {
        return (TextView) findViewById(resourceId);
    }

    public VizImageView findVizImageView(int resourceId) {
        return (VizImageView) findViewById(resourceId);
    }

    public void setText(int resourceId, String text) {
        findTextViewById(resourceId).setText(text);
    }

    public Button findButtonById(int resourceId) {
        return (Button) findViewById(resourceId);
    }

    public LinearLayout findLinearLayoutById(int resourceId) {
        return (LinearLayout) findViewById(resourceId);
    }

    public RelativeLayout findRelativeLayoutById(int resourceId) {
        return (RelativeLayout) findViewById(resourceId);
    }

    public AppCompatImageView findImageView(int viewId) {
        return (AppCompatImageView) findViewById(viewId);
    }

    public AppCompatEditText findEditTextById(int resourceID) {
        return (AppCompatEditText) findViewById(resourceID);
    }

    public TextInputLayout findTextInputLayout(int resourceID) {
        return (TextInputLayout) findViewById(resourceID);
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    //endregion

    // region ACTIVITY_VIEW_NULL
    final protected boolean isActivityNotNull() {
        return getActivity() != null;
    }

    final protected boolean isViewNull() {
        return getView() == null;
    }

    final protected boolean isActivityAndViewNotNull() {
        return isActivityNotNull() && isAdded();
    }

    final public String getLogTag() {
        return getClass().getSimpleName();
    }
    //endregion

    // region NETWORK_OPERATIONS

    /**
     * @return true is connected else not
     */
    @Override
    final public boolean isNetworkConnected() {
        return NetworkUtils.hasNetworkConnection(getActivity(), true);
    }

    /**
     * HTTP response call back from {@link AppNetworkTask}
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkResponse(HttpResponseItem response) {
        boolean status = response.getResponseCode() == HttpURLConnection.HTTP_OK;
        if (status)
            onNetworkSuccess(response);
        else
            onNetworkError(response);
    }

    /**
     * HTTP network operation is successfully completed
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        Logger.info(getLogTag(), "Network operation (" + response.getHttpRequestUrl() + ") successfully completed");
        Log.i(getLogTag(), response.getResponse());
    }

    /**
     * For some reasons there is/are some error(s) in network operation
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkError(HttpResponseItem response) {
        Logger.error(getLogTag(), response.getDefaultResponse() + "(network error)");
        if (response.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
            showSessionExpiredDialog();
        else if (getView() != null)
            ViewUtils.showSnackBar(getView(), getString(R.string.error_message), Snackbar.LENGTH_SHORT);
    }

    /**
     * For some reasons network operation has been cancelled
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    final public void onNetworkCanceled(HttpResponseItem response) {
        Logger.error(getLogTag(), response.getDefaultResponse() + " (operation cancelled by user)");
    }
    // endregion

    /**
     * If server response code is unauthorized (session expired)
     */
    private void showSessionExpiredDialog() {
        SharedPreferenceManager.getInstance().clearPreferences();


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Session expired");
        builder.setMessage("Your session got expired kindly sign in again.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.create().show();
    }

//    public void setTitle(String title){
//        if (getActivity() == null)
//            return;
//        BaseActivity baseActivity = (BaseActivity) getActivity();
//        if (baseActivity != null)
//            baseActivity.setTitle(title);
//    }

    public void setTitle(CharSequence title) {

        if (toolbar != null && findViewById(R.id.tv_title) != null)
            ((TextView) toolbar.findViewById(R.id.tv_title)).setText(title);
    }

    public void showSnackBar(String message) {
        if (getView() == null)
            return;
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Create and return a waiting progress dialog
     *
     * @param cancelable is dialog cancelable
     * @return a new progress dialog
     */
    public Dialog getProgressDialog(boolean cancelable) {
//        ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Please wait!");
//        dialog.setCancelable(cancelable);
//        return dialog;
        return getProgressDialog(cancelable, false);
    }

    public Dialog getProgressDialog(boolean cancelable, boolean isTransparent) {
        Dialog dialog;
        if (isTransparent) {
            dialog = new Dialog(getActivity(), R.style.ThemeDialog);
            dialog.setContentView(R.layout.app_dialog_trans);
        } else {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.app_dialog);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public void showProgressDialog() {
        if (dialog != null)
            dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null)
            dialog.hide();
    }

    private RelativeLayout toolbar;

    public void setActionBar(String title) {
        toolbar = (RelativeLayout) findViewById(R.id.rl_toolbar);

        setTitle(title);
        ImageView ivBack = toolbar.findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPress();

                }
            });
        }
    }

    public void hideActionBar() {
        toolbar = (RelativeLayout) findViewById(R.id.rl_toolbar);

        ImageView ivBack = toolbar.findViewById(R.id.iv_back);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    public void onBackPress() {
        getActivity().onBackPressed();
    }


    public void animate(int view_group_id) {
        View viewGroup = findViewById(view_group_id);
        Transition transition = new AutoTransition();
        transition.setStartDelay(100);
        transition.setDuration(400);

        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup, transition);
    }


}
