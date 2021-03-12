package com.lmk.ct.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;

/**
 * JDBC工具类
 * @author LaoMake
 * @since 1.0
 *
 */
public class JdbcUtils {

	/** 日志记录器 */
	private static Logger log = LoggerFactory.getLogger(JdbcUtils.class);

	/**
	 * 所支持的数据库类型
	 * 目前仅支持MySQL、MariaDB
	 * @author LaoMake
	 * @since 1.0
	 *
	 */
	public static enum DbType{
		MySQL, MariaDB, Oracle, SQLServer
	}

	/** 数据库驱动 */
	private static String DRIVER_CLASS_NAME;

	/** 用户名 */
	private static String USER;
	
	/** 密码 */
	private static String PASSWORD;

	/** 连接地址 */
	private static String URL;
	
	/** 是否已初始化配置 */
	private static boolean hasInit = false;
	
	/**
	 * 简化版初始化方法，默认为MySQL数据库
	 * @author LaoMake
	 * @since 1.0
	 * @param user
	 * @param password
	 * @param url
	 * @param driverClassName
	 */
	public static void init(String user, String password, String url, String driverClassName){
		init(user, password, url, driverClassName, DbType.MySQL);
	}

	/**
	 * 完整版初始化方法
	 * @author LaoMake
	 * @since 1.0
	 * @param user
	 * @param password
	 * @param url
	 * @param driverClassName
	 * @param dbType
	 */
	public static void init(String user, String password, String url, String driverClassName, DbType dbType){
		if(StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(url) && StringUtils.isNotBlank(driverClassName) && (dbType != null)){
			USER = user;
			PASSWORD = password;
			URL = url;
			DRIVER_CLASS_NAME = driverClassName;
			try {
				Class.forName(driverClassName);
				hasInit = true;
				log.info("【JdbcUtils】初始化成功");
			} catch (ClassNotFoundException e) {
				log.error("【JdbcUtils】初始化失败：无法加载驱动({})", driverClassName);
			}
		}else{
			log.error("【JdbcUtils】初始化失败：参数错误\nuser：{}\npassword：{}\nurl：{}\ndriverClassName：{}\ndbType：{}", user, password, url, driverClassName, dbType);
		}
	}
	
	/**
	 * 获取数据库链接
	 * @author LaoMake
	 * @since 1.0
	 * @return
	 */
	public static Connection getConn(){
		Connection conn = null;
		if(hasInit){
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (SQLException e) {
				log.error("【JdbcUtils】获取连接失败\nurl：{}\nuser：{}\npassword：{}", URL, USER, PASSWORD);
				e.printStackTrace();
			}
		}else{
			log.error("【JdbcUtils】获取连接失败：未初始化，请调用init()方法");
		}
		return conn;
	}

	/**
	 * 关闭结果集
	 * @param rs
	 */
	public static void close(ResultSet rs){
		try {
			if(rs != null){
				rs.close();
				rs = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭操作语句
	 * @param stmt
	 */
	public static void close(Statement stmt){
		try {
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭链接
	 * @param conn
	 */
	public static void close(Connection conn){
		try {
			if(conn != null){
				conn.close();
				conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放数据库资源
	 * @author LaoMake
	 * @since 1.0
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn){
		close(rs);
		close(stmt);
		close(conn);
	}

	/**
	 * 普通查询
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 * @return
	 */
	public static List<Object> query(String sql, String... paramters){
		List<Object> result = new ArrayList<Object>();
		
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					while(rs.next()){
						result.add(rs.getObject(1));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		
		return result;
	}
	
	/**
	 * 执行查询，返回单个String，如果有多个返回值
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static String queryString(String sql, String... paramters){
		String result = null;
		
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getString(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}", sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		
		return result;
	}
	
	/**
	 * 执行查询，返回单个Integer
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static Integer queryInt(String sql, String... paramters){
		Integer result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getInt(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个Long
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static Long queryLong(String sql, String... paramters){
		Long result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getLong(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个Float
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static Float queryFloat(String sql, String... paramters){
		Float result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getFloat(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个Double
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static Double queryDouble(String sql, String... paramters){
		Double result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getDouble(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个BigDecimal
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static BigDecimal queryBigDecimal(String sql, String... paramters){
		BigDecimal result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = rs.getBigDecimal(1);
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个Date
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 */
	public static Date queryDate(String sql, String... paramters){
		Date result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					int total = 0;
					while(rs.next()){
						result = new Date(rs.getTimestamp(1).getTime());
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个Map
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 * @return
	 */
	public static Map<String, Object> queryMap(String sql, String... paramters){
		Map<String, Object> result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					rsmd = rs.getMetaData();
					int columns = rsmd.getColumnCount();
					
					int total = 0;
					while(rs.next()){
						result = new LinkedHashMap<String, Object>();
						for(int i = 1; i <= columns; i++){
							result.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						total++;
					}
					if(total != 1){
						result = null;
						log.error("【JdbcUtils】查询失败：返回多个结果\nsql：{}\nparameters：{}" ,sql, Arrays.toString(paramters));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 执行查询，返回单个多个Map
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 * @return
	 */
	public static List<Map<String, Object>> queryMaps(String sql, String... paramters){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			ResultSetMetaData rsmd = null;
			Map<String, Object> item;
			try {
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rs = ps.executeQuery();
					rsmd = rs.getMetaData();
					int columns = rsmd.getColumnCount();
					
					while(rs.next()){
						item = new LinkedHashMap<String, Object>();
						for(int i = 1; i <= columns; i++){
							item.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						result.add(item);
					}
					rsmd = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
	
	/**
	 * 插入记录，返回主键值
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 * @return	主键值
	 */
	public static Object insert(String sql, String... paramters){
		Object pk = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					ps.executeUpdate();
					conn.commit();
					
					rs = ps.getGeneratedKeys();
					while(rs.next()){
						pk = rs.getObject(1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(conn != null){
						conn.rollback();
						pk = null;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				close(rs, ps, conn);
			}
		}
		return pk;
	}
	
	/**
	 * 执行语句，默认开启事务
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @param paramters
	 * @return 受影响记录条数
	 */
	public static int execute(String sql, String... paramters){
		int rows = 0;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn.setAutoCommit(false);
				
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(int i = 0; i < paramters.length; i++){
						ps.setString(i + 1, paramters[i]);
					}
					rows = ps.executeUpdate();
					conn.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(conn != null){
						conn.rollback();
						rows = 0;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				close(rs, ps, conn);
			}
		}
		return rows;
	}
	
	/**
	 * 批量执行语句，默认开启事务
	 * @author LaoMake
	 * @since 1.0
	 * @param sql
	 * @return 受影响记录条数
	 */
	public static int[] executeBatch(String sql, List<String[]> paramterList){
		int[] result = null;
		Connection conn = getConn();
		if(conn != null){
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sql);
				if(ps != null){
					for(String[] paramters : paramterList){
						for(int i = 0; i < paramters.length; i++){
							ps.setString(i + 1, paramters[i]);
						}
						ps.addBatch(); 
					}
					result = ps.executeBatch();
					conn.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if(conn != null){
						conn.rollback();
						result = null;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				close(rs, ps, conn);
			}
		}
		return result;
	}
}
