package mingming.com.basejsbridge.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * 获取项目内资源文件
 * @author lenvov
 */
public class ReadProperties {
	
	String fileName;
	
	/**
	 * 初始化
	 * @param fileName 资源文件名称
	 */
	public ReadProperties(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 根据姓名读取配置文件
	 * @return
	 */
	public Properties getProperties() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
}
