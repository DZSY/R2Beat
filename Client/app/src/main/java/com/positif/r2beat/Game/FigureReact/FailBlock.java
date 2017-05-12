package com.positif.r2beat.Game.FigureReact;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.positif.r2beat.Game.ReactFigure;
import com.positif.r2beat.R;

public class FailBlock implements ReactFigure {
    public int mY = 0;
    int mX = 0;
    int[] mPicsFail = {
            R.drawable.figure0,
            R.drawable.figurefailup1,
            R.drawable.figurefailup1,
            R.drawable.figurefailup2,
            R.drawable.figurefailup2,
            R.drawable.figurefailup2,
            R.drawable.figurefailup1,
            R.drawable.figurefailup1,
            R.drawable.figurefailup1,
            R.drawable.figure0,
    };

    Bitmap mBitmapsFail[] = new Bitmap[mPicsFail.length];

    private int mIndex = 0;

    private Context mCtx = null;
    private Resources res = null;
    private Paint mPaint = null;


    public FailBlock(Context context) {
        res = context.getResources();

        for (int i = 0; i < mPicsFail.length; i++) {
            mBitmapsFail[i] = BitmapFactory.decodeResource(res, mPicsFail[i]);
        }
        mPaint = new Paint();
    }

    public void drawPrepare() {
        mIndex = 0;
    }

    public void drawFail(Canvas canvas) {

        mX = (int) (canvas.getWidth() * 0.325);
        mY = (int) (canvas.getHeight() * 0.65);
        canvas.drawBitmap(
                mBitmapsFail[mIndex],
                new Rect(0, 0, mBitmapsFail[mIndex].getWidth(), mBitmapsFail[mIndex].getHeight()),
                new RectF(
                        mX,
                        mY,
                        mX + (int) (canvas.getWidth() * 0.35),
                        mY + (int) (canvas.getHeight() * 0.48)),
                mPaint);

        if (mIndex < mBitmapsFail.length - 1)
            mIndex++;
    }

    public int getType() {
        return 3;
    }


}
