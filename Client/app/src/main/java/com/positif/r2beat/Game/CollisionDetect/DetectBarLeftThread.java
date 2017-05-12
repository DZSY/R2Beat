package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectBarLeftThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int barleftScore = 0;
    private GameThread gameThread;
    private Object barleftLock;


    public void initial(GameThread gameThread, Object barleftLock) {
        this.gameThread = gameThread;
        this.barleftLock = barleftLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (barleftLock) {
                    barleftLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (barleftScore > 60) {
                gameThread.setSucceed(barleftScore, 5);
                barleftScore = 0;
            } else
                gameThread.setFail(5);
        }
    }

    public synchronized void scoreDec() {
        if (barleftScore > 0)
            barleftScore -= 10;
    }


}

