package org.simpleframework.mvc.proxy;

/**
 * 代理接口
 * Created by Why on 2017/3/9.
 */
public interface Proxy {
    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
