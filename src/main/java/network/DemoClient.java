package network;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DemoClient {
    public static void main(String[] args) throws Exception {
        //연결 시에 소켓이 생성된다. 연결이 안되면 예외 발생
        int port = 5050;
        Socket s = new Socket("127.0.0.1", port);
        System.out.println("접속 완료");

        DataInputStream dis = new DataInputStream(s.getInputStream());

        String str = dis.readUTF();
        int number = dis.readInt();
        System.out.println("서버에서 전송된 값 : " + number);
        System.out.println("서버에서 전송된 문자 : " + str);

        dis.close();
        s.close();
    }
}
