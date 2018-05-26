package mingming.com.basejsbridge.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;



public class LogDeleteUtil {
	public static final String TAG = LogDeleteUtil.class.getSimpleName();

	/**
	 * 该方法用来删除过期的日志
	 * @param parentDir 日志文件根目录
	 * @param logSaveTime 过期时间
	 * @param logPrefix 删除哪一类的日志文件
	 */
	public static void deleteObsoleteLogFile(String parentDir, int logSaveTime, String logPrefix) {
		try {
			//应用日志的目录是  parentDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileBusinessLog/3204"
			//上传日志的目录是  parentDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileBusinessLog/fileUploadLog"
			//如果日志目录下面有文件，那就先获取不需要删除的文件名称列表，包括交易的日志和上传文件的日志目录
			List<String> logFileNameList = getLogFileName(logSaveTime, logPrefix);
			File dir = new File(parentDir);
			File[] logFiles = dir.listFiles();
			//获取日志目录下面的所有日志文件
			if (logFiles != null && logFiles.length > 0) {
				for (int i = 0; i < logFiles.length; i++) {
					File file = logFiles[i];
					if (file != null && file.exists()) {
						String logFileName = file.getName();
						//如果日志文件名称是已指定的日志文件开头
						if (logFileName.startsWith(logPrefix) || logFileName.startsWith("fileClient")) {
							//如果日志文件是在指定有效期内的就保留，否则就删除
							boolean deleteFlag = isDeleteLogFile(logFileNameList, logFileName);
							if (deleteFlag) {//如果该日志文件需要删除
								String filePath = file.getAbsolutePath();
								deleteFileSafely(filePath);
							}
						}
					}
				}
			}
			//在这里来删除日志文件里面的内容
//			StringBuffer stringBuffer = new StringBuffer();
//			stringBuffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());
//			stringBuffer.append(File.separator);
//			stringBuffer.append("mobileBusinessLog");
//			stringBuffer.append(File.separator);
//			stringBuffer.append("fileUploadLog");
//			dir = new File(stringBuffer.toString());
//			logFiles = dir.listFiles();
			//获取日志目录下面的所有日志文件
//			if (logFiles != null && logFiles.length > 0) {
//				for (int i = 0; i < logFiles.length; i++) {
//					File file = logFiles[i];
//					if (file != null && file.exists()) {
//						String logFileName = file.getName();
//						//如果日志文件名称是已指定的日志文件开头
//						if (logFileName.startsWith("fileClient")) {
//							//如果日志文件是在指定有效期内的就保留，否则就删除
//							boolean deleteFlag = isDeleteLogFile(logFileNameList, logFileName);
//							if (deleteFlag) {//如果该日志文件需要删除
//								String filePath = file.getAbsolutePath();
//								deleteFileSafely(filePath);
//							}
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
	}
	
	/**
	 * 根据指定的日志保存日期和前缀获取所有不需要删除的日志文件的名称
	 * @param logSaveTime 指定的日志保存时间
	 * @param logPrefix 日志的前缀
	 * @return
	 */
	public static List<String> getLogFileName(int logSaveTime, String logPrefix) {
		List<String> logFileNameList = new ArrayList<String>();
		try {
			Calendar calendar = Calendar.getInstance();
			String parseDate = "";
			String logFileName = "";
			for (int i = 0; i < logSaveTime; i++) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -1 * i);
				Date date = calendar.getTime();
				parseDate = DateFormat.yyyyMMdd10.format(date);
				//交易日志的名称
				logFileName = logPrefix + "." + parseDate + ".log";
				logFileNameList.add(logFileName);
				//上传日志的名称
				logFileName = "fileClient." + parseDate + ".log";
				logFileNameList.add(logFileName);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return logFileNameList;
	}
	/**
	 * 用来判断该日志文件是否需要删除
	 * @param logFileNameList 不需要删除的文件列表
	 * @param logFileName 当前的日志文件名称
	 * @return
	 */
	public static boolean isDeleteLogFile(List<String> logFileNameList, String logFileName) {
		boolean deleteFlag = true;
		for (int i = 0; i < logFileNameList.size(); i++) {
			//如果日志文件名称在不需要上传的日志文件列表里面
			if (logFileName.equals(logFileNameList.get(i))) { 
				deleteFlag = false;
				break;
			}
		}
		return deleteFlag;
	}
	/**
	 * 根据文件路径安全删除文件
	 * @param filePath 待删除的文件路径
	 */
	public static void deleteFileSafely(String filePath) {
		try {
			File file = new File(filePath);
			if (file != null && file.exists()) {
				String temPath = file.getParent() + File.separator + System.currentTimeMillis();
				File temFile = new File(temPath);
				file.renameTo(temFile);
				if (temFile.delete()) {
					LogUtil.i(TAG, "删除文件成功   ：" + filePath);
				} else {
					LogUtil.i(TAG, "删除文件失败   ：" + filePath);
				}
			} else {
				LogUtil.i(TAG, "文件未创建，不需要删除   ：" + filePath);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "删除文件时异常  ：" + e);
		}
	}
	/**
	 * 删除文件，不打日志，因为首次安装应用如果删除文件的话，那么由于一开始未设置日志路径会导致Loguti应用报错
	 * @param filePath
	 */
	public static void deleteFileSafelyWithNoLog(String filePath) {
		try {
			File file = new File(filePath);
			if (file != null && file.exists()) {
				String temPath = file.getParent() + File.separator + System.currentTimeMillis();
				File temFile = new File(temPath);
				file.renameTo(temFile);
				temFile.delete();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 调用该方法将按天生成日志文件，删除过期的日志文件
	 * @param logSaveTime
	 * @param logParentName
	 * @param logPrefix
	 */
	public static void initLogFile(int logSaveTime, String logParentName, String logPrefix) {
		try {
			// 初始化日志文件
			LogUtil logUtil = new LogUtil();
			//设置要生成的日志文件名称
			String fileLogName = getAbsoluteLogFileName(logParentName, logPrefix);
			String logParentDir = getLogParentDir(logParentName);
			logUtil.setLogFileName(fileLogName);
			deleteObsoleteLogFile(logParentDir, logSaveTime, logPrefix);
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
	}
	/**
	 * 如果使用新的方法生成日志，第一次就把主目录下面的所有日志文件都删除掉
	 * @param context
	 */
	public static void firstDeleteAllFiles(Context context) {
		SharedPreferences shared = context.getSharedPreferences("deleteFiles", Context.MODE_PRIVATE);
		boolean deleteFlag = shared.getBoolean("deleteAllFiles", true);
		if (deleteFlag) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());
			stringBuffer.append(File.separator);
			stringBuffer.append("mobileBusinessLog");
			deleteDirOrFile(stringBuffer.toString());
			shared.edit().putBoolean("deleteAllFiles", false).commit();
		}
	}
	/**
	 * 该方法用来获取日志文件名称   如：Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileBusinessLog/3204/electronicBank.2016-10-11.log
	 * @param logParentName  如：mobileBusinessLog/3204
	 * @param logPrefix
	 * @return
	 */
	public static String getAbsoluteLogFileName(String logParentName, String logPrefix) {
		//日期文件按天生成
		String date = DateFormat.yyyyMMdd10.format(new Date());
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		stringBuffer.append(File.separator);
		stringBuffer.append(logParentName);
		stringBuffer.append(File.separator);
		stringBuffer.append(logPrefix);
		stringBuffer.append(".");
		stringBuffer.append(date);
		stringBuffer.append(".log");
		return stringBuffer.toString();
	}
	/**
	 * 该方法用来获取日志文件父目录    如：Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileBusinessLog/3204/
	 * @param logParentName 如：mobileBusinessLog/3204
	 * @return
	 */
	public static String getLogParentDir(String logParentName) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		stringBuffer.append(File.separator);
		stringBuffer.append(logParentName);
		//获取父文件夹的路径名称
		return stringBuffer.toString();
	}
	/**
	 * 递归删除文件或者目录里面的所有东西
	 * @param dirOrFilePath 传递的文件或者目录的路径
	 */
	public static void deleteDirOrFile(String dirOrFilePath){
		try {
			File file = new File(dirOrFilePath);
			if (file.exists()) {// 如果该文件存在
				if (file.isDirectory()) {// 如果是目录
					if (file.listFiles().length == 0) {// 如果是空目录，直接删除
						file.delete();
					} else {// 如果该目录不为空
						File delFiles[] = file.listFiles();
						for (int i = 0; i < delFiles.length; i++) {
							deleteDirOrFile(delFiles[i].getAbsolutePath());
						}
						file.delete();// 当目录里面的文件都删除后删除目录
					}
				} else {// 如果是文件，直接删除
					deleteFileSafelyWithNoLog (file.getAbsolutePath());
//					file.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}





