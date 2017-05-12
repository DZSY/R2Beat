package com.positif.r2beat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.positif.r2beat.Game.GameThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class FightGameView extends SoloGameView implements SurfaceHolder.Callback {

    private static final String CONNECT = "0";
    private static final String UPDATE = "1";
    private static final String EXIT = "2";
    private static final String GAMEUPDATE = "0";
    private static final String GAMEEXIT = "1";
    private final int PORT = 12345;
    public int screenWidth;
    public int screenHeight;
    public GameThread gameThread = null;
    public String rank1 = new String();
    public String rank2 = new String();
    public boolean meRank1 = false;
    private String HOST = "WAITING";
    private ClientThread clientThread = new ClientThread();
    private Object lock = new Object();
    private Context context = null;
    private SurfaceHolder holder = this.getHolder();
    private String musicName = "WAITING";
    private String alias = "WAITING";


    public FightGameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.getHolder().addCallback(this);
        type = 1;
    }


    private void init() {
        gameThread = null;
        gameThread = new GameThread();
        gameThread.setHolder(holder);

        new Thread() {
            public void run() {
                try {
                    while ("WAITING".equals(musicName) || "WAITING".equals(alias) || "WAITING".equals(HOST))
                        sleep(50);
                    clientThread.initial(
                            context,
                            HOST,
                            musicName,
                            alias,
                            lock);
                    clientThread.start();
                    Canvas canvas = holder.lockCanvas();
                    Bitmap waiting = BitmapFactory.decodeResource(context.getResources(), R.drawable.waiting);
                    Paint paint = new Paint();
                    canvas.drawBitmap(
                            waiting,
                            new Rect(0, 0, waiting.getWidth(), waiting.getHeight()),
                            new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                            paint);
                    holder.unlockCanvasAndPost(canvas);
                    synchronized (lock) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    goBack("出现了奇怪的问题:(");
                }
                gameThread.initial(context, FightGameView.this, screenWidth, screenHeight, musicName);
                gameThread.start();
            }
        }.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {

        this.screenWidth = width;
        this.screenHeight = height;
        init();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        synchronized (gameThread.stop) {
            gameThread.stop = true;
            clientThread.sendMsg(EXIT);
        }
    }


    public synchronized void setAttr(String HOST, String musicName, String alias) {
        this.HOST = HOST;
        this.musicName = musicName;
        this.alias = alias;
    }

    public void updateDuration(int duration) {
        clientThread.sendMsg(UPDATE + " " + duration);
    }

    private void goBack(String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("Message", msg);
        context.startActivity(intent);
        clientThread.interrupt();
    }

    class ClientThread extends Thread {

        private final int PORT = 12345;
        private Context context;
        private Socket socket = null;
        private BufferedReader in = null;
        private PrintWriter out = null;
        private String content = "";
        private StringBuilder sb = null;
        private String HOST = null;
        private String alias;
        private String musicName;
        private Object lock;

        public void initial(Context context, String HOST, String musicName, String alias, Object lock) {
            this.context = context;
            this.HOST = HOST;
            this.musicName = musicName;
            this.alias = alias;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                        "UTF-8"));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);
            } catch (IOException e) {
                e.printStackTrace();
                goBack("无法连接此服务器:(");
            }

            sendMsg(CONNECT + " " + alias + " " + musicName);
            try {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        while ((content = in.readLine()) == null) ;
                        if (content.equals("OK")) {
                            try {
                                while ((content = in.readLine()) == null) ;
                            } catch (RuntimeException e) {
                                goBack("太久没有匹配上了,换首歌试试?");
                            }
                            if (content.equals("OK")) {
                                synchronized (lock) {
                                    lock.notify();
                                }
                                while (true) {
                                    if ((content = in.readLine()) != null) {
                                        String temp[] = content.split(" ");

                                        if (GAMEUPDATE.equals(temp[0])) {
                                            rank1 = temp[1];
                                            rank2 = temp[2];
                                            if (rank1.equals(alias)) {
                                                meRank1 = true;
                                            } else {
                                                meRank1 = false;
                                            }
                                        } else if (GAMEEXIT.equals(temp[0])) {
                                            goBack("你的对手退出了:(");
                                        }
                                    }
                                }
                            } else {
                                goBack("匹配失败:(");
                            }
                        } else {
                            goBack("这个昵称好像已经被使用了哦");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void sendMsg(String msg) {
            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println(msg);
                }
            }
        }
    }
}