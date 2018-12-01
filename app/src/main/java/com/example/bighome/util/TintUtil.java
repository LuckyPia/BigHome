package com.example.bighome.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class TintUtil {
    public static Drawable tintBitmap(Bitmap inBitmap , int tintColor, Resources resources) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap (inBitmap.getWidth(), inBitmap.getHeight() , inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter( new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)) ;
        canvas.drawBitmap(inBitmap , 0, 0, paint) ;
        Drawable  drawable = new BitmapDrawable(resources,outBitmap);
        return drawable ;
    }

    public static Drawable tintBitmap(Bitmap inBitmap , int tintColor) {
        if (inBitmap == null) {
            Log.d("图片","空");
            return null;
        }
        Log.d("图片","有");
        Bitmap outBitmap = Bitmap.createBitmap (inBitmap.getWidth(), inBitmap.getHeight() , inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter( new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)) ;
        canvas.drawBitmap(inBitmap , 0, 0, paint) ;
        Drawable  drawable = new BitmapDrawable(outBitmap);
        return drawable ;
    }


}
