package mingming.com.basejsbridge.plugin.model;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import mingming.com.basejsbridge.application.BaseApplication;
import mingming.com.basejsbridge.db.DBUtil;
import mingming.com.basejsbridge.db.SQLMethod;
import mingming.com.basejsbridge.plugin.manager.Plugin;
import mingming.com.basejsbridge.plugin.manager.PluginResult;
import mingming.com.basejsbridge.utils.EncryptUtils;
import mingming.com.basejsbridge.utils.LogUtil;

/**
 * Created by mingming on 2018/5/23.
 */

public class DBMethod extends Plugin{

    private static final String TAG = DBMethod.class.getSimpleName();

    @Override
    public PluginResult exec(String action, JSONObject args) throws Exception {
        if ("selectDb".equals(action)){
            return selectDb(args);
        }else if("insertDb".equals(action)){
            return insertDb(args);
        }else if("deletedDb".equals(action)){
            return deletedDb(args);
        }else if("updateDb".equals(action)){
            return updateDb(args);
        }
        return super.exec(action, args);
    }
    /**
     *更新数据
     */
    private PluginResult updateDb(JSONObject args) {
        try {
            SQLiteDatabase db = DBUtil.getInstance().openDatabase();
            String sql = args.getString("sql");
            JSONArray AESObj = args.getJSONArray("obj");
            Object[] objects = new Object[AESObj.length()];
            if (args.has("AESFlag")){
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = encrypt(AESObj.getString(j));
                }
            }else {
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = AESObj.getString(j);
                }
            }
            db.execSQL(sql,objects);
        }catch (Exception e){
            LogUtil.e(TAG,e);
        }
        return PluginResult.newEmptyPluginResult();
    }

    /**
     * 数据加密
     *
     * 待加密数据
     * @return
     * @throws Exception
     */
    private String encrypt(String msg) throws Exception{
        return EncryptUtils.encrypt(msg,
                (String) BaseApplication.getGlobal("KEY"),
                (String) BaseApplication.getGlobal("IV"));
    }

    /**
     * 数据解密
     *
     * msg待解密数据
     * @return
     * @throws Exception
     */
    private String decrypt(String msg) throws Exception {
        return EncryptUtils.decrypt(msg,
                (String) BaseApplication.getGlobal("KEY"),
                (String) BaseApplication.getGlobal("IV"));
    }


    /**
     *删除数据
     */
    private PluginResult deletedDb(JSONObject args) {
        try {
            SQLiteDatabase db = DBUtil.getInstance().openDatabase();
            String sql = args.getString("sql");
            JSONArray AESObj = args.getJSONArray("obj");
            Object[] objects = new Object[AESObj.length()];
            if (args.has("AESFlag")) {
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = encrypt(AESObj.getString(j));
                }
            } else {
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = AESObj.getString(j);
                }
            }
            db.execSQL(sql, objects);
        }catch (Exception e){
            LogUtil.e(TAG,e);
        }
        return PluginResult.newEmptyPluginResult();
    }

    /**
     *插入数据
     */
    private PluginResult insertDb(JSONObject args) {
        try {
            SQLiteDatabase db = DBUtil.getInstance().openDatabase();
            String sql = args.getString("sql");
            JSONArray AESObj = args.getJSONArray("obj");
            Object[] objects = new Object[AESObj.length()];
            if (args.has("AESFlag")) {
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = encrypt(AESObj.getString(j));
                }
            } else {
                for (int j = 0; j < AESObj.length(); j++) {
                    objects[j] = AESObj.getString(j);
                }
            }
            db.execSQL(sql, objects);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return PluginResult.newEmptyPluginResult();
    }

    /**
     * 查询本地数据库方法
     */
    private PluginResult selectDb(JSONObject args) {
        JSONArray datas = null;
        JSONObject object = new JSONObject();
        try{
            SQLiteDatabase db = DBUtil.getInstance().openDatabase();
            //获取sql语句
            String sql = args.getString("sql");
            // 存储传入sql的参数
            String[] objects = null;
            // 判断是否有需要传入sql的参数
            if (args.has("obj")) {
                JSONArray obj = args.getJSONArray("obj");
                objects = new String[obj.length()];
                // 判断是否需要加密数据
                if (args.has("AESFlag")) {
                    for (int j = 0; j < obj.length(); j++) {
                        objects[j] = encrypt(obj.getString(j));
                    }
                } else {
                    for (int j = 0; j < obj.length(); j++) {
                        objects[j] = obj.getString(j);
                    }
                }
                object.put("value", obj);
            } else {
                object.put("value", "");
            }
            // 判断是否有关键字key
            if (args.has("key")) {
                String key = args.getString("key");
                object.put("key", key);
            } else {
                object.put("key", "");
            }
            // 执行sql
            datas = new SQLMethod(db).rawQuery(sql, objects);
            // 判断是否需要解密数据
            if (args.has("AESFlag")) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject jsonObject = datas.getJSONObject(i);
                    Iterator<String> it = jsonObject.keys();
                    JSONObject object2 = new JSONObject();
                    while (it.hasNext()) {
                        String key = it.next();
                        String value = jsonObject.getString(key);
                        object2.put(key, decrypt(value));
                    }
                    array.put(object2);
                }
                object.put("datas", array);
            } else {
                object.put("datas", datas);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }

        return PluginResult.newPluginResult(object);
    }
}
