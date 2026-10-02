# KeposAI

An Android Capacitor launcher for a self-hosted Lamplit web application.

## First launch

The app begins with a local setup page where the user enters the complete
`http://` or `https://` URL of their Lamplit server. This supports a service on
the same device, a home network, or a Tailscale hostname.

The selected address is stored in the app's private Android storage. It
survives normal app updates and restarts, and is removed by uninstalling the
app or clearing its storage in Android settings.

## Build and install

```sh
bun install
bun run android:install
```

Build Android on the NUC with JDK 21 (required by Capacitor 8), Android SDK
platform 36 and Build Tools 35. Set `JAVA_HOME` and `ANDROID_HOME` through the
host environment; the build script does not select a host-specific Java path.
The build uses the SDK’s own Build Tools 35 `aapt2`, so NixOS does not need to
execute the unpatched Linux binary downloaded from Maven.
On the managed NUC, start a new login session after Android tooling changes so
these variables are loaded. `bun run android:apk` writes
`android/app/build/outputs/apk/debug/app-debug.apk` locally; no Mac checkout or
Mac build is needed.

When the phone is connected through the Mac ADB service exposed by Kepos,
install the NUC-built APK directly:

```sh
bun run android:apk
adb -H 127.0.0.1 -P 15037 -s 32131JEHN00865 install -r android/app/build/outputs/apk/debug/app-debug.apk
```

If Gradle downloads require the NUC’s local HTTP proxy, Java also needs its
proxy properties (it does not read `HTTPS_PROXY` automatically):

```sh
export JAVA_TOOL_OPTIONS="-Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7890 -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7890"
```

With a phone attached directly to NUC, `bun run android:install` uses local ADB.
The forwarded ADB connection is independent of the build host.

The Android shell includes Camera, Keyboard, and App plugins. Remote pages can
call `Keyboard.hide()` and `Camera.takePhoto({ saveToGallery: false })`.
Pages should listen for `App.addListener('appRestoredResult', ...)` to recover
camera results if Android terminates the shell while the camera Activity is open.
Camera 8.2.3 saves/restores its legacy camera flow; its newer `takePhoto` flow
keeps the pending call in memory. Recovery of `takePhoto` after process death
therefore requires separate device verification and is not guaranteed by merely
installing the App plugin.
The App plugin's back button handler is disabled to preserve the shell's existing
back behavior, including when loading a custom server URL.

Keyboard `resizeOnFullScreen` is left at its default (`false`). This integration
does not override soft input mode, edge-to-edge behavior, or system bar styling.
The shell compiles and targets Android API 36, with a minimum API of 24.

## Companion copying and photo saving

The Android shell also includes `@capacitor/clipboard` 8.0.1 so Companion can
copy messages to the system clipboard even over a plain HTTP LAN connection.

The Android shell includes `@capacitor-community/media` 9.1.0 for Capacitor 8.
Companion uses it to create/reuse the **Lamplit** application album and save
original image bytes. Keep the plugin's default `androidGalleryMode: false`;
this app does not need permission to browse the user's entire photo library.
After adding or updating native plugins, run `bun run android:sync` and rebuild
the APK. An already installed shell must be updated before this feature works.
