package mingming.com.basejsbridge.plugin.manager;

import org.json.JSONObject;

/**
 * Created by mingming on 2018/5/23.
 */

public class PluginResult {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 返回错误信息
     *
     * @param message
     * @return
     */
    public static final PluginResult newErrorPluginResult(Object message) {
        PluginResult result = new PluginResult();
        JSONObject object = new JSONObject();
        try {
            object.put("status", "999999");
            object.put("message", message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setMessage(object.toString());
        return result;
    }

    /**
     * 返回空信息
     *
     * @return
     */
    public static final PluginResult newEmptyPluginResult() {
        PluginResult result = new PluginResult();
        JSONObject object = new JSONObject();
        try {

            object.put("status", "000000");
            object.put("message", "");
            result.setMessage(object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 返回交易信息
     *
     * @return
     */
    public static final PluginResult newPluginResult(Object message) {
        PluginResult result = new PluginResult();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("status", "000000");
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setMessage(jsonObject.toString());
        return result;
    }
}
