package com.lmk.ct.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Property工具类
 * @author LaoMake
 * @since 1.0
 *
 */
public class PropertyUtils {
	
	private Properties properties;
	
	/**
	 * 加载配置文件
	 * @author LaoMake
	 * @since 1.0
	 * @param fileName 完整的文件路径
	 * @return
	 */
	public Properties load(String fileName) {
		if (StringUtils.isBlank(fileName))
			throw new IllegalArgumentException("配置文件不能为空");
		if (fileName.contains(".."))
			throw new IllegalArgumentException("文件名不能包含 \"..\"");
		
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			Properties p = new Properties();
			p.load(inputStream);
			properties = p;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("文件未找到: " + fileName);
		} catch (IOException e) {
			throw new IllegalArgumentException("文件加载失败: " + fileName);
		}
		finally {
			try {if (inputStream != null) inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
		if (properties == null)
			throw new RuntimeException("文件加载失败: " + fileName);
		return properties;
	}
	
	/**
	 * 获取文本型配置项
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		checkPropertyLoading();
		return properties.getProperty(key);
	}
	
	/**
	 * 获取文本型配置项并设置默认值
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		checkPropertyLoading();
		return properties.getProperty(key, defaultValue);
	}
	
	/**
	 * 获取整型配置项
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @return
	 */
	public Integer getInt(String key) {
		checkPropertyLoading();
		Integer resultInt = null;
		String resultStr = properties.getProperty(key);
		if (resultStr != null)
			resultInt =  Integer.valueOf(resultStr);
		return resultInt;
	}
	
	/**
	 * 获取整型配置项并设置默认值
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Integer getInt(String key, Integer defaultValue) {
		Integer result = getInt(key);
		return result != null ? result : defaultValue;
	}
	
	/**
	 * 获取浮点型配置项
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @return
	 */
	public Float getFloat(String key) {
		checkPropertyLoading();
		Float resultInt = null;
		String resultStr = properties.getProperty(key);
		if (resultStr != null)
			resultInt =  Float.valueOf(resultStr);
		return resultInt;
	}
	
	/**
	 * 获取浮点型配置项并设置默认值
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Float getFloat(String key, Float defaultValue) {
		Float result = getFloat(key);
		return result != null ? result : defaultValue;
	}
	
	/**
	 * 获取布尔型配置项
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		checkPropertyLoading();
		String resultStr = properties.getProperty(key);
		Boolean resultBool = null;
		if (resultStr != null) {
			if (resultStr.trim().equalsIgnoreCase("true"))
				resultBool = true;
			else if (resultStr.trim().equalsIgnoreCase("false"))
				resultBool = false;
		}
		return resultBool;
	}
	
	/**
	 * 获取布尔型配置项并设置默认值
	 * @author LaoMake
	 * @since 1.0
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolean(String key, Boolean defaultValue) {
		Boolean result = getBoolean(key);
		return result != null ? result : defaultValue;
	}
	
	/**
	 * 检查是否已加载配置文件
	 * @author LaoMake
	 * @since 1.0
	 */
	private void checkPropertyLoading() {
		if (properties == null)
			throw new RuntimeException("未加载配置文件");
	}
}
