package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectConeLeftThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int coneleftScore = 0;
    private GameThread gameThread;
    private Object coneleftLock;


    public void initial(GameThread gameThread, Object coneleftLock) {
        this.gameThread = gameThread;
        this.coneleftLock = coneleftLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (coneleftLock) {
                    coneleftLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (coneleftScore > 60) {
                gameThread.setSucceed(coneleftScore, 1);
                coneleftScore = 0;
            } else
                gameThread.setFail(1);
        }
    }

    public synchronized void scoreDec() {
        if (coneleftScore > 0)
            coneleftScore -= 10;
    }


}

