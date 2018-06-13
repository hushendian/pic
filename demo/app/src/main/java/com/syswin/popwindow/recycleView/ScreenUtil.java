package com.syswin.popwindow.recycleView;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.ViewConfiguration;

import java.lang.reflect.Method;

/**
 * Created by hushendian on 2018/6/9.
 */

public class ScreenUtil {
    public static DisplayMetrics mDisplayMetrics;
    public static int widthPixels;
    public static int heightPixels;
    public static float density;
    public static int maxImageWidth;
    public static int minImageWidth;
    public static int maxImageHeight;
    public static int minImageHeight;
    public static int mStatusBarHeight;

    public ScreenUtil() {
    }

    public static void init(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        widthPixels = mDisplayMetrics.widthPixels;
        heightPixels = mDisplayMetrics.heightPixels;
        density = mDisplayMetrics.density;
        maxImageWidth = widthPixels / 2;
        minImageWidth = widthPixels / 4;
        maxImageHeight = heightPixels / 3;
        minImageHeight = heightPixels / 8;
    }

    public static int dp2px(float dpValue) {
        return (int)(dpValue * density + 0.5F);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = mDisplayMetrics != null?mDisplayMetrics.scaledDensity:0.0F;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static int px2dp(float pxValue) {
        return (int)(pxValue / density + 0.5F);
    }

    public static int getStatusBarHeight() {
        if(mStatusBarHeight <= 0) {
            mStatusBarHeight = Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        }

        return mStatusBarHeight;
    }

    public static int getViewHeight(int high) {
        return high * heightPixels / 1920;
    }

    public static int getViewWidth(int width) {
        return width * widthPixels / 1080;
    }

    public static int[] getScreenInfo() {
        int[] info = new int[]{widthPixels, heightPixels};
        return info;
    }

    public static int getScreenWidth() {
        return widthPixels;
    }

    public static int getScreenHeight() {
        return heightPixels;
    }

    public static int getTextSize(int size) {
        return size * heightPixels / 1920;
    }

    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        int id = Resources.getSystem().getIdentifier("config_showNavigationBar", "bool", "android");
        if(id > 0) {
            hasNavigationBar = Resources.getSystem().getBoolean(id);
        }

        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", new Class[]{String.class});
            String navBarOverride = (String)m.invoke(systemPropertiesClass, new Object[]{"qemu.hw.mainkeys"});
            hasNavigationBar = "0".equals(navBarOverride);
        } catch (Exception var5) {
            var5.getStackTrace();
        }

        return hasNavigationBar;
    }

    public static int getNavigationBarHeight(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
        if(!hasMenuKey && !hasBackKey) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }
}

