package org.simpleframework.mvc;

import org.simpleframework.mvc.helper.AopHelper;
import org.simpleframework.mvc.helper.BeanHelper;
import org.simpleframework.mvc.helper.ClassHelper;
import org.simpleframework.mvc.helper.ControllerHelper;
import org.simpleframework.mvc.helper.IocHelper;
import org.simpleframework.util.ClassUtil;

/**
 * 助手类加载器
 * Created by Why on 2017/3/9.
 */
public final class HelperLoader {
    public static void init() {
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class};
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
