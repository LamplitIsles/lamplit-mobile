package ai.kepos.companion;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "LauncherConfig")
public class LauncherConfigPlugin extends Plugin {

    @PluginMethod
    public void configure(PluginCall call) {
        if (!LauncherPreferences.save(getContext(), call.getString("url"))) {
            call.reject("Enter an http:// or https:// URL with a hostname.");
            return;
        }

        call.resolve();
        getActivity().runOnUiThread(() -> getActivity().recreate());
    }
}
