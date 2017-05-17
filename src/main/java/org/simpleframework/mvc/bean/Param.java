package org.simpleframework.mvc.bean;

import org.simpleframework.util.CastUtil;
import org.simpleframework.util.CollectionUtil;

import java.util.Map;

/**
 * 封装 request 参数
 * Created by Why on 2017/3/9.
 */
public final class Param {
    private Map<String, Object> fieldMap;
    private Map<String, FileParam> fileMap;

    public Param(Map<String, Object> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Param(Map<String, Object> fieldMap, Map<String, FileParam> fileMap) {
        this.fieldMap = fieldMap;
        this.fileMap = fileMap;
    }

    /**
     * 获取请求参数映射
     */
    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    /**
     * 获取文件参数映射
     */
    public Map<String, FileParam> getFileMap() {
        return fileMap;
    }

    /**
     * 判断 fieldMap，fileMap 是否同时为空
     */
    public boolean isEmpty() {
        return CollectionUtil.isEmpty(fieldMap) && CollectionUtil.isEmpty(fileMap);
    }

    /**
     * 根据参数名获取 long 型参数值
     */
    public long getLong(String name) {
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 String 型参数值
     */
    public String getString(String name) {
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 double 型参数值
     */
    public double getDouble(String name) {
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 int 型参数值
     */
    public int getInt(String name) {
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取 boolean 型参数值
     */
    public boolean getBoolean(String name) {
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

}
