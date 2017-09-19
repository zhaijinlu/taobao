package com.bwie.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class AddAdressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,tel,area,address;
    private Button btn;
    private ImageView back;
    private SharedPreferencesUtil sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        initview();
        sp=SharedPreferencesUtil.getSharedPreferences();
    }

    private void initview() {
        name= (EditText) findViewById(R.id.true_name);
        tel= (EditText) findViewById(R.id.tel);
        area= (EditText) findViewById(R.id.area);
        address= (EditText) findViewById(R.id.adress);
        back= (ImageView) findViewById(R.id.back);
        btn= (Button) findViewById(R.id.sava);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sava:
                //上传数据
                updata();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void updata() {
        String url="http://169.254.64.79/mobile/index.php?act=member_address&op=address_add";
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("key",(String)sp.getData(AddAdressActivity.this,"key",""));
        params.addBodyParameter("true_name",name.getText().toString().trim());
        params.addBodyParameter("mob_phone",tel.getText().toString().trim());
        params.addBodyParameter("city_id","36");
        params.addBodyParameter("area_id","37");
        params.addBodyParameter("address",address.getText().toString().trim());
        params.addBodyParameter("area_info",area.getText().toString().trim());
        params.addBodyParameter("is_default","1");
        params.setCacheMaxAge(1000*6);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    int code = obj.optInt("code");
                    if(code==200){
                        Toast.makeText(AddAdressActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        JSONObject datas = obj.optJSONObject("datas");
                        int address_id = datas.optInt("address_id");
                        sp.savaData(AddAdressActivity.this,"address_id",address_id);
                    }else {
                        Toast.makeText(AddAdressActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
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
}
