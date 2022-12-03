package network;

import persistence.dto.*;
import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.io.*;
import java.net.ProtocolFamily;
import java.net.Socket;

class ClientThread extends Thread {
    Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader br;
    private BufferedWriter bw;
    private byte[] readBuf;
    private Protocol send_protocol;

    private UserService userService;
    private OwnerService ownerService;
    private AdminService adminService;
    int id;
    UserDTO user;

    ClientThread (Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run () {
        try {
            while (true) {
                dis = new DataInputStream(socket.getInputStream());
                br = new BufferedReader(new InputStreamReader(dis));

                dos = new DataOutputStream(socket.getOutputStream());
                bw = new BufferedWriter(new OutputStreamWriter(dos));

                readBuf = br.readLine().getBytes();
                Protocol protocol = new Protocol(readBuf);

                selectFunction(protocol);

            }
        } catch (IOException e) {
            System.out.println("    Thread " + id + " is closed. ");
        }
    }

    private void selectFunction(Protocol protocol) throws IOException {
        byte type = protocol.getType();
        byte code = protocol.getCode();
        DTO data = protocol.getData();

        if(type == ProtocolType.REGISTER) {
            if (code == ProtocolCode.USER) {
                user_register((UserDTO)data);
            }
            else if (code == ProtocolCode.STORE) {
                store_register((StoreDTO)data);
            }
            else if (code == ProtocolCode.MENU) {
                menu_register((MenuDTO)data);
            }
            else if (code == ProtocolCode.REVIEW) {
                review_register((ReviewDTO)data);
            }
            else if (code == ProtocolCode.ORDER) {
                order_register((OrdersDTO)data);
            }
            else {

            }
        }
        else if (type == ProtocolType.MODIFICATION) {
            if (code == ProtocolCode.USER) {
                user_modify((UserDTO)data);
            }
            else if (code == ProtocolCode.STORE) {
                store_modify((StoreDTO)data);
            }
            else if (code == ProtocolCode.ORDER) {
                order_modify((OrdersDTO)data);
            }
            else {

            }
        }
        else if (type == ProtocolType.SEARCH) {
            if (code == ProtocolCode.USER) {
                user_search((UserDTO)data);
            }
            else if (code == ProtocolCode.STORE) {
                store_search();
            }
            else if (code == (ProtocolCode.STORE | ProtocolCode.HISTORY)) {
                store_history_search();
            }
            else if (code == ProtocolCode.ORDER) {
                order_search();
            }
            else if (code == (ProtocolCode.ORDER | ProtocolCode.HISTORY)) {
                order_history_search();
            }
            else if (code == ProtocolCode.REVIEW) {
                review_search();
            }
        }
        else if (type == ProtocolType.RESPONSE) {

        }
        else {

        }
    }

    private void review_search() {

    }

    private void order_history_search() {

    }

    private void order_search() {

    }

    private void store_history_search() {

    }

    private void store_search() {

    }

    private void user_search(UserDTO userDTO) throws IOException {
        UserDTO temp = userService.getUserWithId(userDTO.getId());
        if (temp.getPw().equals(userDTO.getPw())) {
            user = temp;
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, user);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void order_modify(OrdersDTO orderDTO) {

    }

    private void store_modify(StoreDTO storeDTO) {

    }

    private void user_modify(UserDTO userDTO) {

    }

    private void order_register(OrdersDTO orderDTO) {

    }

    private void review_register(ReviewDTO reviewDTO) {

    }

    private void menu_register(MenuDTO menuDTO) {

    }

    private void store_register(StoreDTO storeDTO) throws IOException {
        if (ownerService.insertStore(storeDTO) == 1) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void user_register(UserDTO userDTO) throws IOException {
        if ( userService.insertUser(userDTO) == 1) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }
}