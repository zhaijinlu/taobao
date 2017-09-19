package com.bwie.app.app;

import android.app.Application;

import org.xutils.x;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/8/31 09:54
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
