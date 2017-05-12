package com.positif.r2beat;


import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.positif.r2beat.Game.GameThread;

public class SoloGameView extends SurfaceView implements SurfaceHolder.Callback {

    public int type;
    public int screenWidth;
    public int screenHeight;
    public GameThread gameThread = null;
    private Context context = null;
    private SurfaceHolder holder = this.getHolder();
    private String musicName = "WAITING";

    public SoloGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.getHolder().addCallback(this);
        type = 0;
    }


    private void init() {
        gameThread = null;
        gameThread = new GameThread();
        gameThread.setHolder(holder);
        while (musicName == "WAITING") ;
        gameThread.initial(context, this, screenWidth, screenHeight, musicName);
        gameThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {

        this.screenWidth = width;
        this.screenHeight = height;
        init();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        synchronized (gameThread.stop) {
            gameThread.stop = true;
        }
    }

    public synchronized void setMusicName(String musicName) {
        this.musicName = musicName;
    }


}
