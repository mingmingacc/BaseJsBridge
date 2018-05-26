package mingming.com.basejsbridge.plugin.interfaces;

import android.content.Context;
import android.webkit.WebView;

import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.manager.PluginResult;

/**
 * Created by mingming on 2018/5/23.
 */

public interface IPlugin {


    public static final  String SERVICE = "service";
    public static final  String ACTION = "action";
    public static final String ARGS = "args";

    public void getInstance(Context context, WebView webView);

    public PluginResult exec(String action, JSONObject args) throws Exception;
}
