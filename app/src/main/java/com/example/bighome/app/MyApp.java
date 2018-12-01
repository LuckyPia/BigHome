package com.example.bighome.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.bighome.retrofit.Api;
import com.example.bighome.style.MyToastStyle;
import com.example.bighome.util.SettingsUtil;
import com.example.bighome.websocket.WsConfig;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.realm.Realm;

public class MyApp extends Application {

    private static Context context;
    protected List<Activity> activityList = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        Realm.init(this);
        initUrl();
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null);
        ToastUtils.init(this);
        ToastUtils.initStyle(new MyToastStyle());
    }

    private void initUrl(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sp.getString(SettingsUtil.API_BASE, "").isEmpty()){
            Api.API_BASE=sp.getString(SettingsUtil.API_BASE, "");
        }
        if(!sp.getString(SettingsUtil.LOGIN, "").isEmpty()){
            Api.LOGIN=sp.getString(SettingsUtil.LOGIN, "");
        }
        if(!sp.getString(SettingsUtil.REGISTER, "").isEmpty()){
            Api.REGISTER=sp.getString(SettingsUtil.REGISTER, "");
        }
        if(!sp.getString(SettingsUtil.BANNER, "").isEmpty()){
            Api.BANNER=sp.getString(SettingsUtil.BANNER, "");
        }
        if(!sp.getString(SettingsUtil.URL_WEBSOCKET, "").isEmpty()){
            WsConfig.URL_WEBSOCKET =sp.getString(SettingsUtil.URL_WEBSOCKET, "");
        }
        Log.v("链接",Api.API_BASE);
        Log.v("链接",Api.LOGIN);
        Log.v("链接",Api.REGISTER);
        Log.v("链接",Api.BANNER);
        Log.v("链接",WsConfig.URL_WEBSOCKET);
    }

    public static Context getContext(){
        return context;
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }
}
