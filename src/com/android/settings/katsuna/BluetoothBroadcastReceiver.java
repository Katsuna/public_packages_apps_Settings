package com.android.settings.katsuna;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.android.settings.bluetooth.Utils;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

/**
 * Created by alkis on 24/9/2016.
 */

public class BluetoothBroadcastReceiver {

    private final LocalBluetoothManager mBluetoothManager;
    private final BluetoothCallback mCallback;

    public BluetoothBroadcastReceiver(Context context, BluetoothCallback callback) {
        mCallback = callback;
        mBluetoothManager = Utils.getLocalBtManager(context);
    }

    public void setListening(boolean listening) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) return;
        if (listening) {
            mBluetoothManager.getEventManager().registerCallback(mCallback);
        } else {
            mBluetoothManager.getEventManager().unregisterCallback(mCallback);
        }
    }
}
