import type { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "ai.kepos.companion",
  appName: "KeposAI",
  webDir: "www",
  plugins: {
    App: {
      disableBackButtonHandler: true
    }
  }
};

export default config;
