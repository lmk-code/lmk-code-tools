package com.lmk.ct.config;

import java.io.File;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lmk.ct.utils.*;

/**
 * 应用配置
 */
public class AppConfig {

    /** 日志记录器 */
    private static Logger log = LoggerFactory.getLogger(AppConfig.class);

    /** ClassPath类路径 */
    public static String ClassPathRoot;

    /** Java源码输出目录 */
    public static String javaRootFolder;

    /** Mapper文件输出目录 */
    public static String mapperRootFolder;

    /** 数据库名称 */
    public static String dataBase;

    /** 基础包名 */
    public static String packageName;

    /** 作者 */
    public static String author;

    /** 邮箱 */
    public static String email;

    /** 版本号 */
    public static String version;

    public static void init(){
        // 初始化类路径的根目录
        ClassPathRoot = FileUtils.getRootClassPath(AppConfig.class);

        log.info("加载配置文件...");
        PropertyUtils pu = new PropertyUtils();

        pu.load(ClassPathRoot + "/app.properties");//加载配置文件

        //初始化数据库配置
        dataBase = pu.getString("db.database");
        String user = pu.getString("db.user");
        String password = pu.getString("db.password");
        String host = pu.getString("db.host");
        String port = pu.getString("db.port");
        String url = String.format(CodeConfig.TEMPLATE_URL, host, port, dataBase);

        JdbcUtils.init(user, password, url, CodeConfig.DRIVER_CLASS_NAME);

        //初始化代码配置
        packageName = pu.getString("code.packageName");
        author = pu.getString("code.author");
        email = pu.getString("code.email");
        version = pu.getString("code.version");

        // 初始化代码生成规则
        CodeUtils.parseTablePrefix = pu.getBoolean("code.tablePrefix", true);
        CodeUtils.buildManyToOne = pu.getBoolean("code.manyToOne", false);
        CodeUtils.buildManyToMany = pu.getBoolean("code.manyToMany", true);
        CodeUtils.buildOneToMany = pu.getBoolean("code.oneToMany", false);
    }


    /**
     * 解析模块
     * @param modules
     */
    public static Map<String, Object> parseMoudle(Set<String> modules){
        // 初始化代码文件目录
        String[] packageNameItem = packageName.split("\\.");

        // Java源码目录
        StringBuffer sb = new StringBuffer()
                .append(ClassPathRoot).append(File.separator)
                .append(CodeConfig.SOURCE_NAME).append(File.separator)
                .append(CodeConfig.SOURCE_NAME_JAVA).append(File.separator);

        for(String nameItem : packageNameItem)
            sb.append(nameItem).append(File.separator);

        javaRootFolder = sb.toString();
        FileUtils.makeSureFolderExits(javaRootFolder);
        FileUtils.clearFolder(javaRootFolder);

        // 映射文件源码目录
        sb = new StringBuffer()
                .append(ClassPathRoot).append(File.separator)
                .append(CodeConfig.SOURCE_NAME).append(File.separator)
                .append(CodeConfig.SOURCE_NAME_MAPPER).append(File.separator);

        mapperRootFolder = sb.toString();
        FileUtils.makeSureFolderExits(mapperRootFolder);
        FileUtils.clearFolder(mapperRootFolder);

        // 初始化各子模块的源码目录
        String javaFolderPath;
        String entityFolder, daoFolder, serviceFolder, serviceImplFolder, webFolder;
        for(String moduleName : modules){

            javaFolderPath = javaRootFolder + moduleName + File.separator;
            entityFolder = javaFolderPath + CodeConfig.SOURCE_NAME_ENTITY + File.separator;
            daoFolder = javaFolderPath + CodeConfig.SOURCE_NAME_DAO + File.separator;
            serviceFolder = javaFolderPath + CodeConfig.SOURCE_NAME_SERVICE + File.separator;
            serviceImplFolder = serviceFolder + "impl" + File.separator;
            webFolder = javaFolderPath + CodeConfig.SOURCE_NAME_WEB + File.separator;

            FileUtils.makeSureFolderExits(entityFolder);
            FileUtils.makeSureFolderExits(daoFolder);
            FileUtils.makeSureFolderExits(serviceFolder);
            FileUtils.makeSureFolderExits(serviceImplFolder);
            FileUtils.makeSureFolderExits(webFolder);
        }

        // 构建代码注释等信息
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("author", author);
        data.put("email", email);
        data.put("version", version);
        data.put("date", DateUtils.format(new Date(), DateUtils.FORMAT_LONG));
        data.put("packageName", packageName);

        return data;
    }

}
