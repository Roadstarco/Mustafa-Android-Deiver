package com.roadstar.customerr.app.internationalDelivery.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.internationalDelivery.classes.LockableBottomSheetBehavior;
import com.roadstar.customerr.app.module.ui.booking_activity.AttachmentHandlingActivity;
import com.roadstar.customerr.app.module.ui.payment_method.PaymentMethodActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.FileUtils;
import com.roadstar.customerr.common.views.VizImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;
import static com.roadstar.customerr.common.utils.AppConstants.REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;


public class PostTrip extends Fragment implements View.OnClickListener {
    View view;
    final Calendar myCalendar = Calendar.getInstance();
    private static final int PICK_IMAGE = 100,CAMERA=2,PICK_VIDEO=200,CAMERAVIDEO=300;
    Uri cameraUri;
    TextView tvProductType, tvProductSize;
    TextInputEditText arrivalDate,packageAmount,etEmail,etDeliveryFrom,etDeliveryTo,etItemName;
    private boolean isCategorySelected = true;
    private boolean isProductSelected = true;

    public static boolean isSheetOpen = false;

    ProgressBar progressDoalog;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout inProgress;

    public static BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;

    private EditText recieverName ,recieverPhone;

    int attachmentID;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final boolean cardIsAvailable = false;
    AppCompatButton makeRequest;
    private final int AUTOCOMPLETE_REQUEST_CODE =2587;
    private boolean isTripFromField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_post_trip, container, false);


        initi();
        setupBottomSheet(view);
        setSpinners(view);

        return view;
    }


    public void checkStoragePermission(int attachmentID) {
        this.attachmentID = attachmentID;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION);
        } else if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION);

        } else
            showPictureDialog();
//            addStoreImage();
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Add Image from gallery
    public void addStoreImage() {
        if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }
        } else {
            CropImage.startPickImageActivity(getActivity());

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentURI = data.getData();
                UCrop.of(data.getData(), Uri.fromFile(new File(getActivity().getCacheDir(), "IMG_" + System.currentTimeMillis()))).withAspectRatio(1,1)
                        .start(getActivity(),PostTrip.this);
            }
        }else if (requestCode == CAMERA) {
            UCrop.of(cameraUri, Uri.fromFile(new File(getActivity().getCacheDir(), "IMG_" + System.currentTimeMillis()))).withAspectRatio(1,1)
                    .start(getActivity(),PostTrip.this);

        }else if(requestCode==UCrop.REQUEST_CROP){
            Uri imgUri = UCrop.getOutput(data);
            if (imgUri != null) {
                String selectedImage = imgUri.getPath();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
//                    String path = saveImage(bitmap);
                    setImage(bitmap);//getResizedBitmap(bitmap,400));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
                // load selectedImage into ImageView
            }
        }
        //For Images
        if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.

            performCrop(imageUri);

            //setImage(imageUri);

            // no permissions required or already granted, can start crop image activity
            //startCropImageActivity(imageUri);

        }
        //After Image Cropped
        else if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");


//                    setImage(getResizedBitmap(selectedBitmap,400));
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
        else if (requestCode == AUTOCOMPLETE_REQUEST_CODE){
            Log.d("place name ",data.toString());

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                if (isTripFromField)
                    etDeliveryFrom.setText(place.getName());
                else {
                    etDeliveryTo.setText(place.getName());
                }

                Log.d("place name ",place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

                Log.d("place name ",status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;

        }

    }
    public void removeImage(int removeImageId) {
        view.findViewById(removeImageId).setVisibility(View.GONE);
        switch (removeImageId) {
            case R.id.img_remove1:
                findVizImageView(R.id.iv_attach_1).setImage(null);
                findVizImageView(R.id.iv_attach_1).setPlaceholder(R.drawable.btn_add_photo);

                break;
            case R.id.img_remove2:
                findVizImageView(R.id.iv_attach_2).setImage(null);
                findVizImageView(R.id.iv_attach_2).setPlaceholder(R.drawable.btn_add_photo);
                break;
            case R.id.img_remove3:
                findVizImageView(R.id.iv_attach_3).setImage(null);
                findVizImageView(R.id.iv_attach_3).setPlaceholder(R.drawable.btn_add_photo);
                break;

        }
    }

    private void startCropImageActivity(Uri imageUri) {

        CropImage.activity(imageUri)
                .start(getActivity());
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_"+ Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void setImage(Bitmap imageContentUri) {

//        findVizImageView(attachmentID).createAndSetBitmap(imageContentUri);
        findVizImageView(attachmentID).setImage(String.valueOf(getImageUri(getActivity(),imageContentUri)));
        switch (attachmentID) {
            case R.id.iv_attach_1:
                view.findViewById(R.id.img_remove1).setVisibility(View.VISIBLE);
            case R.id.iv_attach_2:
                view.findViewById(R.id.img_remove2).setVisibility(View.VISIBLE);

            case R.id.iv_attach_3:
                view.findViewById(R.id.img_remove3).setVisibility(View.VISIBLE);

        }
    }

    public VizImageView findVizImageView(@IdRes int viewId) {
        return ((VizImageView) view.findViewById(viewId));
    }

    private void setupBottomSheet(View view) {
        ConstraintLayout bottomSheet = view.findViewById(R.id.bottomSheet);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isSheetOpen = true;
                        if (bottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) bottomSheetBehavior).setLocked(true);
                        }
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        isSheetOpen = false;
                        inProgress.setVisibility(View.GONE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    private void initi() {

        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getActivity().getString(R.string.api_key));
        }

        PlacesClient placesClient = Places.createClient(getActivity());

        progressDoalog = view.findViewById(R.id.progressBar);
        inProgress = view.findViewById(R.id.inProgressLayout);
        tvProductSize = view.findViewById(R.id.tv_product_size);
        etDeliveryFrom = view.findViewById(R.id.et_deliveryFrom);
        etDeliveryTo = view.findViewById(R.id.et_deliveryTo);
        etItemName = view.findViewById(R.id.et_item_name);
        tvProductType = view.findViewById(R.id.tv_product_type);
        arrivalDate = view.findViewById(R.id.arrivalDate);
        packageAmount = view.findViewById(R.id.packageAmount);
        etEmail = view.findViewById(R.id.et_email);
        TextInputEditText etParcelDetail = view.findViewById(R.id.parcel_detail);
        etParcelDetail.setSelection(0);
        recieverName = view.findViewById(R.id.et_receiver_name);
        recieverPhone = view.findViewById(R.id.et_receiver_phone_numb);
        makeRequest = view.findViewById(R.id.btn_pay);
        makeRequest.setText("submit");

        etDeliveryTo.setFocusable(false);
        etDeliveryTo.setOnClickListener(v -> loadNearbyPlaces(false));

        etDeliveryFrom.setFocusable(false);
        etDeliveryFrom.setOnClickListener(v -> loadNearbyPlaces(true));

        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (etEmail.getText().toString().matches(emailPattern) && s.length() > 0) {
                }
                else {
                    etEmail.setError("invalid email");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        view.findViewById(R.id.iv_attach_1).setOnClickListener(this);
        view.findViewById(R.id.iv_attach_2).setOnClickListener(this);
        view.findViewById(R.id.iv_attach_3).setOnClickListener(this);
        view.findViewById(R.id.img_remove1).setOnClickListener(this);
        view.findViewById(R.id.img_remove2).setOnClickListener(this);
        view.findViewById(R.id.img_remove3).setOnClickListener(this);
        view.findViewById(R.id.btn_pkg_dtl_next).setOnClickListener(this);
        view.findViewById(R.id.iv_recev_detail_back).setOnClickListener(this);
        makeRequest.setOnClickListener(this);

        inProgress.setOnClickListener(this);
        arrivalDate.setOnClickListener(this);
        packageAmount.setOnClickListener(this);

    }

    private void loadNearbyPlaces(boolean caller) {
        isTripFromField = caller;
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getActivity());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public boolean isPackageParamValid() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(etDeliveryFrom)) {
            etDeliveryFrom.setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        etDeliveryFrom.setError(null);

        if (!AppUtils.isNotFieldEmpty(etDeliveryTo)){
            etDeliveryTo.setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        etDeliveryTo.setError(null);

        if (!AppUtils.isNotFieldEmpty(arrivalDate)) {
            arrivalDate.setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        arrivalDate.setError(null);

        if (!AppUtils.isNotFieldEmpty(etItemName)) {
            etItemName.setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        etItemName.setError(null);

        /*if (!etEmail.getText().toString().matches(emailPattern) || !AppUtils.isNotFieldEmpty(etEmail)) {
            etEmail.setError(getString(R.string.field_empty_or_invalid));
            valid = false;
            return valid;
        }

        etEmail.setError(null);*/


        if (tvProductSize.getText().toString().equals("")) //Check Product weight
        {
            Toast.makeText(getActivity(), "please Select item size", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if (tvProductType.getText().toString().equals("")) //Check Product weight
        {
            Toast.makeText(getActivity(), "please Select item type", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        AppUtils.hideSoftKeyboard(getActivity());//Hide keyboard
        return valid;
    }

    public void setSpinners(View view) {
        //Spinner Category
        AppCompatSpinner spinnerCategory = (AppCompatSpinner) view.findViewById(R.id.spinner_product_type);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
                if (isCategorySelected) {
                    isCategorySelected = !isCategorySelected;
                } else {
                    tvProductType.setText(spinnerCategory.getSelectedItem().toString());
                    spinnerCategory.setSelection(0);
                    isCategorySelected = true;
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //Spinner Product Type
        Spinner spinnerProduct = (Spinner) view.findViewById(R.id.spinner_product_size);

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here

                if (isProductSelected) {
                    isProductSelected = !isProductSelected;
                } else {
                    tvProductSize.setText(spinnerProduct.getSelectedItem().toString());
                    spinnerProduct.setSelection(0);
                    isProductSelected = true;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    public void dateAndTimePicker(EditText editText){


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };

        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    private void updateLabel(EditText editText) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText( sdf.format(myCalendar.getTime()));
        editText.setFocusable(false);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null)
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_attach_1:
                checkStoragePermission(R.id.iv_attach_1);

                break;
            case R.id.iv_attach_2:
                checkStoragePermission(R.id.iv_attach_2);

                break;
            case R.id.iv_attach_3:
                checkStoragePermission(R.id.iv_attach_3);

                break;
            case R.id.img_remove1:
                removeImage(R.id.img_remove1);

                break;
            case R.id.img_remove2:
                removeImage(R.id.img_remove2);

                break;
            case R.id.img_remove3:
                removeImage(R.id.img_remove3);

                break;

            case R.id.arrivalDate:
                dateAndTimePicker(arrivalDate);
                break;
            case R.id.btn_pkg_dtl_next:

                if (isPackageParamValid()){
                    hideSoftKeyboard(getActivity());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            inProgress.setVisibility(View.VISIBLE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        }
                    },300);

                }
                break;
            case R.id.inProgressLayout:
                break;
            case R.id.iv_recev_detail_back:

                inProgress.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                break;
            case R.id.btn_pay:

                if (TextUtils.isEmpty(recieverName.getText())){
                    recieverName.setError("Add Receiver name");
                }else if (TextUtils.isEmpty(recieverPhone.getText())){
                    recieverPhone.setError("Add Receiver name");
                }else if (!UserManager.isCardAdded()){
                    showConfirmationDialog(PaymentMethodActivity.class);
                }else {
                    callSendRequestApi();
                }

                break;


        }
    }

    private void showConfirmationDialog(Class<PaymentMethodActivity> activityToOpen) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.entrcard_info_layout, null, false);


        TextView link = dialogView.findViewById(R.id.link);

        AppCompatButton proceed = dialogView.findViewById(R.id.proceed);
        AppCompatButton cancel = dialogView.findViewById(R.id.cancel);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.termConditionLink)));
                startActivity(browserIntent);
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent cardDetail = new Intent(getActivity(), activityToOpen);
                getActivity().startActivity(cardDetail);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

    }


    private void showDialogProgressbar() {
        progressDoalog.setVisibility(View.VISIBLE);
    }

    public void callSendRequestApi() {
        showDialogProgressbar();
        /*Create handle for the RetrofitInstance interface*/
        try {


            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.requestTrip(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, getJobReqParam());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    progressDoalog.setVisibility(View.GONE);

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);

                    if (response.code() == 200) {
                        UserManager.setRideInprogress(true);
                        Toast.makeText(getActivity(), "Your request is sent successfully", Toast.LENGTH_LONG).show();
                        getActivity().finish();

                    } else
                        Snackbar.make(view, getString(R.string.job_already_in_progress),  Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDoalog.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            progressDoalog.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    MultipartBody getJobReqParam() {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        try {

            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("receiver_phone", AppUtils.getEditTextString(view.findViewById((R.id.et_receiver_phone_numb))));
            builder.addFormDataPart("receiver_name", AppUtils.getEditTextString(view.findViewById((R.id.et_receiver_name))));
            builder.addFormDataPart("item", AppUtils.getEditTextString(view.findViewById((R.id.et_item_name))));
            builder.addFormDataPart("tripfrom", AppUtils.getEditTextString(view.findViewById((R.id.et_deliveryFrom))));
            builder.addFormDataPart("tripto", AppUtils.getEditTextString(view.findViewById((R.id.et_deliveryTo))));
            builder.addFormDataPart("item_type", tvProductType.getText().toString());
            builder.addFormDataPart("item_size", tvProductSize.getText().toString());
            builder.addFormDataPart("arrival_date", AppUtils.getEditTextString(view.findViewById((R.id.arrivalDate))));
            builder.addFormDataPart("other_information", AppUtils.getEditTextString(view.findViewById(R.id.parcel_detail)));
            builder.addFormDataPart("trip_amount", AppUtils.getEditTextString(view.findViewById(R.id.packageAmount)));

            String pathToStoredImage1 = FileUtils.getPath(getActivity(), Uri.parse(findVizImageView(R.id.iv_attach_1).getImageURL()));

            if (pathToStoredImage1 != null) {
                File file1 = new File(pathToStoredImage1);
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                builder.addFormDataPart("picture1", file1.getName(), requestBody1);
            }


            String pathToStoredImage2 = FileUtils.getPath(getActivity(), Uri.parse(findVizImageView(R.id.iv_attach_2).getImageURL()));
            if (pathToStoredImage2 != null) {
                File file2 = new File(pathToStoredImage2);
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                builder.addFormDataPart("picture2", file2.getName(), requestBody2);
            }
            String pathToStoredImage3 = FileUtils.getPath(getActivity(), Uri.parse(findVizImageView(R.id.iv_attach_3).getImageURL()));
            if (pathToStoredImage3 != null) {
                File file3 = new File(pathToStoredImage3);
                RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/*"), file3);
                builder.addFormDataPart("picture3", file3.getName(), requestBody3);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.build();

    }
    private void showPictureDialog(){

        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(getActivity());
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
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    cameraUri = FileProvider.getUriForFile(getActivity(),"com.roadstar.customerr.fileprovider",new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
                }else
                    cameraUri = FileProvider.getUriForFile(getActivity(),"com.roadstar.customerr.fileprovider",new File(Environment.getExternalStorageDirectory(), "pic_"+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
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