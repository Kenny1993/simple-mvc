package org.simpleframework.mvc;

/**
 * simple.properties 属性文件 key 常量
 * Created by Why on 2017/3/9.
 */
public interface Constant {
    /**
     * simpleframework 配置文件名
     */
    String CONFIG_FILE = "simple.properties";

    /**
     * jdbc driver
     */
    String JDBC_DRIVER = "simple.jdbc.driver";

    /**
     * jdbc url
     */
    String JDBC_URL = "simple.jdbc.url";

    /**
     * jdbc user
     */
    String JDBC_USER = "simple.jdbc.user";

    /**
     * jdbc password
     */
    String JDBC_PASSWORD = "simple.jdbc.password";

    /**
     * 应用的基础包名
     */
    String APP_BASE_PACKAGE = "simple.app.base.package";

    /**
     * 应用 JSP 根路径
     */
    String APP_JSP_PATH = "simple.app.jsp.path";

    /**
     * 应用静态资源根路径
     */
    String APP_ASSET_PATH = "simple.app.asset.path";

    /**
     * 应用编码
     */
    String APP_ENCODING = "simple.app.encoding";

    /**
     * 应用上传文件大小限制
     */
    String APP_UPLOAD_MAX_SIZE_LIMIT = "simple.app.upload.max.size.limit";

    /**
     * 应用允许上传文件的后缀名
     */
    String APP_UPLOAD_ALLOW_EXT = "simple.app.upload.allow.ext";

    /**
     * 代理切面类
     */
    String ASPECT_PROXY_CLASSES = "simple.aspect.proxy.classes";
}
