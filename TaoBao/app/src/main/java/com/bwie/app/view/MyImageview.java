package com.bwie.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 1. 类的用途
 * 2. @author admin
 * 3. @date 2017/9/5 11:30
 */

public class MyImageview  extends android.support.v7.widget.AppCompatImageView {

    private Paint paint ;

    public MyImageview(Context context) {
        super(context);
    }

    public MyImageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }


    /**
     * 绘制圆形图片
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getCircleBitmap(bitmap, 14);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取圆形图片方法
     * @param bitmap
     * @param pixels
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;


    }
}
