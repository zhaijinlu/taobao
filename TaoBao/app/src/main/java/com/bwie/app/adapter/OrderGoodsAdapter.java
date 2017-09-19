package com.bwie.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.bean.ShopCarBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/15 14:49
 */

public class OrderGoodsAdapter extends BaseAdapter {
    private Context context;
    private List<ShopCarBean> list;

    public OrderGoodsAdapter(Context context, List<ShopCarBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView=View.inflate(context, R.layout.okorderlistview_item,null);
            holder.goods_image=(ImageView)convertView. findViewById(R.id.c_image);
            holder.goods_name=(TextView)convertView.findViewById(R.id.c_name);
            holder.goods_price=(TextView)convertView.findViewById(R.id.c_price);
            holder.goods_num=(TextView)convertView.findViewById(R.id.c_num);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        Picasso.with(context).load(list.get(position).getImage()).into(holder.goods_image);
        holder.goods_price.setText(list.get(position).getPrice());
        holder.goods_name.setText(list.get(position).getName());
        holder.goods_num.setText(list.get(position).getNum());
        return convertView;
    }
    class Holder{
        ImageView goods_image;
        TextView goods_name,goods_price,goods_num;
    }
}
