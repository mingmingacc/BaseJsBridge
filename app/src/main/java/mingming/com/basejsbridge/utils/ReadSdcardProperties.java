package mingming.com.basejsbridge.utils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 获取本地资源文件
 * @author lenvov
 */
public class ReadSdcardProperties {

	String fileName;

	/**
	 * 初始化
	 * 
	 * @param fileName
	 *            资源文件名称
	 */
	public ReadSdcardProperties(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 根据姓名读取配置文件
	 * 
	 * @return
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
//			p.load(fileInputStream);
			InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
			p.load(isr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
}
