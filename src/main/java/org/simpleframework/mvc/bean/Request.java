package org.simpleframework.mvc.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 封装请求
 * Created by Why on 2017/3/9.
 */
public final class Request {
    private String reqMethod;
    private String reqPath;

    public Request(String reqMethod, String reqPath) {
        this.reqMethod = reqMethod;
        this.reqPath = reqPath;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    public String getReqPath() {
        return reqPath;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
