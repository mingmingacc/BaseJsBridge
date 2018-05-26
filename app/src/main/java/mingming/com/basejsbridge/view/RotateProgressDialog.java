package mingming.com.basejsbridge.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import mingming.com.basejsbridge.R;

/**
 * Created by mingming on 2018/5/26.
 */

public class RotateProgressDialog extends ProgressDialog {
    public RotateProgressDialog(Context context) {
        super(context);
    }

    public RotateProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.progress_dialog);
    }
    /**
     * 显示加载页面
     */
    public void showDialog() {
        show();
    }

    /**
     * 关闭加载页面
     */
    public void closeDialog() {
        dismiss();
    }
}
