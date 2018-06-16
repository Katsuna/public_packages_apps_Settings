package com.android.settings.katsuna.settings;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.settings.R;
import com.katsuna.commons.entities.OpticalParams;
import com.katsuna.commons.entities.Preference;
import com.katsuna.commons.entities.PreferenceKey;
import com.katsuna.commons.entities.SizeProfileKey;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjuster;
import com.katsuna.commons.utils.PreferenceUtils;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.SizeCalc;

public class HandSetting extends BaseSetting {

    private View mHandInitialContainer;
    private RadioGroup mHandExpandedContainer;
    private RadioButton mRadioRightHand;
    private RadioButton mRadioLeftHand;

    public HandSetting(Context context, @Nullable AttributeSet attrs) {
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
        mTopContainer = findViewById(R.id.hand_setting);
        mHandExpandedContainer = findViewById(R.id.hand_expanded_container);
        mHandExpandedContainer.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        // do nothing because we are just reading values and this is not a use
                        // interaction.
                        if (mSettingsCallback.isReading()) return;

                        Preference preference = new Preference();
                        preference.setKey(PreferenceKey.RIGHT_HAND);
                        if (checkedId == R.id.radio_right_hand) {
                            preference.setValue(String.valueOf(true));
                        } else {
                            preference.setValue(String.valueOf(false));
                        }
                        PreferenceUtils.updatePreference(getContext(), preference);

                        mSettingsCallback.loadProfile();
                        expandContainer();
                        readjustControls();
                    }
                }
        );
        mExpandedContainer = mHandExpandedContainer;
        mRadioRightHand = findViewById(R.id.radio_right_hand);
        mRadioLeftHand = findViewById(R.id.radio_left_hand);

        mHandInitialContainer = findViewById(R.id.hand_initial_container);
        mHandInitialContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_hand_rh :
                    R.layout.katsuna_hand_lh;

            ViewGroup parent = findViewById(R.id.hand_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        if (profile.isRightHanded) {
            mRadioRightHand.setChecked(true);
        } else {
            mRadioLeftHand.setChecked(true);
        }

        SizeAdjuster.applySizeProfile(getContext(), this, profile);

        OpticalParams body2Params = SizeCalc.getOpticalParams(SizeProfileKey.BODY_2,
                profile.opticalSizeProfile);
        SizeAdjuster.adjustText(getContext(), mRadioLeftHand, body2Params);
        SizeAdjuster.adjustText(getContext(), mRadioRightHand, body2Params);

        ColorAdjuster.adjustRadioButton(getContext(), profile, mRadioLeftHand);
        ColorAdjuster.adjustRadioButton(getContext(), profile, mRadioRightHand);
        adjustProfileBase(profile);
    }
}
