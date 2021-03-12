package com.lmk.ct.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Locale;
import java.util.Map;

/**
 * Freemaker工具
 * @author LaoMake
 * @since 1.0
 *
 */
public class FreeMarkerUtils {
	private static Configuration freeMakerConfig;
	
	/**
	 * 获取Freemaker配置
	 * 已设置默认编码和模板文件存放的根目录
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Configuration getFreeMakerConfig() {
		if(freeMakerConfig == null){
			try {
				freeMakerConfig = new Configuration();
				freeMakerConfig.setDirectoryForTemplateLoading(
												new File(new StringBuffer()
																.append(FileUtils.getRootClassPath(FreeMarkerUtils.class)).append(File.separator)
																.append(File.separator).append("template").append(File.separator)
																.toString()));  
				freeMakerConfig.setEncoding(Locale.CHINA, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return freeMakerConfig;
	}
	
	public static void makeFile(Map<String, Object> data, String templateFilePath, String targetFilePath){
		
		Writer writer = null;
		try {
			Template template = getFreeMakerConfig().getTemplate(templateFilePath);
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath), "UTF-8"));
			template.process(data, writer);
			writer.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if(writer != null){
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
