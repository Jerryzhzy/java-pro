package com.common.utils;

import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @ClassName:     ConfigUtil.java
 * @Description:   (加载 properties file) 
 * @version     V2.0
 * @Date        2015年8月14日 下午1:52:27 
 */
public class ConfigUtil {
	private static String CONFIG_FILE = "config.properties";
	private static String BUNDLE_NAME = "config";
	private static PropertiesConfiguration  config = null;
	static{
		try{
			//config = new PropertiesConfiguration("app.properties");
			//config.setEncoding("UTF-8");
			//config.setHeader(header)
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(CONFIG_FILE);
			//config.load("application-url.properties");
			
		}catch(Exception ex){
		}
	}
	
	
	public static int getIntValue(String key){
		int reInt = 1;
		try{
			reInt = config.getInt(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reInt = 0;
		}
		return reInt;
	}	
	
	public static Long getLongValue(String key) {
		Long reLong = 1l;
		try{
			reLong = config.getLong(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reLong = 0l;
		}
		return reLong;
	}
	
	
	
	public static double getDoubleValue(String key){
		double reDouble = 1.0;
		try{
			reDouble = config.getDouble(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			reDouble =1.0;
		}
		return reDouble;
	}
	
	public static String getStringValue(String key){
		String str = "";
		try{
			str = config.getString(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
			str = "";
		}
		return str;
	}
	
	public static Boolean getBooleanValue(String key) {
		Boolean flag = false;
		try{
			flag = config.getBoolean(key);
		}catch(Exception ex){
			ex.fillInStackTrace();
		}
		return flag;
	}
	
	public static void save(String key,Object o){
		config.setProperty(key, o);
		try{
			config.save(CONFIG_FILE);
			config = new PropertiesConfiguration();
			config.setEncoding("UTF-8");
			config.load(CONFIG_FILE);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ConfigUtil.save("jedis.pool.maxTotal", "3000");
		System.out.println(ConfigUtil.getStringValue("pdf2swfPath_windows"));
		System.out.println(ConfigUtil.getStringValue("jedis.pool.maxTotal"));
		System.out.println(ConfigUtil.getStringValue("hibernate.hbm2ddl.auto"));
		System.out.println(ConfigUtil.getStringValue("pdf2swfPath"));
	}
	
	// 读取配置文件
	private static ResourceBundle cache = null;

	static {
		try {
			cache = ResourceBundle.getBundle(BUNDLE_NAME);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能描述：获取配置文件参数值
	 * 
	 * @param str(参数KEY值)
	 * @return
	 */
	public static String getValue(String str) {
		String s = cache.getString(str);
		try {
			s = new String(s.getBytes("ISO8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 功能描述：获取指定配置文件参数的值
	 * 
	 * @param strPropertiesFile(配置文件名称)
	 * @param strItem(参数名称)
	 * @return
	 */
	public String getPropertiesValue(String strPropertiesFile, String strItem) {
		String strItemValue = "";
		ResourceBundle resources1 = null;
		try {
			resources1 = ResourceBundle.getBundle(strPropertiesFile);
			strItemValue = resources1.getString(strItem);
		} catch (MissingResourceException e) {
			System.out.println("ConfigInfo.getPropertiesValue error:"
					+ e.getMessage());
		}
		return strItemValue;
	}

}