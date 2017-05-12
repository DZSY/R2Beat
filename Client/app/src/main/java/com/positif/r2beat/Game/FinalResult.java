package com.positif.r2beat.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.positif.r2beat.R;


public class FinalResult {

    final int dot = 10;
    final int colon = 11;
    int pics[] = {
            R.drawable.num0,
            R.drawable.num1,
            R.drawable.num2,
            R.drawable.num3,
            R.drawable.num4,
            R.drawable.num5,
            R.drawable.num6,
            R.drawable.num7,
            R.drawable.num8,
            R.drawable.num9
    };
    int index = 0;
    private Paint paint = new Paint();
    private Bitmap bitmaps[] = new Bitmap[pics.length];
    private Bitmap bitmapBackground;
    private Bitmap bitmapDot;
    private Bitmap bitmapColon;
    private int duration;
    private int score;


    public FinalResult(Context context, int duration, double score) {
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), pics[i]);
        }
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.finalbackground);
        bitmapDot = BitmapFactory.decodeResource(context.getResources(), R.drawable.numdot);
        bitmapColon = BitmapFactory.decodeResource(context.getResources(), R.drawable.numcolon);
        this.duration = duration;
        this.score = (int) score * 100;
    }

    public boolean draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawBitmap(
                bitmapBackground,
                new Rect(0, 0, bitmaps[index].getWidth(), bitmaps[index].getHeight()),
                new Rect(0, 0, width, height),
                paint);

        int Xs[] = {
                width * 9 / 20,
                width * 10 / 20,
                width * 11 / 20,
                width * 12 / 20,
                width * 13 / 20,
                width * 14 / 20,
                width * 15 / 20,
                width * 16 / 20,
        };
        int Ys[] = {
                height * 5 / 10,
                height * 7 / 10
        };
        int numWidth = height * 3 / 25;
        int numHeight = height * 5 / 25;

        int minute = duration / 60000;
        int second = (duration / 1000) % 60;
        int append = (duration % 1000) / 10;
        int result[] = {
                minute / 10,
                minute % 10,
                colon,
                second / 10,
                second % 10,
                colon,
                append / 10,
                append % 10,
                score / 1000,
                (score / 100) % 10,
                dot,
                (score / 10) % 10,
                score % 10
        };

        canvas.drawBitmap(
                bitmapBackground,
                new Rect(0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight()),
                new Rect(0, 0, width, height),
                paint);

        for (int i = 0; i < result.length; i++) {
            switch (result[i]) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9: {
                    canvas.drawBitmap(
                            bitmaps[result[i]],
                            new Rect(0, 0, bitmaps[result[i]].getWidth(), bitmaps[result[i]].getHeight()),
                            new RectF(Xs[i % Xs.length], Ys[i / Xs.length], Xs[i % Xs.length] + numWidth, Ys[i / Xs.length] + numHeight),
                            paint);
                    break;
                }
                case 10: {
                    canvas.drawBitmap(
                            bitmapDot,
                            new Rect(0, 0, bitmapDot.getWidth(), bitmapDot.getHeight()),
                            new RectF(Xs[i % Xs.length], Ys[i / Xs.length], Xs[i % Xs.length] + numWidth, Ys[i / Xs.length] + numHeight),
                            paint);
                    break;
                }
                case 11: {
                    canvas.drawBitmap(
                            bitmapColon,
                            new Rect(0, 0, bitmapColon.getWidth(), bitmapColon.getHeight()),
                            new RectF(Xs[i % Xs.length], Ys[i / Xs.length], Xs[i % Xs.length] + numWidth, Ys[i / Xs.length] + numHeight),
                            paint);
                    break;
                }
            }
        }

        return false;
    }

}
