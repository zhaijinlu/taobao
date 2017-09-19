package com.bwie.app.bean;

import java.io.Serializable;

/**
 * 1. 购物车
 * 2. @author admin
 * 3. @date 2017/9/13 20:24
 */

public class ShopCarBean implements Serializable {
    private String name;
    private String price;
    private String num;
    private String image;
    private String cart_id;
    private boolean flag;

    public ShopCarBean(String name, String price, String num, String image, String cart_id, boolean flag) {
        this.name = name;
        this.price = price;
        this.num = num;
        this.image = image;
        this.cart_id = cart_id;
        this.flag = flag;
    }

    public ShopCarBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
