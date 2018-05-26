package mingming.com.basejsbridge.constant;

public class BaseConstant {
	public static final String TAG = BaseConstant.class.getSimpleName();
	public static String filesPath;

	public static String getFilesPath() {
		return filesPath;
	}

	public static void setFilesPath(String filesPath) {
		BaseConstant.filesPath = filesPath;
	}

	// IMEI号
	public static final String IMEI = "IMEI";
	// 很抱歉,程序出现异常,即将退出.
	public static final String SYSTEMERR = "很抱歉,程序出现异常,即将退出.";
	// 通讯异常
	public static final String COMMERR = "通讯异常";
	// 交易成功
	public static final String TRANSUCCESS = "交易成功";
	// 交易失败
	public static final String TRANFAIL = "交易失败";
	// 成功Code
	public static final String SUCCESS = "000000";
	
	//3DES加密的密钥
	public static final String KEYDES = "201310012013100120131001";
}
