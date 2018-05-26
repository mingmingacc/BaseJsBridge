package mingming.com.basejsbridge.plugin.model;

import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;

/**
 * Created by mingming on 2018/5/23.
 */

public class Toast extends Plugin {

    @Override
    public PluginResult exec(String action, JSONObject args)throws Exception{
        if ("makeTextShort".equals(action)){
            return makeTextShort(args);
        }else if("makeTextLong".equals(action)){
            return makeTextLong(args);
        }
        return super.exec(action,args);
    }

    private PluginResult makeTextLong(JSONObject args) {
        try {
            String text = args.getString("text");
            android.widget.Toast.makeText(context,text,android.widget.Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
        return PluginResult.newEmptyPluginResult();
    }

    private PluginResult makeTextShort(JSONObject args) {
        try {
            String text = args.getString("text");
            android.widget.Toast.makeText(context,text,android.widget.Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
        return PluginResult.newEmptyPluginResult();
    }
}
