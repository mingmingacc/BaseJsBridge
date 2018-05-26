package mingming.com.basejsbridge.plugin.model;

import org.json.JSONObject;

import java.util.Iterator;

import mingming.com.basejsbridge.application.BaseApplication;
import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;
import mingming.com.basejsbridge.utils.LogUtil;

/**
 * Created by mingming on 2018/5/23.
 */

public class Session extends Plugin{

    private static final String TAG = Session.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("addSession".equals(action)){
            return addSession(args);
        }else if ("removeSession".equals(action)){
            return removeSession(args);
        }else if ("getSession".equals(action)){
            return getSession(args);
        }
        return super.exec(action, args);

    }

    private PluginResult getSession(JSONObject args) {
        return null;
    }

    private PluginResult removeSession(JSONObject args) {
        return null;
    }

    //添加Session
    private PluginResult addSession(JSONObject args) {
        try {
            Iterator<String> it = args.keys();
            while (it.hasNext()){
                String key = it.next();
                BaseApplication.setGlobal(key,args.get(key));
            }
        }catch (Exception e){
            LogUtil.e(TAG,e);
        }
        return PluginResult.newEmptyPluginResult();
    }
}
