package org.simpleframework.mvc.helper;

import org.simpleframework.mvc.Constant;
import org.simpleframework.mvc.bean.Param;
import org.simpleframework.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * request 助手类，封装表单数据
 * Created by Why on 2017/3/9.
 */
public final class RequestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHelper.class);

    public static Param createParam(HttpServletRequest req) {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = req.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)) {
                if (fieldValues.length == 1) {
                    fieldMap.put(fieldName, fieldValues[0]);
                } else {
                    List<Object> list = new ArrayList<Object>();
                    for (String value : fieldValues) {
                        list.add(value);
                    }
                    fieldMap.put(fieldName, list);
                }
            }
        }
        return new Param(fieldMap);
    }

    public static void setCharacterEncoding(HttpServletRequest req) {
        try {
            req.setCharacterEncoding(ConfigHelper.getAppEncoding());
            LOGGER.debug("set request character encoding: " + ConfigHelper.getAppEncoding());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("request set character encoding failure", e);
            throw new RuntimeException(e);
        }
    }
}

