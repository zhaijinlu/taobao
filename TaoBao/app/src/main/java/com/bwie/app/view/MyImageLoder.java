package com.bwie.app.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/6 14:20
 */

public class MyImageLoder extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
