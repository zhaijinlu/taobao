package com.bwie.app.bean;

import java.io.Serializable;

/**
 * 1. 分类的bean
 * 2. @author admin
 * 3. @date 2017/9/8 15:03
 */

public class TypeBean implements Serializable{
    private String typename;
    private String gc_id;
    private boolean ischeck;

    public TypeBean(String typename, String gc_id) {
        this.typename = typename;
        this.gc_id = gc_id;
    }

    public TypeBean() {

    }

    public TypeBean(String typename, String gc_id, boolean ischeck) {
        this.typename = typename;
        this.gc_id = gc_id;
        this.ischeck = ischeck;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}
