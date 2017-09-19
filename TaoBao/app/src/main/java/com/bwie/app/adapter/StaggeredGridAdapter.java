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
 * 3. @date 2017/9/6 19:32
 */

public class StaggeredGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<YBean.DataBean.SubjectsBean.GoodsListBeanX> list;
    private Context context;
    private LayoutInflater inflater;
    private SetOnItemClickListner setOnItemClickListner;

    public StaggeredGridAdapter(List<YBean.DataBean.SubjectsBean.GoodsListBeanX> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.staggered_item, parent, false);
        final MyHolder holder=new MyHolder(view);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            Log.e("affaf","走到");
//            ViewGroup.LayoutParams layoutParams = ((MyHolder) holder).imageView.getLayoutParams();
//            if(position%2==0){
//                layoutParams.height=150;
//            }
//            ((MyHolder) holder).imageView.setLayoutParams(layoutParams);
           Picasso.with(context).load(list.get(position).getGoods_img()).into(((MyHolder) holder).imageView);
            ((MyHolder) holder).name.setText(list.get(position).getEfficacy());

            ((MyHolder) holder).xprice.setText(list.get(position).getShop_price()+"");
            ((MyHolder) holder).yprice.setText(list.get(position).getMarket_price()+"");
            ((MyHolder) holder).yprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,xprice,yprice;
        public MyHolder(View itemView) {
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
