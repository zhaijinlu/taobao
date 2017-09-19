package com.bwie.app.fragment;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.adapter.CotegoryRecyclerviewAdapter;
import com.bwie.app.bean.TypeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import day.bwie.com.okhttp3.OkHttp3Utils;
import day.bwie.com.recyclerviewdecoration.MyDecoration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/8/31 14:14
 */

public class Fragment2 extends Fragment {
    private RecyclerView recyclerView;
    private FrameLayout frameLayout;
    private String path="http://169.254.64.79/mobile/index.php?act=goods_class";
    private View view;
    private List<TypeBean> list;
    private TypeFragment fragment;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    try {
                        JSONObject obj=new JSONObject((String) msg.obj);
                        JSONObject datas = obj.optJSONObject("datas");
                        JSONArray class_list = datas.optJSONArray("class_list");

                        for (int i=0;i<class_list.length();i++){
                            list.add(new TypeBean(class_list.optJSONObject(i).optString("gc_name"),class_list.optJSONObject(i).optString("gc_id"),false));
                    }
                        list.get(0).setIscheck(true);
                        //适配Fragment
                        setFragment();
                        final LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        MyDecoration myDecoration=new MyDecoration(getActivity(),LinearLayoutManager.VERTICAL);
                        recyclerView.addItemDecoration(myDecoration);
                        final CotegoryRecyclerviewAdapter adapter=new CotegoryRecyclerviewAdapter(getActivity(),list);
                        recyclerView.setAdapter(adapter);
                        adapter.setiOnClickListenner(new CotegoryRecyclerviewAdapter.IOnClickListenner() {
                            @Override
                            public void setOnClickListener(int position) {
                                TypeBean typeBean = list.get(position);
                               for(int i=0;i<list.size();i++){
                                   if(list.get(i).getTypename().equals(typeBean.getTypename())){
                                       list.get(i).setIscheck(true);
                                   }else {
                                       list.get(i).setIscheck(false);
                                   }
                               }
                                adapter.notifyDataSetChanged();
                                fragment=new TypeFragment();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.framelayout,fragment);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("bean",list.get(position));
                                fragment.setArguments(bundle);
                                fragmentTransaction.commit();
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment2,null);
        list=new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //找控件
        ininview();
        //得到分类数据
        getData();

    }

    private void setFragment() {
        fragment=new TypeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.framelayout,fragment);
        Bundle bundle=new Bundle();
        bundle.putSerializable("bean",list.get(0));

        fragment.setArguments(bundle);
        fragmentTransaction.commit();


    }

    private void getData() {
        OkHttp3Utils.doGet(path, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message=new Message();
                message.what=0;
                message.obj=response.body().string();
                handler.sendMessage(message);
            }
        });
    }

    private void ininview() {
        recyclerView= (RecyclerView) view.findViewById(R.id.cotegoryRecyclerview);
        frameLayout= (FrameLayout) view.findViewById(R.id.framelayout);
    }



}
