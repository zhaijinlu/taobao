package com.bwie.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MySettingActivity extends AppCompatActivity implements View.OnClickListener {
    private String url="http://169.254.64.79/mobile/index.php?act=logout";
    private ImageView back;
    private Button exit;
    private TextView address;
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        sp = SharedPreferencesUtil.getSharedPreferences();
        ininview();

    }

    private void ininview() {
        back= (ImageView) findViewById(R.id.back);
        exit= (Button) findViewById(R.id.btn_exit);
        address= (TextView) findViewById(R.id.address);
        exit.setOnClickListener(this);
        back.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent=new Intent(MySettingActivity.this,FirstActivity.class);
                intent.putExtra("flag","my");
                startActivity(intent);
                finish();
                break;
            case R.id.btn_exit:
                RequestParams params=new RequestParams(url);
                params.addBodyParameter("key",(String)sp.getData(MySettingActivity.this,"key",""));
                params.addBodyParameter("username",(String)sp.getData(MySettingActivity.this,"username",""));
                params.addBodyParameter("client","android");
                x.http().post(params,new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject obj=new JSONObject(result);
                            int code = obj.optInt("code");
                            if(code==200){
                                sp.savaData(MySettingActivity.this,"islogin",false);
                                Toast.makeText(MySettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MySettingActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("tag", "请求失败");
                    }
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }
                    @Override
                    public void onFinished() {
                    }
                });
                break;
            case R.id.address://跳转地址
                Intent intent1=new Intent(MySettingActivity.this,AderssActivity.class);
                startActivity(intent1);
                break;

        }
    }
}
