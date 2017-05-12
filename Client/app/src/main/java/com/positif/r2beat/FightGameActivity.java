package com.positif.r2beat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class FightGameActivity extends AppCompatActivity implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private FightGameView fightGameView;

    private String alias;
    private String musicName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_game);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏状态栏

        final EditText input = new EditText(this);
        input.setText("10.110.34.77");

        Intent intent = getIntent();
        musicName = intent.getStringExtra("MusicName");
        alias = intent.getStringExtra("Alias");

        fightGameView = (FightGameView) findViewById(R.id.fightgameView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入服务器地址")
                .setIcon(R.drawable.dialogicon)
                .setView(input)
                .setPositiveButton("好的",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    fightGameView.setAttr(
                                            input.getText().toString(),
                                            musicName,
                                            alias);
                                    fightGameView.setOnTouchListener(FightGameActivity.this);
                                    fightGameView.setClickable(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                gestureDetector = new GestureDetector(new simpleGestureListener());
                            }
                        })
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }


    @Override
    protected void onResume() {
        try {
            synchronized (fightGameView.gameThread.pause) {
                fightGameView.gameThread.pause = false;
            }
        } catch (NullPointerException e) {
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        try {
            synchronized (fightGameView.gameThread.stop) {
                fightGameView.gameThread.stop = true;
            }
        } catch (NullPointerException e) {
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        try {
            synchronized (fightGameView.gameThread.stop) {
                fightGameView.gameThread.stop = true;
            }
        } catch (NullPointerException e) {
        }
        fightGameView = null;
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
            if (e.getX() > (fightGameView.screenWidth / 2))
                fightGameView.gameThread.detectConeLeftThread.coneleftScore = 100;
            else {
                fightGameView.gameThread.detectConeRightThread.conerightScore = 100;
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
                fightGameView.gameThread.detectGateThread.gateScore = 100;
            if (velocityY < -200)
                fightGameView.gameThread.detectBlockThread.blockScore = 100;
            return true;
        }

        /*****
         * OnDoubleTapListener的函数
         *****/
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (e.getX() > (fightGameView.screenWidth / 2))
                fightGameView.gameThread.detectBarRightThread.barrightScore = 100;
            else {
                fightGameView.gameThread.detectBarLeftThread.barleftScore = 100;
            }
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {

            return true;
        }

    }
}
