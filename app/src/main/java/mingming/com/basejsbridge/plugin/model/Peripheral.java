package mingming.com.basejsbridge.plugin.model;

/**
 * Created by mingming on 2018/5/23.
 */

import android.os.Handler;

import org.json.JSONObject;

import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;

/**
 * 连接外设
 */
public class Peripheral extends Plugin{

    private static final String TAG = Peripheral.class.getSimpleName();

    Handler mHandler = new Handler();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("goPeripheral".equals(action)){
            return goPeripheral();
        }
        return super.exec(action, args);
    }

    private PluginResult goPeripheral() {
        //		ComShell mComShell = BaseApplication
        //				.getComShell(BaseApplication.mSEContext);
        //		if (mComShell != null && mComShell.GetComShellStatus()) {
        //			PeripheralSS ss = new PeripheralSS(mComShell);
        //			if (mComShell == null)
        //				LogUtil.w(TAG, "建立蓝牙连接失败");
        //			else {
        //				ss.showAlertDialog(BaseApplication.mSEContext, webView,
        //						mHandler, R.string.putidcard, 1);
        //			}
        //		} else {
        //			Tools.showToast("建立蓝牙连接失败，请确认蓝牙是否打开");
        //		}
        return PluginResult.newEmptyPluginResult();
    }
}
