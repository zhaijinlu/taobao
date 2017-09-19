package com.bwie.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.bean.TypeBean;

import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/8 14:28
 */

public class CotegoryRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TypeBean> list;
    private LayoutInflater inflater;
    public IOnClickListenner iOnClickListenner;

    public CotegoryRecyclerviewAdapter(Context context, List<TypeBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cotegory_item, parent, false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            ((MyHolder) holder).textView.setText(list.get(position).getTypename());
            if(list.get(position).ischeck()==true){
                ((MyHolder) holder).textView.setTextColor(Color.RED);
            }else {
                ((MyHolder) holder).textView.setTextColor(Color.GRAY);
            }
            ((MyHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iOnClickListenner.setOnClickListener(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.tv_type);
        }
    }
    public interface IOnClickListenner{
        void setOnClickListener(int position);
    }

    public void setiOnClickListenner(IOnClickListenner iOnClickListenner) {
        this.iOnClickListenner = iOnClickListenner;
    }
}
