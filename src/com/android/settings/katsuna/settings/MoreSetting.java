package com.android.settings.katsuna.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.SizeAdjuster;

public class MoreSetting extends BaseSetting {

    public MoreSetting(Context context, @Nullable AttributeSet attrs) {
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


        View advancedInitialContainer = findViewById(R.id.advanced_initial_container);
        advancedInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.Settings.ACTION_SETTINGS);
                mSettingsCallback.startActivity(i);
            }
        });
    }


    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_advanced_rh :
                    R.layout.katsuna_advanced_lh;

            ViewGroup parent = findViewById(R.id.more_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        adjustProfileBase(profile);
    }
}
