package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.annotation.Action;
import org.simpleframework.mvc.annotation.Controller;
import org.simpleframework.mvc.bean.Handler;
import org.simpleframework.mvc.bean.Request;
import org.simpleframework.util.ArrayUtil;
import org.simpleframework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Controller 助手类
 * Created by Why on 2017/3/9.
 */
public final class ControllerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerHelper.class);

    private static final Map<Request, Handler> ACTION_HANDLER_MAP;

    private static final String ACTION_SUFFIX = ".action";

    /**
     *  为所有 Request 初始化 Hander
     */
    static {
        LOGGER.debug("controller is mapping...");
        ACTION_HANDLER_MAP = new HashMap<Request, Handler>();

        Set<Class<?>> classSet = ClassHelper.getControllerClassSet();
        for (Class<?> cls : classSet) {
            Controller controller = cls.getAnnotation(Controller.class);
            String url = controller.url();
            if (!url.startsWith("/")) {
                LOGGER.debug("url must be start with \"/\": " + cls.getName() + " -> " + url);
                throw new RuntimeException("url must be start with \"/\": " + url);
            }

            Object controllerBean;
            Method[] methods = cls.getDeclaredMethods();

            if (ArrayUtil.isNotEmpty(methods)) {
                controllerBean = BeanHelper.getBean(cls);
                for (Method met : methods) {
                    if (met.isAnnotationPresent(Action.class)) {
                        doPutActionHandler(met, url, controllerBean);
                    }
                }
            }
        }
    }

    public static Handler getHandler(String reqMethod, String reqPath) {
        Request request = new Request(reqMethod, reqPath);
        if (!ACTION_HANDLER_MAP.containsKey(request)) {
            return null;
        }

        return ACTION_HANDLER_MAP.get(request);
    }

    private static void doPutActionHandler(Method met, String url, Object controllerBean) {
        Action action = met.getAnnotation(Action.class);
        String method = action.method().toLowerCase();
        String path = action.path();
        if (!path.startsWith("/")) {
            LOGGER.debug("path must be start with \"/\": " + met.getName() + " -> " + path);
            throw new RuntimeException("path must be start with \"/\": " + path);
        }
        path = url + path + ACTION_SUFFIX;
        Request request = new Request(method, path);
        Handler handler = new Handler(controllerBean, met);
        ACTION_HANDLER_MAP.put(request, handler);
        LOGGER.debug("class -> " + controllerBean.getClass().getName() + " method - > " + met.getName() + " is mapping to " + path);
    }
}
