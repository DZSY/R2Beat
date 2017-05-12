package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectGateThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int gateScore = 0;
    private GameThread gameThread;
    private Object gateLock;


    public void initial(GameThread gameThread, Object gateLock) {
        this.gameThread = gameThread;
        this.gateLock = gateLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (gateLock) {
                    gateLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (gateScore > 60) {
                gameThread.setSucceed(gateScore, 4);
                gateScore = 0;
            } else
                gameThread.setFail(4);
        }
    }

    public synchronized void scoreDec() {
        if (gateScore > 0)
            gateScore -= 10;
    }


}

