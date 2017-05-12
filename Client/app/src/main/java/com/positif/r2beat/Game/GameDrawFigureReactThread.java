package com.positif.r2beat.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.positif.r2beat.Game.FigureReact.FailBarLeft;
import com.positif.r2beat.Game.FigureReact.FailBarRight;
import com.positif.r2beat.Game.FigureReact.FailBlock;
import com.positif.r2beat.Game.FigureReact.FailConeLeft;
import com.positif.r2beat.Game.FigureReact.FailConeRight;
import com.positif.r2beat.Game.FigureReact.FailGate;

import java.util.List;


public class GameDrawFigureReactThread extends Thread {

    private SurfaceHolder surfaceHolder = null;

    private Background background;
    private RhythmPointList rhythmPointList;
    private Score score;
    private List<ActElement> actElements;
    private Rank rank = null;

    private ReactFigure reactFigure = null;


    public void initial(
            Context context,
            Background background,
            RhythmPointList rhythmPointList,
            Score score, List<ActElement> actElements,
            Rank rank,
            int failType, int screenWidth, int screenHeight) {

        this.background = background;
        this.rhythmPointList = rhythmPointList;
        this.score = score;

        this.actElements = actElements;

        if (rank != null)
            this.rank = rank;
        switch (failType) {
            case 1: {
                reactFigure = new FailConeLeft(context);
                break;
            }
            case 2: {
                reactFigure = new FailConeRight(context);
                break;
            }
            case 3: {
                reactFigure = new FailBlock(context);
                break;
            }
            case 4: {
                reactFigure = new FailGate(context);
                break;
            }
            case 5: {
                reactFigure = new FailBarLeft(context);
                break;
            }
            case 6: {
                reactFigure = new FailBarRight(context);
                break;
            }
        }
    }


    @Override
    public void run() {
        try {
            reactFigure.drawPrepare();
            int fragmentCount = 8;
            while ((fragmentCount--) != 0) {
                sleep(30);
                drawFail();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void drawFail() {
        synchronized (surfaceHolder) {
            Canvas canvas = surfaceHolder.lockCanvas();

            background.draw(canvas, true);
            int i = actElements.size() - 1;
            for (; i >= 0; i--) {
                actElements.get(i).draw(canvas, true);
            }
            score.draw(canvas, true);
            reactFigure.drawFail(canvas);
            rhythmPointList.draw(canvas, true);
            if (rank != null) {
                rank.draw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void setHolder(SurfaceHolder sh) {
        surfaceHolder = sh;
    }


}

