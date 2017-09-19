package com.bwie.app.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/12 18:46
 */

public class SharedPreferencesUtil {
    private final String  CONFIG="config";
    //使用单例模式
    private static SharedPreferencesUtil sharedPreferencesUtil;
    private  SharedPreferencesUtil(){}
    public static SharedPreferencesUtil getSharedPreferences(){
        if(sharedPreferencesUtil==null){
            sharedPreferencesUtil=new SharedPreferencesUtil();
        }
        return sharedPreferencesUtil;
    }
    //存取数据
    public void savaData(Context context, String key, Object value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if(value instanceof String){
            edit.putString(key, (String) value);
        }else if(value instanceof Boolean){
            edit.putBoolean(key, (Boolean) value);
        }else if(value instanceof Integer){
            edit.putInt(key, (Integer) value);
        }
        edit.commit();
    }
    //获取数据
    public Object getData(Context context,String key,Object delvalue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        if(delvalue instanceof String){
            return  sharedPreferences.getString(key, (String) delvalue);
        }else if(delvalue instanceof  Boolean){
            return  sharedPreferences.getBoolean(key, (Boolean) delvalue);
        }else if(delvalue instanceof Integer){
            return  sharedPreferences.getInt(key, (Integer) delvalue);
        }
        return null;
    }
}
