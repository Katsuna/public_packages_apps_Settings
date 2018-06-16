package com.android.settings.katsuna.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.settings.R;
import com.katsuna.commons.entities.Notification;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.BackgroundGenerator;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.ToggleButtonAdjuster;

public class NotificationSetting extends BaseSetting {

    private View mNotificationInitialContainer;
    private ToggleButton mNotificationToggle;

    public NotificationSetting(Context context, @Nullable AttributeSet attrs) {
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
        mTopContainer = findViewById(R.id.notification_setting);
        mNotificationToggle = findViewById(R.id.toggle_notification);
        mNotificationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do nothing because we are just reading values and this is not a use
                // interaction.
                if (mSettingsCallback.isReading()) return;

                Preference preference = new Preference();
                preference.setKey(PreferenceKey.NOTIFICATION);
                if (isChecked) {
                    preference.setValue(Notification.ON.name());
                } else {
                    preference.setValue(Notification.OFF.name());
                }
                PreferenceUtils.updatePreference(getContext(), preference);
            }
        });

        mExpandedContainer = findViewById(R.id.notification_expanded_container);

        mNotificationInitialContainer = findViewById(R.id.notification_initial_container);
        mNotificationInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });
    }

    public void enable(boolean enabled) {
        mNotificationToggle.setChecked(enabled);
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_notification_rh :
                    R.layout.katsuna_notification_lh;

            ViewGroup parent = findViewById(R.id.notification_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        ColorAdjusterV2.applyColorProfile(getContext(), this, profile);

        Drawable toggleBg = BackgroundGenerator.createToggleBg(getContext(), profile);
        ToggleButtonAdjuster.adjustToggleButton(getContext(), mNotificationToggle, toggleBg,
                profile);
        adjustProfileBase(profile);
    }
}
