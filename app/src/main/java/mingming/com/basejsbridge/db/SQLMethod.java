package mingming.com.basejsbridge.db;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLMethod {

	SQLiteDatabase db;
	int flag = -1;
	public SQLMethod(SQLiteDatabase db) {
		flag = 0;
		this.db = db;
	}
	
	public SQLMethod() {
		flag = 1;
		this.db = DBUtil.getInstance().openDatabase();
	}

	public void insert(String table, String nullColumnHack, ContentValues values)
			throws Exception {
		db.insertOrThrow(table, nullColumnHack, values);
		if(flag == 1) {
			DBUtil.getInstance().closeDatabase();
		}
	}

	/**
	 * 插入或者更新
	 */
	public void replace(String table, ContentValues values) {
		try {
			db.replaceOrThrow(table, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().closeDatabase();
		}
	}

	/**
	 * 更新上送列表状态
	 */
	public int update(String table, ContentValues cv, String selected,
			String[] args) {
		int count = 0;
		try {
			count = db.update(table, cv, selected, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().closeDatabase();
		}
		return count;
	}

	/**
	 * 查询全部表字段，查询时解密
	 */
	public JSONArray query(String table) {
		JSONArray list = new JSONArray();
		Cursor c = null;
		try {
			c = db.query(table, null, null, null, null, null, null);

			while (c.moveToNext()) {
				JSONObject json = new JSONObject();
				for (int i = 1; i < c.getColumnCount(); i++) {
					if (c.getString(i) == null || "".equals(c.getString(i))) {
						json.put(c.getColumnName(i), "");
					} else {
						// json.put(c.getColumnName(i),
						// AES.decode(c.getString(i)));
						json.put(c.getColumnName(i), c.getString(i));
					}
				}
				list.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
//			DBUtil.getInstance().closeDatabase();
		}
		return list;
	}

	/**
	 * 根据条件查询部分表字段,查询时解密
	 */
	public List<JSONObject> query(String table, String[] columns,
			String selection, String[] parms) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		Cursor c = null;
		try {
			c = db.query(table, columns, selection, parms, null, null, null);

			while (c.moveToNext()) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < c.getColumnCount(); i++) {
					// json.put(c.getColumnName(i), AES.decode(c.getString(i)));
					json.put(c.getColumnName(i), c.getString(i));
				}
				list.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			DBUtil.getInstance().closeDatabase();
		}
		return list;
	}

	/**
	 * 根据条件查询数据库,查询时解密
	 */
	public List<JSONObject> query(String table, String[] columns,
			String selection, String[] parms, String groupBy, String having,
			String orderBy) {
		List<JSONObject> list = new ArrayList<JSONObject>();

		Cursor c = null;
		try {
			c = db.query(table, columns, selection, parms, groupBy, having,
					orderBy);
			while (c.moveToNext()) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < c.getColumnCount(); i++) {
					if (c.getString(i) == null || "".equals(c.getString(i))) {
						json.put(c.getColumnName(i), "");
					} else {
						// json.put(c.getColumnName(i),
						// AES.decode(c.getString(i)));
						json.put(c.getColumnName(i), c.getString(i));
					}
				}
				list.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			DBUtil.getInstance().closeDatabase();
		}
		return list;
	}

	/**
	 * 删除方法
	 */
	public int del(String table, String selection, String[] parms) {
		// 执行删除
		int count = 0;
		try {
			count = db.delete(table, selection, parms);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().closeDatabase();
		}
		return count;
	}

	/**
	 * 自定义查询
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public JSONArray rawQuery(String sql, String[] selectionArgs) {
		JSONArray list = new JSONArray();
		Cursor c = null;
		try {
			c = db.rawQuery(sql, selectionArgs);
			
			while (c.moveToNext()) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < c.getColumnCount(); i++) {
					if (c.getString(i) == null || "".equals(c.getString(i))) {
						json.put(c.getColumnName(i), "");
					} else {
						// json.put(c.getColumnName(i),
						// AES.decode(c.getString(i)));
						json.put(c.getColumnName(i), c.getString(i));
					}
				}
				list.put(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
//			DBUtil.getInstance().closeDatabase();
		}
		return list;
	}

	/**
	 * 查询记录的总数
	 */
	public int queryCount(String tableName, String conditions,
			String[] selectionArgs) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(tableName);
		if (conditions != null) {
			sql.append(" where ");
			sql.append(conditions);
		}
		Cursor c = null;
		try {
			c = db.rawQuery(sql.toString(), selectionArgs);
			c.moveToFirst();
			return c.getInt(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			DBUtil.getInstance().closeDatabase();
		}
		return 0;
	}
}
