package org.simpleframework.mvc.bean;

import java.lang.reflect.Method;

/**
 * 封装 Controller 中带有 Action 注解的方法
 * Created by Why on 2017/3/9.
 */
public final class Handler {
    private Object controllerBean;
    private Method method;

    public Handler(Object controllerBean, Method method) {
        this.controllerBean = controllerBean;
        this.method = method;
    }

    public void setControllerBean(Object controllerBean) {
        this.controllerBean = controllerBean;
    }

    public Object getControllerBean() {
        return controllerBean;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
