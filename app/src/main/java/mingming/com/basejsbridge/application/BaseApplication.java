package mingming.com.basejsbridge.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import mingming.com.basejsbridge.utils.CrashHandler;

/**
 * Created by mingming on 2018/5/24.
 */

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();
    public static Context context = null;
    public static BaseApplication instance;
    public Stack<Activity> activities = null;

    /**
     *  全局变量
     */
    public static Map<Object, Object> Global = new HashMap<Object, Object>();

    public BaseApplication() {
        activities = new Stack<Activity>();
    }


    public static void setGlobal(Object key, Object value) {
        Global.put(key, value);
    }

    public static Object getGlobal(Object key) {
        return Global.get(key);
    }

    /**
     *  移除某个关键字
     * key字段名称
     */
    public static void removeGlobal(Object key) {
        Global.remove(key);
    }

    /**
     * 查看是否有某个关键字
     * key
     * @return 包含返回true，不包含返回false
     */
    public static Boolean hasKey(Object key) {
        return Global.containsKey(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 捕获异常信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    /**
     * 单例模式中获取唯一的BaseApplication实例
     */
    public static BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    /**
     * 添加Activity到容器中
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activities != null && activities.size() > 0) {
            if (!activities.contains(activity)) {
                // 如果没有放过该Activity，那么就放到list列表中去
                activities.add(activity);
            }
        } else {
            activities.add(activity);
        }
        context = activity;
    }

    /**
     * 遍历关闭所有Activity
     */
    public void closeActivities() {
        if (activities != null && activities.size() > 0) {
            for (int i = 0; i < activities.size(); i++) {
                Activity activity = (Activity) activities.get(i);
                activity.finish();
            }
        }
        System.exit(0);
    }
}
