package com.example.bighome.util;

import android.os.Build;
import android.view.View;
import android.view.Window;

public class StatuUtil {
    /**
     * 说明：Android 6.0+ 状态栏图标原生反色操作
     */
    public static void setDarkStatusIcon(boolean dark, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }
}
