package org.simpleframework.mvc.bean;

import java.io.Serializable;

/**
 * 封装对象数据
 * Created by Why on 2017/3/9.
 */
public final class Data<T extends Serializable> {
    private T model;

    public Data(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
