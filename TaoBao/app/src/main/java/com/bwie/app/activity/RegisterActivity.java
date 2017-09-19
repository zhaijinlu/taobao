package com.bwie.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bwie.app.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText username,pwd,pwd1,email;
    private Button regin;
    private String url="http://169.254.64.79/mobile/index.php?act=login&op=register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //找控件
        findview();
    }

    private void findview() {
        username= (EditText) findViewById(R.id.username);
        pwd= (EditText) findViewById(R.id.password);
        pwd1= (EditText) findViewById(R.id.password2);
        email= (EditText) findViewById(R.id.email);
        regin= (Button) findViewById(R.id.btn_regin);
        regin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params=new RequestParams(url);
                params.addBodyParameter("username",username.getText().toString().trim());
                params.addBodyParameter("password",pwd.getText().toString().trim());
                params.addBodyParameter("password_confirm",pwd1.getText().toString().trim());
                params.addBodyParameter("email",email.getText().toString().trim());
                params.addBodyParameter("client","android");
                x.http().post(params,new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(CancelledException cex) {
                    }
                    @Override
                    public void onFinished() {
                    }
                });
            }
        });

    }
}
