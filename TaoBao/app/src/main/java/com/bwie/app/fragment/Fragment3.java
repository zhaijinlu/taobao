package com.bwie.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.activity.LoginActivity;
import com.bwie.app.activity.OkOrderActivity;
import com.bwie.app.adapter.ShopCarAdapter;
import com.bwie.app.bean.GroupBean;
import com.bwie.app.bean.ShopCarBean;
import com.bwie.app.util.SharedPreferencesUtil;
import com.bwie.app.view.XListView2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/8/31 14:15
 */

public class Fragment3 extends Fragment implements View.OnClickListener {
    private TextView textView;
    private ExpandableListView listView;
    private SwipeRefreshLayout refreshLayout;
    private SharedPreferencesUtil sp;
    private List<GroupBean> glist;
    private List<List<ShopCarBean>> clists;
    private boolean islogin;
    private CheckBox both;//全选
    private TextView heji;//合计
    private TextView pay;//支付
    private ShopCarAdapter adapter;
    private Handler handler=new Handler();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = SharedPreferencesUtil.getSharedPreferences();
        islogin = (boolean) sp.getData(getActivity(), "islogin", false);
        Log.e("islogin","---"+ islogin);
        if(islogin){
            view = View.inflate(getActivity(), R.layout.fragment3,null);
            initview();
            return view;
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
       return null;
    }

    /**
     * 初始化控件
     */
    private void initview() {
        textView= (TextView) view.findViewById(R.id.tv_title);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        listView= (ExpandableListView) view.findViewById(R.id.exlistview);
        both= (CheckBox) view.findViewById(R.id.bose_ck);
        heji= (TextView) view.findViewById(R.id.money);
        pay= (TextView) view.findViewById(R.id.tv_js);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(islogin){
            listView.setGroupIndicator(null);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getData();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    },2000);
                }
            });
            //得到数据
            getData();
            //设置二级列表不可点击
            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });
            //全选监听
            both.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = both.isChecked();
                    if(checked){
                        for (int i=0;i<glist.size();i++){
                            glist.get(i).setFlag(true);
                        }
                        for (int i=0;i<clists.size();i++){
                            List<ShopCarBean> clist = clists.get(i);
                            for(int j=0;j<clist.size();j++){
                                clist.get(j).setFlag(true);
                            }
                        }
                        //刷新适配器
                        adapter.notifyDataSetChanged();
                    }else {
                        for (int i=0;i<glist.size();i++){
                            glist.get(i).setFlag(false);
                        }
                        for (int i=0;i<clists.size();i++){
                            List<ShopCarBean> clist = clists.get(i);
                            for(int j=0;j<clist.size();j++){
                                clist.get(j).setFlag(false);
                            }
                        }
                        //刷新适配器
                        adapter.notifyDataSetChanged();
                    }
                    double count=0;
                    int num=0;
                    for(int i=0;i<glist.size();i++){
                        List<ShopCarBean> list = clists.get(i);
                        for(int j=0;j<list.size();j++){
                            if(list.get(j).isFlag()){
                                count+=Double.parseDouble(list.get(j).getPrice())*Integer.parseInt(list.get(j).getNum());
                                num+=Integer.parseInt(list.get(j).getNum());
                            }
                        }
                    }
                    heji.setText("￥"+count);
                    pay.setText("结算("+num+")");
                }

            });
            //监听事件
            pay.setOnClickListener(this);


        }
    }


    private void getData() {
        String url="http://169.254.64.79/mobile/index.php?act=member_cart&op=cart_list&key=";
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("key",(String)sp.getData(getActivity(),"key",""));
        Log.e("key",(String)sp.getData(getActivity(),"key",""));
        params.setCacheMaxAge(1000*6);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    int code = obj.optInt("code");
                    if(code==200){
                        JSONObject datas = obj.optJSONObject("datas");
                        textView.setText("购物车("+datas.optInt("cart_count")+")");
                        JSONArray cart_list = datas.optJSONArray("cart_list");
                        glist=new ArrayList<GroupBean>();
                        for(int i=0;i<cart_list.length();i++){
                            List<ShopCarBean> clist=new ArrayList<ShopCarBean>();
                            JSONObject json = cart_list.optJSONObject(i);
                            glist.add(new GroupBean(json.optString("store_name"),false));
                            JSONArray goods = json.optJSONArray("goods");
                            for(int j=0;j<goods.length();j++){
                                JSONObject job = goods.optJSONObject(j);
                                clist.add(new ShopCarBean(job.optString("goods_name"),job.optString("goods_price"),job.optString("goods_num"),job.optString("goods_image_url"),job.optString("cart_id"),false));
                            }
                            clists=new ArrayList<List<ShopCarBean>>();
                            clists.add(clist);
                        }
                        adapter = new ShopCarAdapter(getActivity(),glist,clists);
                        listView.setAdapter(adapter);
                        if(adapter !=null&& adapter.getGroupCount()>0){
                            for (int i = 0; i< adapter.getGroupCount(); i++){
                                listView.expandGroup(i);
                            }
                        }
                        //一级监听全选
                        adapter.setiOnclickListener(new ShopCarAdapter.IOnclickListener() {
                            @Override
                            public void setOnClick(boolean flag) {
                                both.setChecked(flag);
                            }

                            @Override
                            public void setMoney(Double money) {
                                    heji.setText("￥"+money);
                            }

                            @Override
                            public void setNum(int num) {
                                    pay.setText("结算("+num+")");
                            }
                        });
                    }else {
                        Log.e("tag","失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("tag","请求失败---");
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
            @Override
            public boolean onCache(String result) {
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_js:
                List<ShopCarBean> goodslist=new ArrayList<>();
                for(int i=0;i<glist.size();i++){
                    List<ShopCarBean> list = clists.get(i);
                    for(int j=0;j<list.size();j++){
                        if(list.get(j).isFlag()){
                           goodslist.add(new ShopCarBean(list.get(j).getName(),list.get(j).getPrice(),list.get(j).getNum(),list.get(j).getImage(),list.get(i).getCart_id(),list.get(j).isFlag()));
                        }
                    }
                }
                if(goodslist.size()!=0){
                    //跳转确认订单
                    Intent intent=new Intent(getActivity(), OkOrderActivity.class);
                    intent.putExtra("list", (Serializable) goodslist);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "你还没有选择商品哦！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
