package ai.kepos.companion;

import android.net.Uri;
import android.os.Bundle;
import com.getcapacitor.CapConfig;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerPlugin(LauncherConfigPlugin.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void load() {
        String launchUrl = LauncherPreferences.load(this);
        if (launchUrl != null) {
            Uri launchUri = Uri.parse(launchUrl);
            config = new CapConfig.Builder(getApplicationContext())
                .setServerUrl(launchUrl)
                .setAllowNavigation(new String[] { launchUri.getHost() })
                .create();
        }

        super.load();
    }
}
