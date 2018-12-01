package com.example.bighome.mvp.main.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bighome.R;
import com.example.bighome.mvp.BaseActivity;
import com.example.bighome.mvp.login.LoginActivity;
import com.example.bighome.util.SettingsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity{

    int i=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_child);

        initToolbar("个人设置",R.layout.layout_setting);

    }


    @OnClick({R.id.reset_password,R.id.clear_memory,R.id.open_door,R.id.notification_sound,R.id.notification_vibration,
            R.id.sign_out,R.id.developer_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_password:
                break;
            case R.id.clear_memory:
                break;
            case R.id.open_door:
                break;
            case R.id.notification_sound:
                break;
            case R.id.notification_vibration:
                break;
            case R.id.sign_out:
                navigateToLogin();
                break;
            case R.id.developer_mode:
                if(i<4){
                    i++;
                }else{
                    navigateToDeveloper();
                }
                break;
                default:
                    break;
        }
    }

    public void navigateToLogin() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean(SettingsUtil.AUTO_LOGIN,false).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        removeALLActivity();
    }

    public void navigateToDeveloper() {
        startActivity(new Intent(this, DeveloperActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
