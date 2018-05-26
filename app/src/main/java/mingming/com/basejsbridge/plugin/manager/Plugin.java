package mingming.com.basejsbridge.plugin.manager;

import android.content.Context;
import android.webkit.WebView;

import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.interfaces.IPlugin;

/**
 * Created by mingming on 2018/5/23.
 */

public class Plugin implements IPlugin{

    protected Context context;
    protected WebView webView;
    @Override
    public void getInstance(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        return null;
    }
}
