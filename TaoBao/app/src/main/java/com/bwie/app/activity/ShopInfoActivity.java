package com.bwie.app.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.util.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import day.bwie.com.okhttp3.OkHttp3Utils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 商品详情
 */
public class ShopInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private String url="http://169.254.64.79/mobile/index.php?act=goods&op=goods_detail&goods_id=";
    private ImageView back,goods_iamge;
    private TextView goods_name,goods_price,add,buy;
    SharedPreferencesUtil sp;
    private String id;
    private static int nums=0;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        sp=SharedPreferencesUtil.getSharedPreferences();
        initview();
        getData();

    }

    private void getData() {
        Intent intent = getIntent();
        final String goods_id = intent.getStringExtra("goods_id");
        RequestParams params=new RequestParams(url+goods_id);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    data = obj.optJSONObject("datas");
                    JSONObject goodsInfo = data.optJSONObject("goods_info");
                    id = goodsInfo.optString("goods_id");
                    goods_name.setText(goodsInfo.optString("goods_name"));
                    goods_price.setText(goodsInfo.optString("goods_price"));
                    Picasso.with(ShopInfoActivity.this).load(data.optString("goods_image")).into(goods_iamge);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void initview() {
        back= (ImageView) findViewById(R.id.back);
        goods_iamge= (ImageView) findViewById(R.id.goods_image);
        goods_name= (TextView) findViewById(R.id.goods_name);
        goods_price= (TextView) findViewById(R.id.goods_price);
        add= (TextView) findViewById(R.id.add_car);
        buy= (TextView) findViewById(R.id.buy);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final boolean islogin = (boolean) sp.getData(ShopInfoActivity.this, "islogin", false);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_car://添加购物车
                View view=View.inflate(ShopInfoActivity.this,R.layout.pupupwindow,null);
                ImageView image= (ImageView) view.findViewById(R.id.pic);
                Picasso.with(ShopInfoActivity.this).load(data.optString("goods_image")).into(image);
                Button jia= (Button) view.findViewById(R.id.jia);
                Button jian= (Button) view.findViewById(R.id.jian);
                final TextView num= (TextView) view.findViewById(R.id.goodsnum);
                Button ok= (Button) view.findViewById(R.id.ok);
                PopupWindow popupWindow=new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    popupWindow.showAtLocation(add, Gravity.BOTTOM,0,60);
                }

                jia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nums++;
                        num.setText(nums+"");
                    }
                });
                jian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nums>0){
                            nums--;
                            num.setText(nums+"");
                        }
                    }
                });
                //确定的点击事件
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(islogin!=true){
                            Toast.makeText(ShopInfoActivity.this, "亲，你还没有登录哦！", Toast.LENGTH_SHORT).show();
                        }else {
                            //加入购物车
                            addShopCar();
                        }
                    }
                });


                break;
            case R.id.buy:

                break;

        }
    }

    private void addShopCar() {
        String path="http://169.254.64.79/mobile/index.php?act=member_cart&op=cart_add";
        RequestParams params=new RequestParams(path);
        params.addBodyParameter("key",(String)sp.getData(ShopInfoActivity.this,"key",""));
        Log.e("key",(String)sp.getData(ShopInfoActivity.this,"key",""));
        params.addBodyParameter("goods_id",id);
        params.addBodyParameter("quantity",nums+"");
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("tag","请求成功");
                try {
                    Log.e("key",result);
                    JSONObject obj=new JSONObject(result);
                    int code = obj.optInt("code");
                    if(code==200){
                        Toast.makeText(ShopInfoActivity.this, "加入购物车", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ShopInfoActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("tag","请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
//        Map<String,String> map=new HashMap<>();
//        map.put("key",(String)sp.getData(ShopInfoActivity.this,"key",""));
//        Log.e("key",(String)sp.getData(ShopInfoActivity.this,"key","")+"456");
//        Log.e("id",id+"456");
//        map.put("goods_id",id);
//        map.put("quantity","1");
//        OkHttp3Utils.doPost(path,map,new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                    Log.e("tag","请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("tag","请求成功");
//                try {
//                    JSONObject obj=new JSONObject(response.body().string());
//                    int code = obj.optInt("code");
//                    if(code==200){
//                        Toast.makeText(ShopInfoActivity.this, "加入购物车", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Toast.makeText(ShopInfoActivity.this, "加入购物车失败", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }
}
