package network;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DemoServer {
    public static void main(String[] args) throws IOException {
        int port = 5050;

        int number = Integer.parseInt(args[0]);
        String str = args[1];

        //서버 소켓을 생성
        ServerSocket ss = new ServerSocket(port); //서버 포트 설정
        System.out.println("접속 대기 중");

        while(true) {
            Socket s = ss.accept();
            System.out.println("사용자 접속 확인");
            System.out.println("클라이언트 IP : " + s.getInetAddress());

            //클라이언트와 연결하기 위한 스트림 생성
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            dos.writeUTF(str);
            dos.writeInt(number);
            dos.flush();
            dos.close();
            s.close();
        }
    }
}
