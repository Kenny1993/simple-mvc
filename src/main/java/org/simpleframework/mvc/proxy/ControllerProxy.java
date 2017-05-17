package org.simpleframework.mvc.proxy;

import org.simpleframework.mvc.annotation.Aspect;
import org.simpleframework.mvc.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by Why on 2017/3/9.
 */
@Aspect(Controller.class)
public class ControllerProxy extends AspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerProxy.class);

    private long time;

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    @Override
    protected void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.debug("--------begin--------");
        time = System.currentTimeMillis();
    }

    @Override
    protected void after(Class<?> cls, Method method, Object[] params, Object result) {
        LOGGER.debug("time millis: " + (System.currentTimeMillis() - time) + "ms");
        LOGGER.debug("--------end--------");
    }
}
