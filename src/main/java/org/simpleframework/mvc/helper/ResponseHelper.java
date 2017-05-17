package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.bean.View;
import org.simpleframework.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * response 助手类
 * Created by Why on 2017/3/21.
 */
public final class ResponseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHelper.class);

    /**
     * 响应 JSON 数据
     */
    public static void writeJson(HttpServletResponse resp, String json) {
        try {
            resp.setContentType("application/json;charset=" + ConfigHelper.getAppEncoding());
            PrintWriter writer = resp.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            LOGGER.debug("response json failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 响应 404
     */
    public static void redirect404(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setStatus(404);
            resp.setContentType("text/html");
            PrintWriter writer = resp.getWriter();
            writer.write("<h3>404! " + req.getMethod().toLowerCase() + ":"
                    + req.getServletPath() + " NOT FOUND</h3>");
            writer.flush();
            writer.close();
            LOGGER.debug("404! " + req.getMethod().toLowerCase() + ":"
                    + req.getServletPath() + " NOT FOUND");
        } catch (IOException e) {
            LOGGER.debug("redirect 404 failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置响应编码
     */
    public static void setCharacterEncoding(HttpServletResponse resp) {
        LOGGER.debug("set response character encoding: " + ConfigHelper.getAppEncoding());
        resp.setCharacterEncoding(ConfigHelper.getAppEncoding());
    }

    /**
     * 请求转发
     */
    public static void forward(HttpServletRequest req, HttpServletResponse resp, View view) {
        try {
            String path = view.getPath();
            Map<String, Object> model = view.getModel();
            if (CollectionUtil.isNotEmpty(model)) {
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    req.setAttribute(key, value);
                }
            }
            req.getRequestDispatcher(path).forward(req, resp);
            LOGGER.debug("forward: " + ConfigHelper.getAppJspPath() + path);
        } catch (Exception e) {
            LOGGER.error("response forward failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 重定向
     */
    public static void redirect(HttpServletRequest req, HttpServletResponse resp, View view) {
        try {
            String path = view.getPath();
            resp.sendRedirect(path);
            LOGGER.debug("redirect: " + req.getContextPath() + path);
        } catch (IOException e) {
            LOGGER.error("response redirect failure", e);
            throw new RuntimeException(e);
        }
    }
}
