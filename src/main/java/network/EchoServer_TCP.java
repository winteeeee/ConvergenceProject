package network;

import java.io.*;
import java.net.*;
public class EchoServer_TCP
{
    public static void main(String args[]){
        ServerSocket listenSocket = null;
        Socket commSocket = null;
        InputStream is;
        OutputStream os;
        BufferedReader br;
        BufferedWriter bw;
        String readBuf;

        try{
            //listenSocket = new ServerSocket(5000);
            InetAddress ir = InetAddress.getByName("127.0.0.1");
            listenSocket = new ServerSocket(5000, 50, ir);
            System.out.println("Listen: " + listenSocket.getInetAddress() + ", " + listenSocket.getLocalPort());
            System.out.println("Waiting for connection...");

            commSocket = listenSocket.accept();
            System.out.println("Comm: " + commSocket.getLocalAddress()+ ", " + commSocket.getLocalPort());
            System.out.println("Client: " + commSocket.getInetAddress()+ ", " + commSocket.getPort());

            is = commSocket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            os = commSocket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os));

            while((readBuf = br.readLine()) != null ){
                System.out.println(readBuf);
                if(readBuf.equals("quit")) break;
                bw.write(readBuf+"\r\n");
                bw.flush();
            }
            commSocket.close();
        }catch(IOException e){
            System.err.println(e);
        }finally{
            if(listenSocket != null){
                try{
                    listenSocket.close();
                }catch(IOException e){
                    System.out.println(e);
                }
            }
        }
    }
}