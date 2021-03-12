package com.lmk.ct;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmk.ct.bean.Entity;
import com.lmk.ct.utils.*;
import com.lmk.ct.config.AppConfig;

/**
 * 主程序
 *
 * @author LaoMake
 * @since 1.0
 *
 */
public class Main {

	/** 日志记录器 */
	private static Logger log = LoggerFactory.getLogger(Main.class);


	/**
	 * 主函数入口
	 * @param args
	 */
	public static void main(String[] args) {
		// 初始化配置
		AppConfig.init();

		//读取表名
		Map<String, String> tableMap = CodeUtils.getTableNames(AppConfig.dataBase);
		
		// 解析表结构
		List<Entity> entitys = new ArrayList<Entity>();
		Set<String> modules = new HashSet<String>();
		Set<String> tableNames = tableMap.keySet();
		for(String tableName : tableNames)
			entitys.add(CodeUtils.parseEntity(tableName, tableMap.get(tableName), modules));
		
		// 解析外键关联
		CodeUtils.parseForeignKey(entitys, AppConfig.dataBase);
		
		// 初始化所有模块
		Map<String, Object> data = AppConfig.parseMoudle(modules);

		// 输出代码文件
		for(Entity entity : entitys){
			data.put("entity", entity);
			CodeUtils.makeFile(entity, data);
		}
	}
}