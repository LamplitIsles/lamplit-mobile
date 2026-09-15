package ai.kepos.companion;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

final class LauncherPreferences {

    private static final String PREFERENCES = "lamplit_launcher";
    private static final String URL_KEY = "launch_url";

    private LauncherPreferences() {}

    static String load(Context context) {
        String stored = preferences(context).getString(URL_KEY, null);
        return normalize(stored);
    }

    static boolean save(Context context, String value) {
        String normalized = normalize(value);
        if (normalized == null) return false;
        preferences(context).edit().putString(URL_KEY, normalized).apply();
        return true;
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private static String normalize(String value) {
        if (value == null) return null;

        Uri uri = Uri.parse(value.trim());
        String scheme = uri.getScheme();
        if (
            uri.getHost() == null ||
            uri.getUserInfo() != null ||
            !("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
        ) {
            return null;
        }

        return uri.buildUpon().fragment(null).build().toString();
    }
}
