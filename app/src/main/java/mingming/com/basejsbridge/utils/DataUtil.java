package mingming.com.basejsbridge.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.id.Hex;
import org.apache.commons.id.uuid.UUID;
import org.json.JSONException;
import org.json.JSONObject;


public class DataUtil {
	private static final String TAG = DataUtil.class.getSimpleName();

	/**
	 *  获取UUID
	 * @return
	 */
	public static String getCode() {
		return new String(Hex.encodeHex(UUID.randomUUID().getRawBytes()));
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0 || "null".equals(str))
			return true;
		else
			return false;
	}

	/**
	 *  判断某个字符串是否是json格式
	 * @param value
	 * @return
	 */
	public static boolean isJson(String value) {
		try {
			new JSONObject(value);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	/**
	 *  转16进制String
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0");
				hs.append(stmp);
			} else
				hs.append(stmp);
			if (n < b.length - 1)
				hs.append("");
		}
		return hs.toString().toUpperCase();
	}

	/**
	 *  将InputStream转化为String
	 *  stream需要转换的inputstream
	 *  charSetName字符集名称
	 * @return String 返回字符串
	 */
	public static String InputStream2String(InputStream stream,
			String charSetName) {
		String resultString = "";
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		StringBuffer out = new StringBuffer();
		char[] temp = new char[4096];
		int i = -1;
		try {
			InputStreamReader reader = new InputStreamReader(stream,
					charSetName);
			while ((i = reader.read(temp)) != -1) {
				out.append(temp, 0, i);
			}
			resultString = out.toString();
		} catch (IOException e) {
			LogUtil.e(TAG, e);
		}
		return resultString;
	}

	/**
	 * @see 16进制字符串转byte数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
