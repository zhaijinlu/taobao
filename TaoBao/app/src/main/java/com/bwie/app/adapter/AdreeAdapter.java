package com.bwie.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bwie.app.R;

import org.json.JSONArray;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/14 21:16
 */

public class AdreeAdapter extends BaseAdapter {
    private Context context;
    private JSONArray list;

    public AdreeAdapter(Context context, JSONArray list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length();
    }

    @Override
    public Object getItem(int position) {
        return list.optJSONObject(position);
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
            convertView=View.inflate(context, R.layout.address_item,null);
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.tel= (TextView) convertView.findViewById(R.id.tv_tel);
            holder.address= (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.name.setText(list.optJSONObject(position).optString("true_name"));
        holder.tel.setText(list.optJSONObject(position).optString("mob_phone"));
        holder.address.setText(list.optJSONObject(position).optString("address"));
        return convertView;
    }
    class Holder{
        TextView name;
        TextView tel;
        TextView address;
    }
}
