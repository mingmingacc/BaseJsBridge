package mingming.com.basejsbridge.plugin.manager;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.webkit.WebView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;

import mingming.com.basejsbridge.plugin.interfaces.IPlugin;

/**
 * Created by mingming on 2018/5/26.
 */

public class PluginManager {
    private Context context;

    private WebView webView;
    private static PluginManager instance;
    private HashMap<String, String> configs = new HashMap<String, String>();
    private HashMap<String, IPlugin> plugins = new HashMap<String, IPlugin>();

    public static PluginManager getInstance(Context context, WebView webView) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context, webView);
                }
            }
        }
        return instance;
    }

    private PluginManager(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    private int code = 0;
    public void loadPlugin() {
        String plugins = "plugins_" + code;
        int identifier = context.getResources().getIdentifier(plugins, "xml",
                context.getPackageName());

        if (identifier == 0) {
            return;
        }

        XmlResourceParser xml = context.getResources().getXml(identifier);

        try {
            int eventType = -1;
            while ((eventType = xml.next()) != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    String name = xml.getName();
                    if ("plugin".equals(name)) {
                        String pluginName = xml.getAttributeValue(null, "name");
                        String className = xml.getAttributeValue(null, "class");
                        configs.put(pluginName, className);
                    }
                }
            }
            code++;
            loadPlugin();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public IPlugin getPlugin(String pluginName) throws Exception {
        String className = configs.get(pluginName);
        if (className == null) {

        }
        if (plugins.containsKey(className)) {
            return plugins.get(className);
        } else {
            return addPlugin(className);
        }
    }

    public String exec(String service, String action, JSONObject args)
            throws Exception {
        IPlugin plugin = getPlugin(service);
        // 初始化context
        plugin.getInstance(context, webView);
        PluginResult result = plugin.exec(action, args);
        return result.getMessage();
    }

    /**
     * 添加映射的组件
     * @param className
     * @return
     */
    public IPlugin addPlugin(String className) {
        IPlugin plugin = null;
        try {
            plugin = (IPlugin) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        plugins.put(className, plugin);
        return plugin;
    }
}
