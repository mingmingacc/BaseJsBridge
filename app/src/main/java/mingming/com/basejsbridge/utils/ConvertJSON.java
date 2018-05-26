package mingming.com.basejsbridge.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ExpandableListActivity;
import android.content.ContentValues;

import mingming.com.basejsbridge.application.BaseApplication;
import mingming.com.basejsbridge.constant.BaseConstant;

/**
 * @see不同数据类型间的转换
 * @author lenvov
 */
public class ConvertJSON {

	private static final String TAG = ConvertJSON.class.getSimpleName();

	/**
	 * @seeString转换为JSON
	 * @param str
	 * @return
	 */
	public static JSONObject stringToJson(String str) {
		JSONObject json = null;
		try {
			if (str == null || "".equals(str)) {
				LogUtil.w(TAG, "stringToJson error str is null");
			} else {
				JSONTokener jsonTokener = new JSONTokener(str);
				json = (JSONObject) jsonTokener.nextValue();
			}
		} catch (Exception e) {
			LogUtil.w(TAG, "stringToJson error, str is not json");
		}
		return json;
	}

	/**
	 * 将前端JSON数据封装成NameValue数据
	 * @param jObject
	 * @param fileName
	 * @param pairs
	 * @return
	 */
	public static List<NameValuePair> jsonToNameValue(JSONObject jObject,
			String fileName, List<NameValuePair> pairs) {
		if (jObject == null) {
			LogUtil.w(TAG, "jsonToNameValue error jObject is null");
			return null;
		}
		if (fileName == null) {
			LogUtil.w(TAG, "jsonToNameValue error fileName is null");
			return null;
		}
		if (pairs == null) {
			LogUtil.w(TAG, "jsonToNameValue error pairs is null");
			return null;
		}
		// 解析并序列化配置文件
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "UploadProperties/" + fileName);
		Properties p = properties.getProperties();
		Set<Object> keys = p.keySet();
		for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
			try {
				String key = (String) it.next();
				if (key.indexOf("baseParam") != -1) {// 系统存储的字段
					String name = key.split("_")[1];
					pairs.add(new BasicNameValuePair(name,
							(String) BaseApplication.getGlobal(p
									.getProperty(key))));
				} else if (key.indexOf("baseDefault") != -1) {// 默认值
					String name = key.split("_")[1];
					pairs.add(new BasicNameValuePair(name, p.getProperty(key)));
				} else if (key.indexOf("baseId") != -1) {// 16位序列化ID
					String name = key.split("_")[1];
					pairs.add(new BasicNameValuePair(name, DataUtil.getCode()));
				} else {
					if (jObject.has(p.getProperty(key))) {// 从JSON中获取该字段
						pairs.add(new BasicNameValuePair(key, jObject
								.getString(p.getProperty(key))));
					}
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
		return pairs;
	}

	/**
	 *  前台JSON数据转换为后台JSON格式数据
	 *      ,根据资源文件筛选json字段，baseParam=公共字段，baseDefault=默认值,jsonName
	 *      =嵌套报文,jsonArray=数组报文，baseId=16位标识码。
	 * json带转换JSON
	 * fileName文件名称
	 * @return
	 */
	public static JSONObject jsonToJson(JSONObject json, String fileName,
			String extra) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (json == null) {
			LogUtil.w(TAG, "jsonToJson error json is null");
			return null;
		}
		if (fileName == null) {
			LogUtil.w(TAG, "jsonToJson error fileName is null");
			return null;
		}

		// 获取前台未回显的数据
		JSONObject eva = null;
		if (extra != null && DataUtil.isJson(extra)) {
			eva = ConvertJSON.stringToJson(extra);
		}

		// 解析并序列化配置文件
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "UploadProperties/" + fileName
						+ ".properties");
		Properties p = properties.getProperties();
		Set<Object> keys = p.keySet();
		for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
			try {
				String key = (String) it.next();
				if (key.indexOf("jsonName") != -1) {
					String name = p.getProperty(key);
					JSONObject object = jsonToJson(json, name, null);
					String jsonName = name.split("_")[1]; // 获取内部姓名
					// 获取前台数据
					if (eva != null && !eva.isNull(jsonName)) {
						JSONObject evaChild = eva.getJSONObject(jsonName);
						Iterator<?> evaIt = evaChild.keys();
						while (evaIt.hasNext()) {
							String evaName = (String) evaIt.next();
							object.put(evaName, evaChild.get(evaName));
						}
					}
					jsonObject.put(jsonName, object);
				} else if (key.indexOf("array") != -1) {// 拼接列表
					String name = p.getProperty(key);
					JSONArray object = null;
					String jsonName = name.split("_")[1]; // 获取内部姓名
					// 获取前台数据
					if (extra != null && !eva.isNull(jsonName)) {
						object = eva.getJSONArray(jsonName);
					} else {
						object = jsonToJsonArray(json, name);
					}
					jsonObject.put(jsonName, object);
				} else if (key.indexOf("jsonArray") != -1) {
					String name = p.getProperty(key);
					String jsonName = name.split("_")[1]; // 获取内部姓名
					JSONArray object = getJSONArray(json, name);
					jsonObject.put(jsonName, object);
				} else if (key.indexOf("baseParam") != -1) {
					String name = key.split("_")[1];
					jsonObject.put(name,
							BaseApplication.getGlobal(p.getProperty(key)));
				} else if (key.indexOf("baseDefault") != -1) {
					String name = key.split("_")[1];
					jsonObject.put(name, p.getProperty(key));
				} else if (key.indexOf("baseId") != -1) {
					String name = key.split("_")[1];
					jsonObject.put(name, DataUtil.getCode());
				} else if (key.indexOf("yyyyMMdd") != -1) {// 日期年月日
					String name = key.split("_")[1];
					jsonObject
							.put(name, DateFormat.YYYYMMDD.format(new Date()));
				} else if (key.indexOf("HHmmss") != -1) {// 日期时分秒
					String name = key.split("_")[1];
					jsonObject.put(name, DateFormat.HHmmss.format(new Date()));
				} else if (key.indexOf("yyyyMMddHHmmssSSS") != -1) {// 日期年月日时分秒
					String name = key.split("_")[1];
					jsonObject.put(name,
							DateFormat.yyyyMMddHHmmssSSS.format(new Date()));
				} else {
					if (json.has(p.getProperty(key))) {// 如果在json中取到了该字段，那么就去json里面的值
						jsonObject.put(key, json.get(p.getProperty(key)));
					}
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
		return jsonObject;
	}

	/**
	 * list转换为JsonArray,上送数据使用。作用1、筛选变量字段。2、转换变量名称。
	 * @param list
	 * @param fileName
	 * @return
	 */
	public static String listToJsonArray(List<JSONObject> list, String fileName) {
		JSONArray array = new JSONArray();
		if (list.size() == 0) {
			LogUtil.w(TAG, "jsonToNameValue error json is null");
			return null;
		}
		// 此处转换数据库字段和接口字段
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "UploadProperties/" + fileName);
		// 读取property文件
		Properties p = properties.getProperties();
		Set keys = p.keySet();
		for (JSONObject json : list) {
			JSONObject object = new JSONObject();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				try {
					object.put(key, json.getString(p.getProperty(key)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			array.put(object);
		}
		return array.toString();
	}

	/**
	 * Json转JsonArray
	 * @param json
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static JSONArray jsonToJsonArray(JSONObject json, String fileName)
			throws Exception {
		if (json == null) {
			LogUtil.w(TAG, "jsonToJsonArray error json is null");
			return null;
		}
		// 此处转换数据库字段和接口字段
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "UploadProperties/" + fileName
						+ ".properties");
		// 读取property文件
		Properties p = properties.getProperties();
		Set keys = p.keySet();
		String key = null;
		Object jsonObject = null;
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			key = it.next();
			if (!json.isNull(p.getProperty(key))) {
				jsonObject = json.get(p.getProperty(key));
				break;
			}
		}
		JSONArray jsonArray = new JSONArray();
		if (jsonObject instanceof JSONArray) {
			int length = ((JSONArray) jsonObject).length();
			for (int i = 0; i < length; i++) {
				JSONObject object = new JSONObject();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					try {
						key = it.next();
						if (key.indexOf("baseParam") != -1) {
							String name = key.split("_")[1];
							object.put(name, BaseApplication.getGlobal(p
									.getProperty(key)));
						} else if (key.indexOf("baseDefault") != -1) {
							String name = key.split("_")[1];
							object.put(name, p.getProperty(key));
						} else if (key.indexOf("baseId") != -1) {
							String name = key.split("_")[1];
							object.put(name, DataUtil.getCode());
						} else {
							object.put(key,
									json.getJSONArray(p.getProperty(key))
											.get(i));
						}
					} catch (Exception e) {
						LogUtil.e(TAG, e);
					}
				}
				jsonArray.put(object);
			}
		} else {
			JSONObject object = new JSONObject();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				try {
					key = it.next();
					if (key.indexOf("baseParam") != -1) {
						String name = key.split("_")[1];
						object.put(name,
								BaseApplication.getGlobal(p.getProperty(key)));
					} else if (key.indexOf("baseDefault") != -1) {
						String name = key.split("_")[1];
						object.put(name, p.getProperty(key));
					} else if (key.indexOf("baseId") != -1) {
						String name = key.split("_")[1];
						object.put(name, DataUtil.getCode());
					} else {
						object.put(key, json.getString(p.getProperty(key)));
					}
				} catch (Exception e) {
					LogUtil.e(TAG, e);
				}
			}
			jsonArray.put(object);
		}
		return jsonArray;
	}

	/**
	 *  两个JSONArray拼接
	 * @param mData
	 *            第一个JSONArray
	 * @param array
	 *            第二个JSONArray
	 * @return
	 */
	public static JSONArray joinJSONArray(JSONArray mData, JSONArray array) {
		StringBuffer buffer = new StringBuffer();
		try {
			int len1 = mData.length();
			for (int i = 0; i < len1; i++) {
				JSONObject obj1 = (JSONObject) mData.get(i);
				if (i == len1 - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			int len = array.length();
			if (len1 > 0 && len > 0)
				buffer.append(",");
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = (JSONObject) array.get(i);
				if (i == len - 1)
					buffer.append(obj1.toString());
				else
					buffer.append(obj1.toString()).append(",");
			}
			buffer.insert(0, "[").append("]");
			return new JSONArray(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * JSON转ContentValues
	 * @param json
	 * @return TODO
	 */
	public static ContentValues jsonToContentValues(JSONObject json,
			String fileName) {
		ContentValues values = new ContentValues();
		if (json == null) {
			LogUtil.w(TAG, "jsonToContentValues3DES error json is null");
		}
		// 此处转换数据库字段和接口字段
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "SqlProperties/" + fileName);
		// 读取property文件
		Properties p = properties.getProperties();
		Set keys = p.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			try {
				values.put(key, json.getString(p.getProperty(key)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return values;
	}

	/**
	 * String转ContentValues
	 * @param str
	 * @return
	 */
	public static ContentValues strToContentValues(String str, String fileName)
			throws Exception {
		return jsonToContentValues(stringToJson(str), fileName);
	}
	
	
	public static JSONArray getJSONArray(JSONObject jsonObject, String fileName) throws Exception{
		if (jsonObject == null) {
			LogUtil.w(TAG, "jsonToJsonArray error json is null");
			return null;
		}
		// 此处转换数据库字段和接口字段
		ReadSdcardProperties properties = new ReadSdcardProperties(
				BaseConstant.filesPath + "UploadProperties/" + fileName
						+ ".properties");
		// 读取property文件
		Properties p = properties.getProperties();
		Set keys = p.keySet();
		String key = null;
		Object object = null;
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			key = it.next();
			if (!jsonObject.isNull(key)) {
				object = jsonObject.get(p.getProperty(key));
				break;
			}
		}
		JSONArray jsonArray = null;
		if (object instanceof JSONArray) {
			jsonArray = (JSONArray) object;
		} else {
			jsonArray = new JSONArray();
			jsonArray.put(object);
		}
		return jsonArray;
	}
}
