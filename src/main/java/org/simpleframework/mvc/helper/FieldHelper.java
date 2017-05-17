package org.simpleframework.mvc.helper;

import org.simpleframework.util.ArrayUtil;
import org.simpleframework.util.FieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Field 助手类
 * Created by Why on 2017/3/21.
 */
public final class FieldHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldHelper.class);

    /**
     * 获取目标类中带有某个注解的成员
     */
    public static Field getFieldByAnnotation(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Field[] fields = FieldUtil.getFields(targetClass);
        if (ArrayUtil.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 获取目标类中带有某个注解的所有成员
     */
    public static List<Field> getFieldListByAnnotation(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        List<Field> fieldList = new ArrayList<Field>();
        Field[] fields = FieldUtil.getFields(targetClass);
        if (ArrayUtil.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    fieldList.add(field);
                }
            }
        }
        return fieldList;
    }
}
