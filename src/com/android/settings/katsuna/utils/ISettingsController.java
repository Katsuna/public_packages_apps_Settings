package com.android.settings.katsuna.utils;

public interface ISettingsController {
    int getBrightness();

    void setBrightness(int value);

    int getVolume(int stream);

    void setVolume(int stream, int value);

    int getMaxVolume(int stream);

    boolean isWifiEnabled();

    void setWifiEnabled(boolean enabled);

    boolean isDataEnabled(int subId);

    void setDataEnabled(int subId, boolean enabled);

    boolean isBluetoothEnabled();

    void setBluetoothEnabled(boolean enabled);

    boolean isGpsEnabled();

    void setGpsEnabled(int mode);

}
