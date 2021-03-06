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


public class BarLeft implements ActElement {

    int mX = 0;
    int mY = 0;
    int mWidth = 0;
    int mHeight = 0;
    double X;
    double Y;
    double Width;
    double Height;
    int[] mPics = {
            R.drawable.propsbarleft2,
            R.drawable.propsbarleft1,
            R.drawable.propsbarleft2,
    };

    private int count = 0;
    private int mIndex = 0;

    private Context mContext = null;
    private Resources res = null;
    private BitmapDrawable mDraw = null;
    private Paint mPaint = null;
    private Bitmap[] mBitmaps = null;


    private Object barleftLock;

    public BarLeft(Context ctx, int screenWidth, int screenHeight, Object barleftLock) {
        mContext = ctx;
        res = mContext.getResources();
        mBitmaps = new Bitmap[mPics.length];
        for (int i = 0; i < mPics.length; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(res, mPics[i]);
        }
        mPaint = new Paint();
        X = screenWidth * 0.447;
        Y = screenHeight * 0.365;
        Width = screenWidth * 0.06;
        Height = screenHeight * 0.06;
        this.barleftLock = barleftLock;
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
            count++;
            double diff = canvas.getHeight() * 0.009;
            if (count < 6) {
                Width += canvas.getWidth() * 0.004;
                X -= canvas.getWidth() * 0.005;
                Y += (diff + canvas.getHeight() * 0.003);
            } else if (count < 10) {
                X -= canvas.getWidth() * 0.004;
                Y += (diff + canvas.getHeight() * 0.016);
            } else {
                Height += canvas.getHeight() * 0.004;
                X -= canvas.getWidth() * 0.004;
                Y += (diff + canvas.getHeight() * 0.027);
            }
            Width += canvas.getWidth() * 0.003;
            Height += diff;
            if (count == 3 || count == 12)
                mIndex += 1;
            mIndex %= mPics.length;
            if (count == 18) {
                synchronized (barleftLock) {
                    barleftLock.notify();
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
        return 5;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}