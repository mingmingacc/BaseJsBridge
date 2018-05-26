package mingming.com.basejsbridge.plugin.manager;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import mingming.com.basejsbridge.application.BaseApplication;
import mingming.com.basejsbridge.utils.LogUtil;
import mingming.com.basejsbridge.utils.ReadSdcardProperties;

public class ResolverProperties  {
	private static final String TAG = ResolverProperties.class.getSimpleName(); 
	
	public static void ResolverPropertiesFlies(String name) {
		ReadSdcardProperties properties = new ReadSdcardProperties(name);
		Properties p = properties.getProperties();
		Set keys = p.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			try {
				BaseApplication.setGlobal(key, p.getProperty(key));
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
	}
}
