package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.Constant;
import org.simpleframework.util.PropsUtil;
import org.simpleframework.util.StringUtil;

import java.util.Properties;

/**
 * simpleframework 配置助手类
 * Created by Why on 2017/3/9.
 */
public final class ConfigHelper {
    private static final Properties PROPS;

    static {
        PROPS = PropsUtil.load(Constant.CONFIG_FILE);
    }

    /**
     * 获取 jdbc driver
     */
    public static String getJdbcDriver() {
        return PropsUtil.getString(PROPS, Constant.JDBC_DRIVER);
    }

    /**
     * 获取 jdbc url
     */
    public static String getJdbcUrl() {
        return PropsUtil.getString(PROPS, Constant.JDBC_URL);
    }

    /**
     * 获取 jdbc user
     */
    public static String getJdbcUser() {
        return PropsUtil.getString(PROPS, Constant.JDBC_USER);
    }

    /**
     * 获取 jdbc password
     */
    public static String getJdbcPassword() {
        return PropsUtil.getString(PROPS, Constant.JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage() {
        return PropsUtil.getString(PROPS, Constant.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用 JSP 根路径，默认 /WEB-INF/view
     */
    public static String getAppJspPath() {
        return PropsUtil.getString(PROPS, Constant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取应用静态资源根路径，默认 /asset/
     */
    public static String getAppAssetPath() {
        return PropsUtil.getString(PROPS, Constant.APP_ASSET_PATH, "/asset/");
    }

    /**
     * 获取应用编码，默认 UTF-8
     */
    public static String getAppEncoding() {
        return PropsUtil.getString(PROPS, Constant.APP_ENCODING, "UTF-8");
    }

    /**
     * 获取应用上传文件大小限制，默认最大 10MB
     */
    public static int getAppUploadMaxSizeLimit() {
        return PropsUtil.getInt(PROPS, Constant.APP_UPLOAD_MAX_SIZE_LIMIT, 10);
    }

    /**
     * 获取应用允许上传文件的后缀名，多个后缀名用，隔开
     */
    public static String getAppUploadAllowExt() {
        return PropsUtil.getString(PROPS, Constant.APP_UPLOAD_ALLOW_EXT);
    }

    /**
     * 根据 key 值获取相关类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(PROPS, key);
    }

    /**
     * 根据 key 值获取相关类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(PROPS, key);
    }

    /**
     * 根据 key 值获取相关类型的属性值，可指定默认值
     */
    public static String getString(String key, String defaultValue) {
        return PropsUtil.getString(PROPS, key, defaultValue);
    }

    /**
     * 根据 key 值获取相关类型的属性值，可指定默认值
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PropsUtil.getBoolean(PROPS, key, defaultValue);
    }

    /**
     * 获取代理切面类名
     */
    public static String[] getAspectProxyClasses() {
        String classes = PropsUtil.getString(PROPS, Constant.ASPECT_PROXY_CLASSES);
        return StringUtil.splitString(classes, StringUtil.SEPARATOR);
    }
}
