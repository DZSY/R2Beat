package com.positif.r2beat.Game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.positif.r2beat.R;


public class Score {

    int[] pics = {
            R.drawable.score0,
            R.drawable.score70,
            R.drawable.score80,
            R.drawable.score90,
            R.drawable.score100
    };

    Bitmap bitmaps[] = new Bitmap[pics.length];
    GameThread gameThread;

    private Paint mPaint = null;

    public Score(Context context, GameThread gameThread) {
        Resources res = context.getResources();
        for (int i = 0; i < pics.length; i++)
            bitmaps[i] = BitmapFactory.decodeResource(res, pics[i]);
        mPaint = new Paint();
        this.gameThread = gameThread;
    }


    public boolean draw(Canvas canvas, boolean fail) {
        int X = canvas.getWidth() - canvas.getHeight() / 6;
        int Y = 0;
        if (fail)
            canvas.drawBitmap(
                    bitmaps[0],
                    new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight()),
                    new RectF(
                            canvas.getWidth() - canvas.getHeight() / 4,
                            0,
                            canvas.getWidth(),
                            canvas.getHeight() / 4),
                    mPaint);
        else {
            if (gameThread.succeedCount >= 0) {
                switch (gameThread.succeedScore) {
                    case 70: {
                        canvas.drawBitmap(
                                bitmaps[1],
                                new Rect(0, 0, bitmaps[1].getWidth(), bitmaps[1].getHeight()),
                                new RectF(
                                        canvas.getWidth() - canvas.getHeight() / 4,
                                        0,
                                        canvas.getWidth(),
                                        canvas.getHeight() / 4),
                                mPaint);
                        break;
                    }
                    case 80: {
                        canvas.drawBitmap(
                                bitmaps[2],
                                new Rect(0, 0, bitmaps[2].getWidth(), bitmaps[2].getHeight()),
                                new RectF(
                                        canvas.getWidth() - canvas.getHeight() / 4,
                                        0,
                                        canvas.getWidth(),
                                        canvas.getHeight() / 4),
                                mPaint);
                        break;
                    }
                    case 90: {
                        canvas.drawBitmap(
                                bitmaps[3],
                                new Rect(0, 0, bitmaps[3].getWidth(), bitmaps[3].getHeight()),
                                new RectF(
                                        canvas.getWidth() - canvas.getHeight() / 4,
                                        0,
                                        canvas.getWidth(),
                                        canvas.getHeight() / 4),
                                mPaint);
                        break;
                    }
                    case 100: {
                        canvas.drawBitmap(
                                bitmaps[4],
                                new Rect(0, 0, bitmaps[4].getWidth(), bitmaps[4].getHeight()),
                                new RectF(
                                        canvas.getWidth() - canvas.getHeight() / 4,
                                        0,
                                        canvas.getWidth(),
                                        canvas.getHeight() / 4),
                                mPaint);
                        break;
                    }
                }
            }
        }
        return false;
    }

}
