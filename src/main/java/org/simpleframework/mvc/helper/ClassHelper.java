package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.annotation.Controller;
import org.simpleframework.mvc.annotation.Dao;
import org.simpleframework.mvc.annotation.Service;
import org.simpleframework.util.ArrayUtil;
import org.simpleframework.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class 助手类
 * Created by Why on 2017/3/9.
 */
public final class ClassHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHelper.class);

    public static final Set<Class<?>> CLASS_SET;

    /**
     * 获取应用根路径下的所有类
     */
    static {
        LOGGER.debug("loading classes...");
        CLASS_SET = ClassUtil.getClassSet(ConfigHelper.getAppBasePackage());
        LOGGER.debug("loaded classes from: " + ConfigHelper.getAppBasePackage());
        String[] aspectProxyClasses = ConfigHelper.getAspectProxyClasses();
        if (ArrayUtil.isNotEmpty(aspectProxyClasses)) {
            for (String cls : aspectProxyClasses) {
                CLASS_SET.add(ClassUtil.loadClass(cls, false));
                LOGGER.debug("loaded aspect proxy class: " + cls);
            }
        }
        LOGGER.debug(Arrays.toString(CLASS_SET.toArray()));
    }

    /**
     * 获取所有带有 Dao 注解的类
     *
     * @return
     */
    public static Set<Class<?>> getDaoClassSet() {
        return getClassSetByAnnotation(Dao.class);
    }

    /**
     * 获取所有带有 Service 注解的类
     *
     * @return
     */
    public static Set<Class<?>> getServiceClassSet() {
        return getClassSetByAnnotation(Service.class);
    }

    /**
     * 获取所有带有 Controller 注解的类
     *
     * @return
     */
    public static Set<Class<?>> getControllerClassSet() {
        return getClassSetByAnnotation(Controller.class);
    }

    /**
     * 获取所有带有 Dao Service Controller 注解的类
     *
     * @return
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getDaoClassSet());
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }

    /**
     * 获取应用包名下带有某注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下某父类（或接口）的所有子类（或实现类）
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
