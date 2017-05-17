package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.annotation.Inject;
import org.simpleframework.util.ArrayUtil;
import org.simpleframework.util.CollectionUtil;
import org.simpleframework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 控制反转助手类
 * Created by Why on 2017/3/9.
 */
public final class IocHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);

    static {
        LOGGER.debug("injecting... ");
        // 获取所有的 Bean 类与 Bean 实例之间的映射关系 （简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历 Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 从 Bean Map 中获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类定义的所有成员变量 （简称 Bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历 Bean Field
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean Field 是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 从 Bean Map 中获取 Bean Field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = null;
                            for (Class<?> cls : beanMap.keySet()) {
                                if (beanFieldClass.isAssignableFrom(cls)) {
                                    beanFieldInstance = beanMap.get(cls);
                                    break;
                                }
                            }
                            if (beanFieldInstance != null) {
                                // 通过反射初始化 Bean Field 的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                                LOGGER.debug("successful inject : " + beanFieldClass.getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
