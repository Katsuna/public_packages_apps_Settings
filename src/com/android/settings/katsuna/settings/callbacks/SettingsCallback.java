package com.android.settings.katsuna.settings.callbacks;

import android.content.Intent;

import com.android.settings.katsuna.settings.BaseSetting;
import com.katsuna.commons.entities.UserProfile;

public interface SettingsCallback {
    boolean isReading();

    UserProfile getUserProfile();

    void focusOnSetting(BaseSetting setting);

    void focusOnBaseSetting(BaseSetting setting);

    void loadProfile();

    boolean getSettingFocused();

    void setSettingFocused(boolean flag);

    void startActivity(Intent i);
}
