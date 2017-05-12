package com.positif.r2beat.Game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.positif.r2beat.R;


public class Background {

    int pics[] = {
            R.drawable.background
    };
    int index = 0;
    private Paint paint = new Paint();
    private Bitmap bitmaps[] = new Bitmap[pics.length];


    public Background(Context context) {
        Resources res = context.getResources();
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), pics[i]);
        }
    }

    public boolean draw(Canvas canvas, boolean stay) {

        canvas.drawBitmap(
                bitmaps[index],
                new Rect(0, 0, bitmaps[index].getWidth(), bitmaps[index].getHeight()),
                new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                paint);

        return false;
    }

}
