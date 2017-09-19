package com.bwie.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bwie.app.R;
import com.bwie.app.activity.LoginActivity;
import com.bwie.app.activity.MySettingActivity;
import com.bwie.app.adapter.MyAdapter;
import com.bwie.app.util.SharedPreferencesUtil;
import com.bwie.app.view.XListView;
import com.bwie.app.view.XListView2;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/8/31 14:15
 */

public class Fragment4 extends Fragment implements View.OnClickListener {

    private View view;
    private XListView listView;
    private TextView textView;
    private boolean islogin;
    private SharedPreferencesUtil sp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sp = SharedPreferencesUtil.getSharedPreferences();
        islogin = (boolean) sp.getData(getActivity(), "islogin", false);
        if(islogin){
            view = View.inflate(getActivity(), R.layout.fragment4,null);
            listView= (XListView) view.findViewById(R.id.xlistview);
            textView= (TextView) view.findViewById(R.id.tv_setting);
            return view;
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("mm","my");
            startActivity(intent);
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(islogin){
            MyAdapter adapter=new MyAdapter(getActivity());
            listView.setAdapter(adapter);
            textView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setting:
                Intent intent=new Intent(getActivity(), MySettingActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
