package com.positif.r2beat.Game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;


public class GameMusicAddElementThread extends Thread {

    public boolean pause = false;
    public boolean stop = false;
    private MediaPlayer mediaPlayer = null;
    private Object lock;
    private GameThread gameThread;
    private Context context;
    private Object addelementLock;
    private Object coneleftLock;
    private Object conerightLock;
    private Object blockLock;
    private Object gateLock;
    private Object barleftLock;
    private Object barrightLock;

    private int musicSection;

    public void initial(
            Context context,
            GameThread gameThread,
            String path,
            Object lock,
            Object addelementLock,
            Object coneleftLock,
            Object conerightLock,
            Object blockLock,
            Object gateLock,
            Object barleftLock,
            Object barrightLock,
            int musicSection) {
        try {
            AssetFileDescriptor assetFileDescritor = context.getAssets().openFd(path);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    assetFileDescritor.getFileDescriptor(),
                    assetFileDescritor.getStartOffset(),
                    assetFileDescritor.getLength());
            mediaPlayer.setVolume(2f, 2f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
        this.lock = lock;
        this.addelementLock = addelementLock;
        this.gameThread = gameThread;
        this.gameThread = gameThread;
        this.addelementLock = addelementLock;
        this.coneleftLock = coneleftLock;
        this.conerightLock = conerightLock;
        this.blockLock = blockLock;
        this.gateLock = gateLock;
        this.barleftLock = barleftLock;
        this.barrightLock = barrightLock;
        this.musicSection = musicSection;
    }

    @Override
    public void run() {
        int temp = -1;
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            synchronized (lock) {
                lock.notify();
            }
            while (!stop) {
                if (pause) {
                    pause = false;
                    synchronized (lock) {
                        mediaPlayer.pause();
                        lock.wait();
                        synchronized (addelementLock) {
                            addelementLock.notify();
                        }
                        mediaPlayer.start();
                    }
                } else {
                    sleep(10);
                    if (!mediaPlayer.isPlaying()) {
                        synchronized (gameThread.stop) {
                            gameThread.stop = true;
                        }
                    } else {
                        int i = (mediaPlayer.getCurrentPosition() * 16 / musicSection) + (musicSection / 16) / 50;
                        if (i >= 0 && i < gameThread.elementList.length() && gameThread.elementList.charAt(i) != '0' && temp != i) {
                            gameThread.addElement(i);
                            temp = i;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized int getDuration() {
        return mediaPlayer.getDuration();
    }

}

