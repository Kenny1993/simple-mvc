package org.simpleframework.mvc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 封装多个对象数据
 * Created by Why on 2017/3/21.
 */
public class DataList<T extends Serializable> {
    private List<T> model;

    public DataList(List<T> model) {
        this.model = model;
    }

    public List<T> getModel() {
        return model;
    }

    public void setModel(List<T> model) {
        this.model = model;
    }
}
