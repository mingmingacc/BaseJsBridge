package mingming.com.basejsbridge.plugin.model;

import org.json.JSONObject;


import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;

/**
 * Created by mingming on 2018/5/23.
 */

public class UploadData extends Plugin{

    private static final String TAG = UploadData.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("uploadData".equals(action)){
            return uploadData(args);
        }else if ("uploadNoHead".equals(action)){
            return uploadNoHead(args);
        }
        return super.exec(action, args);
    }

    private PluginResult uploadNoHead(JSONObject args) {
        return null;
    }

    /**
     *  报文类型，有data和head包含
     * @param args 数据参数
     *            ,mtdName为请求方法的名称
     * @return
     */
    private PluginResult uploadData(JSONObject args) {

        return null;
    }
}
