# Katsuna Settings

Katsuna Settings is rom app based on aosp Settings app.

The basic change made on top of aosp implementations was to replace the default starting activity with a Katsuna activity which provides functionalities to change rom settings like (volume, wifi, bluetooth etc.) and katsuna profile settings. At this interface a link was added to the default aosp settings activity to allow the configuration all the available rom settings.



## Technical Info

- KatsunaSettings activity
  - package com.android.settings.katsuna
  - This is the new starting activity for KatsunaSettings. It contains the following controls (all are included in the com.android.settings.katsuna.settings   package):
    - BrightnessSetting
    - SoundSetting
    - ConnectivitySetting  (for wifi, bluetooth and mobile data activation)
    - LocationSetting (for gps activation)
    - UpdateSetting  (to check for katsuna firmware updates)
    - MoreSetting (control to redirect to original aosp settings activity)
    - Katsuna user profile settings
      - AgeSetting (to set user's age)
      - HandSetting  (to change preferred hand)
      - SizeSetting (to change size profile)
      - ColorSettings (to change color profile)
  - All the layouts for the KatsunaSettings controls can be found at default layout folder (res/layout) and their name start with the prefix "katsuna_". There are two versions for each layout. One for right handed users and one for the left handed users.
- SettingsController
  - package com.android.settings.katsuna.utils
  - This is a utility class used to modify system settings. It uses the following android services:
    - AUDIO_SERVICE
    - WIFI_SERVICE
    - TELEPHONY_SERVICE
    - LOCATION_SERVICE
    - BluetoothAdapter



#### Dependencies

- This project (as any other Katsuna app) depends on KatsunaCommon project (dev branch) which is an android library module that contains common classes and resources for all katsuna projects.
- Android support libraries.
- KatsunaSettings requires KatsunaLauncher app because it contains the content provider that manages katsuna user profiles.



## License

This project is licensed under the Apache 2.0 License.
