package com.positif.r2beat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class SoloGameActivity extends AppCompatActivity implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private SoloGameView soloGameView;
    private String alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_game);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏状态栏

        soloGameView = (SoloGameView) findViewById(R.id.sologameView);
        soloGameView.setOnTouchListener(this);
        soloGameView.setClickable(true);
        Intent intent = getIntent();
        alias = intent.getStringExtra("Alias");
        soloGameView.setMusicName(intent.getStringExtra("MusicName"));
        gestureDetector = new GestureDetector(new simpleGestureListener());
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }


    @Override
    protected void onResume() {
        try {
            synchronized (soloGameView.gameThread.pause) {
                soloGameView.gameThread.pause = false;
            }
        } catch (NullPointerException e) {
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        try {
            synchronized (soloGameView.gameThread.stop) {
                soloGameView.gameThread.stop = true;
            }
        } catch (NullPointerException e) {
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        try {
            synchronized (soloGameView.gameThread.stop) {
                soloGameView.gameThread.stop = true;
            }
        } catch (NullPointerException e) {
        }
        soloGameView = null;
        super.onDestroy();
    }


    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }

    private class simpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        /*****
         * OnGestureListener的函数
         *****/
        public boolean onDown(MotionEvent e) {

            return false;
        }

        public void onShowPress(MotionEvent e) {

        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (e.getX() > (soloGameView.screenWidth / 2))
                soloGameView.gameThread.detectConeLeftThread.coneleftScore = 100;
            else {
                soloGameView.gameThread.detectConeRightThread.conerightScore = 100;
            }
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            return true;
        }

        public void onLongPress(MotionEvent e) {

        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (velocityY > 200)
                soloGameView.gameThread.detectGateThread.gateScore = 100;
            if (velocityY < -200)
                soloGameView.gameThread.detectBlockThread.blockScore = 100;
            return true;
        }

        /*****
         * OnDoubleTapListener的函数
         *****/
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (e.getX() > (soloGameView.screenWidth / 2))
                soloGameView.gameThread.detectBarRightThread.barrightScore = 100;
            else {
                soloGameView.gameThread.detectBarLeftThread.barleftScore = 100;
            }
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {

            return true;
        }

    }

}
