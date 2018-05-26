package mingming.com.basejsbridge.plugin.model;

/**
 * Created by mingming on 2018/5/23.
 */

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import mingming.com.basejsbridge.application.BaseApplication;
import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;
import mingming.com.basejsbridge.utils.LogUtil;
import mingming.com.basejsbridge.utils.ReadSdcardProperties;

/**
 * 页面处理
 */
public class PageProcessing extends Plugin{

    private static final String TAG = PageProcessing.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("initPage".equals(action)){
            return initPage(args);
        }else if ("jumpPage".equals(action)){
            return jumpPage(args);
        }
        return super.exec(action, args);
    }

    /**
     * 通过nextPage两个参数存储下一页的标志
     * @param args
     * @return
     */
    private PluginResult jumpPage(JSONObject args) {
        try {
            String nextPage = args.getString("nextPage");
            // 将全部的参数放入到全局变量
            BaseApplication.setGlobal("cng" + nextPage, args);
            ReadSdcardProperties properties = new ReadSdcardProperties((String) BaseApplication.getGlobal("PAGEURL"));
            Properties p = properties.getProperties();
            return PluginResult.newPluginResult(p.getProperty(nextPage));
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
    }

    private PluginResult initPage(JSONObject args) {
        JSONObject object = new JSONObject();
        try {
            String pageName = args.getString("pageName");
            ReadSdcardProperties properties = new ReadSdcardProperties(
                    (String) BaseApplication
                            .getGlobal(pageName));
            // 读取property文件

            Properties p = properties.getProperties();
            Set keys = p.keySet();
            JSONObject jsonObject = new JSONObject();
            for (Iterator<String> it = keys.iterator(); it.hasNext();) {
                String key = it.next();
                jsonObject.put(key, p.getProperty(key));
            }
            object.put("obj",jsonObject);
            object.put("value",BaseApplication.getGlobal("cng"+pageName));
        }catch (Exception e){
            LogUtil.e(TAG,e);
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
        return PluginResult.newPluginResult(object);
    }
}
