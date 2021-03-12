package com.lmk.ct.utils;

import java.io.File;
import java.util.*;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import com.lmk.ct.bean.*;
import com.lmk.ct.config.AppConfig;
import com.lmk.ct.config.CodeConfig;

/**
 * 代码工具类
 * @author LaoMake
 * @version 1.0
 */
public class CodeUtils {

	/** 日志记录器 */
	private static Logger log = LoggerFactory.getLogger(CodeUtils.class);
	
	/** 安全的随机数生成器 */
	private static SecureRandom random = new SecureRandom();

	/** 生成一对多映射 */
	public static Boolean parseTablePrefix = true;

	/** 生成多对一映射 */
	public static Boolean buildManyToOne = false;

	/** 生成多对多映射 */
	public static Boolean buildManyToMany = true;

	/** 生成一对多映射 */
	public static Boolean buildOneToMany = false;

	/**
	 * 查询所有实体名
	 * @param dataBaseName
	 * @return
	 */
	public static Map<String, String> getTableNames(final String dataBaseName){
		log.info("查询表结构...");
		Map<String, String> tableNames = new LinkedHashMap<>();
		
		Connection conn = JdbcUtils.getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(CodeConfig.SQL_TABLE_NAMES);
				if(ps != null){
					ps.setString(1, dataBaseName);
					rs = ps.executeQuery();
					String tableName, entityComment;
					while(rs.next()){
						tableName = rs.getString("table_name");
						entityComment = rs.getString("table_comment");
						if(entityComment == null)
							entityComment = "";//避免FreeMarker报null错误
						if(checkRealEntity(dataBaseName, tableName))
							tableNames.put(tableName, entityComment);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtils.close(rs, ps, conn);
			}
		}
		
		return tableNames;
	}
	
	/**
	 * 解析实体类
	 * @param tableName
	 * @param tableComment
	 * @param modules
	 * @return
	 */
	public static Entity parseEntity(String tableName, String tableComment, Set<String> modules) {
		log.info("分析：{}", tableName);
		Entity entity = new Entity();
		entity.setSerialVersionUID(String.valueOf(random.nextLong()));
		entity.setTableName(tableName);
		entity.setEntityComment(tableComment);
		
		if(parseTablePrefix)
			entity.setModuleName(StringUtils.substringBefore(tableName, "_"));
		else
			entity.setModuleName("");
		
		modules.add(entity.getModuleName());
		String entityName = convertToEntityName(tableName, parseTablePrefix);
		entity.setEntityName(entityName);
		entity.setEntityNameLower(Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1));
		
		List<DbField> fields = getFields(tableName);
		entity.setFields(fields);
		
		//判断是否有日期类型
		boolean hasDateType = false;
		for(DbField field : fields){
			if(field.getFieldDataType() == FieldDataType.Date){
				hasDateType = true;
				break;
			}
		}
		
		//设置主键
		for(DbField field : fields){
			if(field.getKeyType() == KeyType.PrimaryKey){
				entity.setPrimaryKey(field);
				fields.remove(field);
				break;
			}
		}
		
		entity.setHasDateType(hasDateType);
		
		return entity;
	}
	
	/**
	 * 解析外键关联
	 * @param entitys
	 * @param dataBaseName
	 * @return
	 */
	public static void parseForeignKey(List<Entity> entitys, String dataBaseName){
		log.info("关联所有外键...");
		Map<String, Entity> entityMap = new HashMap<String, Entity>();
		for(Entity entity : entitys)
			entityMap.put(entity.getTableName(), entity);
		
		List<ForeignKey> foreignKeys = new ArrayList<>();
		Connection conn = JdbcUtils.getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(CodeConfig.SQL_ALL_FOREIGN_KEY);
				if(ps != null){
					ps.setString(1, dataBaseName);
					rs = ps.executeQuery();
					while(rs.next()){
						foreignKeys.add(new ForeignKey(
								rs.getString("source_column"), 
								rs.getString("source_table"), 
								rs.getString("target_table")));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtils.close(rs, ps, conn);
			}
			
			Entity sourceEntity;
			Entity targetEntity;
			ManyToOne mto = null;
			Set<String> mtmTableSet = new HashSet<String>();
			for(ForeignKey foreignKey : foreignKeys){
				sourceEntity = entityMap.get(foreignKey.getSourceTableName());
				if(sourceEntity != null){//排除多对多关联
					targetEntity = entityMap.get(foreignKey.getTargetTableName());
					if(targetEntity != null){
						//生成一对多
						if(buildOneToMany){
							targetEntity.getChildren().add(sourceEntity);
							targetEntity.getChildMap().put(sourceEntity.getEntityName(), foreignKey.getColumnName());
						}
						
						//生成多对一
						if(buildManyToOne){
							mto = new ManyToOne(targetEntity.getTableName(), foreignKey.getColumnName(), targetEntity.getModuleName(), targetEntity.getEntityName(), targetEntity.getEntityNameLower());
							mto.setEntityComment(targetEntity.getEntityComment());
							sourceEntity.getManyToOnes().add(mto);
						}
					}
				}else
					mtmTableSet.add(foreignKey.getSourceTableName());
			}
			
			//生成多对多
			if(buildManyToMany){
				Iterator<String> iterator = mtmTableSet.iterator();
				while (iterator.hasNext())
					setManyToMany(entityMap, dataBaseName, iterator.next());
			}
		}
	}

	/**
	 * 生成代码
	 * @author LaoMake
	 * @since 1.0
	 * @param entity	实体
	 * @param data	基础信息
	 */
	public static void makeFile(Entity entity, Map<String, Object> data){
		String entityName = entity.getEntityName();
		data.put("entityName", entityName);
		data.put("entityNameLower", Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1));

		//生成Java代码
		String javaFolderPath = AppConfig.javaRootFolder + File.separator + entity.getModuleName() + File.separator;
		String entityFile = javaFolderPath + CodeConfig.SOURCE_NAME_ENTITY + File.separator + entityName + ".java";
		String daoFile = javaFolderPath + CodeConfig.SOURCE_NAME_DAO + File.separator + entityName + "Dao.java";
		String serviceFile = javaFolderPath + CodeConfig.SOURCE_NAME_SERVICE + File.separator + entityName + "Service.java";
		String serviceImplFile = javaFolderPath + CodeConfig.SOURCE_NAME_SERVICE + File.separator + "impl" + File.separator + entityName + "ServiceImpl.java";
		String controllerFile = javaFolderPath + CodeConfig.SOURCE_NAME_WEB + File.separator + entityName + "Controller.java";

		FreeMarkerUtils.makeFile(data, "entity.ftl", entityFile);
		FreeMarkerUtils.makeFile(data, "dao.ftl", daoFile);
		FreeMarkerUtils.makeFile(data, "service.ftl", serviceFile);
		FreeMarkerUtils.makeFile(data, "serviceImpl.ftl", serviceImplFile);
		FreeMarkerUtils.makeFile(data, "controller.ftl", controllerFile);

		//生成Mapper
		String mapperFolderPath = AppConfig.mapperRootFolder + File.separator + entity.getModuleName() + File.separator;
		FileUtils.makeSureFolderExits(mapperFolderPath);
		FreeMarkerUtils.makeFile(data, "mapper.ftl", mapperFolderPath + entity.getEntityNameLower() + "Dao.xml");

		log.info("输出：{}", entity.getModuleName() + "." +entityName);
	}
	
	/**
	 * 设置多对多关联
	 * @param entityMap
	 * @param joinTableName
	 */
	private static void setManyToMany(Map<String, Entity> entityMap, String dataBaseName, String joinTableName){
		
		String[][] tableArray = new String[2][2];
		Connection conn = JdbcUtils.getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(CodeConfig.SQL_MANY_TO_MANY);
				if(ps != null){
					ps.setString(1, dataBaseName);
					ps.setString(2, joinTableName);
					rs = ps.executeQuery();
					int i = 0;
					while(rs.next()){
						tableArray[i][0] = rs.getString("table_name");
						tableArray[i][1] = rs.getString("column_name");
						i++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtils.close(rs, ps, conn);
			}
			
			ManyToMany mtm;
			Entity targetEntity;
			Entity sourceEntity = entityMap.get(tableArray[0][0]);
			if(sourceEntity != null){
				targetEntity = entityMap.get(tableArray[1][0]);
				if(targetEntity != null){
					//开始正向关联
					mtm = new ManyToMany(
							dataBaseName,
							joinTableName,
							targetEntity.getTableName(),
							targetEntity.getModuleName(),
							targetEntity.getEntityName(),
							targetEntity.getEntityNameLower(),
							targetEntity.getEntityComment(),
							tableArray[0][1], 
							tableArray[1][1]);
					sourceEntity.getMtms().add(mtm);
					
					//开始反向关联
					mtm = new ManyToMany(
							dataBaseName,
							joinTableName,
							sourceEntity.getTableName(),
							sourceEntity.getModuleName(),
							sourceEntity.getEntityName(),
							sourceEntity.getEntityNameLower(),
							sourceEntity.getEntityComment(),
							tableArray[1][1], 
							tableArray[0][1]);
					targetEntity.getMtms().add(mtm);
				}
			}
		}
		
	}

	/**
	 * 检查表是否为实体映射表，排除多对多的关联表
	 * @param dataBaseName
	 * @param tableName
	 * @return
	 */
	private static boolean checkRealEntity(String dataBaseName, String tableName){
		long count = JdbcUtils.queryLong(CodeConfig.SQL_COUNT_PRIMARY_KEY, dataBaseName, tableName);
		return count == 1l;
	}
	
	/**
	 * 表名转换成实体名
	 * @param fullTableName
	 * @param parseTablePrefix
	 * @return
	 */
	private static String convertToEntityName(String fullTableName, Boolean parseTablePrefix){
		String tableName = fullTableName;
		if(parseTablePrefix)
			tableName = StringUtils.substringAfter(fullTableName, "_");
		
		StringBuffer entityName = new StringBuffer();
		String[] names = tableName.split("_");
		for(String name : names){
			entityName.append(Character.toUpperCase(name.charAt(0)) + name.substring(1));
		}
		
		return entityName.toString();
	}
	
	/**
	 * 表名转换成实体名
	 * @param fieldName
	 * @return
	 */
	private static String convertToFieldName(String fieldName){
		StringBuffer entityName = new StringBuffer();
		String[] names = fieldName.split("_");
		
		boolean firstName = true;
		for(String name : names){
			if(firstName){
				entityName.append(name);
				firstName = false;
			}else{
				entityName.append(Character.toUpperCase(name.charAt(0)) + name.substring(1));
			}
		}
		
		return entityName.toString();
	}
	
	/**
	 * 获取实体的属性
	 * 注意：对于唯一索引，需要将其Index_type设置为UNIQUE
	 * @param tableName
	 * @return
	 */
	private static List<DbField> getFields(String tableName){
		List<DbField> fields = new ArrayList<DbField>();
		DbField field = null;
				
		Connection conn = JdbcUtils.getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(CodeConfig.SQL_TABLE_FIELDS + tableName);
				if(ps != null){
					rs = ps.executeQuery();
					while(rs.next()){
						String str = rs.getString("Field");
						field = new DbField();
						field.setTableFieldName(str);
						String filedName = convertToFieldName(str);
						field.setFieldName(filedName);
						field.setFieldNameFirstUpper(Character.toUpperCase(filedName.charAt(0)) + filedName.substring(1));
						
						str = rs.getString("Type");
						if (str.contains("varchar")) {
							field.setFieldDataType(FieldDataType.String);
							field.setLength(Integer.valueOf(StringUtils.substringBetween(str, "(", ")")));
						} else if (str.contains("text")) {
							field.setFieldDataType(FieldDataType.String);
							field.setLength(65535);
						} else if ("bigint".equals(str) || str.contains("bigint(20)")) {
							field.setFieldDataType(FieldDataType.Long);
						} else if ("int".equals(str) || str.contains("int(11)")) {
							field.setFieldDataType(FieldDataType.Integer);
						} else if (str.contains("smallint(6)")) {
							field.setFieldDataType(FieldDataType.Short);
						} else if (str.contains("tinyint(4)")) {
							field.setFieldDataType(FieldDataType.Byte);
						} else if (str.contains("tinyint(1)")) {
							field.setFieldDataType(FieldDataType.Boolean);
						} else if (str.contains("double")) {
							field.setFieldDataType(FieldDataType.Double);
						} else if (str.contains("decimal")) {
							field.setFieldDataType(FieldDataType.Double);
						} else if (str.contains("float")) {
							field.setFieldDataType(FieldDataType.Float);
						} else if (str.equals("datetime") || str.contains("timestamp")) {
							field.setFieldDataType(FieldDataType.Date);
							field.setLength(19);
						}else if (str.equals("date")) {
							field.setFieldDataType(FieldDataType.Date);
							field.setLength(10);
						}else if (str.equals("time")) {
							field.setFieldDataType(FieldDataType.Date);
							field.setLength(8);
						}
						
						str = rs.getString("Null");
						field.setNotNull(str.equals("NO"));
						
						field.setDefaultValue(rs.getString("Default"));
						field.setComment(rs.getString("Comment"));
						
						str = rs.getString("Key");
						if(StringUtils.isBlank(str)){
							field.setKeyType(KeyType.NotKey);
							fields.add(field);
						}else{
							switch (str){
								case "UNI":
									// 注意：通过PowerDesigner生成SQL时，需要将候补键（Alternate Key）的创建方式改为：Outside
									// 否则生成的唯一索引键的类型将为 MUL，将会与外键类型混淆，导致字段解析失误
									field.setKeyType(KeyType.UniqueKey);
									fields.add(field);
									break;
								case "PRI":
									field.setKeyType(KeyType.PrimaryKey);
									fields.add(field);
									break;
								case "MUL":
									field.setKeyType(KeyType.Foreignkey);
									break;
								default:
									field.setKeyType(KeyType.NotKey);
									fields.add(field);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JdbcUtils.close(rs, ps, conn);
			}
		}
		
		return fields;
	}
}
