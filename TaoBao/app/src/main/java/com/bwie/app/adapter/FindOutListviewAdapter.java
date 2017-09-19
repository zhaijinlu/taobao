package com.bwie.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/9 08:27
 */

public class FindOutListviewAdapter extends BaseAdapter {
    private Context context;
    private JSONArray list;

    public FindOutListviewAdapter(Context context, JSONArray list) {
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
            convertView=View.inflate(context, R.layout.findout_item,null);
            holder=new Holder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.iv_goods);
            holder.name= (TextView) convertView.findViewById(R.id.tv_goodsname);
            holder.price= (TextView) convertView.findViewById(R.id.tv_goodsprice);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        //赋值
        Picasso.with(context).load(list.optJSONObject(position).optString("goods_image_url")).into(holder.imageView);
        holder.name.setText(list.optJSONObject(position).optString("goods_name"));
        holder.price.setText(list.optJSONObject(position).optString("goods_price"));
        return convertView;
    }
    class  Holder{
        ImageView imageView;
        TextView name,price;
    }
}
