package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectConeRightThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int conerightScore = 0;
    private GameThread gameThread;
    private Object conerightLock;


    public void initial(GameThread gameThread, Object conerightLock) {
        this.gameThread = gameThread;
        this.conerightLock = conerightLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (conerightLock) {
                    conerightLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (conerightScore > 60) {
                gameThread.setSucceed(conerightScore, 2);
                conerightScore = 0;
            } else
                gameThread.setFail(2);
        }
    }

    public synchronized void scoreDec() {
        if (conerightScore > 0)
            conerightScore -= 10;
    }


}

