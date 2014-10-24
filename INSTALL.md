Developer dependencies
----------------------

* Gradle 1.10
* Android 4.4 SDK
* (Optional) Docker

Gradle
------
On Ubuntu, Gradle can be installed through the PPA: `ppa:cwchien/gradle`.

```bash
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle-1.10
```

Android SDK
-----------
Make sure you have the Android SDK from Google. You do not need the ADT Bundle.

<https://developer.android.com/sdk/index.html>

```bash
wget http://dl.google.com/android/android-sdk_r23.0.2-linux.tgz
tar -zxvf android-sdk_r23.0.2-linux.tgz
```

Once you ensured the Android SDK is installed, more packages need to be
installed using the SDK Manager.
In the Android SDK Manager (located at `tools/android`), install the following:

* Android SDK Build-tools 19.0.1
* Android Support Repository

At the root of this project, add a `local.properties` file.
This will contain the path of the Android SDK:

    sdk.dir=/path/to/android-sdk-linux

Building at last
----------------
Building will be done through Gradle.

