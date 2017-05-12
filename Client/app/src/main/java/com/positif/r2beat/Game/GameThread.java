package com.positif.r2beat.Game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.positif.r2beat.FightGameView;
import com.positif.r2beat.Game.CollisionDetect.DetectBarLeftThread;
import com.positif.r2beat.Game.CollisionDetect.DetectBarRightThread;
import com.positif.r2beat.Game.CollisionDetect.DetectBlockThread;
import com.positif.r2beat.Game.CollisionDetect.DetectConeLeftThread;
import com.positif.r2beat.Game.CollisionDetect.DetectConeRightThread;
import com.positif.r2beat.Game.CollisionDetect.DetectGateThread;
import com.positif.r2beat.Game.Props.BarLeft;
import com.positif.r2beat.Game.Props.BarRight;
import com.positif.r2beat.Game.Props.Block;
import com.positif.r2beat.Game.Props.ConeLeft;
import com.positif.r2beat.Game.Props.ConeRight;
import com.positif.r2beat.Game.Props.Gate;
import com.positif.r2beat.MainActivity;
import com.positif.r2beat.R;
import com.positif.r2beat.SoloGameView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class GameThread extends Thread {

    public Boolean pause = false;
    public Boolean stop = false;
    public List<ActElement> actElements = new ArrayList();
    public RhythmPointList rhythmPointList;
    public int succeedType = 0;
    public int succeedCount = -1;
    public int succeedScore = -1;
    public boolean fail = false;
    public int failType = 1;
    public GameMusicAddElementThread gameMusicAddElementThread = new GameMusicAddElementThread();
    public DetectConeLeftThread detectConeLeftThread = new DetectConeLeftThread();
    public DetectConeRightThread detectConeRightThread = new DetectConeRightThread();
    public DetectBlockThread detectBlockThread = new DetectBlockThread();
    public DetectGateThread detectGateThread = new DetectGateThread();
    public DetectBarLeftThread detectBarLeftThread = new DetectBarLeftThread();
    public DetectBarRightThread detectBarRightThread = new DetectBarRightThread();
    public StringBuffer elementList = new StringBuffer();
    private SurfaceHolder surfaceHolder = null;
    private FightGameView gameView;
    private Background background;
    private Figure figure;
    private Rank rank;
    private Score score;
    private Context context;
    private int screenWidth;
    private int screenHeight;
    private String musicPath;
    private String assetPath;
    private Integer protectCount = -1;
    private double finalScore = 0;
    private double finalCount = 0;
    private int finalDuration = 0;
    private Object lock = new Object();
    private Object addelementLock = new Object();
    private Object coneleftLock = new Object();
    private Object conerightLock = new Object();
    private Object blockLock = new Object();
    private Object gateLock = new Object();
    private Object barleftLock = new Object();
    private Object barrightLock = new Object();
    private int musicSection;

    public void initial(Context context, SoloGameView gameView, int screenWidth, int screenHeight, String path) {
        this.context = context;

        this.gameView = gameView.type == 1 ? (FightGameView) gameView : null;
        if (gameView != null) {
            rank = new Rank(context);
        }

        background = new Background(context);
        figure = new Figure(context, this);
        score = new Score(context, this);
        musicPath = path + ".mp3";
        assetPath = path + ".txt";

        switch (path) {
            case "practice": {
                musicSection = 3000;
                break;
            }
            case "yicijiuhao": {
                musicSection = 2824;
                break;
            }
            case "pingfanzhilu": {
                musicSection = 2800;
                break;
            }
            case "haojiubujian": {
                musicSection = 3333;
                break;
            }
            default: {
                musicSection = 3000;
            }
        }

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        rhythmPointList = new RhythmPointList(context, this);

        gameMusicAddElementThread.initial(
                context,
                this,
                musicPath,
                lock,
                addelementLock,
                coneleftLock,
                conerightLock,
                blockLock,
                gateLock,
                barleftLock,
                barrightLock,
                musicSection);

        detectConeLeftThread.initial(this, coneleftLock);
        detectConeRightThread.initial(this, conerightLock);
        detectBlockThread.initial(this, blockLock);
        detectGateThread.initial(this, gateLock);
        detectBarLeftThread.initial(this, barleftLock);
        detectBarRightThread.initial(this, barrightLock);
    }


    @Override
    public void run() {
        try {
            ready();
            GameSoundThread gameSoundThread = new GameSoundThread();
            gameSoundThread.initial(context, false);
            gameSoundThread.start();
            gameSoundThread.join();
            Reader reader = new InputStreamReader(context.getAssets().open(assetPath));
            int temp;
            while ((temp = reader.read()) != -1) {
                char c = (char) temp;
                if (c != '\n')
                    elementList.append(c);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        detectConeLeftThread.start();
        detectConeRightThread.start();
        detectBlockThread.start();
        detectGateThread.start();
        detectBarLeftThread.start();
        detectBarRightThread.start();

        gameMusicAddElementThread.start();


        while (!stop) {
            try {
                sleep(50);
                if (gameView != null) {
                    rank.updateRank(gameView.rank1, gameView.rank2, gameView.meRank1);
                }
                drawAll();
                synchronized (protectCount) {
                    if (protectCount >= 0) {
                        protectCount = protectCount + 1;
                        if (protectCount.equals(Integer.valueOf(20))) {
                            protectCount = -1;
                        }
                    }
                }

                if (fail) {
                    gameMusicAddElementThread.pause = true;
                    GameSoundThread gameSoundThread = new GameSoundThread();
                    gameSoundThread.initial(context, true);
                    gameSoundThread.start();

                    GameDrawFigureReactThread gameDrawFigureReactThread = new GameDrawFigureReactThread();
                    gameDrawFigureReactThread.setHolder(surfaceHolder);
                    if (gameView == null) {
                        gameDrawFigureReactThread.initial(context, background, rhythmPointList, score, actElements, null, failType, screenWidth, screenHeight);
                    } else {
                        gameDrawFigureReactThread.initial(context, background, rhythmPointList, score, actElements, rank, failType, screenWidth, screenHeight);
                    }
                    gameDrawFigureReactThread.start();
                    gameDrawFigureReactThread.join();
                    //每次摔倒需要240毫秒
                    finalDuration += 240;
                    if (gameView != null) {
                        gameView.updateDuration(finalDuration);
                    }
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                    fail = false;
                    protectCount = 0;
                }

                detectConeLeftThread.scoreDec();
                detectConeRightThread.scoreDec();
                detectBlockThread.scoreDec();
                detectGateThread.scoreDec();
                detectBarLeftThread.scoreDec();
                detectBarRightThread.scoreDec();

                if (pause) {
                    synchronized (pause) {
                        pause.wait();
                    }
                    synchronized (lock) {
                        gameMusicAddElementThread.pause = true;
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }
        gameMusicAddElementThread.interrupt();
        detectBarLeftThread.interrupt();
        detectBarRightThread.interrupt();
        detectBlockThread.interrupt();
        detectConeLeftThread.interrupt();
        detectConeRightThread.interrupt();
        detectGateThread.interrupt();

        background = null;
        figure = null;
        finalDuration += gameMusicAddElementThread.getDuration();
        drawFinal();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    private void ready() {
        Canvas canvas = surfaceHolder.lockCanvas();
        Bitmap ready = BitmapFactory.decodeResource(context.getResources(), R.drawable.ready);
        Paint paint = new Paint();
        canvas.drawBitmap(
                ready,
                new Rect(0, 0, ready.getWidth(), ready.getHeight()),
                new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addElement(int index) {
        synchronized (actElements) {
            switch (elementList.charAt(index)) {
                case '1': {
                    rhythmPointList.list.add(0);
                    actElements.add(new ConeLeft(context, screenWidth, screenHeight, coneleftLock));
                    break;
                }
                case '2': {
                    rhythmPointList.list.add(0);
                    actElements.add(new ConeRight(context, screenWidth, screenHeight, conerightLock));
                    break;
                }
                case '3': {
                    rhythmPointList.list.add(0);
                    actElements.add(new Block(context, screenWidth, screenHeight, blockLock));
                    break;
                }
                case '4': {
                    rhythmPointList.list.add(0);
                    actElements.add(new Gate(context, screenWidth, screenHeight, gateLock));
                    break;
                }
                case '5': {
                    rhythmPointList.list.add(0);
                    actElements.add(new BarLeft(context, screenWidth, screenHeight, barleftLock));
                    break;
                }
                case '6': {
                    rhythmPointList.list.add(0);
                    actElements.add(new BarRight(context, screenWidth, screenHeight, barrightLock));
                    break;
                }
            }
        }
    }


    private void drawAll() {

        synchronized (surfaceHolder) {
            Canvas canvas = surfaceHolder.lockCanvas();
            background.draw(canvas, false);

            List<ActElement> temp = new ArrayList<>(actElements);
            if (!fail) {
                score.draw(canvas, false);
            }

            int figurePos = figure.getCount();
            int i = temp.size() - 1;
            for (; i >= 0; i--) {
                int count = temp.get(i).getCount();
                if (count > figurePos)
                    break;
                temp.get(i).draw(canvas, false);
            }
            if (succeedCount < 0)
                figure.draw(canvas, protectCount);
            else
                figure.drawSucceed(canvas, succeedType, succeedCount);

            for (; i >= 0; i--) {
                temp.get(i).draw(canvas, false);
                if (temp.get(i).getCount() > figurePos + 2)
                    actElements.remove(temp.get(i));
            }
            rhythmPointList.draw(canvas, false);
            if (gameView != null) {
                rank.draw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawFinal() {
        synchronized (surfaceHolder) {
            Canvas canvas = surfaceHolder.lockCanvas();
            FinalResult finalResult = new FinalResult(context, finalDuration, finalScore);
            finalResult.draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public synchronized void setSucceed(int score, int type) {
        finalScore = (finalCount * finalScore + score) / (finalCount + 1);
        finalCount++;

        succeedScore = score;
        succeedType = type;
        succeedCount = 0;
    }

    public synchronized void succeedcountPlus() {
        if (succeedCount == 7) {
            succeedCount = -1;
            figure.mIndex = 0;
        } else
            succeedCount++;

    }

    public synchronized void setFail(int type) {
        if (protectCount < 0) {
            failType = type;
            fail = true;
        }
    }

    public void setHolder(SurfaceHolder sh) {
        surfaceHolder = sh;
    }


}

