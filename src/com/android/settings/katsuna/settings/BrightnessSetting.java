package com.android.settings.katsuna.settings;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.android.settings.R;
import com.android.settings.katsuna.utils.SettingsController;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.SeekBarUtils;
import com.katsuna.commons.utils.SizeAdjuster;

public class BrightnessSetting extends BaseSetting {

    private View mBrightnessInitialContainer;

    private View mBrightnessLabelDark;
    private View mBrightnessLabelLight;
    private SeekBar mBrigthnessSeekBar;
    private SettingsController mSettingsController;
    private BrightnessObserver mBrightnessObserver;

    public BrightnessSetting(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init();
        setupBrightnessListener();
    }

    @Override
    protected void init() {
        super.init();
        mTopContainer = findViewById(R.id.brightness_setting);
        mMoreButton = findViewById(R.id.brightness_more);
        mMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openMore();
            }
        });

        mExpandedContainer = findViewById(R.id.brightness_expanded_container);

        mBrightnessInitialContainer = findViewById(R.id.brightness_initial_container);
        mBrightnessInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        mBrightnessLabelDark = findViewById(R.id.brightness_label_dark);
        mBrightnessLabelLight = findViewById(R.id.brightness_label_light);

        mSettingsController = new SettingsController(getContext());

        setupBrightnessControl();
        readBrightness();
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_brightness_rh :
                    R.layout.katsuna_brightness_lh;

            ViewGroup parent = findViewById(R.id.brightness_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);

        SeekBarUtils.adjustSeekbar(getContext(), mBrigthnessSeekBar, profile,
                R.drawable.ic_brightness_7_28dp);

        adjustProfileBase(profile);
    }

    private void setupBrightnessControl() {
        mBrigthnessSeekBar = findViewById(R.id.brightness_seek_bar);
        mBrigthnessSeekBar.setMax(255);
        mBrigthnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSettingsController.setBrightness(progress);
            }
        });

        mBrightnessLabelDark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mBrigthnessSeekBar.getProgress() - 25;
                if (newProgress < 0) {
                    newProgress = 0;
                }
                mBrigthnessSeekBar.setProgress(newProgress, true);
                mSettingsController.setBrightness(newProgress);
            }
        });

        mBrightnessLabelLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mBrigthnessSeekBar.getProgress() + 25;
                if (newProgress > 255) {
                    newProgress = 255;
                }
                mBrigthnessSeekBar.setProgress(newProgress, true);
                mSettingsController.setBrightness(newProgress);
            }
        });
    }

    public void readBrightness() {
        mBrigthnessSeekBar.setProgress(mSettingsController.getBrightness());
    }

    private void openMore() {
        Intent intent = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        getContext().startActivity(intent);
    }

    private void setupBrightnessListener() {
        mBrightnessObserver = new BrightnessObserver(new Handler(),
                getContext().getApplicationContext());
    }

    public void setListening(boolean enabled) {
        if (enabled) {
            mBrightnessObserver.startObserving();
        } else {
            mBrightnessObserver.stopObserving();
        }
    }

    private class BrightnessObserver extends ContentObserver {

        private Context mContext;

        BrightnessObserver(Handler handler, Context context) {
            super(handler);
            mContext = context;
        }

        void startObserving() {
            ContentResolver resolver = mContext.getContentResolver();
            // Listen to brightness and brightness mode
            resolver.registerContentObserver(Settings.System
                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, this);
            resolver.registerContentObserver(Settings.System
                    .getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), false, this);
        }

        void stopObserving() {
            mContext.getContentResolver().unregisterContentObserver(this);
        }

        @Override
        public void onChange(boolean selfChange) {
            readBrightness();
        }
    }

}
