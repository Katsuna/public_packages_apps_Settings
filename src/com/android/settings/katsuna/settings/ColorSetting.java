package com.android.settings.katsuna.settings;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.katsuna.commons.controls.DemoProfileV2;
import com.katsuna.commons.entities.ColorProfile;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.SizeProfile;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.SizeAdjuster;

public class ColorSetting extends BaseSetting {

    private View mColorInitialContainer;
    private DemoProfileV2 mDemoProfileImpairement;
    private DemoProfileV2 mDemoProfileMain;
    private DemoProfileV2 mDemoProfileContrastImpairement;
    private DemoProfileV2 mDemoProfileContrast;

    public ColorSetting(Context context, @Nullable AttributeSet attrs) {
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
        mTopContainer = findViewById(R.id.color_setting);
        mExpandedContainer = findViewById(R.id.color_expanded_container);

        mColorInitialContainer = findViewById(R.id.color_initial_container);
        mColorInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        setupDemoProfiles();
        initDemoColorProfiles();
    }

    private void initDemoColorProfiles() {
        mDemoProfileImpairement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColorProfile(ColorProfile.COLOR_IMPAIREMENT);
                updateColorProfile(ColorProfile.COLOR_IMPAIREMENT);
            }
        });
        mDemoProfileMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColorProfile(ColorProfile.MAIN);
                updateColorProfile(ColorProfile.MAIN);
            }
        });
        mDemoProfileContrastImpairement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColorProfile(ColorProfile.COLOR_IMPAIRMENT_AND_CONTRAST);
                updateColorProfile(ColorProfile.COLOR_IMPAIRMENT_AND_CONTRAST);
            }
        });
        mDemoProfileContrast.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColorProfile(ColorProfile.CONTRAST);
                updateColorProfile(ColorProfile.CONTRAST);
            }
        });
    }

    private void setupDemoProfiles() {
        mDemoProfileImpairement = findViewById(com.katsuna.commons.R.id.profile_impairement_v2);
        mDemoProfileMain = findViewById(com.katsuna.commons.R.id.profile_main_v2);
        mDemoProfileContrastImpairement = findViewById(com.katsuna.commons.R.id.profile_contrast_impairement_v2);
        mDemoProfileContrast = findViewById(com.katsuna.commons.R.id.profile_contrast_v2);

        UserProfile demoProfile = new UserProfile();
        demoProfile.opticalSizeProfile = SizeProfile.INTERMEDIATE;

        demoProfile.colorProfile = ColorProfile.COLOR_IMPAIREMENT;
        mDemoProfileImpairement.adjustProfile(demoProfile);

        demoProfile.colorProfile = ColorProfile.MAIN;
        mDemoProfileMain.adjustProfile(demoProfile);

        demoProfile.colorProfile = ColorProfile.COLOR_IMPAIRMENT_AND_CONTRAST;
        mDemoProfileContrastImpairement .adjustProfile(demoProfile);

        demoProfile.colorProfile = ColorProfile.CONTRAST;
        mDemoProfileContrast .adjustProfile(demoProfile);
    }


    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_color_rh :
                    R.layout.katsuna_color_lh;

            ViewGroup parent = findViewById(R.id.color_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;


        selectColorProfile(profile.colorProfile);

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        adjustProfileBase(profile);
    }

    private void selectColorProfile(ColorProfile colorProfile) {
        mDemoProfileMain.select(false);
        mDemoProfileContrast.select(false);
        mDemoProfileImpairement.select(false);
        mDemoProfileContrastImpairement.select(false);

        switch (colorProfile) {
            case MAIN:
                mDemoProfileMain.select(true);
                break;
            case CONTRAST:
                mDemoProfileContrast.select(true);
                break;
            case COLOR_IMPAIREMENT:
                mDemoProfileImpairement.select(true);
                break;
            case COLOR_IMPAIRMENT_AND_CONTRAST:
                mDemoProfileContrastImpairement.select(true);
                break;
        }
    }

    private void updateColorProfile(ColorProfile colorProfile) {
        Preference preference = new Preference();
        preference.setKey(PreferenceKey.COLOR_PROFILE);
        preference.setValue(colorProfile.name());
        PreferenceUtils.updatePreference(getContext(), preference);

        mSettingsCallback.loadProfile();
    }
}
