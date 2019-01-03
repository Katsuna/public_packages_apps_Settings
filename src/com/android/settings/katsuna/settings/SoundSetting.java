package com.android.settings.katsuna.settings;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.android.settings.R;
import com.android.settings.katsuna.SoundBroadcastReceiver;
import com.android.settings.katsuna.utils.SettingsController;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.SeekBarUtils;
import com.katsuna.commons.utils.SizeAdjuster;


public class SoundSetting extends BaseSetting {

    private View mSoundInitialContainer;
    private View mMediaVolumeSilent;
    private View mMediaVolumeLoud;
    private View mAlarmVolumeSilent;
    private View mAlarmVolumeLoud;
    private View mRingVolumeSilent;
    private View mRingVolumeLoud;
    private SeekBar mRingVolumeSeekBar;
    private SettingsController mSettingsController;
    private SeekBar mAlarmVolumeSeekBar;
    private SeekBar mMediaVolumeSeekBar;
    private SoundBroadcastReceiver mSoundReceiver;

    public SoundSetting(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init();
        setupListener();
    }

    @Override
    protected void init() {
        super.init();
        mTopContainer = findViewById(R.id.sound_setting);
        mMoreButton = findViewById(R.id.sound_more);
        mMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.ACTION_SOUND_SETTINGS);
                getContext().startActivity(i);
            }
        });

        mExpandedContainer = findViewById(R.id.sound_expanded_container);

        mSoundInitialContainer = findViewById(R.id.sound_initial_container);
        mSoundInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        mSettingsController = SettingsController.get();

        mMediaVolumeSeekBar = findViewById(R.id.media_volume_seekbar);
        mAlarmVolumeSeekBar = findViewById(R.id.alarm_volume_seekbar);
        mRingVolumeSeekBar = findViewById(R.id.ring_volume_seekbar);

        mMediaVolumeSilent = findViewById(R.id.media_volume_silent);
        mMediaVolumeLoud = findViewById(R.id.media_volume_loud);
        mAlarmVolumeSilent = findViewById(R.id.alarm_volume_silent);
        mAlarmVolumeLoud = findViewById(R.id.alarm_volume_loud);
        mRingVolumeSilent = findViewById(R.id.ring_volume_silent);
        mRingVolumeLoud = findViewById(R.id.ring_volume_loud);

        setupVolumeControl();
        readVolumes();
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_sound_rh :
                    R.layout.katsuna_sound_lh;

            ViewGroup parent = findViewById(R.id.sound_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;

        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        ColorAdjusterV2.applyColorProfile(getContext(), this, profile);

        SeekBarUtils.adjustSeekbar(getContext(), mMediaVolumeSeekBar, profile,
                R.drawable.ic_volume_up_28dp);
        SeekBarUtils.adjustSeekbar(getContext(), mAlarmVolumeSeekBar, profile,
                R.drawable.ic_volume_up_28dp);
        SeekBarUtils.adjustSeekbar(getContext(), mRingVolumeSeekBar, profile,
                R.drawable.ic_volume_up_28dp);

        adjustProfileBase(profile);
    }

    private void setupVolumeControl() {
        // media volume
        mMediaVolumeSeekBar.setMax(mSettingsController.getMaxVolume(AudioManager.STREAM_MUSIC));
        mMediaVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSettingsController.setVolume(AudioManager.STREAM_MUSIC, progress);
            }
        });

        mMediaVolumeSilent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mMediaVolumeSeekBar.getProgress() - 1;
                mMediaVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_MUSIC, newProgress);
            }
        });

        mMediaVolumeLoud.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mMediaVolumeSeekBar.getProgress() + 1;
                mMediaVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_MUSIC, newProgress);
            }
        });

        // alarm volume
        mAlarmVolumeSeekBar.setMax(mSettingsController.getMaxVolume(AudioManager.STREAM_ALARM));
        mAlarmVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSettingsController.setVolume(AudioManager.STREAM_ALARM, progress);
            }
        });

        mAlarmVolumeSilent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mAlarmVolumeSeekBar.getProgress() - 1;
                mAlarmVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_ALARM, newProgress);
            }
        });

        mAlarmVolumeLoud.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mAlarmVolumeSeekBar.getProgress() + 1;
                mAlarmVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_ALARM, newProgress);
            }
        });

        // ring volume
        mRingVolumeSeekBar.setMax(mSettingsController.getMaxVolume(AudioManager.STREAM_RING));
        mRingVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSettingsController.setVolume(AudioManager.STREAM_RING, progress);
            }
        });

        mRingVolumeSilent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mRingVolumeSeekBar.getProgress() - 1;
                mRingVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_RING, newProgress);
            }
        });

        mRingVolumeLoud.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int newProgress = mRingVolumeSeekBar.getProgress() + 1;
                mRingVolumeSeekBar.setProgress(newProgress, true);
                mSettingsController.setVolume(AudioManager.STREAM_RING, newProgress);
            }
        });
    }

    private void setupListener() {
        mSoundReceiver = new SoundBroadcastReceiver(getContext().getApplicationContext()) {
            @Override
            public void onBroadcastReceived() {
                readVolumes();
            }
        };
    }

    public void readVolumes() {
        mMediaVolumeSeekBar.setProgress(readVolume(AudioManager.STREAM_MUSIC));
        mAlarmVolumeSeekBar.setProgress(readVolume(AudioManager.STREAM_ALARM));
        mRingVolumeSeekBar.setProgress(readVolume(AudioManager.STREAM_RING));
    }

    private int readVolume(int streamId) {
        return mSettingsController.getVolume(streamId);
    }

    public void setListening(boolean enabled) {
        mSoundReceiver.setListening(enabled);
    }
}
