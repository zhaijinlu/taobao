package com.bwie.app.bean;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/13 20:27
 */

public class GroupBean {
    private String store_name;
    private boolean flag;

    public GroupBean(String store_name, boolean flag) {
        this.store_name = store_name;
        this.flag = flag;
    }

    public GroupBean() {
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
