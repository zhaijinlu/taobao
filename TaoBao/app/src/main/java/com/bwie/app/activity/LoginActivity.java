package com.bwie.app.activity;

import android.content.Intent;
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
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 登录的Activity
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username,pwd;
    private Button login,regin;
    private ImageView back;
    private String url1="http://169.254.64.79/mobile/index.php?act=login ";
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = SharedPreferencesUtil.getSharedPreferences();
        //找控件
        findview();

    }

    private void findview() {
        username= (EditText) findViewById(R.id.username);
        pwd= (EditText) findViewById(R.id.password);
        login= (Button) findViewById(R.id.btn_login);
        regin= (Button) findViewById(R.id.btn_regin);
        back= (ImageView) findViewById(R.id.back);
        login.setOnClickListener(this);
        regin.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                RequestParams params=new RequestParams(url1);
                params.addBodyParameter("username",username.getText().toString().trim());
                params.addBodyParameter("password",pwd.getText().toString().trim());
                params.addBodyParameter("client","android");
                x.http().post(params,new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject obj=new JSONObject(result);
                            int code = obj.optInt("code");
                            if(code==200){
                                sp.savaData(LoginActivity.this,"islogin",true);
                                JSONObject data = obj.optJSONObject("datas");
                                String username = data.optString("username");
                                String userid = data.optString("userid");
                                String key = data.optString("key");
                                Log.e("789",key);
                                sp.savaData(LoginActivity.this,"username",username);
                                sp.savaData(LoginActivity.this,"userid",userid);
                                sp.savaData(LoginActivity.this,"key",key);
                                Intent intent1 = getIntent();
                                String mm = intent1.getStringExtra("mm");
                                if("my".equals(mm)){
                                    Intent intent=new Intent(LoginActivity.this,FirstActivity.class);
                                    intent.putExtra("flag","my");
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent=new Intent(LoginActivity.this,FirstActivity.class);
                                    intent.putExtra("flag","car");
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
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
            case R.id.btn_regin:
                //跳转注册页面
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                Intent intent1=new Intent(LoginActivity.this,FirstActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
