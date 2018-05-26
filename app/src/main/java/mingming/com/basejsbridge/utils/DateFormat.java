package mingming.com.basejsbridge.utils;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

/**
 *  日期，时间格式
 * @author lenvov
 */
@SuppressLint("SimpleDateFormat")
public class DateFormat {
	// 时间处理
	public final static SimpleDateFormat formatter14 = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public final static SimpleDateFormat formatter8 = new SimpleDateFormat(
			"yyyyMMdd");
	public final static SimpleDateFormat formatter18 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat formatter19 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	public final static SimpleDateFormat HHmmss = new SimpleDateFormat("hhmmss");
	public final static SimpleDateFormat YYMMDD = new SimpleDateFormat("yyMMdd");
	public final static SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat(
			"yyyyMMdd");
	public final static SimpleDateFormat yyyyMMdd10 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat yyyyMMddLine = new SimpleDateFormat(
			"yyyy/MM/dd");
}
