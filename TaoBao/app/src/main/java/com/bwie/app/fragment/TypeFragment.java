package com.bwie.app.fragment;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.adapter.CotegoryRecyclerviewAdapter;
import com.bwie.app.adapter.CotegoryRightAdapter;
import com.bwie.app.bean.TypeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import day.bwie.com.okhttp3.OkHttp3Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/8 15:11
 */

public class TypeFragment extends Fragment {
    private String path="http://169.254.64.79/mobile/index.php?act=goods_class&gc_id=";
    private RecyclerView recyclerView;
    private List<TypeBean> list1;
    private String gc_id;
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
                            list1.add(new TypeBean(class_list.optJSONObject(i).optString("gc_name"),class_list.optJSONObject(i).optString("gc_id")));
                        }
                        LinearLayoutManager manager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                        CotegoryRightAdapter adapter=new CotegoryRightAdapter(getActivity(),list1);
                        recyclerView.setAdapter(adapter);
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
        View view = View.inflate(getActivity(), R.layout.typefragment, null);
        recyclerView= (RecyclerView) view.findViewById(R.id.type_recyclerview);
        list1=new ArrayList<>();
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            TypeBean bean = (TypeBean) bundle.getSerializable("bean");
            gc_id = bean.getGc_id();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    private void getData() {
        OkHttp3Utils.doGet(path + gc_id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("faf",json);
                Message message=new Message();
                message.what=0;
                message.obj=json;
                handler.sendMessage(message);
            }
        });
    }
}
