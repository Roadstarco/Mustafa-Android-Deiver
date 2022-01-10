package com.roadstar.customerr.common.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.view.View;

import com.roadstar.customerr.R;
import com.roadstar.customerr.common.utils.DateUtils;
import com.roadstar.customerr.common.utils.Logger;
import com.roadstar.customerr.common.utils.PermissionUtils;
import com.roadstar.customerr.common.views.PhotoPickerDialog;

import java.io.File;
import java.io.IOException;

/**
 * Created by bilal on 26/03/2018.
 */

public class TakePhotoActivity extends BaseActivity implements PhotoPickerDialog.OnPickerItemClickListener {

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

    private PhotoPickerDialog dialog;

    /**
     * show user dialog to select either open camera Or gallery
     */
    protected void showChoosePhotoDialog() {
        dialog = new PhotoPickerDialog(this, this);
        dialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == TAKE_PHOTO_PERMISSION && PermissionUtils.verifyPermission(grantResults)) {
            dispatchTakePictureIntent();
        } else if (requestCode == CHOOSE_PHOTO_PERMISSION && PermissionUtils.verifyPermission(grantResults)) {
            dispatchChoosePictureIntent();
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
        if (photo != null && takePictureIntent.resolveActivity(TakePhotoActivity.this.getPackageManager()) != null) {
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

    @Override
    public void onPickerItemClick(View view) {
        switch (view.getId()) {
            case R.id.tv_photo_lib:
                dialog.dismiss();
                if (PermissionUtils.checkAndRequestPermissions(this
                        , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO_PERMISSION))
                    dispatchChoosePictureIntent();
                break;
            case R.id.tv_take_photo:
                dialog.dismiss();
                if (PermissionUtils.checkAndRequestPermissions(this
                        , new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, TAKE_PHOTO_PERMISSION))
                    dispatchTakePictureIntent();
                break;
            case R.id.cancel_dialog_tv:
                dialog.dismiss();
                break;
        }
    }
}
