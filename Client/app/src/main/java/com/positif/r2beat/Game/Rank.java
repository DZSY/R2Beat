package com.positif.r2beat.Game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.positif.r2beat.R;


public class Rank {

    int[] pics = {
            R.drawable.crown,
            R.drawable.rankme,
            R.drawable.rankrival
    };


    Bitmap bitmaps[] = new Bitmap[pics.length];
    String rank1 = new String();
    String rank2 = new String();
    boolean meRank1 = false;
    private Paint paint = null;

    public Rank(Context context) {
        Resources res = context.getResources();
        for (int i = 0; i < pics.length; i++)
            bitmaps[i] = BitmapFactory.decodeResource(res, pics[i]);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
    }

    public void updateRank(String rank1, String rank2, boolean meRank1) {
        this.rank1 = rank1;
        this.rank2 = rank2;
        this.meRank1 = meRank1;
    }


    public boolean draw(Canvas canvas) {
        final int X = 0;
        final int Y1 = canvas.getHeight() / 13;
        final int Y2 = canvas.getHeight() / 5;
        final int width = canvas.getWidth() / 5;
        final int height = canvas.getHeight() / 8;
        int first = meRank1 ? 1 : 2;
        int second = meRank1 ? 2 : 1;
        canvas.drawBitmap(
                bitmaps[first],
                new Rect(0, 0, bitmaps[first].getWidth(), bitmaps[first].getHeight()),
                new RectF(X, Y1, X + width, Y1 + height),
                paint);
        canvas.drawBitmap(
                bitmaps[second],
                new Rect(0, 0, bitmaps[second].getWidth(), bitmaps[second].getHeight()),
                new RectF(X, Y2, X + width, Y2 + height),
                paint);
        canvas.drawBitmap(
                bitmaps[0],
                new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight()),
                new RectF(canvas.getWidth() / 50, canvas.getHeight() / 20, canvas.getWidth() / 20, canvas.getHeight() / 10),
                paint);
        canvas.drawText(rank1, canvas.getWidth() / 15, canvas.getHeight() / 7, paint);
        canvas.drawText(rank2, canvas.getWidth() / 15, canvas.getWidth() / 6, paint);
        return false;
    }

}
