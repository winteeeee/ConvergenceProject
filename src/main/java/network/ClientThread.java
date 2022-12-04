package network;

import persistence.dto.*;
import persistence.enums.Authority;
import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.io.*;
import java.net.ProtocolFamily;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

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
    private int id;
    private UserDTO user;
    private String authority;
    private long store_id;

    ClientThread (Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run () {
        try {
            dis = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(dis));

            dos = new DataOutputStream(socket.getOutputStream());
            bw = new BufferedWriter(new OutputStreamWriter(dos));

            while (dis.read(readBuf) != -1) {
                //readBuf = dis.readAllBytes();
                //readBuf = br.readLine().getBytes();
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
                order_register((TotalOrdersDTO)data);
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
                if(authority.equals(Authority.OWNER)) {
                    store_history_search((StoreDTO)data);
                }
                else {
                    store_history_search();
                }

            }
            else if (code == (ProtocolCode.ORDER | ProtocolCode.HISTORY)) {
                order_history_search();
            }
            else if (code == ProtocolCode.REVIEW) {
                review_search((StoreDTO)data);
            }
        }
        else if (type == ProtocolType.RESPONSE) {
            if (code == (ProtocolCode.STORE | ProtocolCode.ACCEPT)) {
                store_regist_accept((StoreDTO)data);
            }
            else if (code == (ProtocolCode.STORE | ProtocolCode.REFUSAL)) {
                store_regist_refuse((StoreDTO)data);
            }
            else if (code == (ProtocolCode.MENU | ProtocolCode.ACCEPT)) {
                menu_regist_accept((MenuDTO)data);
            }
            else if (code == (ProtocolCode.MENU | ProtocolCode.REFUSAL)) {
                menu_regist_refuse((MenuDTO)data);
            }
            else if (code == (ProtocolCode.USER | ProtocolCode.ACCEPT)) {
                user_regist_accpet((UserDTO)data);
            }
            else if (code == (ProtocolCode.USER | ProtocolCode.REFUSAL)) {
                user_regist_refuse((UserDTO)data);
            }
            else {

            }
        }
        else {

        }
    }

    private void user_regist_refuse(UserDTO userDTO) {
        adminService.rejectUser(userDTO.getPk());
    }

    private void user_regist_accpet(UserDTO userDTO) {
        adminService.acceptUser(userDTO.getPk());
    }

    private void menu_regist_refuse(MenuDTO menuDTO) {
        adminService.rejectMenu(menuDTO.getId());
    }

    private void menu_regist_accept(MenuDTO menuDTO) {
        adminService.acceptMenu(menuDTO.getId());
    }

    private void store_regist_refuse(StoreDTO storeDTO) {
        adminService.rejectStore(storeDTO.getId());
    }

    private void store_regist_accept(StoreDTO storeDTO) {
        adminService.acceptStore(storeDTO.getId());
    }

    private void review_search(StoreDTO storeDTO) throws IOException {
        Integer max_page = ownerService.getMaxPage(storeDTO.getId());
        send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, max_page, null);
        dos.write(send_protocol.getBytes());

        List<ReviewDTO> reviewDTOs;
        Protocol temp_protocol;
        byte[] temp;
        while((temp = dis.readAllBytes()) != null) {
            temp_protocol = new Protocol(temp);
            reviewDTOs = ownerService.getReviewList(storeDTO.getId(), temp_protocol.getDataLength());

            for(int i = 0; i < reviewDTOs.size(); i++) {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, reviewDTOs.get(i));
                dos.write(send_protocol.getBytes());
            }
        }
    }

    private void order_history_search() throws IOException {
        List<TotalOrdersDTO>totalOrdersDTOs = userService.getOrders(user.getPk());
        for(int i = 0; i < totalOrdersDTOs.size(); i++) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, totalOrdersDTOs.size(), totalOrdersDTOs.get(i));
            dos.write(send_protocol.getBytes());
        }
    }

    private void store_history_search(StoreDTO storeDTO) throws IOException {
        List<StatisticsDTO> statisticsDTOs = ownerService.getStatistics(storeDTO.getId());
        for(int i = 0; i < statisticsDTOs.size(); i++) {
            StatisticsDTO statisticsDTO = statisticsDTOs.get(i);
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, statisticsDTOs.size(), statisticsDTO);
            dos.write(send_protocol.getBytes());
        }
    }

    private void store_history_search() throws IOException {
        List<StatisticsDTO> statisticsDTOs = adminService.getStatistics();
        for(int i = 0; i < statisticsDTOs.size(); i++) {
            StatisticsDTO statisticsDTO = statisticsDTOs.get(i);
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, statisticsDTOs.size(), statisticsDTO);
            dos.write(send_protocol.getBytes());
        }
    }

    private void store_search() throws IOException {
        List<StoreDTO>stores = userService.getAllStore();
        StoreDTO store;
        for(int i = 0; i < stores.size(); i++) {
            store = stores.get(i);
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, stores.size(), store);
            dos.write(send_protocol.getBytes());
        }
    }

    private void user_search(UserDTO userDTO) throws IOException {
        UserDTO temp = userService.getUserWithId(userDTO.getId());
        if (temp.getPw().equals(userDTO.getPw())) {
            user = temp;
            authority = user.getAuthority();
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, user);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void order_modify(OrdersDTO orderDTO) throws IOException {
        if (userService.cancelOrder(orderDTO.getTotal_orders_id()) == 1) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void store_modify(StoreDTO storeDTO) throws IOException {
        if (ownerService.updateTime(storeDTO.getId(), storeDTO.getOpen_time(), storeDTO.getClose_time()) == 1) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void user_modify(UserDTO userDTO) throws IOException {
        UserDTO temp = userService.update(userDTO);
        if (temp != null) {
            user = temp;
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, user);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void order_register(TotalOrdersDTO totalOrdersDTO) throws IOException {
        List<OrdersDTO> ordersDTOs = null;
        readBuf = dis.readAllBytes();
        Protocol temp_protocol = new Protocol(readBuf);

        OrdersDTO data = (OrdersDTO)temp_protocol.getData();
        int size = temp_protocol.getDataLength();

        ordersDTOs.add(data);

        for(int i = 1; i < size; i++) {
            readBuf = dis.readAllBytes();
            temp_protocol = new Protocol(readBuf);
            data = (OrdersDTO)temp_protocol.getData();
            ordersDTOs.add(data);
        }
        if (userService.order(totalOrdersDTO, ordersDTOs) == 1) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void review_register(ReviewDTO reviewDTO) throws IOException {
        if (authority.equals(Authority.OWNER)) {
            if (ownerService.updateOwnerComment(reviewDTO.getId(), reviewDTO.getComment()) == 1) {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
            }
            else {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
            }
        }
        else if (authority.equals(Authority.USER)) {
            if (userService.writeReview(reviewDTO) == 1) {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
            }
            else {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
            }
        }
        dos.write(send_protocol.getBytes());
    }

    private void menu_register(MenuDTO menuDTO) {
        //ownerService.insertMenu(menuDTO);
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