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

The local build uses Homebrew OpenJDK 21, as required by Capacitor 8.

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
