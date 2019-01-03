package com.android.settings;

import android.app.Application;
import android.util.Log;

public class SettingsApp extends Application {

    private static final String TAG = SettingsApp.class.getSimpleName();

    private static SettingsApp instance;

    public static SettingsApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        instance = this;
    }
}
