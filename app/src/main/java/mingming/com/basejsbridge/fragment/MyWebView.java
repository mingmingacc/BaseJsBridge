package mingming.com.basejsbridge.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;

public class MyWebView extends WebView {
	WebView webView;
	/** 加载信息使用 */
	private String loadInfo;

	public MyWebView(Context context) {
		super(context);
		webView = this;
	}

	public void goBack(final String info) {
		goBack();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadInfo = info;
				if (info != null) {
					Message msg = Message.obtain();
					msg.what = 0;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				webView.loadUrl("javascript:showErr('" + loadInfo + "')");
				break;
			case 1:
				break;
			default:
				break;
			}
		}
	};
}
