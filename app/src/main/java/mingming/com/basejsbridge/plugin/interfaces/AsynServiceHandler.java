package mingming.com.basejsbridge.plugin.interfaces;

import android.os.Handler;
import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by mingming on 2018/5/26.
 */

public interface AsynServiceHandler extends Runnable{

    public void setKey(String key);

    public void setService(String service);

    public void setAction(String action);

    public void setArgs(JSONObject arg);

    public void setWebView(WebView webView);

    public void setMessageHandler(Handler myHandler);

    @Override
    public void run();
}
