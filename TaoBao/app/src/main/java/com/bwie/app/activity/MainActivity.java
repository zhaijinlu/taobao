package com.bwie.app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bwie.app.R;

/**
 * 第一次创建
 */
public class MainActivity extends AppCompatActivity {
    private int i=2;
    private Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(i>0){
                i--;
                handler.postDelayed(runnable,800);
            }else {
                Intent intent=new Intent(MainActivity.this,FirstActivity.class);
                startActivity(intent);
                handler.removeCallbacks(runnable);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //四秒自动跳转
        handler.postDelayed(runnable,800);
    }
}
