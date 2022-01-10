package com.roadstar.customerr.common.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.URLUtil;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.OnNetworkTaskListener;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.SplashActivity;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.utils.ApiConstants;
import com.roadstar.customerr.common.utils.AppConstants;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.BitmapUtils;
import com.roadstar.customerr.common.utils.FileUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JunaidAhmed on 8/7/2018.
 */
public class VizImageView extends RelativeLayout implements OnNetworkTaskListener {

    private boolean isCircular = false;
    private boolean enableProgress = true;
    private float radius = 0;
    private int placeholder = 0;
    private int resource = 0;

    private boolean hasBorder = false;
    private float borderRadius = 0;
    private int borderColor = 0;

    private AVLoadingIndicatorView loadingView = null;
    private RoundedImageView roundedImageView = null;
    private OnImageLoadingListener onImageLoadingListener;
    private @IdRes
    int imageViewID;
    private String imageURL = "";
    private boolean isUploading = false;

    private String endPoint =  ApiConstants.UPLOAD_PROFILE_IMAGE;

    public interface OnImageLoadingListener {
        void onLoadingResponse(boolean isSuccessful, String imageURL, @IdRes int imageViewID, String message);
    }

    public VizImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
        inflateLayout(context);
        initLoadingView();
        setProperties();
    }

    private void setProperties() {
        RoundedImageView roundedImageView = findViewById(R.id.iv_picture);

        if (isCircular)
            roundedImageView.setCornerRadius(200);
        else
            roundedImageView.setCornerRadius(radius);

        if (resource != 0)
            roundedImageView.setImageResource(resource);
        else if (placeholder != 0) {
            roundedImageView.setImageResource(placeholder);
        }

        if (hasBorder) {
            roundedImageView.setBorderWidth(borderRadius);
            roundedImageView.setBorderColor(borderColor);
        }
    }

    private void setupAttributes(AttributeSet attrs) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.VizImageView, 0, 0);
        try {
            isCircular = array.getBoolean(R.styleable.VizImageView_viv_setCircular, false);
            enableProgress = array.getBoolean(R.styleable.VizImageView_viv_enableProgress, true);
            radius = array.getDimensionPixelSize(R.styleable.VizImageView_viv_radius, 20);
            placeholder = array.getResourceId(R.styleable.VizImageView_viv_placeholder, 0);
            resource = array.getResourceId(R.styleable.VizImageView_viv_src, 0);
            hasBorder = array.getBoolean(R.styleable.VizImageView_viv_showBorder, false);
            borderRadius = array.getDimensionPixelSize(R.styleable.VizImageView_viv_borderWidth, 0);
            borderColor = array.getColor(R.styleable.VizImageView_viv_borderColor, 0);
        } finally {
            array.recycle();
        }
    }

    private void initLoadingView() {
        loadingView = getAvLoadingIndicatorView();
    }

    private void inflateLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_imageview, this, true);
    }

    public void setImage(final String url) {
        final RoundedImageView roundedImageView = findViewById(R.id.iv_picture);
        if (!AppUtils.ifNotNullEmpty(url))
        {
            roundedImageView.setImageResource(R.drawable.ic_car);
            return;
        }

        this.imageURL = url;

        showProgress();
        RequestCreator requestCreator = Picasso.with(getContext()).load(url);
        if (placeholder != 0)
            requestCreator.placeholder(placeholder);
        requestCreator.fit();
        requestCreator.centerCrop();
        requestCreator.into(roundedImageView, new Callback() {
            @Override
            public void onSuccess() {
                hideProgress();
            }



            @Override
            public void onError() {
                hideProgress();
                if (resource != 0)
                    roundedImageView.setImageResource(resource);
            }
        });
    }

    public void setPlaceholder(int imageViewID) {

        final RoundedImageView roundedImageView = findViewById(R.id.iv_picture);

        roundedImageView.setImageResource(imageViewID);

    }
    public void setCircular(Boolean isCircular) {
        RoundedImageView roundedImageView = findViewById(R.id.iv_picture);

        if (isCircular)
            roundedImageView.setCornerRadius(200);
        else
            roundedImageView.setCornerRadius(radius);
    }

    public void clear(){
         if (resource != 0)
             roundedImageView1.setImageResource(resource);
    }

    public void clearBitmap(){

        if(roundedImageView1!=null)
        roundedImageView1.setImageResource(R.drawable.btn_add_photo);
    }

    private void hideProgress() {
        if (enableProgress)
            loadingView.setVisibility(GONE);
    }

    private void showProgress() {
        if (enableProgress)
            loadingView.setVisibility(VISIBLE);
    }

    private AVLoadingIndicatorView getAvLoadingIndicatorView() {
        return findViewById(R.id.photo_loader);
    }

    public void uploadImageToServer(String filePath) {


        HttpRequestItem requestItem = new HttpRequestItem(AppConstants.getServerUrl(
                AppUtils.ifNotNullEmpty(endPoint) ? endPoint : ApiConstants.UPLOAD_PROFILE_IMAGE));
        requestItem.setHttpRequestType(NetworkUtils.HTTP_MULTIPART);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());

        Map<String, Object> data = new HashMap<>();
        data.put("file", filePath);
        requestItem.setParams(data);
        requestItem.setHttpRequestTimeout(NetworkUtils.HTTP_MULTIPART_TIMEOUT);
        isUploading = true;
        AppNetworkTask appNetworkTask = new AppNetworkTask(null, this);
        appNetworkTask.execute(requestItem);
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void loadImageInView(@IdRes int imageViewID, Uri imageURL) {
        loadImageInView(imageViewID, imageURL, null);
    }

    public void loadImageInView(@IdRes int imageViewID, Uri imageURL,
                                OnImageLoadingListener onImageLoadingListener) {
        fadeIn();
        final String filePath = FileUtils.getPath(getContext(), imageURL);
        this.imageViewID = imageViewID;
        this.onImageLoadingListener = onImageLoadingListener;
        roundedImageView = findViewById(R.id.iv_picture);
        showProgress();
        if (URLUtil.isValidUrl(imageURL.toString())) {
            Glide.with(getContext()).load(AppUtils.preparePathForPicture(imageURL.toString(), AppConstants.IMAGE_MINI_THUMB)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    hideProgress();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {

                    uploadImageToServer(filePath);
                    return false;
                }


            }).into(roundedImageView);
        } else
            createAndSetBitmap(imageURL, filePath);
    }

    private void fadeIn() {
        RoundedImageView roundedImageView = findViewById(R.id.iv_picture);
        roundedImageView.setAlpha(0.5f);
    }

    private void fadeOut() {
        RoundedImageView roundedImageView = findViewById(R.id.iv_picture);
        roundedImageView.setAlpha(1.0f);
    }

    /**
     * Create bitmap from Uri and set it to Image View
     *
     * @param uri photo Uri
     */
    private void createAndSetBitmap(Uri uri, String filePath) {
        String[] imageInfo = BitmapUtils.getRealPathWithIdFromURI(getContext(), uri,
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID);
        if (imageInfo != null && imageInfo.length > 1) {
            Bitmap bitmap = BitmapUtils.getThumbnail(getContext().getContentResolver(),
                    Long.parseLong(imageInfo[1]), MediaStore.Images.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                roundedImageView.setImageBitmap(bitmap);
                uploadImageToServer(filePath);
            }
        }
    }
    RoundedImageView roundedImageView1;
    public void createAndSetBitmap(Uri uri) {
         roundedImageView1 = findViewById(R.id.iv_picture);
//
//        roundedImageView.setImageResource(imageViewID);
//        roundedImageView = findViewById(R.id.img_news_pic);
        String[] imageInfo = BitmapUtils.getRealPathWithIdFromURI(getContext(), uri,
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID);
        if (imageInfo != null && imageInfo.length > 1) {
            Bitmap bitmap = BitmapUtils.getThumbnail(getContext().getContentResolver(),
                    Long.parseLong(imageInfo[1]), MediaStore.Images.Thumbnails.MINI_KIND);
            if (bitmap != null) {
                roundedImageView1.setImageBitmap(bitmap);

            }
        }
    }
    @Override
    public void onNetworkResponse(HttpResponseItem response) {
        isUploading = false;
        boolean status = response.getResponseCode() == HttpURLConnection.HTTP_OK;
        if (status)
            onNetworkSuccess(response);
        else
            onNetworkError(response);
    }

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        fadeOut();
        hideProgress();
        String imageURL = getImageURL(response.getResponse());
        this.imageURL = imageURL;
        try {
          //  String message = new JSONObject(response.getResponse()).getString("message");
            String message = "";
            if (onImageLoadingListener != null)
                onImageLoadingListener.onLoadingResponse(true, imageURL, imageViewID, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                //getContext().stopService(new Intent(getContext(), SocketService.class));
                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        builder.create().show();
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        if (response.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            showSessionExpiredDialog();
            return;
        }
        try {
            String message = new JSONObject(response.getResponse()).getString("message");
            if (onImageLoadingListener != null)
                onImageLoadingListener.onLoadingResponse(true, imageURL, imageViewID, message);
        } catch (Exception e) {
            Log.e("VizImageView", e.toString());
        }

        hideProgress();
        fadeOut();
        if (imageURL.isEmpty()) {
            if (placeholder == 0)
                roundedImageView.setImageDrawable(null);
            else
                roundedImageView.setImageResource(resource);
        } else {
            setImage(imageURL);
        }
        Toast.makeText(getContext(), "Image uploading failed", Toast.LENGTH_SHORT).show();
    }

    private String getImageURL(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            if(endPoint.equalsIgnoreCase(ApiConstants.UPLOAD_PLAYLIST_PIC))
            return data.getString("filename");
            else
                return data.getString("path")+data.getString("filename");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onNetworkCanceled(HttpResponseItem response) {
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.hasNetworkConnection(getContext(), true);
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean isUploading() {
        return isUploading;
    }

}

