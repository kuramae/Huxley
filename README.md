Build and Run
=========
Android app
-----------
From the main directory (for the release build use release instead of debug):
```
cd androidsrc
ant debug
$(ANDROIDSDK)/sdk/tools/android avd
```
In the Virtual Devices view, select an AVD and click Start.
```
$(ANDROIDSDK)/sdk/platform-tools/adb <path_to_your_bin>.apk
$(ANDROIDSDK)/sdk/platform-tools/adb install bin/huxley-debug-unaligned.apk
```
Or if there's more than one emulator running
```
$(ANDROIDSDK)/sdk/platform-tools/adb -s emulator-XYWZ install bin/huxley-debug-unaligned.apk
```

Android test
------------


Huxley
=========

First proptotype. A use case:
- Blah
