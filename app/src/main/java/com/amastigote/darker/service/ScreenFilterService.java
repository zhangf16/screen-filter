package com.amastigote.darker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.amastigote.darker.R;
import com.amastigote.darker.model.DarkerSettings;

public class ScreenFilterService extends Service{
    static LinearLayout linearLayout;
    static WindowManager.LayoutParams layoutParams;
    static WindowManager windowManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createScreenFilter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (linearLayout != null)
            windowManager.removeView(linearLayout);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings(value = "all")
    private void createScreenFilter() {
        layoutParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                           | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                           | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                           | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        LayoutInflater layoutInflater = LayoutInflater.from(getApplication());
        linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.screen_filter, null);
        windowManager.addView(linearLayout, layoutParams);
        removeScreenFilter();
    }

    public static void updateScreenFilter(DarkerSettings darkerSettings) {
        layoutParams.screenBrightness = darkerSettings.getBrightness();
        layoutParams.alpha = darkerSettings.getAlpha();
        if (darkerSettings.isUseColor())
            linearLayout.setBackgroundColor(darkerSettings.getColor());
        else
            linearLayout.setBackgroundColor(Color.BLACK);
        if (darkerSettings.isKeepScreenOn())
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        else
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowManager.updateViewLayout(linearLayout, layoutParams);
    }

    public static void removeScreenFilter() {
        layoutParams.screenBrightness = DarkerSettings.BRIGHTNESS_AUTO;
        layoutParams.alpha = DarkerSettings.ALPHA_MINIMUM;
        windowManager.updateViewLayout(linearLayout, layoutParams);
    }
}
