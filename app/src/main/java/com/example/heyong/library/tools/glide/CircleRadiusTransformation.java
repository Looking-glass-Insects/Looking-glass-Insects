package com.example.heyong.library.tools.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 *
 * 圆形裁剪带边框
 */

public class CircleRadiusTransformation extends BitmapTransformation {

    private int radius;//单位：px
    private int radiusColor;
    private BitmapPool mBitmapPool;

    public CircleRadiusTransformation(Context context, int radius, int radiusColor) {
        super(context);
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = radius;
        this.radiusColor = radiusColor;
    }

    public CircleRadiusTransformation(BitmapPool pool, int radius, int radiusColor) {
        super(pool);
        this.mBitmapPool = pool;
        this.radius = radius;
        this.radiusColor = radiusColor;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight()) + 2*radius;

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        int r = size / 2;
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColor(radiusColor);
        canvas.drawCircle(r,r,r,paint);

        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        canvas.drawCircle(r,r,r-radius,paint);
        return result;
    }

    @Override
    public String getId() {
        return "CircleRadiusTransformation" + radius + radiusColor;
    }
}
