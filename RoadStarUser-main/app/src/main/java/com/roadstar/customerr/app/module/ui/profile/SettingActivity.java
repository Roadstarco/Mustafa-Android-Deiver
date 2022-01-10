package com.roadstar.customerr.app.module.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.core.app.NotificationManagerCompat;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.common.base.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    void init() {
        setActionBar(getString(R.string.settings));
        bindClicklisteners();
    }

    private void bindClicklisteners() {

        findViewById(R.id.lay_change_pass).setOnClickListener(this);
//        findSwitchByID(R.id.switch_notif).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // do something, the isChecked will be
//                // true if the switch is in the On position
//                UserManager.setNotifSetting(isChecked);
//            }
//        });
        findSwitchByID(R.id.switch_notif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);

                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

                startActivity(intent);
            }
        });
//        findSwitchByID(R.id.switch_notif).setChecked(UserManager.getNotifSetting());

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lay_change_pass:
                startActivity(this, ChangePasswordActivity.class);
                break;

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        findSwitchByID(R.id.switch_notif).setChecked(NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled());
    }
}