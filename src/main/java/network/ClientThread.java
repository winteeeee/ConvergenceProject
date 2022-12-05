package network;

import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.Authority;
import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.io.*;
import java.net.ProtocolFamily;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ClientThread extends Thread {
    Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private InputStream is;
    private OutputStream os;
    private BufferedReader br;
    private BufferedWriter bw;

    private final int BUF_SIZE = 1024;
    private byte[] readBuf = new byte[BUF_SIZE];
    private Protocol send_protocol;

    private static final StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final TotalOrdersDAO totalOrdersDAO = new TotalOrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    private static final StatisticsDAO statisticsDAO = new StatisticsDAO(MyBatisConnectionFactory.getSqlSessionFactory());

    private static final UserService userService = new UserService(ordersDAO, storeDAO, userDAO, menuDAO, reviewDAO, totalOrdersDAO, classificationDAO, detailsDAO);
    private static final OwnerService ownerService = new OwnerService(userDAO, storeDAO, menuDAO, totalOrdersDAO, ordersDAO, reviewDAO, classificationDAO, statisticsDAO, detailsDAO);
    private static final AdminService adminService = new AdminService(storeDAO, userDAO, menuDAO, totalOrdersDAO, statisticsDAO);

    private int id;
    private UserDTO user;
    private Authority authority;
    private long store_id;

    ClientThread (Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        authority = null;
    }

    @Override
    public void run () {
        try {
            dis = new DataInputStream(socket.getInputStream());
            br = new BufferedReader(new InputStreamReader(dis));

            dos = new DataOutputStream(socket.getOutputStream());
            bw = new BufferedWriter(new OutputStreamWriter(dos));

            while (dis.read(readBuf) != -1) {
                System.out.println("Thread" + id);
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
            else if (code == ProtocolCode.CLASSIFICATION) {
                classificaion_register((ClassificationDTO)data);
            }
            else if (code == ProtocolCode.OPTION) {
                option_register((DetailsDTO)data);
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
                order_modify((TotalOrdersDTO)data);
            }
            else if (code == ProtocolCode.REVIEW){
                review_modify((ReviewDTO)data);
            }
        }
        else if (type == ProtocolType.SEARCH) {
            if (code == ProtocolCode.USER) {
                /*
                1. 로그인 기능
                2. 보류 중인 점주 조회 -> authority == admin
                * */
                if (data != null) {
                    user_search((UserDTO)data);
                }
                else if (data == null && authority.equals(Authority.ADMIN)) {
                    user_search();
                }

            }
            else if (code == ProtocolCode.STORE) {
                if (data == null) {
                    store_search();
                }
                else {
                    store_search((UserDTO)data);
                }
            }
            else if (code == ProtocolCode.ORDER) {
                if (data.getClass().getName().contains("UserDTO")) {
                    order_search((UserDTO)data);
                }
                else if (data.getClass().getName().contains("StoreDTO")) {
                    order_search((StoreDTO)data);
                }

            }
            else if (code == (ProtocolCode.STORE | ProtocolCode.HISTORY)) {
                if(authority.equals(Authority.OWNER)) {
                    store_history_search((StoreDTO)data);
                }
                else {
                    store_history_search();
                }
            }
            else if (code == ProtocolCode.REVIEW) {
                review_search((StoreDTO)data);
            }

            else if (code == (ProtocolCode.REVIEW | ProtocolCode.HISTORY)) {
                review_searchWithoutPage((StoreDTO)data);
            }
            else if (code == ProtocolCode.MENU) {
                if (data == null) {
                    menu_search();
                }
                else {
                    menu_search((StoreDTO)data);
                }

            }
            else if (code == ProtocolCode.OPTION) {
                if (data.getClass().getName().contains("MenuDTO")) {
                    option_search((MenuDTO) data);
                }
                else {
                    option_search((StoreDTO) data);
                }
            }
            else if (code == ProtocolCode.CLASSIFICATION) {
                classificaion_search((StoreDTO)data);
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
            else if (code == (ProtocolCode.ORDER | ProtocolCode.ACCEPT)) {
                order_regist_accept((TotalOrdersDTO)data);
            }
            else if (code == (ProtocolCode.ORDER | ProtocolCode.REFUSAL)) {
                order_regist_refuse((TotalOrdersDTO)data);
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

    private void order_regist_accept(TotalOrdersDTO data) {
        ownerService.acceptOrders(data.getId());
    }

    private void order_regist_refuse(TotalOrdersDTO data) {
        ownerService.cancelOrders(data.getId());
    }

    private void classificaion_search(StoreDTO storeDTO) throws IOException {
        List<ClassificationDTO>classificationDTOs = userService.getMenuGroups(storeDTO.getId());
        dos.write(Serializer.intToByteArray(classificationDTOs.size()));
        receive_ack();

        for(ClassificationDTO classificationDTO : classificationDTOs) {
            dos.write(new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, classificationDTO).getBytes());
            receive_ack();
        }
    }

    private void option_register(DetailsDTO optionDTO) throws IOException {
        if (ownerService.insertDetails(optionDTO) != 0) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void classificaion_register(ClassificationDTO classificationDTO) throws IOException {
        if (ownerService.insertClassification(classificationDTO) != 0) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void option_search(StoreDTO storeDTO) throws IOException {
        List<DetailsDTO>detailsDTOs = ownerService.selectAllWithStoreId(storeDTO);
        dos.write(Serializer.intToByteArray(detailsDTOs.size()));
        receive_ack();

        for(DetailsDTO detailsDTO : detailsDTOs) {
            dos.write(new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, detailsDTO).getBytes());
            receive_ack();
        }
    }

    private void option_search(MenuDTO menuDTO) throws IOException {
        List<DetailsDTO>detailsDTOs = userService.getDetailsWithMenuId(menuDTO.getId());
        dos.write(Serializer.intToByteArray(detailsDTOs.size()));
        receive_ack();

        for(DetailsDTO detailsDTO : detailsDTOs) {
            dos.write(new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, detailsDTO).getBytes());
            receive_ack();
        }
    }

    private void order_search(StoreDTO storeDTO) throws IOException {
        List<TotalOrdersDTO>totalOrdersDTOs = ownerService.getTotalOrders(storeDTO.getId());
        dos.write(Serializer.intToByteArray(totalOrdersDTOs.size()));
        receive_ack();

        for(int i = 0; i < totalOrdersDTOs.size(); i++) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, totalOrdersDTOs.get(i));
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void order_search(UserDTO userDTO) throws IOException {
        List<TotalOrdersDTO>totalOrdersDTOs = userService.getOrders(userDTO.getPk());
        dos.write(Serializer.intToByteArray(totalOrdersDTOs.size()));
        receive_ack();

        for(int i = 0; i < totalOrdersDTOs.size(); i++) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, totalOrdersDTOs.get(i));
            dos.write(send_protocol.getBytes());
            receive_ack();
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
        int cur_page = 1;
        int max_page = storeDTO.getReview_count() / 2 + (storeDTO.getReview_count() & 1);
        dos.write(Serializer.intToByteArray(max_page));

        while(true) {
            if (dis.read(readBuf) != -1) {
                cur_page = Deserializer.byteArrayToInt(readBuf);
            }

            if (!(1 <= cur_page && cur_page <= max_page)) {
                break;
            }

            List<ReviewDTO> reviewDTOs = userService.getStoreReview(storeDTO.getId(), cur_page);
            dos.write(Serializer.intToByteArray(reviewDTOs.size()));

            for(ReviewDTO reviewDTO : reviewDTOs) {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, reviewDTO);
                dos.write(send_protocol.getBytes());
            }
        }
    }

    private void review_searchWithoutPage(StoreDTO data) throws IOException {
        List<ReviewDTO> reviewDTOs = ownerService.getAllReview(data);
        dos.write(Serializer.intToByteArray(reviewDTOs.size()));
        receive_ack();

        for(ReviewDTO DTO : reviewDTOs) {
            dos.write(new Protocol(ProtocolType.RESPONSE, ProtocolCode.REVIEW, 0, DTO).getBytes());
            receive_ack();
        }
    }

    private void store_history_search(StoreDTO storeDTO) throws IOException {
        List<StatisticsDTO> statisticsDTOs = ownerService.getStatistics(storeDTO.getId());
        dos.write(Serializer.intToByteArray(statisticsDTOs.size()));
        receive_ack();

        for(int i = 0; i < statisticsDTOs.size(); i++) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, statisticsDTOs.get(i));
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void store_history_search() throws IOException {
        List<StatisticsDTO> statisticsDTOs = adminService.getStatistics();
        dos.write(Serializer.intToByteArray(statisticsDTOs.size()));
        receive_ack();

        for(int i = 0; i < statisticsDTOs.size(); i++) {
            StatisticsDTO statisticsDTO = statisticsDTOs.get(i);
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, statisticsDTO);
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void store_search(UserDTO userDTO) throws IOException {
        List<StoreDTO> storeDTOS = ownerService.getStoreWithUser_pk(userDTO.getPk());
        dos.write(Serializer.intToByteArray(storeDTOS.size()));
        receive_ack();

        for(StoreDTO storeDTO : storeDTOS) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, storeDTO);
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void store_search() throws IOException {
        List<StoreDTO> stores;
        if (authority.equals(Authority.USER)) {
            stores = userService.getAllStore();
        }
        else {
            stores = adminService.getHoldStoreList();

        }

        dos.write(Serializer.intToByteArray(stores.size()));
        receive_ack();

        for(int i = 0; i < stores.size(); i++) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, stores.get(i));
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private boolean receive_ack() throws IOException {
        if (dis.read(readBuf) != -1) {
            return true;
        }
        return false;
    }

    private void user_search() throws IOException {
        List<UserDTO> userDTOs = adminService.getHoldUserList();
        dos.write(Serializer.intToByteArray(userDTOs.size()));
        receive_ack();

        for(UserDTO userDTO : userDTOs) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, userDTO);
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void user_search(UserDTO userDTO) throws IOException {
        UserDTO temp = userService.getUserWithId(userDTO.getId());
        if (temp != null && temp.getPw().equals(userDTO.getPw())) {
            user = temp;
            authority = user.getAuthorityEnum();
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, user);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void menu_search() throws IOException {
        List<MenuDTO> menuDTOS = adminService.getHoldMenuList();
        dos.write(Serializer.intToByteArray(menuDTOS.size()));
        receive_ack();

        for(MenuDTO menuDTO : menuDTOS) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, menuDTO);
            dos.write(send_protocol.getBytes());
            receive_ack();
        }
    }

    private void menu_search(StoreDTO storeDTO) throws IOException {
        List<ClassificationDTO> classificationDTOs = userService.getMenuGroups(storeDTO.getId());
        dos.write(Serializer.intToByteArray(classificationDTOs.size()));
        receive_ack();

        for (ClassificationDTO classificationDTO : classificationDTOs) {
            List<MenuDTO> menuDTOs = userService.getMenusWithGroup_id(classificationDTO.getId());
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, classificationDTO);
            dos.write(send_protocol.getBytes());
            receive_ack();

            dos.write(Serializer.intToByteArray(menuDTOs.size()));
            receive_ack();

            for(MenuDTO menuDTO : menuDTOs) {
                send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, menuDTO);
                dos.write(send_protocol.getBytes());
                receive_ack();
            }
        }
    }

    private void order_modify(TotalOrdersDTO totalOrdersDTO) throws IOException {
        if (userService.cancelOrder(totalOrdersDTO.getId()) != 0) {
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

    private void review_modify(ReviewDTO data) throws IOException {
        if (ownerService.updateOwnerComment(data.getId(), data.getOwner_comment()) == 1) {
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

    /*
    * 바뀔거 같은 느낌
    * */
    private void send_ack() throws IOException {
        Protocol protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACK, 0, null);
        dos.write(protocol.getBytes());
    }

    private void order_register(TotalOrdersDTO totalOrdersDTO) throws IOException {
        StoreDTO storeDTO = null;
        if (dis.read(readBuf) != -1) {
            storeDTO = (StoreDTO) new Protocol(readBuf).getData();
            send_ack();
        }

        menu_search(storeDTO);

        StoreDTO storeDTO1 = null;
        if (dis.read(readBuf) != -1) {
            storeDTO1 = (StoreDTO) new Protocol(readBuf).getData();
            option_search(storeDTO1);
        }

        List<OrdersDTO> ordersDTOs = new ArrayList<>();
        int list_length = 0;
        if (dis.read(readBuf) != -1) {
            list_length = Deserializer.byteArrayToInt(readBuf);
            send_ack();
        }

        for(int i = 0; i < list_length; i++) {
            if (dis.read(readBuf) != -1) {
                ordersDTOs.add((OrdersDTO) new Protocol(readBuf).getData());
                send_ack();
            }
        }

        totalOrdersDTO.setUser_pk(user.getPk());
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

    private void menu_register(MenuDTO menuDTO) throws IOException {
        int option_size = 0;
        if (dis.read(readBuf) != -1) {
            option_size = Deserializer.byteArrayToInt(readBuf);
            send_ack();
        }

        List<Long> option_list = new ArrayList<>();
        for(int i = 0; i < option_size; i++) {
            DetailsDTO optionDTO = null;
            if (dis.read(readBuf) != -1) {
                optionDTO = (DetailsDTO) new Protocol(readBuf).getData();
                send_ack();
            }
            option_list.add(optionDTO.getId());
        }
        ownerService.insertMenu(menuDTO, option_list);
    }

    private void store_register(StoreDTO storeDTO) throws IOException {
        long temp = ownerService.insertStore(storeDTO);
        System.out.println(temp);
        if (temp != 0) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }

    private void user_register(UserDTO userDTO) throws IOException {
        boolean flag = false;

        if (userDTO.getAuthorityEnum().equals(Authority.USER)) {
            flag = userService.insertUser(userDTO) != 0;
        }
        else if (userDTO.getAuthorityEnum().equals(Authority.OWNER)) {
            flag = ownerService.insertOwner(userDTO) != 0;
        }

        if (flag) {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.ACCEPT, 0, null);
        }
        else {
            send_protocol = new Protocol(ProtocolType.RESPONSE, ProtocolCode.REFUSAL, 0, null);
        }
        dos.write(send_protocol.getBytes());
    }
}