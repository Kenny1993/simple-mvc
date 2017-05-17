package org.simpleframework.mvc.annotation;

import java.lang.annotation.*;

/**
 * 切面类注解
 * Created by Why on 2017/3/9.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
