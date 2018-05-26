package mingming.com.basejsbridge.fragment;

/**
 * Created by mingming on 2018/5/26.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.json.JSONObject;

import mingming.com.basejsbridge.AsynServiceHandlerImpl;
import mingming.com.basejsbridge.R;
import mingming.com.basejsbridge.plugin.interfaces.AsynServiceHandler;
import mingming.com.basejsbridge.plugin.interfaces.IPlugin;
import mingming.com.basejsbridge.plugin.manager.AppJavascriptInterface;
import mingming.com.basejsbridge.plugin.manager.PluginManager;
import mingming.com.basejsbridge.utils.ConvertJSON;
import mingming.com.basejsbridge.utils.LogUtil;
import mingming.com.basejsbridge.view.RotateProgressDialog;

/**
 * Fragment 基类，所有的fragment都继承此基类
 */
public class BaseFragment extends Fragment{
    private static final String TAG = BaseFragment.class.getSimpleName();

    //网页加载等待框
    public RotateProgressDialog dialog;
    public MyWebView webView;
    public LinearLayout rootView;
    private PluginManager mPluginManager;
    private int threadIdCounter = 0;
    public Handler mHandler = new Handler(){};
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        webView = new MyWebView(mContext);
        mPluginManager = PluginManager.getInstance(mContext,webView);
        mPluginManager.loadPlugin();
        rootView = (LinearLayout) inflater.inflate(R.layout.base_fragment,container,false);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.addView(webView);
        //webview设置
        WebSettings setting = webView.getSettings();
        //能够执行javaseript脚本
        setting.setJavaScriptEnabled(true);
        setting.setDefaultTextEncodingName("utf-8");
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        //自适应屏幕
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setLoadWithOverviewMode(true);

        //弹出对话框
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置滚动条宽度
        webView.setScrollBarSize(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setScrollBarSize(0);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                JSONObject args = null;
                JSONObject head = null;
                try {
                    head = ConvertJSON.stringToJson(message);
                    args = ConvertJSON.stringToJson(defaultValue);
                    String execResult = mPluginManager.exec(head.getString(IPlugin.SERVICE),head.getString(IPlugin.ACTION),args);
                    result.confirm(execResult);
                }catch (Exception e){

                    dialog.closeDialog();
                    e.printStackTrace();
                }
                return true;
            }
        });

        //loading图
        dialog = new RotateProgressDialog(mContext);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.showDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.closeDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("androidhtml")){
                    String id = url.substring(url.indexOf("id=")+3);
                    JSONObject cmd = null;
                    JSONObject arg = null;
                    try {
                        cmd = ConvertJSON.stringToJson(AppJavascriptInterface.getCmdOnce(id));
                        arg = ConvertJSON.stringToJson(AppJavascriptInterface.getArgOnce(id));
                    }catch (Exception e){
                        LogUtil.e(TAG,e);
                        return false;
                    }

                    try {
                        AsynServiceHandler asyn = new AsynServiceHandlerImpl(mContext);
                        asyn.setKey(id);
                        asyn.setService(cmd.getString(IPlugin.SERVICE));
                        asyn.setAction(cmd.getString(IPlugin.ACTION));
                        asyn.setArgs(arg);
                        asyn.setWebView(webView);
                        asyn.setMessageHandler(mHandler);
                        Thread thread = new Thread(asyn,"asyn_"+(threadIdCounter++));
                        thread.start();
                    }catch (Exception e){

                        LogUtil.e(TAG,e);
                        return false;
                    }
                    return true;
                }
                webView.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new AppJavascriptInterface(),"niftf");
        return rootView;
    }
}
