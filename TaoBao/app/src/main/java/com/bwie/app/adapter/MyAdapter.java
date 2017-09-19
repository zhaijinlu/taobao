package com.bwie.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.util.SharedPreferencesUtil;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/5 11:17
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private SharedPreferencesUtil sp;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return R.layout.myshop_top;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder=new Holder();
            convertView=View.inflate(context,R.layout.myshop_top,null);
            holder.username= (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        sp = SharedPreferencesUtil.getSharedPreferences();
        holder.username.setText((String)sp.getData(context,"username",""));


        return convertView;
    }
    class Holder{
        TextView username;

    }
}
