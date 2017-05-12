import javafx.util.Pair;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Thread.sleep;


public class R2BeatServer {
    private static final int PORT = 12345;
    private ServerSocket server = null;
    private ExecutorService executorService = null;

    private static final String CONNECT = "0";

    private Map waiting_Map = new HashMap();
    private Map alias_socket_Map = new HashMap();

    public static void main(String[] args) {
        new R2BeatServer();
    }

    public R2BeatServer()
    {
        try
        {
            server = new ServerSocket(PORT);
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            executorService = Executors.newCachedThreadPool();
            System.out.println("R2BeatServer is running on " + ip + ":" + PORT + "...\n");
        }catch( Exception e ) {
            e.printStackTrace();
        }
        acceptClient();
    }

    private void acceptClient() {
        try {
            while(true)
            {
                Socket newClient = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
                String msg;
                while ((msg = in.readLine()) == null);
                System.out.println(msg);
                String temp[] = msg.split(" ");
                PrintWriter pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(newClient.getOutputStream(), "UTF-8")), true);
                //如果确定是连接的指令
                if (CONNECT.equals(temp[0])) {
                    String newAlias = temp[1];
                    String musicName = temp[2];
                    //如果这个昵称已经被使用了就返回NO
                    if (alias_socket_Map.containsKey(newAlias)){
                        pout.println("NO");
                    }
                    //如果昵称没有被使用就返回OK
                    else {
                        pout.println("OK");
                        alias_socket_Map.put(newAlias,newClient);
                        if (waiting_Map.containsValue(musicName)){
                            String rivalAlias = new String();
                            Set set = waiting_Map.entrySet();
                            Iterator it = set.iterator();
                            while( it.hasNext() ) {
                                Map.Entry entry = (Map.Entry)it.next();
                                if(entry.getValue().equals(musicName)) {
                                    rivalAlias = (String) entry.getKey();
                                    break;
                                }
                            }
                            waiting_Map.remove(rivalAlias);
                            executorService.execute(new Fight( rivalAlias, newAlias ));
                        }
                        else {
                            waiting_Map.put(newAlias,musicName);
                        }
                    }
                }
                //如果不是连接的指令就返回NO
                else {
                    System.out.println(msg);
                    pout.println("NO");
                }
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }



    class Fight implements Runnable
    {
        private Socket socket1;
        private Socket socket2;
        private BufferedReader input1 = null;
        private BufferedReader input2 = null;
        private PrintWriter output1;
        private PrintWriter output2;
        private String msg = "";

        private String player1Alias;
        private String player2Alias;
        private int player1Time = 0;
        private int player2Time = 0;

        private static final String UPDATE = "1";
        private static final String EXIT = "2";

        private static final String GAMEUPDATE = "0";
        private static final String GAMEEXIT = "1";

        boolean stop = false;

        public Fight(String player1Alias, String player2Alias) {
            this.player1Alias = player1Alias;
            this.player2Alias = player2Alias;
            socket1 = (Socket) alias_socket_Map.get(player1Alias);
            socket2 = (Socket) alias_socket_Map.get(player2Alias);
            try {
                input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
                output1 = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket1.getOutputStream(),
                                        "UTF-8")),
                        true);
                output2 = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket2.getOutputStream(),
                                        "UTF-8")),
                        true);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

        boolean handle1working = false;
        boolean handle2working = false;


        @Override
        public void run() {
            try{
                sleep(1000);
                sendmsg(0,"OK");
                sleep(1000);

                while(!stop)
                {
                    if (!handle1working) {
                        handle1working = true;
                        Thread handle1 = new Thread() {
                            public void run() {
                                String msg1 = null;
                                try {
                                    msg1 = input1.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (msg1 != null)
                                {
                                    System.out.println("1 : " + msg1);
                                    String temp[] = msg1.split(" ");
                                    if ( UPDATE.equals(temp[0]) ) {
                                        try {
                                            player1Time = Integer.parseInt(temp[1]);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            System.out.println(player1Alias + " : " + msg1);
                                        }
                                    }
                                    else if ( EXIT.equals(temp[0])) {
                                        sendmsg(2, EXIT);
                                        stop = true;
                                    }
                                    else
                                        System.out.println(player1Alias + " : " + msg1);
                                }
                                handle1working = false;
                            }
                        };
                        handle1.start();
                    }

                    if (!handle2working) {
                        handle2working = true;
                        Thread handle2 = new Thread() {
                            public void run() {
                                String msg2 = null;
                                try {
                                    msg2 = input2.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    sendmsg(1, EXIT);
                                    sendmsg(2, EXIT);
                                    stop = true;
                                }
                                if (msg2 != null)
                                {
                                    System.out.println("2 : " + msg2);
                                    String temp[] = msg2.split(" ");
                                    if ( UPDATE.equals(temp[0]) ) {
                                        try {
                                            player2Time = Integer.parseInt(temp[1]);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            System.out.println(player2Alias + " : " + msg2);
                                        }
                                    }
                                    else if ( EXIT.equals(temp[0])) {
                                        sendmsg(1, EXIT);
                                        stop = true;
                                    }
                                    else
                                        System.out.println(player2Alias + " : " + msg2);
                                }
                                handle2working = false;
                            }
                        };
                        handle2.start();
                    }
                    sleep(20);
                    if (player2Time > player1Time) {
                        sendmsg(0,GAMEUPDATE + " " + player1Alias + " " + player2Alias);
                    }
                    else {
                        sendmsg(0,GAMEUPDATE + " " + player2Alias + " " + player1Alias);
                    }

                }
                alias_socket_Map.remove(player1Alias);
                alias_socket_Map.remove(player2Alias);
            }catch(Exception e){e.printStackTrace();}
        }

        public void sendmsg(int who, String msg) {
            System.out.println("Server : " + msg);
            if (who == 0) {
                output2.println(msg);
                output1.println(msg);
            }
            else if (who == 1) {
                output1.println(msg);
            }
            else if (who == 2) {
                output2.println(msg);
            }
        }
    }
}