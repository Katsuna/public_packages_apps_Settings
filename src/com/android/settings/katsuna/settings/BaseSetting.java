package com.android.settings.katsuna.settings;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.settings.R;
import com.android.settings.katsuna.settings.callbacks.SettingsCallback;
import com.katsuna.commons.entities.ColorProfileKeyV2;
import com.katsuna.commons.entities.UserProfile;
import com.katsuna.commons.utils.ColorCalcV2;
import com.katsuna.commons.utils.DrawUtils;

public class BaseSetting extends LinearLayout {

    View mTopContainer;
    View mExpandedContainer;
    Button mMoreButton;
    SettingsCallback mSettingsCallback;
    boolean rightHandLayout = true;
    private View mCardHandler;
    private TextView mSettingLabel;
    private boolean mExpanded;
    private int mColorTransparent;
    private int mColorPrimary2;
    private int mColorBlack54;
    private int mColorBlack87;
    private ImageView mSettingIcon;

    public BaseSetting(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mColorTransparent = ContextCompat.getColor(getContext(), R.color.common_transparent);
        mColorBlack54 = ContextCompat.getColor(getContext(), R.color.common_black54);
        mColorBlack87 = ContextCompat.getColor(getContext(), R.color.common_black87);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    protected void init() {
        mCardHandler = findViewById(R.id.card_handler);
        mSettingLabel = findViewById(R.id.setting_label);
        mSettingIcon = findViewById(R.id.setting_icon);
    }

    public void setSettingsCallback(SettingsCallback callback) {
        mSettingsCallback = callback;
    }

    public View getExpandedContainer() {
        return mExpandedContainer;
    }

    public void show() {
        mExpanded = true;
        mExpandedContainer.setVisibility(View.VISIBLE);

        int elevation = getContext().getResources()
                .getDimensionPixelSize(R.dimen.common_selection_elevation);
        ViewCompat.setElevation(mTopContainer, elevation);
    }

    public void hide() {
        mExpanded = false;
        mExpandedContainer.setVisibility(GONE);
        if (mMoreButton != null) {
            mMoreButton.setVisibility(INVISIBLE);
        }

        if (mTopContainer != null) {
            mTopContainer.setBackgroundColor(mColorTransparent);

            ViewCompat.setElevation(mTopContainer, 0);
        }
        readjustControls();
    }

    void expand() {
        mExpanded = !mExpanded;
        if (mExpanded) {
            expandContainer();
        } else {
            hideContainer();
        }
        readjustControls();
    }

    protected void expandContainer() {
        mSettingsCallback.focusOnSetting(BaseSetting.this);

        if (mTopContainer != null) {
            int color = ContextCompat.getColor(getContext(), R.color.common_white);
            mTopContainer.setBackgroundColor(color);
        }

        if (mMoreButton != null) {
            mMoreButton.setVisibility(VISIBLE);
        }
        mSettingsCallback.setSettingFocused(true);
    }

    protected void hideContainer() {
        hide();
        mSettingsCallback.setSettingFocused(false);
    }

    void replaceFirstViewInParent(ViewGroup parent, int layoutId) {
        // remove old one

        parent.removeViewAt(0);

        //add
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, parent, false);
        parent.addView(view, 0);
    }

    protected void readjustControls() {
        adjustCardHandler();
        adjustSettingLabel();
        adjustSettingIcon();
    }

    void adjustProfileBase(UserProfile profile) {
        mColorPrimary2 = ColorCalcV2.getColor(getContext(), ColorProfileKeyV2.PRIMARY_COLOR_2,
                profile.colorProfile);

        readjustControls();
    }

    private void adjustCardHandler() {
        if (mCardHandler != null) {
            int color = mExpanded ? mColorPrimary2 : mColorTransparent;
            mCardHandler.setBackgroundColor(color);
        }
    }

    private void adjustSettingLabel() {
        if (mSettingLabel != null) {
            mSettingLabel.setTextColor(mExpanded ? mColorBlack87 : mColorBlack54);
        }
    }

    public void adjustSettingIcon() {
        if (mSettingIcon != null) {
            if (mExpanded) {
                DrawUtils.setColor(mSettingIcon.getDrawable(), mColorPrimary2);
            } else {
                // we show icon in color primary 2 if no setting is focused
                if (mSettingsCallback != null && !mSettingsCallback.getSettingFocused()) {
                    DrawUtils.setColor(mSettingIcon.getDrawable(), mColorPrimary2);
                } else {
                    DrawUtils.setColor(mSettingIcon.getDrawable(), mColorBlack54);
                }
            }
        }
    }

}
