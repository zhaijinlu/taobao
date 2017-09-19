package com.bwie.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bwie.app.R;
import com.bwie.app.adapter.FindOutListviewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import day.bwie.com.okhttp3.OkHttp3Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindOutActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private ImageView back;
    private JSONArray jsonarray;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    try {
                        JSONObject obj=new JSONObject((String) msg.obj);
                        JSONObject datas = obj.optJSONObject("datas");
                        JSONArray goods_list = datas.optJSONArray("goods_list");
                        jsonarray=new JSONArray();
                        for(int i=0;i<goods_list.length();i++){
                            jsonarray.put(goods_list.optJSONObject(i));
                        }
                        FindOutListviewAdapter adapter=new FindOutListviewAdapter(FindOutActivity.this,jsonarray);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_out);
        listView= (ListView) findViewById(R.id.lv_findout);
        back= (ImageView) findViewById(R.id.back);
        //请求数据
        getData();
        back.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private void getData() {
        String url="http://169.254.64.79/mobile/index.php?act=goods&op=goods_list&page=100";
        OkHttp3Utils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("结果","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String josn=response.body().string();
                Message message=new Message();
                message.what=0;
                message.obj=josn;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    //listview的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(FindOutActivity.this,ShopInfoActivity.class);
        String goods_id=null;
        for (int i=0;i<jsonarray.length();i++){
            if(i==position){
                goods_id=jsonarray.optJSONObject(i).optString("goods_id");
            }
        }
        intent.putExtra("goods_id",goods_id);
        startActivity(intent);
    }
}
