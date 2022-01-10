package com.roadstar.customerr.app.module.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.booking_status.Provider;
import com.roadstar.customerr.app.module.ui.chat.model.User;
import com.roadstar.customerr.common.utils.SharedHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> users = new ArrayList<>();
    UserMsgsAdapter adapter;
    ProgressDialog progressDialog;
    AppCompatTextView appCompatTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        appCompatTextView = findViewById(R.id.tv_title);
        progressDialog = new ProgressDialog(MessagesActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },3000);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UserMsgsAdapter(getApplicationContext(),users, new UserMsgsAdapter.OnItemClick() {
            @Override
            public void onClick(View view, int pos) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                Provider user = new Provider();
                user.setId(Integer.parseInt(users.get(pos).id));
                user.setEmail(users.get(pos).email);
                user.setFirstName(users.get(pos).name);
                user.setAvatar(users.get(pos).avata);

                intent.putExtra("user", user);
                intent.putExtra("canmessage",true);
                intent.putExtra("message","msg");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        users.clear();
        FirebaseDatabase.getInstance().getReference().child("Friends").child(UserManager.getUserId()).orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    HashMap mapUser = (HashMap) dataSnapshot.getValue();
                    User user = new User();
                    user.id = (String) mapUser.get("id");
                    user.name = (String) mapUser.get("name");
                    user.avata = (String) mapUser.get("avata");

                    if(mapUser.get("type")!=null)
                        user.type = (String) mapUser.get("type").toString();
                    else user.type = "1";

                    if(mapUser.get("message")!=null) {
                        HashMap mapMsg = (HashMap) mapUser.get("message");
                        if(mapMsg.get("text")!=null && mapMsg.get("text").toString().length()>0)
                            user.message.text = (String) mapMsg.get("text");
                        else user.message.text = (String) mapMsg.get("");
                        if(mapMsg.get("status")!=null)
                            user.message.status = (String) mapMsg.get("status");
                    }
                    if(mapUser.get("memberIds")!=null){
                        user.memberIds = (String) mapUser.get("memberIds");
                    }
                    appCompatTextView.setVisibility(View.GONE);
                    users.add(user);
                    adapter.notifyDataSetChanged();
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
                databaseError.getMessage();
            }
        });
    }
}