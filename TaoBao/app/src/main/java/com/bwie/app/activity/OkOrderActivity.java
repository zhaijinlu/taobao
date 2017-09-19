package com.bwie.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bwie.app.R;
import com.bwie.app.adapter.AdreeAdapter;
import com.bwie.app.adapter.OrderGoodsAdapter;
import com.bwie.app.bean.PayResult;
import com.bwie.app.bean.ShopCarBean;
import com.bwie.app.util.SharedPreferencesUtil;
import com.bwie.app.util.SignUtils;
import com.bwie.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.security.auth.login.LoginException;

/**
 * 确认订单
 */
public class OkOrderActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private ImageView back;
    private TextView name,tel,address,gps,money;
    private Button orderlist;
    private ListView listView;
    private SharedPreferencesUtil sp;
    private List<ShopCarBean> list;
    // 商户PID
    public static final String PARTNER = "2088901305856832";
    // 商户收款账号
    public static final String SELLER = "8@qdbaiu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM" +
            "/KCxg/OIj6er2GEig0DOkHqBSzEPHGigMbTXP1k2nrxEHeE59xOOuy" +
            "ovQH/A1LgbuVKyOac3uAN4GXIBEoozRVzDhu5dobeNm48BPcpYSAfvN3K" +
            "/5GLacvJeENqsiBx8KufM/9inlHaDRQV7WhX1Oe2ckat1EkdHwxxQgc" +
            "36NhAgMBAAECgYEAwn3sWpq6cUR65LD8h9MIjopTImTlpFjgz72bhsHD" +
            "ZK6A+eJDXcddrwh7DI34t/0IBqu+QEoOc/f0fIEXS9hMwTvFY59XG7M8" +
            "M6SdeaAbemrGmZ1IdD6YDmpbQFHn2ishaYF0YDZIkBS3WLDFrtk/efaar" +
            "BCpGAVXeEiVQE4LewECQQD5W1rpkq+dHDRzzdtdi1bJ479wun5CfmVDV" +
            "b2CJH7Iz0t8zyp/iEVV2QELnxZMphmoSzKaLXutTTj2OImpzCtRAkEA1" +
            "VMxG6nQq9NkU51H1+I3mlUXRZ0XeFA1GFJ7xWpNRAVhEWlDz2zy9v/g" +
            "X+RFpNC3f5uznycas70Xp78ew43TEQJAZFFqi9mlqTF1sLk67bFnIyX" +
            "rGPEOZrXvC13tNfR0xVkQZ4/46wHp0xXQo9pG4GNaoyhNnVV7EkelCPn" +
            "J+HPZYQJAQh6T9QgQZoGR8hyovPAf3dUL7oa/VIo/urcuJ8VIB5JHQNdI" +
            "rk0NjaNHj1E4iNosVgATj3vWWel9IIArb99QkQJAKvfm78lwnImtg5IM6" +
            "04hdn/Wu1XF8tpxsKLWcnfchMr0bM9rCmKmhAY+wdmqSyPZRiNb1QaaaD" +
            "TqJxLy6AnQ+Q==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd6rV3vOE578e6V" +
            "lGEakZpPdsX2QmGdIfi/yHe cg1CIEWzX9wn2LNFGtu1EzYQyKACG/RKeog0pUJEVGfBG30zFdNY2YocYJNdPtA" +
            "DqhJbS0GJm7f8 1vRiLKtOwKjdiz9oMEwxhc/5fysfMbercidRmlCDPU9BNL1UPb9bAx25JwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OkOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OkOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OkOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_order);
        sp=SharedPreferencesUtil.getSharedPreferences();
        Intent intent = getIntent();
        list = (List<ShopCarBean>) intent.getSerializableExtra("list");
        //初始化定位
        initLocation();
        //初始化控件
        initview();
        //获取收货地址信息
        getAdressData();
    }

    private void getAdressData() {
        String url="http://169.254.64.79/mobile/index.php?act=member_address&op=address_list";
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("key",(String)sp.getData(OkOrderActivity.this,"key",""));
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
                        JSONObject object = address_list.optJSONObject(0);
                        name.setText(object.optString("true_name"));
                        tel.setText(object.optString("mob_phone"));
                        address.setText(object.optString("address"));
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

        name= (TextView) findViewById(R.id.name);
        tel= (TextView) findViewById(R.id.tv_tel);
        address= (TextView) findViewById(R.id.tv_address);
        gps= (TextView) findViewById(R.id.gps);
        money= (TextView) findViewById(R.id.money);
        orderlist= (Button) findViewById(R.id.tv_js);
        listView= (ListView) findViewById(R.id.okorderlist);
        OrderGoodsAdapter adapter=new OrderGoodsAdapter(OkOrderActivity.this,list);
        listView.setAdapter(adapter);
        double count=0;
        for(int i=0;i<list.size();i++){
            count+=Double.parseDouble(list.get(i).getPrice())*Double.parseDouble(list.get(i).getNum());
        }
        money.setText(count+"");
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
        //提交订单的监听
        orderlist.setOnClickListener(this);
    }
    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("" + location.getProvince());
                    sb.append("" + location.getCity());
                    sb.append("" + location.getDistrict());
                    sb.append("" + location.getAddress());
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //解析定位结果，
                String result = sb.toString();
                gps.setText(result);
            } else {
                gps.setText("定位失败，loc is null");
            }
        }
    };

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }
    private void paySignFromClient() {
        //获得订单信息
        String orderInfo = getOrderInfo("来自Client测试商品", "购买一部手机", "0.01");
        //进行加密签名
        String sign = sign(orderInfo);
        //通过URLEncoder进行编码
        try {
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //拼装最终的支付信息
        StringBuffer sb = new StringBuffer(orderInfo);
        sb.append("&sign=\"");
        sb.append(sign);
        sb.append("\"&");
        sb.append(getSignType());
        //获取必须来自服务端
        final String payInfo = sb.toString();//获得最终的支付信息
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OkOrderActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Log.i("TAG", "走了pay支付方法.............");

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void paySignFromServer() {
        //添加参数，暂时写死，项目中，从页面获取
        String url = "http://169.254.26.25:8080/PayServer/AlipayDemo";
        StringBuffer sb = new StringBuffer("?");
        sb.append("subject=");
        sb.append("来自Server测试的商品");
        sb.append("&");
        sb.append("body=");
        sb.append("该测试商品的详细描述");
        sb.append("&");
        sb.append("total_fee=");
        sb.append("0.01");
        url = url + sb.toString();
        //到服务器进行订单加密
        RequestParams params=new RequestParams(url);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("abc+++",result);
                final String signResult = result;
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(OkOrderActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(signResult, true);
                        Log.i("TAG", "走了pay支付方法.............");

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "失败............" + ex);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_js:
                String path="http://169.254.64.79/mobile/index.php?act=member_buy&op=buy_step1";
                View view=View.inflate(OkOrderActivity.this,R.layout.paywindow,null);
                RadioGroup group= (RadioGroup) view.findViewById(R.id.group);
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        switch (checkedId){
                            case R.id.ali:
                                //支付宝支付
                                //paySignFromClient();
                                paySignFromServer();
                                break;
                            case R.id.wx:
                                break;
                        }
                    }
                });
                PopupWindow popupWindow=new PopupWindow(view, getWindowManager().getDefaultDisplay().getWidth()/3*2,250);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.setTouchable(true);
                popupWindow.setFocusable(true);
                if(popupWindow.isShowing()){
                    popupWindow.dismiss();
                }else {
                    popupWindow.showAtLocation(View.inflate(OkOrderActivity.this,R.layout.activity_ok_order,null), Gravity.CENTER,0,0);
                }
                //第一次提交后台数据
//                RequestParams params=new RequestParams(path);
//                params.addBodyParameter("key",(String) sp.getData(OkOrderActivity.this,"key",""));
//                StringBuilder id=new StringBuilder();
//                for(int i=0;i<list.size();i++){
//                    if(i==0){
//                        id.append(list.get(i).getCart_id()+"|"+list.get(i).getNum());
//                    }else {
//                        id.append(","+list.get(i).getCart_id()+"|"+list.get(i).getNum());
//                    }
//                }
//                params.addBodyParameter("cart_id",id.toString());
//                params.addBodyParameter("ifcart","1");
//                x.http().post(params, new Callback.CacheCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.e("json++",result);
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                                Log.e("json++","失败");
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//
//                    @Override
//                    public boolean onCache(String result) {
//                        return false;
//                    }
//                });
                break;
        }

    }
}
