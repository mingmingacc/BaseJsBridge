package mingming.com.basejsbridge.plugin.manager;

import android.webkit.JavascriptInterface;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by mingming on 2018/5/26.
 */

public class AppJavascriptInterface implements Serializable{

    private static Hashtable<String,String> CMDS = new Hashtable<>();
    private static Hashtable<String, String> ARGS = new Hashtable<String, String>();

    @JavascriptInterface
    public static void setCmds(String cmds,String id){
        CMDS.put(id,cmds);
    }

    @JavascriptInterface
    public static void setArgs(String args, String id) {
        ARGS.put(id, args);
    }

    public static String getCmdOnce(String id) {
        String result = CMDS.get(id);
        CMDS.remove(id);
        return result;
    }

    public static String getArgOnce(String id) {
        String result = ARGS.get(id);
        ARGS.remove(id);
        return result;
    }
}
