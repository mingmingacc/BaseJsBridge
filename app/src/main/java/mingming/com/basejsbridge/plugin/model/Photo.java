package mingming.com.basejsbridge.plugin.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;
import mingming.com.basejsbridge.utils.LogDeleteUtil;
import mingming.com.basejsbridge.utils.LogUtil;

/**
 * Created by mingming on 2018/5/23.
 */

public class Photo extends Plugin{
    private static final String TAG = Photo.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("photoGraph".equals(action)) {
            return photoGraph(args);
        } else if ("choicePhoto".equals(action)) {
            return choicePhoto(args);
        } else if ("screenShot".equals(action)) {
            return screenShot(args);
        }
        return super.exec(action, args);
    }

    /**
     * 该方法永安里截取当前屏幕
     * @param args
     * @return
     */
    private PluginResult screenShot(JSONObject args) {
        try {
            //获取当前屏幕的大小
            View decorView = ((Activity)context).getWindow().getDecorView();
            int width = decorView.getRootView().getWidth();
            int height = decorView.getRootView().getHeight();
            FileOutputStream  fos = null;
            Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
            //decorView.setDrawingCacheEnabled(true);
            Canvas canvas = new Canvas(bitmap);
            decorView.draw(canvas);
            String filePath = Environment.getExternalStorageDirectory().getPath() + args.getString("filePath");
            File file = new File(filePath);
            //如果文件不存在就首先创建目录
            if (!file.exists()) {
                //首先创建目录
                File dir = new File(file.getParent());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }else {
                LogDeleteUtil.deleteFileSafely(file.getAbsolutePath());
            }
            try {
                fos = new FileOutputStream(file);
                if (null != fos) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
                    fos.flush();
                }
            } catch (FileNotFoundException e) {
                LogUtil.e(TAG, e, "异常FileNotFoundException=" + e);
                return PluginResult.newErrorPluginResult("截屏文件未找到");
            } catch (Exception e) {
                LogUtil.e(TAG, "生成截屏文件异常  ：" + e);
                return PluginResult.newErrorPluginResult("截屏异常");
            } finally {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        LogUtil.e(TAG, e);
                    }
                }
                Runtime.getRuntime().gc();
            }
        }catch (Exception e) {
            LogUtil.e(TAG, e);
            return PluginResult.newErrorPluginResult("截屏发生错误");
        }
        return PluginResult.newEmptyPluginResult();
    }

    private PluginResult choicePhoto(JSONObject args) {
        try {
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
        return PluginResult.newEmptyPluginResult();
    }

    private PluginResult photoGraph(JSONObject args) {
        try {
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
            return PluginResult.newErrorPluginResult(e.getMessage());
        }
        return PluginResult.newEmptyPluginResult();
    }
}
