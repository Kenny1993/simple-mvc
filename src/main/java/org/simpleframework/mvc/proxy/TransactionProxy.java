package org.simpleframework.mvc.proxy;

import org.simpleframework.mvc.annotation.Aspect;
import org.simpleframework.mvc.annotation.Service;
import org.simpleframework.mvc.annotation.Transaction;
import org.simpleframework.mvc.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 数据库事务代理
 * Created by Why on 2017/3/9.
 */
@Aspect(Service.class)
public class TransactionProxy extends AspectProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    /**
     * 判断当前线程中是否已经有事务，初始为 false
     */
    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    protected void begin() {
        /**
         * 事务开始
         */
        FLAG_HOLDER.set(true);
        DatabaseHelper.beginTransaction();
        LOGGER.debug("begin transaction");
    }

    @Override
    protected void after(Class<?> cls, Method method, Object[] params, Object result) {
        DatabaseHelper.commitTransaction();
        LOGGER.debug("commit transaction");
    }

    @Override
    protected void error(Class<?> cls, Method method, Object[] params, Throwable e) {
        DatabaseHelper.rollbackTransaction();
        LOGGER.debug("rollback transaction");
    }

    @Override
    protected void end() {
        /**
         * 事务结束
         */
        FLAG_HOLDER.set(false);
        LOGGER.debug("end transaction");
    }

    @Override
    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        /**
         * 如果当前线程没有事务，且该类或方法中带有 Transaction 注解，则开启事务
         */
        return !FLAG_HOLDER.get() && (cls.isAnnotationPresent(Transaction.class) || method.isAnnotationPresent(Transaction.class));
    }
}
