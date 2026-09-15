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
