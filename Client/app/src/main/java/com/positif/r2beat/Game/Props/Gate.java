package com.positif.r2beat.Game.Props;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import com.positif.r2beat.Game.ActElement;
import com.positif.r2beat.R;


public class Gate implements ActElement {

    int mX = 0;
    int mY = 0;
    int mWidth = 0;
    int mHeight = 0;
    double X;
    double Y;
    double Width;
    double Height;
    int[] mPics = {
            R.drawable.gate1,
            R.drawable.gate2,
            R.drawable.gate3,
            R.drawable.gate4,
    };

    private int count = 0;
    private int mIndex = 0;

    private Context mContext = null;
    private Resources res = null;
    private BitmapDrawable mDraw = null;
    private Paint mPaint = null;
    private Bitmap[] mBitmaps = null;

    private Object gateLock;


    public Gate(Context ctx, int screenWidth, int screenHeight, Object gateLock) {
        mContext = ctx;
        res = mContext.getResources();
        mBitmaps = new Bitmap[mPics.length];
        for (int i = 0; i < mPics.length; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(res, mPics[i]);
        }
        mPaint = new Paint();
        X = screenWidth * 0.475;
        Y = screenHeight * 0.32;
        Width = screenWidth * 0.052;
        Height = screenHeight * 0.08;
        this.gateLock = gateLock;
    }


    public boolean draw(Canvas canvas, boolean stay) {

        if (!stay) {
            mX = (int) X;
            mY = (int) Y;
            mWidth = (int) Width;
            mHeight = (int) Height;
            canvas.drawBitmap(
                    mBitmaps[mIndex],
                    new Rect(0, 0, mBitmaps[mIndex].getWidth(), mBitmaps[mIndex].getHeight()),
                    new RectF(mX, mY, mX + mWidth, mY + mHeight),
                    mPaint);
            double diff = canvas.getHeight() * 0.016;
            count++;
            if (count < 6) {
                X -= canvas.getWidth() * 0.0047;
                Y += (diff - canvas.getHeight() * 0.008);
            } else if (count < 10) {
                Height += canvas.getHeight() * 0.002;
                Width += canvas.getWidth() * 0.002;
                X -= canvas.getWidth() * 0.007;
                Y += (diff - canvas.getHeight() * 0.01);
            } else {
                Width -= canvas.getWidth() * 0.002;
                X -= canvas.getWidth() * 0.0038;
                Y += (diff + canvas.getHeight() * 0.0038);
            }

            Height += diff;
            Width += canvas.getWidth() * 0.01;
            if (count == 18 || count == 19 || count == 20) {
                mIndex++;
                Height -= diff * 3;
                Y += diff * 3;
            }
            if (count == 18) {
                synchronized (gateLock) {
                    gateLock.notify();
                }
            }
        } else {
            canvas.drawBitmap(
                    mBitmaps[mIndex],
                    new Rect(0, 0, mBitmaps[mIndex].getWidth(), mBitmaps[mIndex].getHeight()),
                    new RectF(mX, mY, mX + mWidth, mY + mHeight),
                    mPaint);
        }

        return true;
    }

    public void drawStatic(Canvas canvas) {

        mX = (int) X;
        mY = (int) Y;
        mWidth = (int) Width;
        mHeight = (int) Height;
        canvas.drawBitmap(
                mBitmaps[mIndex],
                new Rect(0, 0, mBitmaps[mIndex].getWidth(), mBitmaps[mIndex].getHeight()),
                new RectF(mX, mY, mX + mWidth, mY + mHeight),
                mPaint);
    }

    public int getType() {
        return 4;
    }

    public int getCount() {
        return (count - 2);
    }

    public void setCount(int count) {
        this.count = count;
    }
}