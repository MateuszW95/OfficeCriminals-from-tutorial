package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;

/**
 * Created by mateusz on 12.04.18.
 */

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path,int destWidth, int destHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        float srcWidth=options.outWidth;
        float srcHeigth=options.outHeight;

        //okreśłenie współczynika skalowania
        int inSampleSizie=1;
        if(srcHeigth>destHeight || srcWidth>destWidth) {
            float heigthScale = srcHeigth / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSizie = Math.round(heigthScale > widthScale ? heigthScale : widthScale);
        }
            options=new BitmapFactory.Options();
            options.inSampleSize=inSampleSizie;

            //odczytanie pliku itworzenie finalnej bitmapy
            return BitmapFactory.decodeFile(path,options);

    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path,size.x,size.y);
    }

    public static  Bitmap getScaledBitmap(String path,View cotainer){
        return getScaledBitmap(path,cotainer.getWidth(),cotainer.getHeight());
    }
}
