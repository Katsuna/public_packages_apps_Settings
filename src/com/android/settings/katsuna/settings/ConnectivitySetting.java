package com.android.settings.katsuna.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.settings.R;
import com.android.settings.katsuna.utils.SettingsController;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.BackgroundGenerator;
import com.katsuna.commons.utils.ColorAdjusterV2;
import com.katsuna.commons.utils.SizeAdjuster;
import com.katsuna.commons.utils.ToggleButtonAdjuster;

public class ConnectivitySetting extends BaseSetting {

    private View mConnectivityInitialContainer;
    private ToggleButton mWifiToggle;
    private ToggleButton mCellularToggle;
    private ToggleButton mBluetoothToggle;
    private SettingsController mSettingsController;
    private boolean mManualWifiChange;
    private boolean mManualBluetoothChange;

    public ConnectivitySetting(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();

        setupWifiListener();
        setupBluetoothListener();
    }

    @Override
    protected void init() {
        super.init();
        mTopContainer = findViewById(R.id.connectivity_setting);
        mMoreButton = findViewById(R.id.connectivity_more);
        mMoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                getContext().startActivity(i);
            }
        });

        mSettingsController = new SettingsController(getContext());

        mExpandedContainer = findViewById(R.id.connectivity_expanded_container);

        mConnectivityInitialContainer = findViewById(R.id.connectivity_initial_container);
        mConnectivityInitialContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        mWifiToggle = findViewById(R.id.wifi_toggle);
        mCellularToggle = findViewById(R.id.cellular_toggle);
        mBluetoothToggle = findViewById(R.id.bluetooth_toggle);

        setupWifiControl();
        readWifi();

        setupCellularControl();
        readCellularData();

        setupBluetoothControl();
        readBluetooth();
    }

    public void loadProfile(UserProfile profile) {
        // change hand layouts if necessary
        if (rightHandLayout != profile.isRightHanded) {
            int layoutId = profile.isRightHanded ? R.layout.katsuna_connectivity_rh :
                    R.layout.katsuna_connectivity_lh;

            ViewGroup parent = findViewById(R.id.connectivity_setting);
            replaceFirstViewInParent(parent, layoutId);

            init();
        }

        rightHandLayout = profile.isRightHanded;



        SizeAdjuster.applySizeProfile(getContext(), this, profile);
        ColorAdjusterV2.applyColorProfile(getContext(), this, profile);

        Drawable toggleBg = BackgroundGenerator.createToggleBg(getContext(), profile);
        ToggleButtonAdjuster.adjustToggleButton(getContext(), mWifiToggle, toggleBg, profile);
        ToggleButtonAdjuster.adjustToggleButton(getContext(), mCellularToggle, toggleBg, profile);
        ToggleButtonAdjuster.adjustToggleButton(getContext(), mBluetoothToggle, toggleBg, profile);

        adjustProfileBase(profile);
    }

    private void setupWifiControl() {
        mWifiToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mManualWifiChange = true;
                        mSettingsController.setWifiEnabled(isChecked);
                    }
                }
        );
    }

    private void setupCellularControl() {
        mCellularToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setMobileDataEnabled(isChecked);
                    }
                });
    }

    private void setupBluetoothControl() {
        mBluetoothToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mManualBluetoothChange = true;
                mSettingsController.setBluetoothEnabled(isChecked);
            }
        });
    }

    private void setMobileDataEnabled(boolean enabled) {
        mSettingsController.setDataEnabled(SettingsController.SINGLE_SIM_DEFAULT_SUB_ID, enabled);
    }

    private void readWifi() {
        mWifiToggle.setChecked(mSettingsController.isWifiEnabled());
    }

    private void readCellularData() {
        mCellularToggle.setChecked(mSettingsController.isDataEnabled(
                SettingsController.SINGLE_SIM_DEFAULT_SUB_ID));
    }

    private void readBluetooth() {
        mBluetoothToggle.setChecked(mSettingsController.isBluetoothEnabled());
    }

    public void loadValues() {
        readWifi();
        readCellularData();
        readBluetooth();
    }

    private void setupWifiListener() {
        mWifiReceiver = new WifiBroadcastReceiver(getContext().getApplicationContext()) {
            @Override
            public void onBroadcastReceived() {
                if (mManualWifiChange) {
                    mManualWifiChange = false;
                    return;
                }

                readWifi();
            }
        };
    }

    private void setupBluetoothListener() {

    }

    public void setListening(boolean enabled) {

    }

}
