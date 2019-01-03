package com.android.settings.katsuna.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.settings.SettingsApp;

public class SettingsController implements ISettingsController {

    public static final int SINGLE_SIM_DEFAULT_SUB_ID = 1;
    private static final String TAG = SettingsController.class.getSimpleName();
    private static final String MODE_CHANGING_ACTION = "com.android.settings.location.MODE_CHANGING";
    private static final String NEW_MODE_KEY = "NEW_MODE";
    private static SettingsController instance;
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private ContentResolver mContentResolver;
    private AudioManager mAudioManager;
    private WifiManager mWifiManager;
    private LocationManager mLocationManager;
    private TelephonyManager mTelephonyManager;

    private SettingsController() {
        mContext = SettingsApp.get();
        mContentResolver = mContext.getContentResolver();

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static SettingsController get() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized SettingsController getSync() {
        if (instance == null) instance = new SettingsController();
        return instance;
    }

    @Override
    public int getBrightness() {
        int output = 0;

        try {
            Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            output = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public void setBrightness(int value) {
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, value);
    }

    @Override
    public int getVolume(int stream) {
        return mAudioManager.getStreamVolume(stream);
    }

    @Override
    public void setVolume(int stream, int value) {
        mAudioManager.setStreamVolume(stream, value, 0);
    }

    @Override
    public int getMaxVolume(int stream) {
        return mAudioManager.getStreamMaxVolume(stream);
    }

    @Override
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    @Override
    public void setWifiEnabled(boolean enabled) {
        try {
            mWifiManager.setWifiEnabled(enabled);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    @Override
    public boolean isDataEnabled(int subId) {
        return mTelephonyManager.getDataEnabled(subId);
    }

    @Override
    public void setDataEnabled(int subId, boolean enabled) {
        mTelephonyManager.setDataEnabled(subId, enabled);
    }

    @Override
    public boolean isBluetoothEnabled() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    @Override
    public void setBluetoothEnabled(boolean enabled) {
        if (mBluetoothAdapter != null) {
            if (enabled) {
                mBluetoothAdapter.enable();
            } else {
                mBluetoothAdapter.disable();
            }
        }
    }

    @Override
    public boolean isGpsEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void setGpsEnabled(int mode) {
        Intent intent = new Intent(MODE_CHANGING_ACTION);
        intent.putExtra(NEW_MODE_KEY, mode);
        mContext.sendBroadcast(intent, android.Manifest.permission.WRITE_SECURE_SETTINGS);
        Settings.Secure.putInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);
    }

}
