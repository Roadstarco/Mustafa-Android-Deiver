package com.roadstar.customerr.common.base;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.OnNetworkTaskListener;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.SplashActivity;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewAdapter;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewHolder;
import com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener;
import com.roadstar.customerr.common.utils.DateUtils;
import com.roadstar.customerr.common.utils.Logger;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.roadstar.customerr.common.utils.PermissionUtils;
import com.roadstar.customerr.common.utils.ViewUtils;
import com.roadstar.customerr.common.views.CustomSnackBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by bilal on 23/05/2018.
 */

public abstract class TakePhotoBaseBottomSheetFragment extends BottomSheetDialogFragment implements OnNetworkTaskListener, OnRecyclerViewItemClickListener {

    private RecyclerView recyclerView;

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

    /**
     * @return true is connected else not
     */
    @Override
    final public boolean isNetworkConnected() {
        return NetworkUtils.hasNetworkConnection(getActivity(), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerView != null) {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
            if (adapter != null)
                adapter.clearResources();
            recyclerView.setAdapter(null);
        }
    }

    /**
     * Set adapter
     *
     * @param adapter {@link BaseRecyclerViewAdapter}
     */
    protected void setAdapter(BaseRecyclerViewAdapter adapter) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Returns name of current fragment
     *
     * @return BaseRecyclerViewFragment name
     */
    public String getRecyclerViewFragmentTag() {
        return getClass().getSimpleName();
    }

    /**
     * @param holder view holder on clicked item
     */
    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        Logger.info(getRecyclerViewFragmentTag(), "onRecyclerViewItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition());
    }

    /**
     * @param holder     view holder on clicked item
     * @param resourceId resource id of clicked item
     */
    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
        Logger.info(getRecyclerViewFragmentTag(), "onRecyclerViewChildItemClick adapter position " +
                holder.getAdapterPosition() + " layout position " + holder.getLayoutPosition() +
                ", resourceId " + resourceId);
    }

    /**
     * Get reference of RecyclerView
     *
     * @return {@link RecyclerView}
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    // region LIFE_CYCLE
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
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
        if (!isActivityAndViewNotNull())
            return;
        SharedPreferenceManager.getInstance().clearPreferences();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Session expired");
        builder.setMessage("Your session got expired kindly sign in again.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        });
        builder.create().show();
    }

    public void setTitle(String title){
        if (getActivity() == null)
            return;
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.setTitle(title);
    }

    public void showSnackBar(String message) {
        showSnackBar(message, Snackbar.LENGTH_LONG);
    }

    public void showSnackBar(String message, int duration) {
        if (getView() == null)
            return;
        ViewGroup viewGroup = getView().findViewById(R.id.layout_main);
        if (viewGroup == null)
            Snackbar.make(getView(), message, (duration <= 0 && duration >= -2 ? duration : Snackbar.LENGTH_SHORT)).show();
        else {
            CustomSnackBar customSnackbar = CustomSnackBar.make(viewGroup, duration);
            customSnackbar.setText(message);
            customSnackbar.show();
        }
    }

    public void showToast(String message){
        if (getContext() == null)
            return;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create and return a waiting progress dialog
     *
     * @param cancelable    is dialog cancelable
     * @return      a new progress dialog
     */
//    public ProgressDialog getProgressDialog(boolean cancelable){
//        ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Please wait!");
//        dialog.setCancelable(cancelable);
//        return dialog;
//    }

    public Dialog getProgressDialog(boolean cancelable){
        return getProgressDialog(cancelable, false);
    }

    public Dialog getProgressDialog(boolean cancelable, boolean isTransparent) {
        Dialog dialog;
        if (isTransparent) {
            dialog = new Dialog(getActivity(), R.style.ThemeDialog);
            dialog.setContentView(R.layout.app_dialog_trans);
        }else {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.app_dialog);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(cancelable);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void animate() {
        if (getView() != null && getView() instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) getView());
    }

    public void animate(int view_group_id) {
        if (getView() == null)
            return;
        View viewGroup = getView().findViewById(view_group_id);
        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup);
    }

    public void animate(int view_group_id, Transition transition) {
        if (getView() == null)
            return;
        View viewGroup = getView().findViewById(view_group_id);
        if (viewGroup != null && viewGroup instanceof ViewGroup)
            TransitionManager.beginDelayedTransition((ViewGroup) viewGroup);
    }


    public Button findButtonById(@IdRes int id) {
        return (Button) getActivity().findViewById(id);
    }

    public ImageView findImageViewById(@IdRes int id) {
        return (ImageView) getActivity().findViewById(id);
    }

    public EditText findFieldById(@IdRes int id) {
        return (EditText) getActivity().findViewById(id);
    }
    public TextInputLayout findTextInputLayout(int resourceID) {
        return getActivity().findViewById(resourceID);
    }
    public TextView findTexViewById(@IdRes int id) {
        return (TextView) getActivity().findViewById(id);
    }

    /**
     * Flag to identify camera access permission is requested
     */
    public static final int TAKE_PHOTO_PERMISSION = 101;
    /**
     * Flag to identify gallery access permission is requested
     */
    public static final int CHOOSE_PHOTO_PERMISSION = 102;
    /**
     * Flag to identify camera intent is called
     */
    public static final int TAKE_PHOTO_RESULT_CODE = 110;
    /**
     * Flag to identify gallery intent is called
     */
    public static final int CHOOSE_PHOTO_RESULT_CODE = 120;
    /**
     * Flag to identify gallery intent is called
     */
    public static final int CHOOSE_MULTIPLE_RESULT_CODE = 130;

    public Uri selectedPhotoUri;

    /**
     * show user dialog to select either open camera Or gallery
     */
    protected void showChoosePhotoDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog.setContentView(R.layout.choose_photo_dialog);
        TextView takePhotoTv = dialog.findViewById(R.id.tv_take_photo);
        TextView choosePhotoTv = dialog
                .findViewById(R.id.tv_photo_lib);
        takePhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (PermissionUtils.checkAndRequestPermissions(getActivity()
                        , new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_PERMISSION))
                    dispatchTakePictureIntent();
            }
        });

        choosePhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (PermissionUtils.checkAndRequestPermissions(getActivity()
                        , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO_PERMISSION))
//                    checkAndDispatchGalleryIntent();
                    dispatchChoosePictureIntent();
            }
        });
        dialog.findViewById(R.id.cancel_dialog_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_PERMISSION && PermissionUtils.verifyPermission(grantResults)) {
            dispatchTakePictureIntent();
        } else if (requestCode == CHOOSE_PHOTO_PERMISSION && PermissionUtils.verifyPermission(grantResults)) {
            checkAndDispatchGalleryIntent();
        }
    }

    /**
     * Check build os version and then dispatch single or multiple choose picture intent
     */
    private void checkAndDispatchGalleryIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            dispatchMultipleChoosePictureIntent();
        else
            dispatchChoosePictureIntent();
    }

    /**
     * create and call open camera intent
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = getPhotoFile();
        if (photo != null && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoUri = Uri.fromFile(photo));
            startActivityForResult(Intent.createChooser(takePictureIntent, ""), TAKE_PHOTO_RESULT_CODE);
        }
    }

    /**
     * create and open choose from gallery/photos intent
     */
    public void dispatchChoosePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), CHOOSE_PHOTO_RESULT_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void dispatchMultipleChoosePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), CHOOSE_MULTIPLE_RESULT_CODE);
    }

    /**
     * create and return a file to store the taken image in the device storage
     *
     * @return created file in device storage
     */

    final public File getPhotoFile() {
        String timeStamp = DateUtils.convertDate(System.currentTimeMillis(), DateUtils.FILE_NAME);
        String imageFileName = "IMG" + timeStamp + "";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoFile = null;
        try {
            if(!storageDir.exists())
                storageDir.mkdirs();
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            Logger.caughtException(e);
        }
        return photoFile;
    }
}
