package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectBarRightThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int barrightScore = 0;
    private GameThread gameThread;
    private Object barrightLock;


    public void initial(GameThread gameThread, Object barrightLock) {
        this.gameThread = gameThread;
        this.barrightLock = barrightLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (barrightLock) {
                    barrightLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (barrightScore > 60) {
                gameThread.setSucceed(barrightScore, 6);
                barrightScore = 0;
            } else
                gameThread.setFail(6);
        }
    }

    public synchronized void scoreDec() {
        if (barrightScore > 0)
            barrightScore -= 10;
    }


}

