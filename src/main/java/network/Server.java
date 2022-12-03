package network;

import persistence.dto.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
/*
* 서버 소켓과 클라이언트 소켓을 같은 포트로 연결
* 클라이언트가 접속할 때까지 lock (accpet() 이용)
* 클라이언트 접속 확인
* Thread를 생성 해 서브 소켓과 연결, 서버 소켓과의 연결은 끊음
* 클라이언트와 소켓에 각각 Buffer를 달아 줌
* 서버 소켓은 또 다른 클라이언트의 통신을 받을 수 있게함
* */
public class Server
{
    private static final int PORT = 5000;
    private List<Thread> thread_list;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader br;

    public Server() {
        thread_list = new ArrayList<Thread>();
        System.out.println("Server ready...");
    }

    public void run (){
       try {
           InetAddress ir = InetAddress.getByName("127.0.0.1");
           /*backlog
           * queue에 size
           * backlog가 10이고 한 번에 처리할 수 있는 양(Thread)가 3일 경우
           * 10개의 Client로 부터 요청이 들어올 경우
           * 1, 2, 3은 연결이 되어 서버와 데이터를 주고 받을 수 있음
           * 4, 5, 6, 7, 8의 경우 연결은 되었지만 서버와 데이터를 주고 받을 수 없음
           * 9, 10의 경우 Client는 게속 SYNC, Server는 계속 패킷을 Drop
           * */
           serverSocket = new ServerSocket(PORT, 50, ir);

           while(true) {
                System.out.println("Connection Waiting...");
                socket = serverSocket.accept();
                System.out.println("Connection Success!!");

                Thread thread = new Thread(new InnerThread(socket));
                thread.start();
           }

       } catch(IOException e) {
            e.printStackTrace();
       }
    }

    class InnerThread implements Runnable {
        private Socket commSocket;

        InnerThread(Socket socket) {
            this.commSocket = socket;
        }

        public void run() {
            InputStream is;
            OutputStream os;
            final int BUFFER_MAX_SIZE = 1024;
            byte[] readBuf = new byte [BUFFER_MAX_SIZE];

            try {
                is = commSocket.getInputStream();

                os = commSocket.getOutputStream();

                int size;
                while((size = is.read(readBuf)) > 0 ){
                    Protocol protocol = new Protocol(readBuf);
                    if(false/*종료조건*/) {
                        quit();
                        break;
                    }
                    selectFunction(protocol);
                }
                commSocket.close();

            } catch (Exception e1) {
                System.out.println("Unconnected");
                try {
                    commSocket.close();
                } catch (Exception e2) {
                    System.out.println("Occur Memory Leak");
                }
            }
        }
    }
    private void selectFunction(Protocol protocol) {
        DTO data = protocol.getData();
        if (protocol.getType() == ProtocolType.REGISTER) {
            if (protocol.getCode() == ProtocolCode.STORE) {
                //가게 등록
                store_register((StoreDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.ORDER) {
                //주문 등록
                order_register((OrdersDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.REVIEW) {
                //리뷰 등록
                review_register((ReviewDTO)data);
            }
            else if (protocol.getCode() == ProtocolCode.USER) {
                //회원가입
                user_register((UserDTO)data);
            }
        }

        else if (protocol.getType() == ProtocolType.MODIFICATION) {
            if (protocol.getCode() == ProtocolCode.MENU) {
                //메뉴 수정
                menu_modify((MenuDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.OPTION) {
                //옵션 수정
                option_modify((DetailsDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.ORDER) {
                //주문 수정
                order_modify((OrdersDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.REVIEW) {
                //리뷰 수정
                review_modify((ReviewDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.STORE) {
                //가게 정보 수정
                store_modify((StoreDTO)data);
            }

            else if (protocol.getCode() == (ProtocolCode.STORE | ProtocolCode.REGIST)) {
                store_register_modify((StoreRegistDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.USER) {
                //유저 수정
                user_modify((UserDTO)data);
            }
        }

        else if(protocol.getType() == ProtocolType.SEARCH) {
            if (protocol.getCode() == ProtocolCode.MENU) {
                //메뉴 조회
                menu_search((StoreDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.OPTION) {
                //옵션 조회
                option_search((StoreDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.ORDER) {
                //주문 조회
                order_search((OrdersDTO)data);
            }

            else if (protocol.getCode() == ProtocolCode.REVIEW) {
                //리뷰 조회
                review_search();
            }

            else if (protocol.getCode() == ProtocolCode.STORE) {
                //가게 정보 조회
                store_search();
            }

            else if (protocol.getCode() == (ProtocolCode.STORE | ProtocolCode.REGIST)) {
                //가게 등록 조회
                store_register_search();
            }

            else if (protocol.getCode() == ProtocolCode.USER) {
                //유저 조회
                search_user((UserDTO)protocol.getData());
            }
        }

        else if (protocol.getType() == ProtocolType.RESPONSE) {
            if (protocol.getCode() == ProtocolCode.ACCEPT) {
                //응답 Y
            }

            else if (protocol.getCode() == ProtocolCode.REFUSAL) {
                //응답 N
            }
        }
    }
    //UserController, OwnerController, StoreController

    private boolean store_register(StoreDTO store) {
        return true;
        /*StoreController storeController = new StoreController();
        return storeController.insert(store);*/
    }
    private boolean order_register(OrdersDTO order) {
        return true;
    }

    private boolean review_register(ReviewDTO review) {
        return true;
    }

    private boolean user_register(UserDTO user) {
        return true;/*
        UserController userController = new UserController();
        return userController.insert(user);*/
    }

    private boolean menu_modify(MenuDTO menu) {
        return true;/*
        StoreController storeController = new StoreController();
        return true;*/
    }

    private boolean option_modify(DetailsDTO detail) {
        return true;/*
        StoreController storeController = new StoreController();
        return true;*/
    }

    private boolean order_modify(OrdersDTO order) {
        return true;/*
        UserController userController = new UserController();
        return true;*/
    }

    private boolean review_modify(ReviewDTO review) {
        return true;/*
        UserController userController = new UserController();
        return true;*/
    }

    private boolean store_modify(StoreDTO store) {
        return true;
    }

    private boolean store_register_modify(StoreRegistDTO store_regist) {
        return true;/*
        OwnerController ownerController = new OwnerController();
        return true;*/
    }

    private boolean user_modify(UserDTO user) {
        return true;/*
        UserController userController = new UserController();
        return userController.update(user);
    */}

    private UserDTO search_user(UserDTO user) {
        return user;/*
        UserController userController = new UserController();
        return userController.login(user);*/
    }

    private void menu_search(StoreDTO store) {/*
        StoreController storeController = new StoreController();
*/
    }

    private void option_search(StoreDTO store) {/*
        StoreController storeController = new StoreController();
*/
    }

    private void order_search(OrdersDTO order) {/*
        UserController userController = new UserController();
*/
    }

    private void review_search() {

    }

    private void store_search() {

    }

    private void store_register_search() {

    }

    private void quit() {

    }
}