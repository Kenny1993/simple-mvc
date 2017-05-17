package org.simpleframework.mvc;

import org.simpleframework.mvc.bean.Data;
import org.simpleframework.mvc.bean.DataList;
import org.simpleframework.mvc.bean.Handler;
import org.simpleframework.mvc.bean.Param;
import org.simpleframework.mvc.bean.View;
import org.simpleframework.mvc.helper.ControllerHelper;
import org.simpleframework.mvc.helper.RequestHelper;
import org.simpleframework.mvc.helper.ResponseHelper;
import org.simpleframework.mvc.helper.ServletHelper;
import org.simpleframework.mvc.helper.UploadHelper;
import org.simpleframework.util.CollectionUtil;
import org.simpleframework.util.JsonUtil;
import org.simpleframework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.simpleframework.mvc.helper.ConfigHelper;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 请求转发器
 * Created by Why on 2017/3/9.
 */
@WebServlet(value = "*.action", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init() throws ServletException {
        // 初始化相关 Helper 类
        HelperLoader.init();

        // 获取 ServletContext 对象 （用于注册 Servlet）
        ServletContext servletContext = getServletConfig().getServletContext();

        // 注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext
                .getServletRegistration("jsp");

        //初始化 JSP 编码
        jspServlet.setInitParameter("pageEncoding", ConfigHelper.getAppEncoding());
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 注册处理静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext
                .getServletRegistration("default");

        //初始化静态资源文件读取编码
        defaultServlet.setInitParameter("fileEncoding", ConfigHelper.getAppEncoding());
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置 req,resp 编码
        RequestHelper.setCharacterEncoding(req);
        ResponseHelper.setCharacterEncoding(resp);

        String reqMethod = req.getMethod().toLowerCase();
        String servletPath = req.getServletPath();
        String reqPath;
        int idx = servletPath.lastIndexOf("?");
        if (idx != -1) {
            reqPath = servletPath.substring(0, idx);
        } else {
            reqPath = servletPath;
        }

        Handler handler = ControllerHelper.getHandler(reqMethod, reqPath);

        if (handler == null) {
            handle404(req, resp);
            return;
        }

        // 初始化 Servlet Helper
        ServletHelper.init(req, resp);

        Param param;
        if (UploadHelper.isMultipart(req)) {
            param = UploadHelper.createParam(req);
            LOGGER.debug("field param: " + param.getFieldMap());
            LOGGER.debug("file param: " + param.getFileMap());
        } else {
            param = RequestHelper.createParam(req);
            LOGGER.debug("field param: " + param.getFieldMap());
        }

        Object controllerBean = handler.getControllerBean();
        Method method = handler.getMethod();
        List<Class<?>> classList = Arrays.asList(method.getParameterTypes());
        Object result;

        if (CollectionUtil.isEmpty(classList)) {
            result = ReflectionUtil.invokeMethod(controllerBean, method);
        } else if (classList.contains(Param.class)) {
            result = ReflectionUtil.invokeMethod(controllerBean, method, param);
        } else {
            LOGGER.debug(controllerBean.getClass().getName() + " -> " + method.getName() + ": args nether Null nor Param");
            throw new RuntimeException("args nether Null nor Param");
        }

        if (result instanceof View) {
            handleViewResult((View) result, req, resp);
        } else if (result instanceof Data) {
            handleDataResult((Data) result, resp);
        } else if (result instanceof DataList) {
            handleDataListResult((DataList) result, resp);
        }
    }

    private void handleDataListResult(DataList dataList, HttpServletResponse resp) {
        List<Serializable> modelList = dataList.getModel();
        String json = JsonUtil.toJson(modelList);
        LOGGER.debug("response json: " + json);
        ResponseHelper.writeJson(resp, json);
    }

    private void handleDataResult(Data data, HttpServletResponse resp) {
        Serializable model = data.getModel();
        String json = JsonUtil.toJson(model);
        LOGGER.debug("response json: " + json);
        ResponseHelper.writeJson(resp, json);
    }

    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = view.getPath();
        if (path.startsWith("/")) {
            ResponseHelper.redirect(req, resp, view);
            LOGGER.debug("redirect: " + path);
        } else {
            ResponseHelper.forward(req, resp, view);
            LOGGER.debug("forward: " + path);
        }
    }

    private void handle404(HttpServletRequest request,
                           HttpServletResponse response) {
        ResponseHelper.redirect404(request, response);
        LOGGER.debug("redirect 404");
    }
}
