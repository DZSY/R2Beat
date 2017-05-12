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

import java.util.ArrayList;


public class RhythmPointList {

    public ArrayList<Integer> list = new ArrayList();
    int pics[] = {
            R.drawable.point1,
            R.drawable.point2,
            R.drawable.point3,
            R.drawable.point4,
            R.drawable.point5,
    };
    Paint paint = new Paint();
    private Bitmap bitmaps[] = new Bitmap[pics.length];
    private Bitmap bitmapBack;
    private GameThread gameThread;

    public RhythmPointList(Context context, GameThread gameThread) {
        Resources res = context.getResources();
        for (int i = 0; i < pics.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(res, pics[i]);
        }
        this.gameThread = gameThread;
    }

    public void draw(Canvas canvas, boolean stay) {
        int diff = canvas.getWidth() / 40;
        int X = canvas.getWidth() * 14 / 30;
        int Y = canvas.getHeight() / 2;
        int size = canvas.getHeight() / 15;
        for (int i = 0; i < list.size(); i++) {
            canvas.drawBitmap(
                    bitmaps[0],
                    new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight()),
                    new RectF(diff * list.get(i).intValue(), Y, diff * list.get(i).intValue() + size, Y + size),
                    paint);
            canvas.drawBitmap(
                    bitmaps[0],
                    new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight()),
                    new RectF(diff * (40 - list.get(i).intValue()), Y, diff * (40 - list.get(i).intValue()) + size, Y + size),
                    paint);
            if (!stay) {
                if (list.get(i) == 20)
                    list.remove(list.get(i));
                else
                    list.set(i, list.get(i) + 1);
            }
        }

        if (gameThread.succeedCount >= 0) {
            switch (gameThread.succeedScore) {
                case 100: {
                    canvas.drawBitmap(
                            bitmaps[0],
                            new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight()),
                            new RectF(X, Y - size / 2, X + 2 * size, Y + size * 3 / 2),
                            paint);
                    break;
                }
                case 90: {
                    canvas.drawBitmap(
                            bitmaps[1],
                            new Rect(0, 0, bitmaps[1].getWidth(), bitmaps[1].getHeight()),
                            new RectF(X, Y - size / 2, X + 2 * size, Y + size * 3 / 2),
                            paint);
                    break;
                }
                case 80: {
                    canvas.drawBitmap(
                            bitmaps[2],
                            new Rect(0, 0, bitmaps[2].getWidth(), bitmaps[2].getHeight()),
                            new RectF(X, Y - size / 2, X + 2 * size, Y + size * 3 / 2),
                            paint);
                    break;
                }
                case 70: {
                    canvas.drawBitmap(
                            bitmaps[3],
                            new Rect(0, 0, bitmaps[3].getWidth(), bitmaps[3].getHeight()),
                            new RectF(X, Y - size / 2, X + 2 * size, Y + size * 3 / 2),
                            paint);
                    break;
                }
            }
        }
    }


}
