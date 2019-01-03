package com.android.settings.katsuna.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.settings.R;
import com.android.settings.katsuna.utils.SettingsController;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.BackgroundGenerator;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.ToggleButtonAdjuster;

public class LocationSetting extends BaseSetting {

    private View mLocationInitialContainer;
    private ToggleButton mGpsToggle;
    private SettingsController mSettingsController;

    public LocationSetting(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    protected void init() {
        super.init();
        mTopContainer = findViewById(R.id.location_setting);
        mMoreButton = findViewById(R.id.location_more);
        mMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(i);
            }
        });
        mExpandedContainer = findViewById(R.id.location_expanded_container);

        mLocationInitialContainer = findViewById(R.id.location_initial_container);
        mLocationInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        mGpsToggle = findViewById(R.id.location_toggle);
        mSettingsController = SettingsController.get();

        setupGpsControl();
        readGpsControl();
        setupListener();
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_location_rh :
                    R.layout.katsuna_location_lh;

            ViewGroup parent = findViewById(R.id.location_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        ColorAdjusterV2.applyColorProfile(getContext(), this, profile);

        Drawable toggleBg = BackgroundGenerator.createToggleBg(getContext(), profile);
        ToggleButtonAdjuster.adjustToggleButton(getContext(), mGpsToggle, toggleBg, profile);
        adjustProfileBase(profile);
    }

    private void setupGpsControl() {
        mGpsToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mSettingsController.setGpsEnabled(
                                    Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
                        } else {
                            mSettingsController.setGpsEnabled(
                                    Settings.Secure.LOCATION_MODE_OFF);
                        }
                    }
                }
        );
    }

    public void readGpsControl() {
        mGpsToggle.setChecked(mSettingsController.isGpsEnabled());
    }

    private void setupListener() {

    }

    public void setListening(boolean enabled) {

    }

}
