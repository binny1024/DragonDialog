package com.smart.dialog_library;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by xu on 2017/3/2.
 */

public class Utils {
    public static void setViewAlphaAnimation(View view) {
        AlphaAnimation alphaAni = new AlphaAnimation(0.05f, 1.0f);
        alphaAni.setDuration(1000);            // 设置动画效果时间
        view.startAnimation(alphaAni);        // 添加光效动画到VIew
    }
    public static DisplayMetrics getScreenPixels(Activity activity) {
        if (activity == null) {
            return null;
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

   /*获取应用程序的名字*/
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

}
