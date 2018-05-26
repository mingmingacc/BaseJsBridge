package mingming.com.basejsbridge.plugin.model;

import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;
import mingming.com.basejsbridge.utils.LogUtil;

/**
 * Created by mingming on 2018/5/23.
 */

public class WebViewMethod extends Plugin {

    private static final String TAG = WebViewMethod.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("goBack".equals(action)){
            return goBack();
        }else if("goReload".equals(action)){
            return goReload();
        }
        return super.exec(action, args);
    }

    private PluginResult goReload() {
        try {
            webView.goBack();
        }catch (Exception e){LogUtil.e(TAG,e);

        }
        return PluginResult.newEmptyPluginResult();
    }

    private PluginResult goBack() {
        try {
            webView.goBack();
        }catch (Exception e){
            LogUtil.e(TAG,e);
        }
        return PluginResult.newEmptyPluginResult();
    }
}
