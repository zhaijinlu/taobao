package com.bwie.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.bean.GroupBean;
import com.bwie.app.bean.ShopCarBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/13 20:43
 */

public class ShopCarAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<GroupBean> glist;
    private List<List<ShopCarBean>> clist;
    public IOnclickListener iOnclickListener;
    private boolean ischeck;

    public ShopCarAdapter(Context context, List<GroupBean> glist, List<List<ShopCarBean>> clist) {
        this.context = context;
        this.glist = glist;
        this.clist = clist;
    }

    @Override
    public int getGroupCount() {
        return glist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return clist.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return glist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return clist.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GHoler gHoler;
        if(convertView==null){
            gHoler=new GHoler();
            convertView=View.inflate(context, R.layout.group_item,null);
            gHoler.checkBox= (CheckBox) convertView.findViewById(R.id.g_ck);
            gHoler.textView= (TextView) convertView.findViewById(R.id.g_tv);
            convertView.setTag(gHoler);
        }else {
            gHoler= (GHoler) convertView.getTag();
        }
        gHoler.textView.setText(glist.get(groupPosition).getStore_name());
        gHoler.checkBox.setChecked(glist.get(groupPosition).isFlag());
        //父级单选框监听
        gHoler.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = gHoler.checkBox.isChecked();
                //一级控制二级
                if(checked){
                    glist.get(groupPosition).setFlag(true);
                    List<ShopCarBean> list = clist.get(groupPosition);
                    for(int j=0;j<list.size();j++){
                        list.get(j).setFlag(true);
                    }
                }else {
                    glist.get(groupPosition).setFlag(false);
                    List<ShopCarBean> list = clist.get(groupPosition);
                    for(int j=0;j<list.size();j++){
                        list.get(j).setFlag(false);
                    }
                }
                //一级控制全选
                int m=0;
                for(int i=0;i<glist.size();i++){
                    boolean flag = glist.get(i).isFlag();
                    if(flag==true){
                        m++;
                    }
                }
                if(m==glist.size()){
                    ischeck=true;
                }else {
                    ischeck=false;
                }
                iOnclickListener.setOnClick(ischeck);
                //计算价格
                getData();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CHolder cHolder;
        if(convertView==null){
            cHolder=new CHolder();
            convertView=View.inflate(context, R.layout.child_item,null);
            cHolder.checkBox= (CheckBox) convertView.findViewById(R.id.c_ck);
            cHolder.imageView= (ImageView) convertView.findViewById(R.id.c_image);
            cHolder.name= (TextView) convertView.findViewById(R.id.c_name);
            cHolder.price= (TextView) convertView.findViewById(R.id.c_price);
            cHolder.num= (TextView) convertView.findViewById(R.id.c_num);
            cHolder.nums= (TextView) convertView.findViewById(R.id.goodsnum);
            convertView.setTag(cHolder);
        }else {
            cHolder= (CHolder) convertView.getTag();
        }
        cHolder.name.setText(clist.get(groupPosition).get(childPosition).getName());
        cHolder.price.setText(clist.get(groupPosition).get(childPosition).getPrice());
        cHolder.num.setText(clist.get(groupPosition).get(childPosition).getNum());
        cHolder.nums.setText(clist.get(groupPosition).get(childPosition).getNum());
        cHolder.checkBox.setChecked(clist.get(groupPosition).get(childPosition).isFlag());
        Picasso.with(context).load(clist.get(groupPosition).get(childPosition).getImage()).into(cHolder.imageView);
        //二级单选框监听
        cHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = cHolder.checkBox.isChecked();
                if(checked){
                    clist.get(groupPosition).get(childPosition).setFlag(true);
                }else {
                    clist.get(groupPosition).get(childPosition).setFlag(false);
                }
                //二级控制一级
                int m=0;
                for(int i=0;i<clist.get(groupPosition).size();i++){
                    boolean flag = clist.get(groupPosition).get(i).isFlag();
                    if(flag){
                        m++;
                    }
                }
                if(m==clist.get(groupPosition).size()){
                    glist.get(groupPosition).setFlag(true);
                }else {
                    glist.get(groupPosition).setFlag(false);
                }
                //二级控制全选
                int n=0;
                for(int i=0;i<glist.size();i++){
                    boolean flag = glist.get(i).isFlag();
                    if(flag==true){
                        n++;
                    }
                }
                if(n==glist.size()){
                    ischeck=true;
                }else {
                    ischeck=false;
                }
                iOnclickListener.setOnClick(ischeck);
                //计算价格
                getData();

                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GHoler{
        CheckBox checkBox;
        TextView textView;
    }
    class CHolder{
        CheckBox checkBox;
        TextView name,price,num,nums;
        ImageView imageView;
    }
    //接口回调
    public interface  IOnclickListener{
        void setOnClick(boolean flag);
        void setMoney(Double money);
        void setNum(int num);
    }
    public void setiOnclickListener(IOnclickListener iOnclickListener) {
        this.iOnclickListener = iOnclickListener;
    }
    //计算价格的方法
    public void getData(){
        double count=0;
        int num=0;
        for(int i=0;i<glist.size();i++){
            List<ShopCarBean> list = clist.get(i);
            for(int j=0;j<list.size();j++){
                if(list.get(j).isFlag()){
                    count+=Double.parseDouble(list.get(j).getPrice())*Integer.parseInt(list.get(j).getNum());
                    num+=Integer.parseInt(list.get(j).getNum());
                }
            }
        }
        iOnclickListener.setMoney(count);
        iOnclickListener.setNum(num);
    }

}
