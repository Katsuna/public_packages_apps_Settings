package com.android.settings.katsuna;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * Created by alkis on 24/9/2016.
 */

public abstract class SoundBroadcastReceiver extends BroadcastReceiver {

    private final Context mContext;

    public SoundBroadcastReceiver(Context context) {
        mContext = context;
    }

    public void setListening(boolean listening) {
        if (listening) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(AudioManager.VOLUME_CHANGED_ACTION);
            filter.addAction(AudioManager.STREAM_DEVICES_CHANGED_ACTION);
            filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
            filter.addAction(AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION);
            filter.addAction(AudioManager.STREAM_MUTE_CHANGED_ACTION);
            filter.addAction(NotificationManager.ACTION_EFFECTS_SUPPRESSOR_CHANGED);
            mContext.registerReceiver(this, filter);
        } else {
            mContext.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onBroadcastReceived();
    }

    public abstract void onBroadcastReceived();

}
