package com.positif.r2beat.Game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;


public class GameSoundThread extends Thread {

    private MediaPlayer mediaPlayer = null;

    public void initial(Context context, boolean collide) {
        try {
            AssetFileDescriptor assetFileDescritor;
            mediaPlayer = new MediaPlayer();
            if (collide) {
                assetFileDescritor = context.getAssets().openFd("fail.mp3");
                mediaPlayer.setVolume(0.3f, 0.3f);
            } else {
                assetFileDescritor = context.getAssets().openFd("ready.mp3");
                mediaPlayer.setVolume(0.5f, 0.5f);
            }
            mediaPlayer.setDataSource(
                    assetFileDescritor.getFileDescriptor(),
                    assetFileDescritor.getStartOffset(),
                    assetFileDescritor.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            while (mediaPlayer.isPlaying()) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

