package com.bwie.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bwie.app.R;
import com.bwie.app.adapter.AdreeAdapter;
import com.bwie.app.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class AderssActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private ListView listView;
    private Button addadd;
    private SharedPreferencesUtil sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aderss);
        initview();
        sp=SharedPreferencesUtil.getSharedPreferences();
        //请求数据
        getData();
    }

    private void getData() {
        String url="http://169.254.64.79/mobile/index.php?act=member_address&op=address_list";
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("key",(String)sp.getData(AderssActivity.this,"key",""));
        params.setCacheMaxAge(1000*6);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    int code = obj.optInt("code");
                    if(code==200){
                        JSONObject datas = obj.optJSONObject("datas");
                        JSONArray address_list = datas.optJSONArray("address_list");
                        AdreeAdapter adapter=new AdreeAdapter(AderssActivity.this,address_list);
                        listView.setAdapter(adapter);
                    }else {
                        Log.e("tag","请求失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("tag","请求错误");
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

    private void initview() {
        back= (ImageView) findViewById(R.id.back);
        listView= (ListView) findViewById(R.id.add_list);
        addadd= (Button) findViewById(R.id.add_address);
        addadd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_address:
                Intent intent=new Intent(AderssActivity.this,AddAdressActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
