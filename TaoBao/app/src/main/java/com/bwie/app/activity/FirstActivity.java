package com.bwie.app.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bwie.app.R;
import com.bwie.app.fragment.Fragment1;
import com.bwie.app.fragment.Fragment2;
import com.bwie.app.fragment.Fragment3;
import com.bwie.app.fragment.Fragment4;

public class FirstActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private FrameLayout frameLayout;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private FragmentManager supportFragmentManager;
    private RadioButton b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("activity","oncreat");
        setContentView(R.layout.activity_first);
        //找控件
        find();

    }


    private void find() {
        radioGroup= (RadioGroup) findViewById(R.id.radiogroup);
        frameLayout= (FrameLayout) findViewById(R.id.frame);
        b1= (RadioButton) findViewById(R.id.b1);
        supportFragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
       // Log.e("flag",flag);
        if(flag!=null){
            Log.e("flag",flag);
            if(flag.equals("car")){
                radioGroup.check(R.id.b3);
                FragmentTransaction fragmentTransaction3 = supportFragmentManager.beginTransaction();
                if(fragment3==null){
                    fragment3=new Fragment3();
                    fragmentTransaction3.add(R.id.frame,fragment3);
                }else {
                    fragmentTransaction3.show(fragment3);
                }
                fragmentTransaction3.commit();
            }else {
                radioGroup.check(R.id.b4);
                FragmentTransaction fragmentTransaction4 = supportFragmentManager.beginTransaction();
                if(fragment4==null){
                    fragment4=new Fragment4();
                    fragmentTransaction4.add(R.id.frame,fragment4);
                }else {
                    fragmentTransaction4.show(fragment4);
                }
                fragmentTransaction4.commit();
            }
        }else {
            boolean checked = b1.isChecked();
            if(checked==true){
                FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
                if(fragment1==null){
                    fragment1=new Fragment1();
                    fragmentTransaction1.add(R.id.frame,fragment1);
                }else {
                    fragmentTransaction1.show(fragment1);
                }
                fragmentTransaction1.commit();
            }
       }

        //radiogroup的监听
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        //隐藏所有的fragment
        hideAllFragment(fragmentTransaction);
        switch (checkedId){
            case R.id.b1:
                FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
                if(fragment1==null){
                    fragment1=new Fragment1();
                    fragmentTransaction1.add(R.id.frame,fragment1);
                }else {
                    fragmentTransaction1.show(fragment1);
                }
                fragmentTransaction1.commit();
                break;
            case R.id.b2:
                FragmentTransaction fragmentTransaction2 = supportFragmentManager.beginTransaction();
                if(fragment2==null){
                    fragment2=new Fragment2();
                    fragmentTransaction2.add(R.id.frame,fragment2);
                }else {
                    fragmentTransaction2.show(fragment2);
                }
                fragmentTransaction2.commit();
                break;
            case R.id.b3:
                FragmentTransaction fragmentTransaction3 = supportFragmentManager.beginTransaction();
                if(fragment3==null){
                    fragment3=new Fragment3();
                    fragmentTransaction3.add(R.id.frame,fragment3);
                }else {
                    fragmentTransaction3.show(fragment3);
                }
                fragmentTransaction3.commit();

                break;
            case R.id.b4:
                FragmentTransaction fragmentTransaction4 = supportFragmentManager.beginTransaction();
                if(fragment4==null){
                    fragment4=new Fragment4();
                    fragmentTransaction4.add(R.id.frame,fragment4);
                }else {
                    fragmentTransaction4.show(fragment4);
                }
                fragmentTransaction4.commit();
                break;
        }

    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(fragment1!=null){
            fragmentTransaction.hide(fragment1);
        }
        if(fragment2!=null){
            fragmentTransaction.hide(fragment2);
        }
        if(fragment3!=null){
            fragmentTransaction.hide(fragment3);
        }
        if(fragment4!=null){
            fragmentTransaction.hide(fragment4);
        }
        fragmentTransaction.commit();

    }
}
