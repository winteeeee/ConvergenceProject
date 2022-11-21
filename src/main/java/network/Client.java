package network;

import java.io.*;
import java.net.*;
import persistence.dto.*;

public class Client
{
    private Socket cliSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader keyInput;
    private ClientController con;
    private UserDTO me;

    public Client() {
        try{
            cliSocket = new Socket("localhost", 5000);
            dis = new DataInputStream(cliSocket.getInputStream());
            dos = new DataOutputStream(cliSocket.getOutputStream());
            keyInput = new BufferedReader(new InputStreamReader(System.in));
            con = new ClientController(dis, dos, keyInput);
        }catch(UnknownHostException e){
            System.err.println("서버를 찾지 못했습니다.");
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
    
    public void run() throws IOException {
        while(true){
            me = con.login();

            if(me != null) {
                String userAuthority = me.getAuthorityEnum().getName();
                if (userAuthority.equals("ADMIN")) {
                    adminRun();
                }

                else if (userAuthority.equals("OWNER")) {
                    ownerRun();
                }

                else if (userAuthority.equals("USER")) {
                    userRun();
                }
            }

            else {
                System.out.println(ErrorMessage.LOGIN_FAILED);
            }
        }
    }

    private void adminRun() throws IOException {
        boolean login = true;
        final int REGIST_STORE_DETERMINATION = 1;
        final int VIEW_ALL_STORE = 2;
        final int VIEW_OWNER_AND_USER = 3;
        final int LOGOUT = 4;

        while(login) {
            con.showAdminScreen(me);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case REGIST_STORE_DETERMINATION:
                    con.registStoreDetermination();
                    break;

                case VIEW_ALL_STORE:
                    con.viewAllStore();
                    break;

                case VIEW_OWNER_AND_USER:
                    con.viewOwnerAndUser();
                    break;

                case LOGOUT:
                    con.showLogoutMessage();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    private void ownerRun() throws IOException {
        boolean login = true;
        final int REQUEST_STORE_REGIST = 1;
        final int REGIST_MENU = 2;
        final int MODIFICATION_MANAGEMENT_TIME = 3;
        final int DETERMINATION_ORDER = 4;
        final int VIEW_STORE_INFO = 5;
        final int VIEW_MENU_INFO = 6;
        final int LOGOUT = 7;

        while(login) {
            con.showOwnerScreen(me);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case REQUEST_STORE_REGIST:
                    con.registStore(me);
                    break;

                case REGIST_MENU:
                    con.registMenu(me);
                    break;

                case MODIFICATION_MANAGEMENT_TIME:
                    con.setRunningTime(me);
                    break;

                case DETERMINATION_ORDER:
                    con.orderDetermination(me);
                    break;

                case VIEW_STORE_INFO:
                    con.viewStore(me);
                    break;

                case VIEW_MENU_INFO:
                    con.viewMenu(me);
                    break;

                case LOGOUT:
                    con.showLogoutMessage();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    private void userRun() throws IOException {
        boolean login = true;
        final int VIEW_STORE = 1;
        final int REGIST_ORDER = 2;
        final int CANCEL_ORDER = 3;
        final int VIEW_ORDER = 4;
        final int REGIST_REVIEW = 5;
        final int VIEW_REVIEW = 6;
        final int VIEW_ACCOUNT_INFO = 7;
        final int LOGOUT = 8;

        while(login) {
            con.showUserScreen(me);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case VIEW_STORE:
                    con.viewStore();
                    break;

                case REGIST_ORDER:
                    con.registOrder(me);
                    break;

                case CANCEL_ORDER:
                    con.orderCancel(me);
                    break;

                case VIEW_ORDER:
                    con.viewOrder(me);
                    break;

                case REGIST_REVIEW:
                    con.registReview(me);
                    break;

                case VIEW_REVIEW:
                    con.viewReview(me);
                    break;

                case VIEW_ACCOUNT_INFO:
                    con.viewAccountInfo(me);
                    break;

                case LOGOUT:
                    con.showLogoutMessage();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }
}