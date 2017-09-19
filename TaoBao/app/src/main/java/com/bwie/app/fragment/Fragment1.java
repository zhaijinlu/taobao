package com.bwie.app.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.activity.FindActivity;
import com.bwie.app.activity.WebViewActivity;
import com.bwie.app.adapter.RecyclerAdapter;
import com.bwie.app.bean.YBean;
import com.bwie.app.util.RecordSQLiteOpenHelper;
import com.google.gson.Gson;
import com.google.zxing.WeChatCaptureActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yalantis.phoenix.PullToRefreshView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cz.msebera.android.httpclient.Header;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/8/31 14:13
 */

public class Fragment1 extends Fragment implements View.OnClickListener {
    private TextView findy;
    private ImageView findx,ewm;
    private PullToRefreshView refreshView;
    private View view;
    private RecyclerView recyclerView;
    private String path="http://m.yunifang.com/yunifang/mobile/home";
    private Handler handler=new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment1,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //找控件
        findid();
        //获取网络数据
        getServerData();
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                           refreshView.setRefreshing(false);
                    }
                },2000);
            }
        });

    }

    private void getServerData() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(getActivity(), path, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson=new Gson();
                final YBean data = gson.fromJson(responseString, YBean.class);
                Log.e("content",data.getData().getAd1().toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                RecyclerAdapter adapter=new RecyclerAdapter(data,getActivity());
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);
                adapter.setiOnItemclickListener(new RecyclerAdapter.IOnItemclickListener() {
                    @Override
                    public void setOnItemclick(int position) {
                        Intent intent=new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url",data.getData().getAd1().get(position).getAd_type_dynamic_data());
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
    }

    private void findid() {
       findy= (TextView) view.findViewById(R.id.findy);
        findx= (ImageView) view.findViewById(R.id.findx);
        recyclerView= (RecyclerView) view.findViewById(R.id.id_recyclerview);
        refreshView= (PullToRefreshView) view.findViewById(R.id.pulltorefresh);
        ewm= (ImageView) view.findViewById(R.id.erweima);

        findy.setOnClickListener(this);
        findx.setOnClickListener(this);
        ewm.setOnClickListener(this);
        querylast();


    }


    public void querylast(){
        RecordSQLiteOpenHelper helper=new RecordSQLiteOpenHelper(getActivity());
        SQLiteDatabase db=helper.getReadableDatabase();
        String sql="select name from records";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            if(cursor.isLast()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Log.e("name",name);
                findy.setText(name);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), FindActivity.class);
        switch (v.getId()){
            case R.id.findx:
                startActivityForResult(intent,200);
                break;
            case R.id.findy:
                startActivityForResult(intent,200);
                break;
            case R.id.erweima:
                Intent intent1=new Intent(getActivity(), WeChatCaptureActivity.class);
                getActivity().startActivityForResult(intent1,300);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==500){
            //接受回传的值
            String pass=data.getStringExtra("name");
            //设值展示
            findy.setText(pass);
        }else if(requestCode==300){

        }
    }


}
