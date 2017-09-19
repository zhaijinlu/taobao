package com.bwie.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.app.R;
import com.bwie.app.activity.WebViewActivity;
import com.bwie.app.bean.YBean;
import com.bwie.app.view.MyImageLoder;
import com.bwie.app.view.XListView2;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/5 16:21
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private YBean data;
    private Context context;
    LayoutInflater inflater;
    private int TYPE_1=0;
    private int TYPE_2=1;
    private int TYPE_3=2;
    private int TYPE_4=3;
    public IOnItemclickListener iOnItemclickListener;

    public RecyclerAdapter(YBean list, Context context) {
        this.data = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case 0:
                view=inflater.inflate( R.layout.homedefalt_item,parent,false);
                holder=new BannerHolder(view);
                 break;
            case 1:
                view=inflater.inflate( R.layout.recyclerview_item,parent,false);
                holder=new Item2(view);
                break;
           case 2:
               view=inflater.inflate( R.layout.staggeredgrid_item,parent,false);
               holder=new Staggeredgrid(view);
               break;
            case 3:
                return new Type3Holder(inflater.inflate(R.layout.recyclerview_type3,parent,false));

        }
      return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if(holder instanceof  Item2){
           Picasso.with(context).load(data.getData().getAd5().get(0).getImage()).into(((Item2) holder).imageView1);
           ((Item2) holder).textView1.setText(data.getData().getAd5().get(0).getTitle());
           Picasso.with(context).load(data.getData().getAd5().get(1).getImage()).into(((Item2) holder).imageView2);
           ((Item2) holder).textView2.setText(data.getData().getAd5().get(1).getTitle());
           Picasso.with(context).load(data.getData().getAd5().get(2).getImage()).into(((Item2) holder).imageView3);
           ((Item2) holder).textView3.setText(data.getData().getAd5().get(2).getTitle());
           Picasso.with(context).load(data.getData().getAd5().get(3).getImage()).into(((Item2) holder).imageView4);
           ((Item2) holder).textView4.setText(data.getData().getAd5().get(3).getTitle());
           ((Item2) holder).imageView1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context,WebViewActivity.class);
                   intent.putExtra("url",data.getData().getAd5().get(0).getAd_type_dynamic_data());
                   context.startActivity(intent);
               }
           });
           ((Item2) holder).imageView2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context,WebViewActivity.class);
                   intent.putExtra("url",data.getData().getAd5().get(1).getAd_type_dynamic_data());
                   context.startActivity(intent);
               }
           });
           ((Item2) holder).imageView3.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context,WebViewActivity.class);
                   intent.putExtra("url",data.getData().getAd5().get(2).getAd_type_dynamic_data());
                   context.startActivity(intent);
               }
           });
           ((Item2) holder).imageView4.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context,WebViewActivity.class);
                   intent.putExtra("url",data.getData().getAd5().get(3).getAd_type_dynamic_data());
                   context.startActivity(intent);
               }
           });
       }else if(holder instanceof  BannerHolder){
           ((BannerHolder) holder).banner.setImageLoader(new MyImageLoder());
           List<String> list=new ArrayList<>();
           List<YBean.DataBean.Ad1Bean> ad1 = data.getData().getAd1();
           for (int i=0;i<ad1.size();i++){
               list.add(ad1.get(i).getImage());
           }
           ((BannerHolder) holder).banner.setImages(list);
           ((BannerHolder) holder).banner.start();
           ((BannerHolder) holder).banner.setOnBannerListener(new OnBannerListener() {
               @Override
               public void OnBannerClick(int position) {
                   iOnItemclickListener.setOnItemclick(position);
               }
           });
     }else if(holder instanceof Staggeredgrid){
           StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
           GridLayoutManager gridLayoutManager=new GridLayoutManager(context,2);
           ((Staggeredgrid) holder).recyelerView.setLayoutManager(gridLayoutManager);
            StaggeredGridAdapter adapter=new StaggeredGridAdapter(data.getData().getSubjects().get(0).getGoodsList(),context);
            ((Staggeredgrid) holder).recyelerView.setAdapter(adapter);
            adapter.setSetOnItemClickListner(new StaggeredGridAdapter.SetOnItemClickListner() {
                @Override
                public void setOnItemClickListner(int position) {
                    Intent intent=new Intent(context, WebViewActivity.class);
                    //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                   // context.startActivity(intent);
                    Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                }
            });
       }else if(holder instanceof Type3Holder){
           LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
           switch (position){
               case 2:
                   Picasso.with(context).load(data.getData().getSubjects().get(0).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(0).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter1=new Type3Adapter(data.getData().getSubjects().get(0).getGoodsList(),context);
                   adapter1.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter1);
                   break;
               case 3:
                   Picasso.with(context).load(data.getData().getSubjects().get(1).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(1).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter2=new Type3Adapter(data.getData().getSubjects().get(1).getGoodsList(),context);
                   adapter2.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter2);
                   break;
               case 4:
                   Picasso.with(context).load(data.getData().getSubjects().get(2).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(2).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter3=new Type3Adapter(data.getData().getSubjects().get(2).getGoodsList(),context);
                   adapter3.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter3);
                   break;
               case 5:
                   Picasso.with(context).load(data.getData().getSubjects().get(3).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(3).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter4=new Type3Adapter(data.getData().getSubjects().get(3).getGoodsList(),context);
                   adapter4.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter4);
                   break;
               case 6:
                   Picasso.with(context).load(data.getData().getSubjects().get(4).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(4).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter5=new Type3Adapter(data.getData().getSubjects().get(4).getGoodsList(),context);
                   adapter5.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter5);
                   break;
               case 7:
                   Picasso.with(context).load(data.getData().getSubjects().get(5).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(5).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter6=new Type3Adapter(data.getData().getSubjects().get(5).getGoodsList(),context);
                   adapter6.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter6);
                   break;
               case 8:
                   Picasso.with(context).load(data.getData().getSubjects().get(6).getDescImage()).into(((Type3Holder) holder).imageView);
                   ((Type3Holder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(context,WebViewActivity.class);
                           intent.putExtra("url",data.getData().getSubjects().get(6).getWapUrl());
                           context.startActivity(intent);
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setLayoutManager(linearLayoutManager);
                   Type3Adapter adapter7=new Type3Adapter(data.getData().getSubjects().get(6).getGoodsList(),context);
                   adapter7.setSetOnItemClickListner(new Type3Adapter.SetOnItemClickListner() {
                       @Override
                       public void setOnItemClickListner(int position) {
                           Intent intent=new Intent(context, WebViewActivity.class);
                           //intent.putExtra("url",data.getData().getSubjects().get(0).getGoodsList().get(position).getWatermarkUrl()) ;
                           // context.startActivity(intent);
                           Toast.makeText(context, "条目"+position, Toast.LENGTH_SHORT).show();
                       }
                   });
                   ((Type3Holder) holder).recyclerView.setAdapter(adapter7);
                   break;
           }




       }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_1;
        }else if(position==1){
            return TYPE_2;
        }else  if(position==9){
            return TYPE_3;
        }else  {
            return TYPE_4;
        }

    }

    class Item2 extends RecyclerView.ViewHolder{
        ImageView imageView1,imageView2,imageView3,imageView4;
        TextView textView1,textView2,textView3,textView4;
        public Item2(View itemView) {
            super(itemView);
            imageView1= (ImageView) itemView.findViewById(R.id.iv_image1);
            imageView2= (ImageView) itemView.findViewById(R.id.iv_image2);
            imageView3= (ImageView) itemView.findViewById(R.id.iv_image3);
            imageView4= (ImageView) itemView.findViewById(R.id.iv_image4);
            textView1= (TextView) itemView.findViewById(R.id.tv_text1);
            textView2= (TextView) itemView.findViewById(R.id.tv_text2);
            textView3= (TextView) itemView.findViewById(R.id.tv_text3);
            textView4= (TextView) itemView.findViewById(R.id.tv_text4);
        }
    }
    class BannerHolder extends RecyclerView.ViewHolder{
        Banner banner;
        public BannerHolder(View itemView) {
            super(itemView);
            banner= (Banner) itemView.findViewById(R.id.banner);
        }
    }
    class Staggeredgrid extends RecyclerView.ViewHolder{
        RecyclerView recyelerView;
        public Staggeredgrid(View itemView) {
            super(itemView);
            recyelerView= (RecyclerView) itemView.findViewById(R.id.recyclerview_item);
        }
    }
    class Type3Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RecyclerView recyclerView;
        public Type3Holder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.iv_type3);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.recyclerview_item2);
        }
    }
    public  interface IOnItemclickListener{
        void setOnItemclick(int position);
    }

    public void setiOnItemclickListener(IOnItemclickListener iOnItemclickListener) {
        this.iOnItemclickListener = iOnItemclickListener;
    }

}
