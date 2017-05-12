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

public class Figure {

    public int mY = 0;
    public int mIndex = 0;
    int mX = 0;
    int[] picsNormal = {
            R.drawable.figure0,
            R.drawable.figure1,
            R.drawable.figure2,
            R.drawable.figure3,
            R.drawable.figure11,
            R.drawable.figure22,
            R.drawable.figure33,
    };
    int[] picsProtected = {
            R.drawable.figure0protected,
            R.drawable.figure1protected,
            R.drawable.figure2protected,
            R.drawable.figure3protected,
            R.drawable.figure11protected,
            R.drawable.figure22protected,
            R.drawable.figure33protected
    };
    int[] picsconeLeft = {
            R.drawable.figure0,
            R.drawable.figureleft1,
            R.drawable.figureleft2,
            R.drawable.figureleft3,
    };
    int[] picsconeRight = {
            R.drawable.figure0,
            R.drawable.figureright1,
            R.drawable.figureright2,
            R.drawable.figureright3,
    };
    int[] picsBlock = {
            R.drawable.figure0,
            R.drawable.figureup1,
            R.drawable.figureup2,
            R.drawable.figureup3,
    };
    int[] picsGate = {
            R.drawable.figure0,
            R.drawable.figuredown1,
            R.drawable.figuredown2,
            R.drawable.figuredown3,
    };
    int[] picsbarLeft = {
            R.drawable.figure0,
            R.drawable.figureleftup1,
            R.drawable.figureleftup2,
            R.drawable.figureleftup3,
            R.drawable.figureleftup4,
    };
    int[] picsbarRight = {
            R.drawable.figure0,
            R.drawable.figurerightup1,
            R.drawable.figurerightup2,
            R.drawable.figurerightup3,
            R.drawable.figurerightup4,
    };
    Bitmap bitmapsNormal[] = new Bitmap[picsNormal.length];
    Bitmap bitmapsProtect[] = new Bitmap[picsProtected.length];
    Bitmap bitmapsconeLeft[] = new Bitmap[picsconeLeft.length];
    Bitmap bitmapsconeRight[] = new Bitmap[picsconeRight.length];
    Bitmap bitmapsBlock[] = new Bitmap[picsBlock.length];
    Bitmap bitmapsGate[] = new Bitmap[picsGate.length];
    Bitmap bitmapsbarLeft[] = new Bitmap[picsbarLeft.length];
    Bitmap bitmapsbarRight[] = new Bitmap[picsbarRight.length];
    int[] Normal = {0, 1, 2, 3, 3, 2, 1, 0, 4, 5, 6, 6, 5, 4, 0};
    int[] Protect = {0, 1, 2, 3, 3, 2, 1, 0, 4, 5, 6, 6, 5, 4, 0};
    int[] coneLeft = {0, 1, 1, 2, 3, 3, 2, 1};
    int[] coneRight = {0, 1, 1, 2, 3, 3, 2, 1};
    int[] Block = {0, 1, 1, 2, 3, 3, 2, 1};
    int[] Gate = {0, 1, 1, 2, 3, 3, 2, 1};
    int[] barLeft = {0, 1, 2, 3, 4, 3, 2, 1};
    int[] barRight = {0, 1, 2, 3, 4, 3, 2, 1};
    private Context mCtx = null;
    private Resources res = null;
    private Paint mPaint = null;

    private int count = 0;
    private GameThread gameThread;


    public Figure(Context ctx, GameThread gameThread) {
        mCtx = ctx;
        res = mCtx.getResources();
        for (int i = 0; i < picsNormal.length; i++) {
            bitmapsNormal[i] = BitmapFactory.decodeResource(res, picsNormal[i]);
            bitmapsProtect[i] = BitmapFactory.decodeResource(res, picsProtected[i]);
        }

        for (int i = 0; i < picsconeLeft.length; i++) {
            bitmapsconeLeft[i] = BitmapFactory.decodeResource(res, picsconeLeft[i]);
            bitmapsconeRight[i] = BitmapFactory.decodeResource(res, picsconeRight[i]);
            bitmapsBlock[i] = BitmapFactory.decodeResource(res, picsBlock[i]);
            bitmapsGate[i] = BitmapFactory.decodeResource(res, picsGate[i]);
        }

        for (int i = 0; i < picsbarLeft.length; i++) {
            bitmapsbarLeft[i] = BitmapFactory.decodeResource(res, picsbarLeft[i]);
            bitmapsbarRight[i] = BitmapFactory.decodeResource(res, picsbarRight[i]);
        }
        mPaint = new Paint();
        this.gameThread = gameThread;
    }


    public boolean draw(Canvas canvas, Integer protectCount) {

        mX = (int) (canvas.getWidth() * 0.325);
        mY = (int) (canvas.getHeight() * 0.65);
        synchronized (protectCount) {
            if (protectCount >= 0) {
                canvas.drawBitmap(
                        bitmapsProtect[Protect[mIndex]],
                        new Rect(0, 0, bitmapsProtect[Protect[mIndex]].getWidth(), bitmapsProtect[Protect[mIndex]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
            } else {
                canvas.drawBitmap(
                        bitmapsNormal[Normal[mIndex]],
                        new Rect(0, 0, bitmapsNormal[Normal[mIndex]].getWidth(), bitmapsNormal[Normal[mIndex]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
            }
        }
        if (count % 2 == 1) {
            mIndex += 1;
            mIndex %= picsNormal.length;
        }
        count++;

        return true;
    }

    public void drawSucceed(Canvas canvas, int type, int succeedCount) {

        mX = (int) (canvas.getWidth() * 0.325);
        mY = (int) (canvas.getHeight() * 0.65);
        switch (type) {
            case 1: {
                canvas.drawBitmap(
                        bitmapsconeLeft[coneLeft[succeedCount]],
                        new Rect(0, 0, bitmapsconeLeft[coneLeft[succeedCount]].getWidth(), bitmapsconeLeft[coneLeft[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
            case 2: {
                canvas.drawBitmap(
                        bitmapsconeRight[coneRight[succeedCount]],
                        new Rect(0, 0, bitmapsconeRight[coneRight[succeedCount]].getWidth(), bitmapsconeRight[coneRight[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
            case 3: {
                canvas.drawBitmap(
                        bitmapsBlock[Block[succeedCount]],
                        new Rect(0, 0, bitmapsBlock[Block[succeedCount]].getWidth(), bitmapsBlock[Block[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
            case 4: {
                canvas.drawBitmap(
                        bitmapsGate[Gate[succeedCount]],
                        new Rect(0, 0, bitmapsGate[Gate[succeedCount]].getWidth(), bitmapsGate[Gate[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
            case 5: {
                canvas.drawBitmap(
                        bitmapsbarLeft[barLeft[succeedCount]],
                        new Rect(0, 0, bitmapsbarLeft[barLeft[succeedCount]].getWidth(), bitmapsbarLeft[barLeft[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
            case 6: {
                canvas.drawBitmap(
                        bitmapsbarRight[barRight[succeedCount]],
                        new Rect(0, 0, bitmapsbarRight[barRight[succeedCount]].getWidth(), bitmapsbarRight[barRight[succeedCount]].getHeight()),
                        new RectF(
                                mX,
                                mY,
                                mX + (int) (canvas.getWidth() * 0.35),
                                mY + (int) (canvas.getHeight() * 0.48)),
                        mPaint);
                break;
            }
        }
        gameThread.succeedcountPlus();
    }

    public int getCount() {
        return 20;
    }

}
