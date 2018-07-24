package com.android.settings.katsuna;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.settings.R;
import com.android.settings.katsuna.settings.AgeSetting;
import com.android.settings.katsuna.settings.BaseSetting;
import com.android.settings.katsuna.settings.BrightnessSetting;
import com.android.settings.katsuna.settings.ColorSetting;
import com.android.settings.katsuna.settings.ConnectivitySetting;
import com.android.settings.katsuna.settings.GenderSetting;
import com.android.settings.katsuna.settings.HandSetting;
import com.android.settings.katsuna.settings.LocationSetting;
import com.android.settings.katsuna.settings.MoreSetting;
import com.android.settings.katsuna.settings.SizeSetting;
import com.android.settings.katsuna.settings.SoundSetting;
import com.android.settings.katsuna.settings.UpdateSetting;
import com.android.settings.katsuna.settings.callbacks.SettingsCallback;
import com.katsuna.commons.entities.Notification;
import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.SizeProfileKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ProfileReader;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalc;
import com.katsuna.commons.utils.ViewUtils;

public class KatsunaSettings extends Activity implements SettingsCallback {

    private static final String TAG = "Settings";

    private BrightnessSetting mBrightnessSetting;

    private AgeSetting mAgeSetting;
    private GenderSetting mGenderSetting;
    private HandSetting mHandSetting;
    private SizeSetting mSizeSetting;
    private ColorSetting mColorSetting;

    private boolean mIsReading;
    private UserProfile mProfile;
    private TextView mQuickSettingsText;
    private TextView mUsabilitySettingsText;
    private SoundSetting mSoundSetting;
    private ConnectivitySetting mConnectivitySetting;
    private LocationSetting mLocationSetting;
    private UpdateSetting mUpdateSetting;
    private MoreSetting mMoreSetting;
    private ScrollView mScrollViewContainer;
    private View mQuickAccessCardHandler;
    private View mUsabilityCardHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.katsuna_settings_activity);

        mScrollViewContainer = findViewById(R.id.scroll_view_container);

        mQuickSettingsText = findViewById(R.id.text_quick_access_settings);
        mUsabilitySettingsText = findViewById(R.id.text_usability_settings);

        mBrightnessSetting = findViewById(R.id.brightness_setting);
        mBrightnessSetting.setSettingsCallback(this);
        mSoundSetting = findViewById(R.id.sound_setting);
        mSoundSetting.setSettingsCallback(this);
        mConnectivitySetting = findViewById(R.id.connectivity_setting);
        mConnectivitySetting.setSettingsCallback(this);
        mLocationSetting = findViewById(R.id.location_setting);
        mLocationSetting.setSettingsCallback(this);
        mUpdateSetting = findViewById(R.id.update_setting);
        mUpdateSetting.setSettingsCallback(this);

        mMoreSetting = findViewById(R.id.more_setting);
        mMoreSetting.setSettingsCallback(this);

        mAgeSetting = findViewById(R.id.age_setting);
        mAgeSetting.setSettingsCallback(this);

        mGenderSetting = findViewById(R.id.gender_setting);
        mGenderSetting.setSettingsCallback(this);

        mHandSetting = findViewById(R.id.hand_setting);
        mHandSetting.setSettingsCallback(this);

        mSizeSetting = findViewById(R.id.size_setting);
        mSizeSetting.setSettingsCallback(this);

        mColorSetting = findViewById(R.id.color_setting);
        mColorSetting.setSettingsCallback(this);

        mQuickAccessCardHandler = findViewById(R.id.quick_access_card_handler);
        mUsabilityCardHandler = findViewById(R.id.usability_card_handler);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadProfile();
        loadValues();
        setListening(true);
    }

    private void loadValues() {
        mBrightnessSetting.readBrightness();
        mSoundSetting.readVolumes();
        mConnectivitySetting.loadValues();
        mLocationSetting.readGpsControl();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgeSetting.updateAge();
        mGenderSetting.updateOtherGender();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setListening(false);
    }

    private void setListening(boolean enabled) {
        mSoundSetting.setListening(enabled);
        mConnectivitySetting.setListening(enabled);
        mBrightnessSetting.setListening(enabled);
    }

    @Override
    public boolean isReading() {
        return mIsReading;
    }

    @Override
    public UserProfile getUserProfile() {
        return mProfile;
    }

    @Override
    public void focusOnSetting(final BaseSetting setting) {
        hideExpanded();
        setting.show();
        focusOnBaseSetting(setting);
        setSettingFocused(true);
    }

    @Override
    public void focusOnBaseSetting(final BaseSetting setting) {
        final View v = setting.getExpandedContainer();
        v.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.verticalScrollToView(KatsunaSettings.this, mScrollViewContainer, v);
            }
        });
    }

    private void hideExpanded() {
        mBrightnessSetting.hide();
        mSoundSetting.hide();
        mConnectivitySetting.hide();
        mLocationSetting.hide();
        mAgeSetting.hide();
        mGenderSetting.hide();
        mHandSetting.hide();
        mSizeSetting.hide();
        mColorSetting.hide();
    }

    @Override
    public void loadProfile() {
        mProfile = ProfileReader.getUserProfileFromKatsunaServices(this);
        Log.d(TAG, mProfile.toString());

        mIsReading = true;

        // adjust hand profile
        if (mProfile.isRightHanded) {
            alignParentStart(mQuickAccessCardHandler);
            alignParentStart(mUsabilityCardHandler);
            alignTextViewInLinearLayout(mQuickSettingsText, Gravity.START);
            alignTextViewInLinearLayout(mUsabilitySettingsText, Gravity.START);
        } else {
            alignParentEnd(mQuickAccessCardHandler);
            alignParentEnd(mUsabilityCardHandler);
            alignTextViewInLinearLayout(mQuickSettingsText, Gravity.END);
            alignTextViewInLinearLayout(mUsabilitySettingsText, Gravity.END);
        }

        mBrightnessSetting.loadProfile(mProfile);
        mSoundSetting.loadProfile(mProfile);
        mConnectivitySetting.loadProfile(mProfile);
        mLocationSetting.loadProfile(mProfile);
        mUpdateSetting.loadProfile(mProfile);

        mMoreSetting.loadProfile(mProfile);

        // load age
        mAgeSetting.setAge(mProfile.age);
        mAgeSetting.loadProfile(mProfile);

        // load gender
        mGenderSetting.loadProfile(mProfile);

        // load hand settings
        mHandSetting.loadProfile(mProfile);

        // load optical size
        mSizeSetting.loadProfile(mProfile);

        // load color profile
        mColorSetting.loadProfile(mProfile);

        adjustProfile(mProfile);

        mIsReading = false;
    }

    private void alignTextViewInLinearLayout(View view, int gravity) {
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
        mQuickSettingsText.setLayoutParams(params);
    }

    private void alignParentStart(View view) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
    }

    private void alignParentEnd(View view) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
    }

    private boolean mSettingFocused = false;

    @Override
    public boolean getSettingFocused() {
        return mSettingFocused;
    }

    @Override
    public void setSettingFocused(boolean flag) {
        mSettingFocused = flag;
        readjustSettingsIcons();
    }

    private void readjustSettingsIcons() {
        mBrightnessSetting.adjustSettingIcon();
        mSoundSetting.adjustSettingIcon();
        mConnectivitySetting.adjustSettingIcon();
        mLocationSetting.adjustSettingIcon();
        mUpdateSetting.adjustSettingIcon();
        mMoreSetting.adjustSettingIcon();
        mAgeSetting.adjustSettingIcon();
        mGenderSetting.adjustSettingIcon();
        mHandSetting.adjustSettingIcon();
        mSizeSetting.adjustSettingIcon();
        mColorSetting.adjustSettingIcon();
    }

    private void adjustProfile(UserProfile profile) {
        OpticalParams body2Params = SizeCalc.getOpticalParams(SizeProfileKey.BODY_2, profile.opticalSizeProfile);
        SizeAdjuster.adjustText(this, mQuickSettingsText, body2Params);
        SizeAdjuster.adjustText(this, mUsabilitySettingsText, body2Params);
    }

}
