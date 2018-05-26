package mingming.com.basejsbridge;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.interfaces.AsynServiceHandler;
import mingming.com.basejsbridge.plugin.manager.PluginManager;

/**
 * Created by mingming on 2018/5/26.
 */

public class AsynServiceHandlerImpl implements AsynServiceHandler {

    private String key;
    private String service;
    private String action;
    private JSONObject args;
    private WebView webView;
    private Handler messageHandler;
    private Handler handler = new Handler();
    private Context context;


    public AsynServiceHandlerImpl(Context context) {
        this.context = context;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void setArgs(JSONObject arg) {
        this.args = arg;
    }

    public JSONObject getArgs() {
        return args;
    }


    @Override
    public void run() {
        try {
            final String responseBody = PluginManager.getInstance(context, webView).exec(
                    service, action, args);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(action.contains("jumpPage")) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            webView.loadUrl(jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        webView.loadUrl("javascript:AndroidHtml5.callBackJs('"
                                + responseBody + "', '" + key + "')");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
