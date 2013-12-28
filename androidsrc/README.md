Build and Run
=========
Android app
-----------
From the main directory (for the release build use release instead of debug):
```
cd androidsrc
ant debug
```
If a virtual device is not running:
```
$(ANDROIDSDK)/sdk/tools/android avd
```
In the Virtual Devices view, select an AVD and click Start.
```
$(ANDROIDSDK)/sdk/platform-tools/adb <path_to_your_bin>.apk
$(ANDROIDSDK)/sdk/platform-tools/adb install bin/huxley-debug-unaligned.apk
```
Or if there's more than one emulator running (see http://developer.android.com/tools/building/building-cmdline.html)
```
$(ANDROIDSDK)/sdk/platform-tools/adb -s emulator-XYWZ install bin/huxley-debug-unaligned.apk
```

Android test
------------
From the main directory (for the release build use release instead of debug):
```
cd androidsrc/tests
```
If a virtual device is not running:
```
$(ANDROIDSDK)/sdk/tools/android avd
```
```
ant test
```


Huxley
=========

First proptotype. A use case:
- Blah
