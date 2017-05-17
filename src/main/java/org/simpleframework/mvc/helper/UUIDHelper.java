package org.simpleframework.mvc.helper;

import java.util.UUID;

/**
 * UUID 助手类
 * Created by Why on 2017/3/21.
 */
public final class UUIDHelper {
    /**
     * 获取没有 “-” 的 UUID 字符串
     */
    public static String getString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
