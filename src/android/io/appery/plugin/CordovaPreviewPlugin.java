package io.appery.plugin;

import android.net.Uri;
import android.util.Log;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class CordovaPreviewPlugin extends CordovaPlugin {
    public static final String TAG = "CordovaPreview";
    private String baseUrl;
    private Set<String> remapedPathes;
    
    /**
     * Constructor.
     */
    public CordovaPreviewPlugin() {
        this.remapedPathes = new HashSet<>();
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.d(TAG, "Initialize");
        super.initialize(cordova, webView);

        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(cordova.getContext());
        this.baseUrl = parser.getLaunchUrl();

        prepareRemap();
    }

    @Override
    public Boolean shouldAllowNavigation(String url) {
        if (url.startsWith(baseUrl)) {
            return true;
        }
        return super.shouldAllowNavigation(url);
    }

    @Override
    public Uri remapUri(Uri uri) {
        String url = uri.toString();
        if (url.startsWith(baseUrl)) {
            String path = url.substring(this.baseUrl.length());
            for (String remapedPath : remapedPathes) {
                if (path.startsWith(remapedPath)) {
                    return Uri.fromFile(new File("/android_asset/www/" + path));
                }
            }
        }
        return super.remapUri(uri);
    }

    private void prepareRemap() {
        this.remapedPathes.add("cordova.js");
        this.remapedPathes.add("cordova_plugins.js");
        this.remapedPathes.add("plugins");
    }
}
