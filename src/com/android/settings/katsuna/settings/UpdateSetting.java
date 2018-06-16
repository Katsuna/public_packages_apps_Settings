package com.android.settings.katsuna.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.SizeAdjuster;

public class UpdateSetting extends BaseSetting {

    private View mUpdateInitialContainer;

    public UpdateSetting(Context context, @Nullable AttributeSet attrs) {
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
        mUpdateInitialContainer = findViewById(R.id.update_initial_container);
        mUpdateInitialContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName cp = new ComponentName("com.katsuna.updater",
                        "com.katsuna.updater.UpdatesSettings");
                Intent i = new Intent();
                i.setComponent(cp);
                getContext().startActivity(i);
            }
        });
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_update_rh :
                    R.layout.katsuna_update_lh;

            ViewGroup parent = findViewById(R.id.update_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        adjustProfileBase(profile);
    }
}
