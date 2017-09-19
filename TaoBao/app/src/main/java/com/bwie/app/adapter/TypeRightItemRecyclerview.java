package com.bwie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * 3. @date 2017/9/11 19:34
 */

public class TypeRightItemRecyclerview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TypeBean> list;
    private LayoutInflater inflater;

    public TypeRightItemRecyclerview(Context context, List<TypeBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.gridview_item,parent,false);
        GridHolder holder=new GridHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof GridHolder){
            ((GridHolder) holder).textView.setText(list.get(position).getTypename());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class GridHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public GridHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.gridview_text);

        }
    }
}
