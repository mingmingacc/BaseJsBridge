package mingming.com.basejsbridge.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

import mingming.com.basejsbridge.utils.LogUtil;

/**
 * Created by mingming on 2018/5/24.
 */

public class DBUtil {

    private static final String TAG = DBUtil.class.getSimpleName();

    private final AtomicInteger mOpenCounter = new AtomicInteger();
    private static DBUtil instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper){
        if (instance == null){
            instance = new DBUtil();
            mDatabaseHelper = helper;
        }
    }
    public static synchronized DBUtil getInstance(){
        if (instance == null){
            throw new IllegalStateException();
        }
        return instance;
    }


    /**
     * 获取数据库（默认返回可读可写数据库）
     * @return
     * @author panby
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * 关闭数据库
     * @author panby
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
        LogUtil.i(TAG, "数据库关闭完毕");
    }
}
