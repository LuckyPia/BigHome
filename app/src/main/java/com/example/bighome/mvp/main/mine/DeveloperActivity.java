package com.example.bighome.mvp.main.mine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bighome.R;
import com.example.bighome.mvp.BaseActivity;
import com.example.bighome.retrofit.Api;
import com.example.bighome.util.SettingsUtil;
import com.example.bighome.util.StatuUtil;
import com.example.bighome.websocket.WsConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeveloperActivity extends BaseActivity {

    @BindViews({R.id.et_api_base,R.id.et_banner,R.id.et_login,R.id.et_register,R.id.et_web_socket})
    List<AppCompatEditText> editTextList;
//    @BindViews({R.id.btn_api_base,R.id.btn_banner,R.id.btn_login,R.id.btn_register,R.id.btn_web_socket})
//    List<Button> buttonList;
//    @BindViews({R.id.btn_re_api_base,R.id.btn_re_banner,R.id.btn_re_login,R.id.btn_re_register,R.id.btn_re_web_socket})
//    List<Button> reButtonList;
//    声明Sharedpreferenced对象
     private SharedPreferences sp;

    public static final  String DEFAULT_API_BASE = "http://www.wanandroid.com/";
    public static final  String DEFAULT_BANNER = "banner/json";
    public static final  String DEFAULT_LOGIN = "user/login";
    public static final  String DEFAULT_REGISTER = "user/register";
    public static final  String DEFAULT_URL_WEBSOCKET = "ws://192.168.43.145:8080/WebMobileGroupChatServer/chat?name=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_child);

        initToolbar("开发者模式",R.layout.activity_developer);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        editTextList.get(0).setText(Api.API_BASE);
        editTextList.get(1).setText(Api.BANNER);
        editTextList.get(2).setText(Api.LOGIN);
        editTextList.get(3).setText(Api.REGISTER);
        editTextList.get(4).setText(WsConfig.URL_WEBSOCKET);

    }

    @OnClick({R.id.btn_api_base,R.id.btn_banner,R.id.btn_login,R.id.btn_register,R.id.btn_web_socket,
            R.id.btn_re_api_base,R.id.btn_re_banner,R.id.btn_re_login,R.id.btn_re_register,R.id.btn_re_web_socket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_api_base:
                String newApiBase=editTextList.get(0).getText().toString();
                if(!newApiBase.isEmpty()){
                    sp.edit().putString(SettingsUtil.API_BASE,newApiBase).apply();
                    Api.API_BASE=sp.getString(SettingsUtil.API_BASE, "");
                    editTextList.get(0).setText(sp.getString(SettingsUtil.API_BASE, ""));
                }
                break;
            case R.id.btn_banner:
                String newBanner=editTextList.get(1).getText().toString();
                if(!newBanner.isEmpty()){
                    sp.edit().putString(SettingsUtil.BANNER,newBanner).apply();
                    Api.BANNER=sp.getString(SettingsUtil.BANNER, "");
                    editTextList.get(1).setText(sp.getString(SettingsUtil.BANNER, ""));
                }
                break;
            case R.id.btn_login:
                String newLogin=editTextList.get(2).getText().toString();
                if(!newLogin.isEmpty()){
                    sp.edit().putString(SettingsUtil.LOGIN,newLogin).apply();
                    Api.LOGIN=sp.getString(SettingsUtil.LOGIN, "");
                    editTextList.get(2).setText(sp.getString(SettingsUtil.LOGIN, ""));
                }
                break;
            case R.id.btn_register:
                String newRegister=editTextList.get(3).getText().toString();
                if(!newRegister.isEmpty()){
                    sp.edit().putString(SettingsUtil.REGISTER,newRegister).apply();
                    Api.REGISTER=sp.getString(SettingsUtil.REGISTER, "");
                    editTextList.get(3).setText(sp.getString(SettingsUtil.REGISTER, ""));
                }
                break;
            case R.id.btn_web_socket:
                String newUrlWebSocket=editTextList.get(4).getText().toString();
                if(!newUrlWebSocket.isEmpty()){
                    sp.edit().putString(SettingsUtil.URL_WEBSOCKET,newUrlWebSocket).apply();
                    WsConfig.URL_WEBSOCKET =sp.getString(SettingsUtil.URL_WEBSOCKET, "");
                    editTextList.get(4).setText(sp.getString(SettingsUtil.URL_WEBSOCKET, ""));
                }
                break;
            case R.id.btn_re_api_base:
                sp.edit().putString(SettingsUtil.API_BASE,DEFAULT_API_BASE).apply();
                Api.API_BASE=sp.getString(SettingsUtil.API_BASE, "");
                editTextList.get(0).setText(sp.getString(SettingsUtil.API_BASE, ""));
                break;
            case R.id.btn_re_banner:
                sp.edit().putString(SettingsUtil.BANNER,DEFAULT_BANNER).apply();
                Api.BANNER=sp.getString(SettingsUtil.BANNER, "");
                editTextList.get(1).setText(sp.getString(SettingsUtil.BANNER, ""));
                break;
            case R.id.btn_re_login:
                sp.edit().putString(SettingsUtil.LOGIN,DEFAULT_LOGIN).apply();
                Api.LOGIN=sp.getString(SettingsUtil.LOGIN, "");
                editTextList.get(2).setText(sp.getString(SettingsUtil.LOGIN, ""));
                break;
            case R.id.btn_re_register:
                sp.edit().putString(SettingsUtil.REGISTER,DEFAULT_REGISTER).apply();
                Api.REGISTER=sp.getString(SettingsUtil.REGISTER, "");
                editTextList.get(3).setText(sp.getString(SettingsUtil.REGISTER, ""));
                break;
            case R.id.btn_re_web_socket:
                sp.edit().putString(SettingsUtil.URL_WEBSOCKET,DEFAULT_URL_WEBSOCKET).apply();
                WsConfig.URL_WEBSOCKET=sp.getString(SettingsUtil.URL_WEBSOCKET, "");
                editTextList.get(4).setText(sp.getString(SettingsUtil.URL_WEBSOCKET, ""));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
