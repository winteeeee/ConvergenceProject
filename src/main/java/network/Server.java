package network;

import persistence.dto.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
* 서버 소켓과 클라이언트 소켓을 같은 포트로 연결
* 클라이언트가 접속할 때까지 lock (accpet() 이용)
* 클라이언트 접속 확인
* Thread를 생성 해 서브 소켓과 연결, 서버 소켓과의 연결은 끊음
* 클라이언트와 소켓에 각각 Buffer를 달아 줌
* 서버 소켓은 또 다른 클라이언트의 통신을 받을 수 있게함
* */
public class Server {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader br;

    public Server() {
        System.out.println("Server ready...");
    }

    public void run() {
        try {
            InetAddress ir = InetAddress.getByName("127.0.0.1");
            Scanner sc = new Scanner(System.in);
            /*backlog
             * queue에 size
             * backlog가 10이고 한 번에 처리할 수 있는 양(Thread)가 3일 경우
             * 10개의 Client로 부터 요청이 들어올 경우
             * 1, 2, 3은 연결이 되어 서버와 데이터를 주고 받을 수 있음
             * 4, 5, 6, 7, 8의 경우 연결은 되었지만 서버와 데이터를 주고 받을 수 없음
             * 9, 10의 경우 Client는 게속 SYNC, Server는 계속 패킷을 Drop
             * */
            serverSocket = new ServerSocket(PORT, 50, ir);

            ConnectThread connectThread = new ConnectThread(serverSocket);
            connectThread.start();

            int temp = sc.nextInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}