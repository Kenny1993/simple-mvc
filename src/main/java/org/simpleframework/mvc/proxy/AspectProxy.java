package org.simpleframework.mvc.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理抽象父类 (切面模板)，子类只需要关心切面编程
 * Created by Why on 2017/3/9.
 */
public abstract class AspectProxy implements Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        if (intercept(cls, method, params)) {
            begin();
            try {
                before(cls, method, params);
                result = proxyChain.doProxyChain();
                after(cls, method, params, result);
            } catch (Exception e) {
                error(cls, method, params, e);
                throw e;
            } finally {
                end();
            }
        } else {
            try {
                result = proxyChain.doProxyChain();
            } catch (Exception e) {
                throw e;
            }
        }
        return result;
    }

    /**
     * 代理开始
     */
    protected void begin() {
    }

    /**
     * 代理链执行之前
     */
    protected void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    }

    /**
     * 代理链执行之后
     */
    protected void after(Class<?> cls, Method method, Object[] params, Object result) {
    }

    /**
     * 代理链执行时抛出异常
     */
    protected void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    /**
     * 代理结束
     */
    protected void end() {
    }

    /**
     * 拦截判断，返回 true 时代理生效
     */
    public abstract boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable;
}
