package com.android.settings.katsuna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.android.settings.R;
import com.android.settingslib.wifi.WifiStatusTracker;

/**
 * Created by alkis on 24/9/2016.
 */

public abstract class WifiBroadcastReceiver extends BroadcastReceiver {

    private final Context mContext;
    private final WifiStatusTracker mWifiTracker;

    public WifiBroadcastReceiver(Context context) {
        mContext = context;
        WifiManager mWifiManager = mContext.getSystemService(WifiManager.class);
        mWifiTracker = new WifiStatusTracker(mWifiManager);
    }

    public void setListening(boolean listening) {
        if (listening) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            mContext.registerReceiver(this, filter);
        } else {
            mContext.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mWifiTracker.handleBroadcast(intent);
        onBroadcastReceived();
    }

    public abstract void onBroadcastReceived();

    public String getSummary() {
        if (!mWifiTracker.enabled) {
            return mContext.getString(R.string.wifi_disabled_generic);
        }
        if (!mWifiTracker.connected) {
            return mContext.getString(R.string.disconnected);
        }
        return mWifiTracker.ssid;
    }

    public boolean isConnected() {
        return (mWifiTracker.enabled && mWifiTracker.connected);
    }

}
