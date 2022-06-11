# Smart Pot Android App

This repository contains the source code for the Android app for the Smart Pot.

## Requirements

To use the app, you need to be on the same WiFi network as the [Smart Pot Hub][smart-pot/hub] or connect to the network via VPN.
For the app to be able to resolve the address of the Hub, the hostname of the Raspberry Pi must have been set to `smart-pot-hub` as described in the [OS Configuration][smart-pot/hub/os-configuration] guide.

The app requires Android 12 or higher since mDNS is used to resolve the address.
When you use an earlier version of Android, assign a fixed IP-address to the Smart Pot Hub and edit the `HUB_URL` constant in [`app/src/main/java/com/example/smartpot/HubApi.java`][smart-pot/hub/HubApi.java] accordingly.

## License

The Smart Pot Android app is licensed under the MIT license agreement.
See the [LICENSE][smart-pot/android/LICENSE] file for details.

[smart-pot/android/LICENSE]:
  https://github.com/mysmartpot/android/blob/main/LICENSE
  "The MIT License"

[smart-pot/hub]:
  https://github.com/mysmartpot/hub
  "Smart Pot Hub — GitHub"

[smart-pot/hub/os-configuration]:
  https://github.com/mysmartpot/hub#os-configuration
  "OS Configuration — Smart Pot Hub — GitHub"

[smart-pot/hub/HubApi.java]:
  https://github.com/mysmartpot/android/blob/main/app/src/main/java/com/example/smartpot/HubApi.java
  "HubApi.java — Smart Pot Hub — GitHub"