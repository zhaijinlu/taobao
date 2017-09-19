package com.bwie.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.bean.YBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/8 10:01
 */

public class Type3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<YBean.DataBean.SubjectsBean.GoodsListBeanX> list;
    private Context context;
    private LayoutInflater inflater;
    private SetOnItemClickListner setOnItemClickListner;


    public Type3Adapter(List<YBean.DataBean.SubjectsBean.GoodsListBeanX> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.linearlayout_horital, parent, false);
        final MyHoder holder=new MyHoder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                setOnItemClickListner.setOnItemClickListner(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHoder){
            Picasso.with(context).load(list.get(position).getGoods_img()).into(((MyHoder) holder).imageView);
            ((MyHoder) holder).name.setText(list.get(position).getGoods_name());

            ((MyHoder) holder).xprice.setText(list.get(position).getShop_price()+"");
            ((MyHoder) holder).yprice.setText(list.get(position).getMarket_price()+"");
            ((MyHoder) holder).yprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHoder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,xprice,yprice;
        public MyHoder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.stagg_image);
            name= (TextView) itemView.findViewById(R.id.shop_name);
            xprice= (TextView) itemView.findViewById(R.id.xprice);
            yprice= (TextView) itemView.findViewById(R.id.yprice);

        }
    }
    public  interface SetOnItemClickListner{
        void setOnItemClickListner(int position);
    }

    public void setSetOnItemClickListner(SetOnItemClickListner setOnItemClickListner) {
        this.setOnItemClickListner = setOnItemClickListner;
    }

}
