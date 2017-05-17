package org.simpleframework.mvc.proxy.factory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.simpleframework.mvc.proxy.Proxy;
import org.simpleframework.mvc.proxy.ProxyChain;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理工厂类，生产代理对象
 * Created by Why on 2017/3/9.
 */
public abstract class ProxyFactory {
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
            }
        });
    }
}
