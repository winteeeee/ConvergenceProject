package network;

import java.io.*;
import java.net.*;
public class EchoClient_TCP
{
    public static void main(String args[]){
        Socket cliSocket = null;
        InputStream is;
        OutputStream os;
        BufferedReader br, keyInput;
        BufferedWriter bw;
        String readBuf = null, writeBuf = null;
        String host = "localhost";

        try{
            cliSocket = new Socket(host, 5000);
            System.out.println("Connection successful");

            is = cliSocket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            os = cliSocket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os));

            keyInput = new BufferedReader(new InputStreamReader(System.in));

            while(true){
                System.out.print("Input Message: ");
                writeBuf = keyInput.readLine();
                bw.write(writeBuf+"\r\n");
                bw.flush();

                if(writeBuf.equals("quit")) break;

                readBuf = br.readLine();
                System.out.println("RX Message: " + readBuf);
            }
        }catch(UnknownHostException e){
            System.err.println("Server not found");
        }catch(IOException e){
            System.err.println(e);
        }finally{
            try{
                cliSocket.close();
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
}