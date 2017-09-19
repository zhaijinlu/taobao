package com.bwie.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.bean.TypeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import day.bwie.com.okhttp3.OkHttp3Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/10 20:55
 */

public class CotegoryRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TypeBean> list;
    private LayoutInflater inflater;
    private String path="http://169.254.64.79/mobile/index.php?act=goods_class&gc_id=";
    private List<TypeBean> list2;

    public CotegoryRightAdapter(Context context, List<TypeBean> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.right_recyclerview_item, parent, false);
        RightHolder holder=new RightHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RightHolder){
            ((RightHolder) holder).textView.setText(list.get(position).getTypename());
            String gc_id = list.get(position).getGc_id();
            RequestParams params=new RequestParams(path+gc_id);
            x.http().get(params, new org.xutils.common.Callback.CacheCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject obj=new JSONObject((result.toString()));
                        JSONObject datas = obj.optJSONObject("datas");
                        JSONArray class_list = datas.optJSONArray("class_list");
                        list2=new ArrayList<TypeBean>();
                        for (int i=0;i<class_list.length();i++){
                            list2.add(new TypeBean(class_list.optJSONObject(i).optString("gc_name"),class_list.optJSONObject(i).optString("gc_id")));
                        }
                       GridLayoutManager gridLayoutManager=new GridLayoutManager(context,4){
                           @Override
                           public boolean canScrollVertically() {
                               return false;
                           }
                       };
                        ((RightHolder) holder).recyclerView.setLayoutManager(gridLayoutManager);
                        TypeRightItemRecyclerview adapter=new TypeRightItemRecyclerview(context,list2);
                        ((RightHolder) holder).recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public boolean onCache(String result) {
                    return false;
                }
            });


        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    class RightHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RecyclerView recyclerView;
        public RightHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.right_type);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.type_right_recyclerview);
        }
    }
}
