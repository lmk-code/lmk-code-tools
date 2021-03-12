package com.lmk.ct.config;

/**
 * 代码生成配置
 */
public class CodeConfig {

    /** 数据库驱动名称 */
    public static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /** 数据库链接地址模板 */
    public static final String TEMPLATE_URL = "jdbc:mysql://%s:%s/%s?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8";

    /** 查询所有表 */
    public static final String SQL_TABLE_NAMES = "SELECT table_name, table_comment FROM information_schema.tables WHERE table_schema = ?";

    /** 统计主键的数目 */
    public static final String SQL_COUNT_PRIMARY_KEY = "SELECT COUNT(k.column_name) FROM information_schema.key_column_usage k WHERE k.constraint_name = 'PRIMARY' AND k.table_schema = ? AND k.table_name = ?";

    /** 查询表的所有字段 */
    public static final String SQL_TABLE_FIELDS = "SHOW FULL FIELDS FROM ";

    /** 查询所有外键关联 */
    public static final String SQL_ALL_FOREIGN_KEY = "SELECT table_name AS source_table, column_name AS source_column, referenced_table_name AS target_table, referenced_column_name AS target_column FROM information_schema.key_column_usage WHERE referenced_table_name IS NOT NULL AND table_schema = ?";

    /** 查询多对多中间表的Key */
    public static final String SQL_MANY_TO_MANY = "SELECT DISTINCT k.REFERENCED_TABLE_NAME AS table_name, k.COLUMN_NAME AS column_name FROM information_schema.KEY_COLUMN_USAGE k WHERE k.CONSTRAINT_SCHEMA = ? AND k.REFERENCED_TABLE_NAME IS NOT NULL AND k.TABLE_NAME = ?";

    /** 源码目录名称 */
    public static final String SOURCE_NAME = "sourceCodes";

    /** Java源码目录名称 */
    public static final String SOURCE_NAME_JAVA = "java";

    /** 控制器层源码目录名称 */
    public static final String SOURCE_NAME_WEB = "web";

    /** 服务层源码目录名称 */
    public static final String SOURCE_NAME_SERVICE = "service";

    /** 数据访问层源码目录名称 */
    public static final String SOURCE_NAME_DAO = "dao";

    /** 实体类源码目录名称 */
    public static final String SOURCE_NAME_ENTITY = "entity";

    /** MyBatis映射文件目录名称 */
    public static final String SOURCE_NAME_MAPPER = "mapper";
}
