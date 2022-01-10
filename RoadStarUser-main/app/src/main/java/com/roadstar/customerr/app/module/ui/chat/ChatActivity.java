package com.roadstar.customerr.app.module.ui.chat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.module.ui.chat.model.Consersation;
import com.roadstar.customerr.app.module.ui.chat.model.Message;
import com.roadstar.customerr.app.module.ui.chat.model.User;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.SharedHelper;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference RootRef;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private String messageSenderID;
    private Boolean checkCancel = false;
    private RecyclerView recyclerChat;
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    private ListMessageAdapter adapter;
    private ImageView imageView, cancel;
    private Boolean mBooean = false;
    private String roomId;
    private File imgFile;
    private boolean galleryOrCamera = false;
    private Consersation consersation;
    private ImageButton btnSend, btnCalendar, btnPdf;
    private EditText editWriteMessage;
    private LinearLayoutManager linearLayoutManager;
    private CircularImageView circularImageViewProfile;
    private TextView textViewName, textViewError;
    //    public static HashMap<String, Bitmap> bitmapAvataFriend;
//    public Bitmap bitmapAvataUser;
    private com.roadstar.customerr.app.data.models.booking_status.Provider model;
    private Uri imageuri;
    private String filePath;
//    private SharedPrefrencesMain sharedPrefrencesMain;
    private Long futureTimeInMillis;
    boolean isFutureMessage = false;
    boolean isMsgSent = false,isNotify = false;
    boolean canMessage = true;
    boolean photoOrVideo = false;
    private ProgressDialog dialog;
    private RelativeLayout relativeLayoutMsg, relativeLayoutBlock;
    private String checker = "text";
    private SharedHelper sharedPrefrencesMain;
    public UserManager profileResp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intentData = getIntent();
        //receive
        model = (com.roadstar.customerr.app.data.models.booking_status.Provider) getIntent().getSerializableExtra("user");
        canMessage = getIntent().getBooleanExtra("canmessage", true);
        String message = getIntent().getStringExtra("message");
        sharedPrefrencesMain = new SharedHelper();
        imageuri = null;

        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();


//        idFriend = model.getId();//intentData.getCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID);

//        profileResp = SharedHelper.getKey(this, "UserProfile",UserProfileResp.class);

        roomId = model.getId() + "_" + UserManager.getUserId();//intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID);
        String nameFriend = model.getFirstName();//intentData.getStringExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND);

        consersation = new Consersation();
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        btnPdf = (ImageButton) findViewById(R.id.btnpdf);
        btnCalendar = findViewById(R.id.btnCalendar);
        relativeLayoutMsg = findViewById(R.id.relaveMsg);
        relativeLayoutBlock = findViewById(R.id.relativeBlock);
        textViewError = findViewById(R.id.textViewBlock);
        cancel = findViewById(R.id.cancelfile);
        imageView = findViewById(R.id.image);
        btnSend.setOnClickListener(this);

        if (canMessage) {
            relativeLayoutMsg.setVisibility(View.VISIBLE);
            relativeLayoutBlock.setVisibility(View.GONE);
        } else {
            relativeLayoutMsg.setVisibility(View.GONE);
            relativeLayoutBlock.setVisibility(View.VISIBLE);
            textViewError.setText(message);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                editWriteMessage.setVisibility(View.VISIBLE);
                checkCancel = true;
            }
        });

//        String base64AvataUser = SharedPreferenceHelper.getInstance(this).getUserInfo().avata;
//        if (!base64AvataUser.equals(StaticConfig.STR_DEFAULT_BASE64)) {
//            byte[] decodedString = Base64.decode(base64AvataUser, Base64.DEFAULT);
//            bitmapAvataUser = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        } else {
//            bitmapAvataUser = null;
//        }

        editWriteMessage = (EditText) findViewById(R.id.editWriteMessage);
        textViewName = findViewById(R.id.tvName);
        circularImageViewProfile = findViewById(R.id.imgProfile);


        if (model.getAvatar() != null && model.getAvatar().toString().length() > 0)
            Glide.with(getApplicationContext()).load(model.getAvatar()).into(circularImageViewProfile);
        else
            Glide.with(getApplicationContext()).load(R.drawable.ic_user_name).into(circularImageViewProfile);
        textViewName.setText(model.getFirstName());

        if (String.valueOf(model.getId()) != null && nameFriend != null) {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerChat = (RecyclerView) findViewById(R.id.recyclerChat);
            recyclerChat.setLayoutManager(linearLayoutManager);
            adapter = new ListMessageAdapter(this, consersation, null, null);
            DatabaseReference firebaseDatabase;
            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(UserManager.getUserId() + "_" + model.getId());

//            if (model.getType().equals("1"))
//                firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId());
//            else {
//                Glide.with(getApplicationContext()).load(R.drawable.group_icon).into(circularImageViewProfile);
//                firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName());
//                btnCalendar.setVisibility(View.GONE);
//            }

            firebaseDatabase.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getValue() != null) {
                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                        Message newMessage = new Message();
                        newMessage.idSender = (String) String.valueOf(mapMessage.get("idSender"));
                        newMessage.idReceiver = (String) String.valueOf(mapMessage.get("idReceiver"));
                        newMessage.text = (String) mapMessage.get("text");
                        newMessage.type = (String) mapMessage.get("type");
                        try {
                            newMessage.timestamp = (long) mapMessage.get("timestamp");
                        }catch (ClassCastException classCastException){
                            classCastException.getMessage();
                        }

                        if (mapMessage.get("nameSender") != null && mapMessage.get("nameSender").toString().length() > 0)
                            newMessage.nameSender = (String) mapMessage.get("nameSender");
                        if (mapMessage.get("isFuture") != null && (Boolean) mapMessage.get("isFuture")) {
                            if (Long.parseLong(mapMessage.get("timestamp").toString()) <= System.currentTimeMillis())
                                consersation.getListMessageData().add(newMessage);
                        } else consersation.getListMessageData().add(newMessage);
                        adapter.notifyDataSetChanged();
                        linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recyclerChat.setAdapter(adapter);
        }

//        btnCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hitGetCountApi();
//
//            }
//        });

//        btnPdf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CharSequence options[] = new CharSequence[]{
//                        "Images",
//                        "PDF Files",
//                        "Videos"
//                };
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
//                builder.setTitle("Select the Files");
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            checker = "image";
//                            photoOrVideo = false;
//                            openFileContent();
////                            Intent intent = new Intent();
////                            intent.setAction(Intent.ACTION_GET_CONTENT);
////                            intent.setType("image/*");
////                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 438);
//                        }
//                        if (which == 1) {
//                            checker = "pdf";
//                            Intent galleryIntent = new Intent();
//                            galleryIntent.setType("application/pdf");
//                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                            startActivityForResult(Intent.createChooser(galleryIntent, "Select Pdf"), 438);
//                        }
//                        if (which == 2) {
//                            checker = "video";
//                            photoOrVideo = true;
//                            openFileContent1();
////                            Intent galleryIntent = new Intent();
////                            galleryIntent.setType("video/*");
////                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                            // We will be redirected to choose pdf
////                            startActivityForResult(Intent.createChooser(galleryIntent, "Select Video"), 438);
//                        }
//                    }
//                });
//                builder.create().show();
//
//
////                startActivityForResult(galleryIntent, 438);
//            }
//        });
    }

    void openFileContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((ChatActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && ChatActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (ChatActivity.this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select Camera or Gallery");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryOrCamera = false;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                        if (cameraIntent.resolveActivity(ChatActivity.this.getPackageManager()) != null) {
                            File pictureFile = null;
                            try {
                                pictureFile = createImageFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChatActivity.this, "Photo can't be created, Please Try Again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (pictureFile != null) {
                                //FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
                                Uri phototUrl = FileProvider.getUriForFile(ChatActivity.this, ChatActivity.this.getPackageName() + ".provider", pictureFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, phototUrl);
                                startActivityForResult(cameraIntent, 5);
                            }
                        }
                        //startActivityForResult(cameraIntent, 5);

                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryOrCamera = true;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 7);

//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 7);
                    }
                }).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 5);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Select Camera or Gallery");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    galleryOrCamera = false;
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
                    if (cameraIntent.resolveActivity(ChatActivity.this.getPackageManager()) != null) {
                        File pictureFile = null;
                        try {
                            pictureFile = createImageFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, "Photo can't be created, Please Try Again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (pictureFile != null) {
                            //FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
                            Uri phototUrl = FileProvider.getUriForFile(ChatActivity.this, ChatActivity.this.getPackageName() + ".provider", pictureFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, phototUrl);
                            startActivityForResult(cameraIntent, 5);
                        }
                    }
                    //startActivityForResult(cameraIntent, 5);

                }
            }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    galleryOrCamera = true;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 7);
                }
            }).show();
        }

    }

    void openFileContent1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((ChatActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && ChatActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (ChatActivity.this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select Camera or Gallery");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryOrCamera = false;
                        dispatchTakeVideoIntent();
                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        galleryOrCamera = true;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("video/mp4");
                        startActivityForResult(intent, 7);
                    }
                }).show();


                //startActivityForResult(intent, 7);

            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 5);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Select Camera or Gallery");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(cameraIntent,5);
                    dispatchTakeVideoIntent();
                }
            }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("video/mp4");
                    startActivityForResult(intent, 7);
                }
            }).show();
        }

    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(ChatActivity.this.getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, 8);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED && requestCode == 5) {
            if (!photoOrVideo) {
                openFileContent();
            } else {
//                openFileContent1();
            }
        }
    }

    private File createImageFile() throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = ChatActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageuri = Uri.parse(image.getAbsolutePath());
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

        if (checker.equals("pdf")) {
            if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                dialog = new ProgressDialog(this);
                imageuri = data.getData();
                editWriteMessage.setVisibility(View.GONE);
                checkCancel = false;
                imageView.setVisibility(View.VISIBLE);
//                imageView.setImageResource(R.drawable.pdf);
                cancel.setVisibility(View.VISIBLE);
            }
        } else if (checker.equals("image")) {
            switch (requestCode) {
                case 7:
                    if (resultCode == ChatActivity.this.RESULT_OK) {
                        imageuri = data.getData();
                        imageView.setVisibility(View.VISIBLE);
                        editWriteMessage.setVisibility(View.GONE);
                        cancel.setVisibility(View.VISIBLE);
                        checkCancel = false;
                        dialog = new ProgressDialog(this);
                        imageView.setImageURI(imageuri);
                    }
                    break;
                case 5:
                    if (resultCode == ChatActivity.this.RESULT_OK) {
                        imgFile = new File(String.valueOf(imageuri));
                        filePath = imgFile.getPath();
                        if (imgFile.exists()) {
                            checkCancel = false;

                            imageView.setImageURI(Uri.fromFile(imgFile));
                            imageView.setVisibility(View.VISIBLE);
                            editWriteMessage.setVisibility(View.GONE);
                            cancel.setVisibility(View.VISIBLE);
                            dialog = new ProgressDialog(this);
                        }
                    }
                    break;
            }


        } else if (checker.equals("video")) {
            switch (requestCode) {
                case 7:
                    if (resultCode == ChatActivity.this.RESULT_OK) {
                        checkCancel = false;
                        dialog = new ProgressDialog(this);
                        imageuri = data.getData();
                        imageView.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);

                        Glide.with(ChatActivity.this).load(imageuri)
                                .transition(withCrossFade())
                                //.apply(options.skipMemoryCache(true))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }

                                })
                                .into(imageView);


                        editWriteMessage.setVisibility(View.GONE);

                    }
                    break;
                case 8:
                    if (data != null) {
                        checkCancel = false;
                        dialog = new ProgressDialog(this);
                        imageuri = data.getData();
                        imageView.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        editWriteMessage.setVisibility(View.GONE);

                        if (imageuri != null) {
//                            filePath = UriUtils.getPathFromUri(ChatActivity.this, imageuri);
//                            imageView.setImageBitmap(getVideoThumb(filePath));
                        }
                    }
                    break;
            }

//            dialog = new ProgressDialog(this);
//            imageuri = data.getData();
//            imageView.setVisibility(View.VISIBLE);
//
//            Glide.with(ChatActivity.this).load(imageuri)
//                    .transition(withCrossFade())
//                    //.apply(options.skipMemoryCache(true))
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            return false;
//                        }
//
//                    })
//                    .into(imageView);
//
//
//            editWriteMessage.setVisibility(View.GONE);

        }

    }

    public static Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return media.getFrameAtTime();
    }

    Calendar date;

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ChatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        isFutureMessage = true;
                        futureTimeInMillis = date.getTimeInMillis();
                        if (futureTimeInMillis < System.currentTimeMillis()) {
                            Toast.makeText(ChatActivity.this, "Time should be grater then current time", Toast.LENGTH_SHORT).show();
                            isFutureMessage = false;
                        }
//                        Toast.makeText(ChatActivity.this, "The choosen one " + date.getTime(), Toast.LENGTH_SHORT).show();
//                        Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        Date maxDate = new Date (9999, 11, 31);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent result = new Intent();
            result.putExtra("idFriend", model.getId());
            setResult(RESULT_OK, result);
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        if (isNotify && isMsgSent && model.getType().equals("1") && consersation.getListMessageData().size() > 0) {
//            if (consersation.getListMessageData().get(consersation.getListMessageData().size() - 1).idSender.equals(sharedPrefrencesMain.getId())) {
//                hitNotifyApi(model.getId());
//            } else this.finish();
//        } else this.finish();
//        Intent result = new Intent();
//        result.putExtra("idFriend", model.getId());
//        setResult(RESULT_OK, result);
//        this.finish();
    }

//    public void hitNotifyApi(String pid) {
//        Call<GetNotifications> verify;
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        verify = apiInterface.sendnotification("Bearer " + sharedPrefrencesMain.getToken(), pid);
//
//        verify.enqueue(new Callback<GetNotifications>() {
//            @Override
//            public void onResponse(Call<GetNotifications> call, Response<GetNotifications> response) {
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<GetNotifications> call, Throwable t) {
//                finish();
//                Toast.makeText(AppContext.getAppContext(), "fail", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    public String getExtention() {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(imageuri));
    }

    private void SendMessage(View view){
        if (view.getId() == R.id.btnSend) {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
                checker = "text";
                checkCancel = false;
            }
            if (!checkCancel) {
                checkCancel = true;
//                if (checker.equals("pdf")) {
//                    if (imageuri != null) {
//                        mBooean = true;
//                        isMsgSent = true;
//                        editWriteMessage.setText("");
//                        Message newMessage = new Message();
//                        newMessage.idSender = sharedPrefrencesMain.getId();
//                        newMessage.idReceiver = model.getId();
//                        newMessage.status = "1";
//                        newMessage.type = checker;
//                        newMessage.isFuture = isFutureMessage;
//                        if (isFutureMessage)
//                            newMessage.timestamp = futureTimeInMillis;
//                        else
//                            newMessage.timestamp = System.currentTimeMillis();
//
//                        Message recMessage = new Message();
//                        recMessage.idSender = sharedPrefrencesMain.getId();
//                        recMessage.idReceiver = model.getId();
//                        recMessage.status = "0";
//                        recMessage.type = checker;
//                        recMessage.isFuture = isFutureMessage;
//                        if (isFutureMessage)
//                            recMessage.timestamp = futureTimeInMillis;
//                        else
//                            recMessage.timestamp = System.currentTimeMillis();
//
//                        com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                        user.id = model.getId();
//                        user.avata = model.getProfilepic();
//                        user.email = model.getEmail();
//                        user.message = newMessage;
//                        user.name = model.getName();
//
//                        com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                        user2.id = sharedPrefrencesMain.getId();
//                        user2.avata = sharedPrefrencesMain.getProfileUrl();
//                        user2.email = sharedPrefrencesMain.getEmail();
//                        user2.message = recMessage;
//                        user2.name = sharedPrefrencesMain.getName();
//
//                        if (model.getType().equals("1")) {
//                            user.type = "1";
//                            dialog.setMessage("Uploading");
//                            dialog.setCanceledOnTouchOutside(false);
//                            dialog.show();
//                            final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
//                            reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            newMessage.text = uri.toString();
//                                            recMessage.text = uri.toString();
//                                            FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                            FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                            FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                            FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                            imageView.setVisibility(View.GONE);
//                                            dialog.dismiss();
//                                            Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                    try {
//                                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                        dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                        dialog.setCancelable(false);
//                                    } catch (Exception exception) {
//                                        Log.d("pppp", "ggg" + exception);
//                                    }
//
//                                }
//                            });
//
//                        } else {
//                            Message grpMessage = new Message();
//                            grpMessage.idSender = sharedPrefrencesMain.getId();
//                            grpMessage.nameSender = sharedPrefrencesMain.getName();
//                            grpMessage.idReceiver = model.getName();
//                            grpMessage.status = "1";
//                            grpMessage.type = checker;
//                            grpMessage.timestamp = System.currentTimeMillis();
//                            dialog.show();
//                            final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
//                            reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            grpMessage.text = uri.toString();
//                                            FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                            imageView.setVisibility(View.GONE);
//                                            dialog.dismiss();
//                                            Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                    try {
//                                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                        dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                        dialog.setCancelable(false);
//                                    } catch (Exception exception) {
//                                        Log.d("pppp", "ggg" + exception);
//                                    }
//
//                                }
//                            });
//
//
//                        }
//                    }
//
//
//                }
//                if (checker.equals("image")) {
//                    if (galleryOrCamera) {
//                        if (imageuri != null) {
//                            mBooean = true;
//                            isMsgSent = true;
//                            editWriteMessage.setText("");
//
//                            Message newMessage = new Message();
//                            newMessage.idSender = sharedPrefrencesMain.getId();
//                            newMessage.idReceiver = model.getId();
//                            newMessage.status = "1";
//                            newMessage.type = checker;
//                            newMessage.isFuture = isFutureMessage;
//
//                            if (isFutureMessage)
//                                newMessage.timestamp = futureTimeInMillis;
//                            else
//                                newMessage.timestamp = System.currentTimeMillis();
//
//                            Message recMessage = new Message();
//                            recMessage.idSender = sharedPrefrencesMain.getId();
//                            recMessage.idReceiver = model.getId();
//                            recMessage.status = "0";
//                            recMessage.type = checker;
//                            recMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                recMessage.timestamp = futureTimeInMillis;
//                            else
//                                recMessage.timestamp = System.currentTimeMillis();
//
//                            com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                            user.id = model.getId();
//                            user.avata = model.getProfilepic();
//                            user.email = model.getEmail();
//                            user.message = newMessage;
//                            user.name = model.getName();
//
//                            com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                            user2.id = sharedPrefrencesMain.getId();
//                            user2.avata = sharedPrefrencesMain.getProfileUrl();
//                            user2.email = sharedPrefrencesMain.getEmail();
//                            user2.message = recMessage;
//                            user2.name = sharedPrefrencesMain.getName();
//
//                            if (model.getType().equals("1")) {
//                                user.type = "1";
//                                dialog.setMessage("Uploading");
//                                dialog.setCanceledOnTouchOutside(false);
//                                dialog.show();
//
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".jpg");
//                                reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                newMessage.text = uri.toString();
//                                                recMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            dialog.setCancelable(false);
//
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            } else {
//                                Message grpMessage = new Message();
//                                grpMessage.idSender = sharedPrefrencesMain.getId();
//                                grpMessage.nameSender = sharedPrefrencesMain.getName();
//                                grpMessage.idReceiver = model.getName();
//                                grpMessage.status = "1";
//                                grpMessage.type = checker;
//                                grpMessage.timestamp = System.currentTimeMillis();
//
//                                dialog.show();
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".jpg");
//                                reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                grpMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            dialog.setCancelable(false);
//
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//
//                            }
//                        } else {
//
//                        }
//                    } else {
//                        if (imageuri != null) {
//                            mBooean = true;
//                            isMsgSent = true;
//                            editWriteMessage.setText("");
//
//                            Message newMessage = new Message();
//                            newMessage.idSender = sharedPrefrencesMain.getId();
//                            newMessage.idReceiver = model.getId();
//                            newMessage.status = "1";
//                            newMessage.type = checker;
//                            newMessage.isFuture = isFutureMessage;
//
//                            if (isFutureMessage)
//                                newMessage.timestamp = futureTimeInMillis;
//                            else
//                                newMessage.timestamp = System.currentTimeMillis();
//
//                            Message recMessage = new Message();
//                            recMessage.idSender = sharedPrefrencesMain.getId();
//                            recMessage.idReceiver = model.getId();
//                            recMessage.status = "0";
//                            recMessage.type = checker;
//                            recMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                recMessage.timestamp = futureTimeInMillis;
//                            else
//                                recMessage.timestamp = System.currentTimeMillis();
//
//                            com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                            user.id = model.getId();
//                            user.avata = model.getProfilepic();
//                            user.email = model.getEmail();
//                            user.message = newMessage;
//                            user.name = model.getName();
//
//                            com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                            user2.id = sharedPrefrencesMain.getId();
//                            user2.avata = sharedPrefrencesMain.getProfileUrl();
//                            user2.email = sharedPrefrencesMain.getEmail();
//                            user2.message = recMessage;
//                            user2.name = sharedPrefrencesMain.getName();
//
//                            if (model.getType().equals("1")) {
//                                user.type = "1";
//                                dialog.setMessage("Uploading");
//                                dialog.setCanceledOnTouchOutside(false);
//                                dialog.show();
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".jpg");
//                                reference.putFile(Uri.fromFile(new File(filePath))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                newMessage.text = uri.toString();
//                                                recMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            } else {
//                                Message grpMessage = new Message();
//                                grpMessage.idSender = sharedPrefrencesMain.getId();
//                                grpMessage.nameSender = sharedPrefrencesMain.getName();
//                                grpMessage.idReceiver = model.getName();
//                                grpMessage.status = "1";
//                                grpMessage.type = checker;
//                                grpMessage.timestamp = System.currentTimeMillis();
//
//                                dialog.show();
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".jpg");
//                                reference.putFile(Uri.fromFile(new File(filePath))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                grpMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            }
//                        } else {
//
//                        }
//                    }
//
//                }
//                if (checker.equals("video")) {
//                    if (galleryOrCamera) {
//                        if (imageuri != null) {
//                            mBooean = true;
//                            isMsgSent = true;
//                            editWriteMessage.setText("");
//                            Message newMessage = new Message();
//                            newMessage.idSender = sharedPrefrencesMain.getId();
//                            newMessage.idReceiver = model.getId();
//                            newMessage.status = "1";
//                            newMessage.type = checker;
//                            newMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                newMessage.timestamp = futureTimeInMillis;
//                            else
//                                newMessage.timestamp = System.currentTimeMillis();
//                            Message recMessage = new Message();
//                            recMessage.idSender = sharedPrefrencesMain.getId();
//                            recMessage.idReceiver = model.getId();
//                            recMessage.status = "0";
//                            recMessage.type = checker;
//                            recMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                recMessage.timestamp = futureTimeInMillis;
//                            else
//                                recMessage.timestamp = System.currentTimeMillis();
//
//                            com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                            user.id = model.getId();
//                            user.avata = model.getProfilepic();
//                            user.email = model.getEmail();
//                            user.message = newMessage;
//                            user.name = model.getName();
//
//                            com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                            user2.id = sharedPrefrencesMain.getId();
//                            user2.avata = sharedPrefrencesMain.getProfileUrl();
//                            user2.email = sharedPrefrencesMain.getEmail();
//                            user2.message = recMessage;
//                            user2.name = sharedPrefrencesMain.getName();
//
//                            if (model.getType().equals("1")) {
//                                user.type = "1";
//                                dialog.setMessage("Uploading");
//                                dialog.setCanceledOnTouchOutside(false);
//                                dialog.show();
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + getExtention());
//                                reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                newMessage.text = uri.toString();
//                                                recMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            dialog.setCancelable(false);
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            } else {
//                                Message grpMessage = new Message();
//                                grpMessage.idSender = sharedPrefrencesMain.getId();
//                                grpMessage.nameSender = sharedPrefrencesMain.getName();
//                                grpMessage.idReceiver = model.getName();
//                                grpMessage.status = "1";
//                                grpMessage.type = checker;
//                                grpMessage.timestamp = System.currentTimeMillis();
//                                dialog.show();
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + getExtention());
//                                reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                grpMessage.text = uri.toString();
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            dialog.setCancelable(false);
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            }
//                        } else {
//                            isMsgSent = true;
//                            mBooean = false;
//                            editWriteMessage.setText("");
//                            Message newMessage = new Message();
//                            newMessage.text = content;
//                            newMessage.idSender = sharedPrefrencesMain.getId();
//                            newMessage.idReceiver = model.getId();
//                            newMessage.type = "text";
//                            newMessage.status = "1";
//                            newMessage.isFuture = isFutureMessage;
//
//                            if (isFutureMessage)
//                                newMessage.timestamp = futureTimeInMillis;
//                            else
//                                newMessage.timestamp = System.currentTimeMillis();
//
//                            Message recMessage = new Message();
//                            recMessage.text = content;
//                            recMessage.idSender = sharedPrefrencesMain.getId();
//                            recMessage.idReceiver = model.getId();
//                            recMessage.status = "0";
//                            recMessage.type = checker;
//                            recMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                recMessage.timestamp = futureTimeInMillis;
//                            else
//                                recMessage.timestamp = System.currentTimeMillis();
//                            com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                            user.id = model.getId();
//                            user.avata = model.getProfilepic();
//                            user.email = model.getEmail();
//                            user.message = newMessage;
//                            user.name = model.getName();
//
//                            com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                            user2.id = sharedPrefrencesMain.getId();
//                            user2.avata = sharedPrefrencesMain.getProfileUrl();
//                            user2.email = sharedPrefrencesMain.getEmail();
//                            user2.message = recMessage;
//                            user2.name = sharedPrefrencesMain.getName();
//                            if (model.getType().equals("1")) {
//                                user.type = "1";
//                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                            } else {
//                                Message grpMessage = new Message();
//                                grpMessage.text = content;
//                                grpMessage.idSender = sharedPrefrencesMain.getId();
//                                grpMessage.nameSender = sharedPrefrencesMain.getName();
//                                grpMessage.idReceiver = model.getName();
//                                grpMessage.type = checker;
//                                grpMessage.status = "1";
//                                grpMessage.timestamp = System.currentTimeMillis();
//                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//                            }
//                        }
//                    } else {
//                        if (imageuri != null) {
//                            mBooean = true;
//                            isMsgSent = true;
//                            editWriteMessage.setText("");
//
//                            Message newMessage = new Message();
//                            newMessage.idSender = sharedPrefrencesMain.getId();
//                            newMessage.idReceiver = model.getId();
//                            newMessage.status = "1";
//                            newMessage.type = checker;
//                            newMessage.isFuture = isFutureMessage;
//
//                            if (isFutureMessage)
//                                newMessage.timestamp = futureTimeInMillis;
//                            else
//                                newMessage.timestamp = System.currentTimeMillis();
//
//                            Message recMessage = new Message();
//                            recMessage.idSender = sharedPrefrencesMain.getId();
//                            recMessage.idReceiver = model.getId();
//                            recMessage.status = "0";
//                            recMessage.type = checker;
//                            recMessage.isFuture = isFutureMessage;
//                            if (isFutureMessage)
//                                recMessage.timestamp = futureTimeInMillis;
//                            else
//                                recMessage.timestamp = System.currentTimeMillis();
//
//                            com.codercrew.fpw.chat.model.User user = new com.codercrew.fpw.chat.model.User();
//                            user.id = model.getId();
//                            user.avata = model.getProfilepic();
//                            user.email = model.getEmail();
//                            user.message = newMessage;
//                            user.name = model.getName();
//
//                            com.codercrew.fpw.chat.model.User user2 = new com.codercrew.fpw.chat.model.User();
//                            user2.id = sharedPrefrencesMain.getId();
//                            user2.avata = sharedPrefrencesMain.getProfileUrl();
//                            user2.email = sharedPrefrencesMain.getEmail();
//                            user2.message = recMessage;
//                            user2.name = sharedPrefrencesMain.getName();
//
//                            if (model.getType().equals("1")) {
//                                user.type = "1";
//                                dialog.setMessage("Uploading");
//                                dialog.setCanceledOnTouchOutside(false);
//                                dialog.show();
//
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + getExtention());
//                                reference.putFile(Uri.fromFile(new File(filePath))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                newMessage.text = uri.toString();
//                                                recMessage.text = uri.toString();
//
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(sharedPrefrencesMain.getId()).child(model.getId()).setValue(user);
//                                                FirebaseDatabase.getInstance().getReference().child("Friends").child(sharedPrefrencesMain.getFmCode()).child(model.getId()).child(sharedPrefrencesMain.getId()).setValue(user2);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(sharedPrefrencesMain.getId() + "_" + model.getId()).push().setValue(newMessage);
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + sharedPrefrencesMain.getId()).push().setValue(recMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//                            } else {
//                                Message grpMessage = new Message();
//                                grpMessage.idSender = sharedPrefrencesMain.getId();
//                                grpMessage.nameSender = sharedPrefrencesMain.getName();
//                                grpMessage.idReceiver = model.getName();
//                                grpMessage.status = "1";
//                                grpMessage.type = checker;
//                                grpMessage.timestamp = System.currentTimeMillis();
//
//                                final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + getExtention());
//                                reference.putFile(Uri.fromFile(new File(filePath))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                            @Override
//                                            public void onSuccess(Uri uri) {
//                                                newMessage.text = uri.toString();
//                                                recMessage.text = uri.toString();
//                                                FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//                                                dialog.dismiss();
//                                                Toast.makeText(ChatActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                                        try {
//                                            float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                                            dialog.setMessage("Uploaded :" + (int) percent + "%");
//                                            imageView.setVisibility(View.GONE);
//                                            editWriteMessage.setVisibility(View.VISIBLE);
//                                        } catch (Exception exception) {
//                                            Log.d("pppp", "ggg" + exception);
//                                        }
//
//                                    }
//                                });
//
//
//                            }
//                        }
//
//                    }
//
//                }
                if (checker.equals("text")) {
                    isMsgSent = true;
                    mBooean = false;
                    editWriteMessage.setText("");
                    Message newMessage = new Message();
                    newMessage.text = content;
                    newMessage.idSender = UserManager.getUserId();
                    newMessage.idReceiver = model.getId()+"";
                    newMessage.type = "text";
                    newMessage.status = "1";
                    newMessage.isFuture = isFutureMessage;

                    if (isFutureMessage)
                        newMessage.timestamp = futureTimeInMillis;
                    else
                        newMessage.timestamp = System.currentTimeMillis();

                    Message recMessage = new Message();
                    recMessage.text = content;
                    recMessage.idSender = UserManager.getUserId();
                    recMessage.idReceiver = model.getId()+"";
                    recMessage.status = "0";
                    recMessage.type = checker;
                    recMessage.isFuture = isFutureMessage;
                    if (isFutureMessage)
                        recMessage.timestamp = futureTimeInMillis;
                    else
                        recMessage.timestamp = System.currentTimeMillis();
                    User user = new User();
                    user.id = model.getId()+"";
                    if(model.getAvatar()!=null && model.getAvatar().toString().length()>0)
                        user.avata = model.getAvatar().toString();
                    else user.avata = "";
                    user.email = model.getEmail();
                    user.message = newMessage;
                    user.name = model.getFirstName();

                    User user2 = new User();
                    user2.id = UserManager.getUserId();
                    user2.avata = UserManager.getImage();
                    user2.email = UserManager.getEmail();
                    user2.message = recMessage;
                    user2.name = UserManager.getFirstname();
//                    if (model.getType().equals("1")) {
                        user.type = "1";
                        FirebaseDatabase.getInstance().getReference().child("Friends").child(UserManager.getUserId()).child(model.getId()+"").setValue(user);
                        FirebaseDatabase.getInstance().getReference().child("Friends").child(model.getId()+"").child(UserManager.getUserId()).setValue(user2);
                        FirebaseDatabase.getInstance().getReference().child("Messages").child(UserManager.getUserId() + "_" + model.getId()).push().setValue(newMessage);
                        FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getId() + "_" + UserManager.getUserId()).push().setValue(recMessage);
                    hitNotifyApi(model.getId()+"");
//                    }
//                    else {
//                        Message grpMessage = new Message();
//                        grpMessage.text = content;
//                        grpMessage.idSender = sharedPrefrencesMain.getId();
//                        grpMessage.nameSender = sharedPrefrencesMain.getName();
//                        grpMessage.idReceiver = model.getName();
//                        grpMessage.type = checker;
//                        grpMessage.status = "1";
//                        grpMessage.timestamp = System.currentTimeMillis();
//                        FirebaseDatabase.getInstance().getReference().child("Messages").child(model.getName()).push().setValue(grpMessage);
//                    }
                }
            } else {
                return;
            }
            if(isFutureMessage){
                isNotify = false;
                isFutureMessage = false;
//                hitDecreaseCountApi();
            }else isNotify = true;

        }
    }

    @Override
    public void onClick(View view) {
        if(checker.equals("video")){
//            if(imageuri!=null)
//            CheckVideo(new File(UriUtils.getPathFromUri(getApplicationContext(),imageuri)),view);
        }else if(checker.equals("image")){
//            if(imageuri!=null && filePath==null)
//                CheckImage(new File(UriUtils.getPathFromUri(getApplicationContext(),imageuri)),view);
//            else
//                CheckImage(new File(filePath),view);
        }else if(checker.equals("text"))
            SendMessage(view);
//            CheckText(editWriteMessage.getText().toString(),view);
        else {
            String content = editWriteMessage.getText().toString().trim();
            if (content.length() > 0) {
//                CheckText(editWriteMessage.getText().toString(),view);
            }else
                SendMessage(view);
        }

    }

    class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public Context context;
        public Consersation consersation;


        public ListMessageAdapter(Context context, Consersation consersation, HashMap<String, Bitmap> bitmapAvata, Bitmap bitmapAvataUser) {
            this.context = context;
            this.consersation = consersation;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
                View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
                return new ItemMessageFriendHolder(view);
            } else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
                View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
                return new ItemMessageUserHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemMessageFriendHolder) {
                if (consersation.getListMessageData().get(position).nameSender != null && consersation.getListMessageData().get(position).nameSender.length() > 0) {
                    ((ItemMessageFriendHolder) holder).txtContent.setText(Html.fromHtml(Utils.getFormated(consersation.getListMessageData().get(position).nameSender + ":", " " + consersation.getListMessageData().get(position).text)));
//                    if (consersation.getListMessageData().get(position).type.equals("pdf")) {
//                        ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.VISIBLE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.VISIBLE);
//                        ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).pdf_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                        ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(context, viewpdf.class);
//                                intent.putExtra("fileurl", consersation.getListMessageData().get(position).text);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                            }
//                        });
//                    } else if (consersation.getListMessageData().get(position).type.equals("image")) {
//                        ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_id_play.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.VISIBLE);
//                        ((ItemMessageFriendHolder) holder).pdf_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                        ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                                intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                                startActivity(intent);
//                            }
//                        });
//                        Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                                .transition(withCrossFade())
//                                //.apply(options.skipMemoryCache(true))
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//
//                                })
//                                .into(((ItemMessageFriendHolder) holder).receiver_banner_image);
//
//
//                    } else if (consersation.getListMessageData().get(position).type.equals("video")) {
//                        ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                        ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.VISIBLE);
//                        ((ItemMessageFriendHolder) holder).receiver_id_play.setVisibility(View.VISIBLE);
//                        ((ItemMessageFriendHolder) holder).pdf_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                        ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                                intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                                startActivity(intent);
//                            }
//                        });
//
//                        Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                                .transition(withCrossFade())
//                                //.apply(options.skipMemoryCache(true))
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                        return false;
//                                    }
//
//                                })
//                                .into(((ItemMessageFriendHolder) holder).receiver_banner_image);
//
//                    } else {

//                    }

                }
                ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
                ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
                ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.GONE);
                ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
                ((ItemMessageFriendHolder) holder).textViewTime.setText(Utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//                else if (consersation.getListMessageData().get(position).type.equals("pdf")) {
//                    ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).pdf_textViewTime.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.GONE);
//
//                    ((ItemMessageFriendHolder) holder).pdf_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                    ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, viewpdf.class);
//                            intent.putExtra("fileurl", consersation.getListMessageData().get(position).text);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        }
//                    });
//
//                } else if (consersation.getListMessageData().get(position).type.equals("image")) {
//                    ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).videoImage_textViewTime.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_id_play.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).videoImage_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//                    ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                            intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                            startActivity(intent);
//                        }
//                    });
//                    Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                            .transition(withCrossFade())
//                            //.apply(options.skipMemoryCache(true))
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                            })
//                            .into(((ItemMessageFriendHolder) holder).receiver_banner_image);
//
//
//                } else if (consersation.getListMessageData().get(position).type.equals("video")) {
//                    ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).videoImage_textViewTime.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).receiver_id_play.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).videoImage_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                    ((ItemMessageFriendHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                            intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                            startActivity(intent);
//                        }
//                    });
//
//                    Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                            .transition(withCrossFade())
//                            //.apply(options.skipMemoryCache(true))
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    ((ItemMessageFriendHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                            })
//                            .into(((ItemMessageFriendHolder) holder).receiver_banner_image);
//
//                } else {
//                    ((ItemMessageFriendHolder) holder).receiver_relativeLayout.setVisibility(View.VISIBLE);
//                    ((ItemMessageFriendHolder) holder).reciver_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).receiver_layout.setVisibility(View.GONE);
//                    ((ItemMessageFriendHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
//                    ((ItemMessageFriendHolder) holder).textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//                }
//
//            } else if (holder instanceof ItemMessageUserHolder) {
//                if (consersation.getListMessageData().get(position).type.equals("pdf")) {
//                    ((ItemMessageUserHolder) holder).pdf.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).linearLayout.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout_pdf.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                    ((ItemMessageUserHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, viewpdf.class);
//                            intent.putExtra("fileurl", consersation.getListMessageData().get(position).text);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        }
//                    });
//                    return;
//
//                } else if (consersation.getListMessageData().get(position).type.equals("image")) {
//                    ((ItemMessageUserHolder) holder).pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).linearLayout.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).id_play.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).videoImage_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                    ((ItemMessageUserHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                            intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                            startActivity(intent);
//                        }
//                    });
//                    Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                            .transition(withCrossFade())
//                            //.apply(options.skipMemoryCache(true))
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    ((ItemMessageUserHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    ((ItemMessageUserHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                            })
//                            .into(((ItemMessageUserHolder) holder).banner_image);
//                    return;
//                } else if (consersation.getListMessageData().get(position).type.equals("video")) {
//                    ((ItemMessageUserHolder) holder).pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).linearLayout.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).id_play.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).videoImage_textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//
//                    ((ItemMessageUserHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(), ViewPostNewActivity.class);
//                            intent.putExtra("url", consersation.getListMessageData().get(position).text);
//                            startActivity(intent);
//                        }
//                    });
//
//                    Glide.with(context).load(consersation.getListMessageData().get(position).text)
//                            .transition(withCrossFade())
//                            //.apply(options.skipMemoryCache(true))
//                            .listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    ((ItemMessageUserHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    ((ItemMessageUserHolder) holder).progressBar.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                            })
//                            .into(((ItemMessageUserHolder) holder).banner_image);
//                    return;
//                } else {
//                    ((ItemMessageUserHolder) holder).linearLayout.setVisibility(View.VISIBLE);
//                    ((ItemMessageUserHolder) holder).layout_pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).layout.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).pdf.setVisibility(View.GONE);
//                    ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
//                    ((ItemMessageUserHolder) holder).textViewTime.setText(utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
//                    return;
//                }
//
            }else {
                    ((ItemMessageUserHolder) holder).linearLayout.setVisibility(View.VISIBLE);
                    ((ItemMessageUserHolder) holder).layout_pdf.setVisibility(View.GONE);
                    ((ItemMessageUserHolder) holder).pdf_textViewTime.setVisibility(View.GONE);
                    ((ItemMessageUserHolder) holder).layout.setVisibility(View.GONE);
                    ((ItemMessageUserHolder) holder).pdf.setVisibility(View.GONE);
                    ((ItemMessageUserHolder) holder).txtContent.setText(consersation.getListMessageData().get(position).text);
                    ((ItemMessageUserHolder) holder).textViewTime.setText(Utils.getTimeAgo(Long.parseLong(consersation.getListMessageData().get(position).timestamp + "")));
                }
        }

        @Override
        public int getItemViewType(int position) {
            return consersation.getListMessageData().get(position).idSender.equals(UserManager.getUserId()) ? ChatActivity.VIEW_TYPE_USER_MESSAGE : ChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
        }

        @Override
        public int getItemCount() {
            return consersation.getListMessageData().size();
        }
    }

    class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public TextView txtContent, textViewTime, videoImage_textViewTime, pdf_textViewTime;
        public CircularImageView avata;
        public RelativeLayout relativeLayout, layout, layout_pdf;
        ImageView pdf, imagefromgallery, banner_image, id_play;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.textContentUser);
            avata = (CircularImageView) itemView.findViewById(R.id.imageView2);
            videoImage_textViewTime = itemView.findViewById(R.id.video_textTime);
            pdf_textViewTime = itemView.findViewById(R.id.pdf_textTime);
            textViewTime = itemView.findViewById(R.id.textTime);
            relativeLayout = itemView.findViewById(R.id.layout2);
            layout_pdf = itemView.findViewById(R.id.layout_pdf);
            layout = itemView.findViewById(R.id.rel);
            banner_image = itemView.findViewById(R.id.iv_banner);
            id_play = itemView.findViewById(R.id.id_play);
            progressBar = itemView.findViewById(R.id.progress);
            pdf = itemView.findViewById(R.id.pdf);
            linearLayout = itemView.findViewById(R.id.chat);
        }

    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public TextView txtContent, textViewTime, videoImage_textViewTime, pdf_textViewTime;
        public CircularImageView avata;
        public RelativeLayout receiver_relativeLayout, receiver_layout, receiver_layout_pdf;
        ImageView reciver_pdf, receiver_banner_image, receiver_id_play;
        ProgressBar progressBar;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.textContentFriend);
            avata = (CircularImageView) itemView.findViewById(R.id.imageView3);
            textViewTime = itemView.findViewById(R.id.textTime);
            videoImage_textViewTime = itemView.findViewById(R.id.video_textTime);
            pdf_textViewTime = itemView.findViewById(R.id.pdf_textTime);
            receiver_relativeLayout = itemView.findViewById(R.id.reciver_layout);
            receiver_layout = itemView.findViewById(R.id.receiver_rel);
            receiver_banner_image = itemView.findViewById(R.id.iv_receiver_banner);
            receiver_id_play = itemView.findViewById(R.id.id_receiver_play);
            progressBar = itemView.findViewById(R.id.receiver_progress);
            reciver_pdf = itemView.findViewById(R.id.reciver_pdf);
            receiver_layout_pdf = itemView.findViewById(R.id.receiver_layout_pdf);
        }

    }


    public void hitNotifyApi(String id) {
        Call<String> verify;
        com.roadstar.customerr.app.data.models.booking_status.User user = new com.roadstar.customerr.app.data.models.booking_status.User();
//         submitRatingReq = new SubmitRatingReq();
        user.setUser_id(id);

        ApiInterface mApiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<ResponseBody> call = mApiInterface.sendnotification(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                response.toString();
//                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                finish();
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            }
        });
    }
}
