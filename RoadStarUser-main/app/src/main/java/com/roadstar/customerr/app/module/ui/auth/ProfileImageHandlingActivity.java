package com.roadstar.customerr.app.module.ui.auth;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.module.ui.booking_activity.AttachmentHandlingActivity;
import com.roadstar.customerr.common.base.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static com.roadstar.customerr.common.utils.AppConstants.REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;

public class ProfileImageHandlingActivity extends BaseActivity {
    int attachmentID;
    private static final int PICK_IMAGE = 100,CAMERA=2,PICK_VIDEO=200,CAMERAVIDEO=300;
    Uri cameraUri;

    public void checkStoragePermission(int attachmentID) {
        this.attachmentID = attachmentID;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION);
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION);

        } else
            showPictureDialog();
//            addStoreImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION:
            case CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE:
                checkStoragePermission(attachmentID);
                break;
        }


    }

    //Add Image from gallery
    public void addStoreImage() {
        if (CropImage.isExplicitCameraPermissionRequired(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            CropImage.startPickImageActivity(this);

        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "IMG_" + System.currentTimeMillis()))).withAspectRatio(1,1)
                        .start(ProfileImageHandlingActivity.this);
            }
        }else if (requestCode == CAMERA) {
            UCrop.of(cameraUri, Uri.fromFile(new File(getCacheDir(), "IMG_" + System.currentTimeMillis()))).withAspectRatio(1,1)
                    .start(ProfileImageHandlingActivity.this);

        }else if(requestCode==UCrop.REQUEST_CROP){
            Uri imgUri = UCrop.getOutput(data);
            if (imgUri != null) {
                String selectedImage = imgUri.getPath();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
//                    String path = saveImage(bitmap);
                    setImage(bitmap);//getResizedBitmap(bitmap,400));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
                // load selectedImage into ImageView
            }
        }

        //For Images
        if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.

            // no permissions required or already granted, can start crop image activity
            //startCropImageActivity(imageUri);

            performCrop(imageUri);
        }
        //After Image Cropped
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    setImage(selectedBitmap);
                }

               /* File wallpaper_file = new File(result.getUri().getPath());
                Uri imageContentUri = getImageContentUri(getActivity(), wallpaper_file.getAbsolutePath());

                setImage(imageContentUri);*/
//                imageFilePath = result.getUri();
//
//                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
                .start(this);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_"+ Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void setImage(Bitmap imageContentUri) {

        //findVizImageView(attachmentID).createAndSetBitmap(imageContentUri);
        findVizImageView(attachmentID).setImage(String.valueOf(getImageUri(getApplicationContext(),imageContentUri)));
        switch (attachmentID) {
            case R.id.iv_attach_1:
                findViewById(R.id.img_remove1).setVisibility(View.VISIBLE);
            case R.id.iv_attach_2:
                findViewById(R.id.img_remove2).setVisibility(View.VISIBLE);

            case R.id.iv_attach_3:
                findViewById(R.id.img_remove3).setVisibility(View.VISIBLE);

        }
    }


    void setImage(Uri imageContentUri) {
      //  findVizImageView(attachmentID).createAndSetBitmap(imageContentUri);
        findVizImageView(attachmentID).setImage(String.valueOf(imageContentUri));
//        switch (attachmentID) {
//            case R.id.iv_attach_1:
//                findViewById(R.id.img_remove1).setVisibility(View.VISIBLE);
//            case R.id.iv_attach_2:
//                findViewById(R.id.img_remove2).setVisibility(View.VISIBLE);
//
//            case R.id.iv_attach_3:
//                findViewById(R.id.img_remove3).setVisibility(View.VISIBLE);
//
//        }
    }


   public void removeImage(int removeImageId) {
        findViewById(removeImageId).setVisibility(View.GONE);
        switch (removeImageId) {
            case R.id.img_remove1:
                findVizImageView(R.id.iv_profile).setImage(null);
                findVizImageView(R.id.iv_attach_1).setPlaceholder(R.drawable.profile_place_holder);

                break;

        }
    }

    private void showPictureDialog(){

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ProfileImageHandlingActivity.this);
        pictureDialog.setTitle("Choose From");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();

    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, PICK_IMAGE);
            }
            else {
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
            }
        }else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            gallery.setType("image/*");
            startActivityForResult(gallery, PICK_IMAGE);
        }
    }

    private void takePhotoFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    cameraUri = FileProvider.getUriForFile(getApplicationContext(),"com.roadstar.customerr.fileprovider",new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
                }else
                    cameraUri = FileProvider.getUriForFile(getApplicationContext(),"com.roadstar.customerr.fileprovider",new File(Environment.getExternalStorageDirectory(), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//                    intent.setType("image/*");
                startActivityForResult(intent, CAMERA);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 4);
            }
        }else {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setType("image/*");
            startActivityForResult(intent, CAMERA);
        }
    }
}
