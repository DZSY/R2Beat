package com.positif.r2beat.Game.CollisionDetect;

import com.positif.r2beat.Game.GameThread;


public class DetectBlockThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    public int blockScore = 0;
    private GameThread gameThread;
    private Object blockLock;


    public void initial(GameThread gameThread, Object blockLock) {
        this.gameThread = gameThread;
        this.blockLock = blockLock;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                synchronized (blockLock) {
                    blockLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (blockScore > 60) {
                gameThread.setSucceed(blockScore, 3);
                blockScore = 0;
            } else
                gameThread.setFail(3);
        }
    }

    public synchronized void scoreDec() {
        if (blockScore > 0)
            blockScore -= 10;
    }


}

