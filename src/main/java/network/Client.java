package network;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;
import persistence.dto.*;
import persistence.enums.OrdersStatus;

public class Client
{
    public static void main(String args[]){
        Socket cliSocket = null;

        try{
            cliSocket = new Socket("localhost", 5000);
            DataInputStream dis = new DataInputStream(cliSocket.getInputStream());
            BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream dos = new DataOutputStream(cliSocket.getOutputStream());

            mainRun(dis, dos, keyInput);
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

    public static void mainRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        while(true){
            System.out.println("********** 음식 주문 시스템 **********");
            System.out.print("ID : ");
            String ID = keyInput.readLine();
            System.out.print("PW : ");
            String PW = keyInput.readLine();

            UserDTO userInfo = login(dis, dos, ID, PW);

            if(userInfo != null) {
                String userAuthority = userInfo.getAuthorityEnum().getName();
                if (userAuthority.equals("ADMIN")) {
                    adminRun(dis, dos, keyInput, userInfo);
                }

                else if (userAuthority.equals("OWNER")) {
                    ownerRun(dis, dos, keyInput, userInfo);
                }

                else if (userAuthority.equals("USER")) {
                    userRun(dis, dos, keyInput, userInfo);
                }
            }

            else {
                System.out.println(ErrorMessage.LOGIN_FAILED);
            }
        }
    }

    public static void adminRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        boolean login = true;
        final int REGIST_STORE_DETERMINATION = 1;
        final int VIEW_ALL_STORE = 2;
        final int VIEW_OWNER_AND_USER = 3;
        final int LOGOUT = 4;

        while(login) {
            Viewer.adminScreen(userInfo);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case REGIST_STORE_DETERMINATION:
                    registStoreDetermination(dis, dos, keyInput);
                    break;

                case VIEW_ALL_STORE:
                    viewAllStore(dis, dos);
                    break;

                case VIEW_OWNER_AND_USER:
                    viewOwnerAndUser(dis, dos);
                    break;

                case LOGOUT:
                    Viewer.logout();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    public static void ownerRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        boolean login = true;
        final int REQUEST_STORE_REGIST = 1;
        final int REGIST_MENU = 2;
        final int MODIFICATION_MANAGEMENT_TIME = 3;
        final int DETERMINATION_ORDER = 4;
        final int VIEW_STORE_INFO = 5;
        final int VIEW_MENU_INFO = 6;
        final int LOGOUT = 7;

        while(login) {
            Viewer.ownerScreen(userInfo);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case REQUEST_STORE_REGIST:
                    registStore(dos, keyInput, userInfo);
                    break;

                case REGIST_MENU:
                    registMenu(dis, dos, keyInput, userInfo);
                    break;

                case MODIFICATION_MANAGEMENT_TIME:
                    setRunningTime(dis, dos, keyInput, userInfo);
                    break;

                case DETERMINATION_ORDER:
                    orderDetermination(dis, dos, keyInput, userInfo);
                    break;

                case VIEW_STORE_INFO:
                    viewStoreWithUser(dis, dos, userInfo);
                    break;

                case VIEW_MENU_INFO:
                    viewMenuWithUser(dis, dos, keyInput, userInfo);
                    break;

                case LOGOUT:
                    Viewer.logout();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    public static void userRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        boolean login = true;
        final int MODIFICATION_USER = 1;
        final int VIEW_STORE = 2;
        final int REGIST_ORDER = 3;
        final int CANCEL_ORDER = 4;
        final int VIEW_ORDER = 5;
        final int REGIST_REVIEW = 6;
        final int VIEW_ACCOUNT_INFO = 7;
        final int LOGOUT = 8;

        while(login) {
            Viewer.userScreen(userInfo);

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case MODIFICATION_USER:
                    modificationUser(dos, keyInput, userInfo);
                    break;

                case VIEW_STORE:
                    viewStoreInUserRun(dis, dos, keyInput);
                    break;

                case REGIST_ORDER:
                    registOrder(dis, dos, keyInput, userInfo);
                    break;

                case CANCEL_ORDER:
                    orderCancel(dis, dos, keyInput, userInfo);
                    break;

                case VIEW_ORDER:
                    viewOrderWithUser(dis, dos, userInfo);
                    break;

                case REGIST_REVIEW:
                    registReview(dis, dos, keyInput, userInfo);
                    break;

                case VIEW_ACCOUNT_INFO:
                    viewAccountInfo(userInfo);
                    break;

                case LOGOUT:
                    Viewer.logout();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    public static UserDTO login(DataInputStream dis, DataOutputStream dos, String ID, String PW) throws IOException {
        UserDTO user = new UserDTO();
        user.setId(ID);
        user.setPw(PW);

        Protocol loginInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, user);
        dos.write(loginInfo.getBytes());

        UserDTO userInfo = (UserDTO) new Protocol(dis.readAllBytes()).getData();

        return userInfo;
    }

    public static ArrayList<StoreRegistDTO> getAllStoreRegistDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol requestAllRegistStoreDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.REGIST | ProtocolCode.ALL), 0, null);
        dos.write(requestAllRegistStoreDTOs.getBytes());

        ArrayList<StoreRegistDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreRegistDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<OrdersDTO> getAllOrderDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol requestAllOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER | ProtocolCode.ALL), 0, null);
        dos.write(requestAllOrderDTOs.getBytes());

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<OrdersDTO> getAllOrderDTOWithUser(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER | ProtocolCode.ALL), 0, userInfo);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //userInfo에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<OrdersDTO> getAllOrderDTOWithStore(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER | ProtocolCode.ALL), 0, storeInfo);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //storeInfo에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<OrdersDTO> getAllHoldOrderDTOWithUser(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER | ProtocolCode.ALL), 0, userInfo);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //userInfo에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            OrdersDTO cur = (OrdersDTO) new Protocol(dis.readAllBytes()).getData();

            if(cur.getStatusEnum().getName().equals("HOLD")) {
                DTOs.add(cur);
            }
        }

        return DTOs;
    }

    public static ArrayList<StoreDTO> getAllStoreDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.ALL), 0, null);
        dos.write(searchStoreInfo.getBytes());

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }
    
    public static ArrayList<StoreDTO> getAllStoreDTOWithName(DataInputStream dis, DataOutputStream dos, String storeName) throws IOException {
        StoreDTO targetStore = new StoreDTO();
        targetStore.setName(storeName);
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.ALL), 0, targetStore);
        //데이터로 전달한 DTO의 이름으로 검색
        dos.write(searchStoreInfo.getBytes());

        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        
        for (int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }
        
        return DTOs;
    }

    public static ArrayList<StoreDTO> getAllStoreDTOWithClassification(DataInputStream dis, DataOutputStream dos, ClassificationDTO classificationInfo) throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.ALL), 0, classificationInfo);
        //데이터로 전달한 DTO의 외래키를 기반으로 검색
        dos.write(searchStoreInfo.getBytes());

        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        ArrayList<StoreDTO> DTOs = new ArrayList<>();

        for (int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<StoreDTO> getAllStoreDTOWithUser(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        Protocol requestAllMyStoreDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.ALL), 0, userInfo);
        dos.write(requestAllMyStoreDTOs.getBytes());
        //userInfo에 해당하는 모든 Store 리스트를 가져옴

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<MenuDTO> getAllMenuDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol searchMenuInfo = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.MENU | ProtocolCode.ALL), 0, null);
        dos.write(searchMenuInfo.getBytes());

        ArrayList<MenuDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((MenuDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<MenuDTO> getAllMenuDTOWithStore(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyMenuDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.ALL), 0, storeInfo);
        dos.write(requestAllMyMenuDTOs.getBytes());
        //storeInfo가 지닌 모든 menu 리스트를 가져옴

        ArrayList<MenuDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((MenuDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<DetailsDTO> getAllOptionDTOWithStore(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyOptionDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.OPTION | ProtocolCode.ALL), 0, storeInfo);
        dos.write(requestAllMyOptionDTOs.getBytes());
        //storeInfo가 지닌 모든 DetailsDTO 리스트를 가져옴

        ArrayList<DetailsDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((DetailsDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<ClassificationDTO> getAllClassificationDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol requestAllMyClassificationDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.CLASSIFICATION | ProtocolCode.ALL), 0, null);
        dos.write(requestAllMyClassificationDTOs.getBytes());
        //모든 카테고리DTO 리스트를 가져옴

        ArrayList<ClassificationDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public static ArrayList<ClassificationDTO> getAllClassificationDTOWithStore(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyClassificationDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.CLASSIFICATION | ProtocolCode.ALL), 0, storeInfo);
        dos.write(requestAllMyClassificationDTOs.getBytes());
        //storeInfo가 지닌 모든 카테고리DTO 리스트를 가져옴

        ArrayList<ClassificationDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }


    public static ArrayList<UserDTO> getAllOwnerAndUserDTO(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol requestAllUserDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.USER | ProtocolCode.ALL), 0, null);
        dos.write(requestAllUserDTOs.getBytes());

        ArrayList<UserDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            UserDTO cur = (UserDTO) new Protocol(dis.readAllBytes()).getData();
            String curAuthority = cur.getAuthorityEnum().getName();
            if (curAuthority.equals("OWNER") || curAuthority.equals("USER")) {
                DTOs.add(cur);
            }
        }

        return DTOs;
    }

    public static void registStoreDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        ArrayList<StoreRegistDTO> DTOs = getAllStoreRegistDTO(dis, dos);

        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreRegistDTO) new Protocol(dis.readAllBytes()).getData());
        }

        while(DTOs.size() > 0) {
            for (int i = 0; i < DTOs.size(); i++) {
                System.out.println("[" + i + "] " + DTOs.get(i).toString());
            }

            System.out.println("승인 / 거절할 요청 선택(범위 외 값 입력 시 종료) : ");
            int idx = Integer.parseInt(keyInput.readLine());

            if (0 <= idx && idx < DTOs.size()) {
                while (true) {
                    System.out.println("승인 : Y/y, 거절 : N/n");
                    String ans = keyInput.readLine();

                    if (ans.equals("Y") || ans.equals("y")) {
                        System.out.println("승인되었습니다.\n");
                        Protocol registAccept = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.STORE | ProtocolCode.ACCEPT), 0, DTOs.get(idx));
                        //전달한 가게 등록 DTO의 정보를 가게 등록 테이블에서 삭제하고 가게 테이블에 등록하시오.
                        dos.write(registAccept.getBytes());
                        DTOs.remove(idx);
                        break;
                    }

                    else if (ans.equals("N") || ans.equals("n")) {
                        System.out.println("거절되었습니다.\n");
                        Protocol registRefuse = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.STORE | ProtocolCode.REFUSAL), 0, DTOs.get(idx));
                        //전달한 가게 등록 DTO의 정보를 가게 등록 테이블에서 삭제하시오.
                        DTOs.remove(idx);
                        break;
                    }

                    else {
                        System.out.println(ErrorMessage.OUT_OF_BOUND);
                    }
                }
            }

            else {
                break;
            }
        }
    }

    public static void registStore(DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        String name, comment, address, phone;

        System.out.println("[가게 등록]");
        System.out.println("상호명 : ");
        name = keyInput.readLine();
        System.out.println("간단한 가게 소개 : ");
        comment = keyInput.readLine();
        System.out.println("주소 : ");
        address = keyInput.readLine();
        System.out.println("가게 전화번호 : ");
        phone = keyInput.readLine();

        StoreDTO newStore = new StoreDTO();
        newStore.setName(name);
        newStore.setComment(comment);
        newStore.setAddress(address);
        newStore.setPhone(phone);
        newStore.setUser_pk(userInfo.getPk());

        Protocol requestStoreRegist = new Protocol(ProtocolType.REGISTER, ProtocolCode.STORE, 0, newStore);
        dos.write(requestStoreRegist.getBytes());
    }

    public static void registMenu(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(dis, dos, userInfo);
        viewAllStore(storeDTOs);
        StoreDTO storeInfo = null;
        while(true) {
            System.out.println("가게를 선택해주세요 : ");
            int select = Integer.parseInt(keyInput.readLine());

            if(0 <= select && select <  storeDTOs.size()) {
                storeInfo = storeDTOs.get(select);
                break;
            }

            else {
                System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(dis, dos, storeInfo);
        ArrayList<ClassificationDTO> classificationDTOs = getAllClassificationDTOWithStore(dis, dos, storeInfo);
        ArrayList<Integer> selectedOption = new ArrayList<>();

        viewClassification(classificationDTOs);
        ClassificationDTO selectedClass = null;
        while(true) {
            System.out.println("분류를 선택해주세요 : ");
            int select = Integer.parseInt(keyInput.readLine());

            if(0 <= select && select < classificationDTOs.size()) {
                selectedClass = classificationDTOs.get(select);
                break;
            }

            else {
                System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }

        viewOption(optionDTOs);
        System.out.println("등록할 옵션을 모두 선택하세요");
        System.out.println("범위 바깥 값을 입력하거나 모든 옵션을 선택하면 입력이 종료됩니다.");
        while(selectedOption.size() < optionDTOs.size()) {
            System.out.print("입력 : ");
            selectedOption.add(Integer.parseInt(keyInput.readLine()));
            System.out.println("등록되었습니다.");
        }

        System.out.println("메뉴 정보를 등록합니다.");
        System.out.print("메뉴 이름 : ");
        String name = keyInput.readLine();
        System.out.print("가격 : ");
        int price = Integer.parseInt(keyInput.readLine());
        System.out.print("수량 : ");
        int stock = Integer.parseInt(keyInput.readLine());

        MenuDTO newMenu = new MenuDTO();
        newMenu.setClassification_id(selectedClass.getId());
        newMenu.setName(name);
        newMenu.setPrice(price);
        newMenu.setStock(stock);

        Protocol registMenu = new Protocol(ProtocolType.REGISTER, ProtocolCode.MENU, 0, newMenu);
        dos.write(registMenu.getBytes());
        //옵션을 제외한 정보들을 먼저 보내고

        dos.write(Serializer.intToByteArray(selectedOption.size()));
        //보낼 옵션 리스트의 크기
        for(int i = 0; i < selectedOption.size(); i++) {
            dos.write(optionDTOs.get(selectedOption.get(i)).getBytes());
            //옵션들의 정보들을 쭉 보낸다.
        }

        System.out.println();
    }

    public static void registOrder(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO(dis, dos);
        viewAllStore(storeDTOs);
        System.out.print("주문할 가게를 선택하세요 : ");
        int storeIdx = Integer.parseInt(keyInput.readLine());

        ArrayList<MenuDTO> menuDTOs = getAllMenuDTOWithStore(dis, dos, storeDTOs.get(storeIdx));
        viewAllMenu(menuDTOs);
        System.out.print("메뉴를 선택하세요 : ");
        int menuIdx = Integer.parseInt(keyInput.readLine());

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(dis, dos, storeDTOs.get(storeIdx));
        viewOption(optionDTOs);
        ArrayList<Integer> optionIdxes = new ArrayList<>();
        System.out.println("옵션을 선택하세요");
        System.out.println("범위 바깥 값을 입력하거나 모든 옵션을 선택하면 입력이 종료됩니다.");
        while(optionIdxes.size() < optionDTOs.size()) {
            System.out.print("입력 : ");
            optionIdxes.add(Integer.parseInt(keyInput.readLine()));
            System.out.println("등록되었습니다.");
        }

        OrdersDTO newOrder = new OrdersDTO();
        newOrder.setStatus(OrdersStatus.HOLD.getCode());
        newOrder.setRegdate(LocalDateTime.now());
        newOrder.setStore_id(storeDTOs.get(storeIdx).getId());
        newOrder.setMenu_id(menuDTOs.get(menuIdx).getId());
        newOrder.setUser_pk(userInfo.getPk());

        Protocol registOrder = new Protocol(ProtocolType.REGISTER, ProtocolCode.ORDER, 0, newOrder);
        dos.write(registOrder.getBytes());
        //등록할 주문 정보를 보내고

        dos.write(Serializer.intToByteArray(optionIdxes.size()));
        //옵션의 크기를 보냄
        for(int i = 0; i < optionIdxes.size(); i++) {
            dos.write(optionDTOs.get(optionIdxes.get(i)).getBytes());
            //옵션 정보들을 하나씩 보낸다.
        }


        System.out.println("주문이 정상적으로 등록되었습니다.");
    }

    public static void registReview(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(dis, dos, userInfo);

        while(true) {
            viewOrder(DTOs);
            System.out.print("리뷰를 등록할 주문을 선택하세요(범위 외 입력 시 종료) : ");
            int select = Integer.parseInt(keyInput.readLine());

            if (0 <= select && select < DTOs.size()) {
                System.out.println("리뷰 내용 입력 : ");
                String contents = keyInput.readLine();
                System.out.println("별점 입력(1 ~ 5) : ");
                int starRank = Integer.parseInt(keyInput.readLine());

                ReviewDTO newReivew = new ReviewDTO();
                newReivew.setContents(contents);
                newReivew.setRegdate(LocalDateTime.now());
                newReivew.setStar_rating(starRank);
                newReivew.setUser_pk(userInfo.getPk());
                newReivew.setOrders_id(DTOs.get(select).getId());

                Protocol registReview = new Protocol(ProtocolType.REGISTER, ProtocolCode.REVIEW, 0, newReivew);
                //데이터로 전달한 녀석을 리뷰 테이블에 insert
                dos.write(registReview.getBytes());
                System.out.println("리뷰가 등록되었습니다.");
                System.out.println();
            }

            else {
                break;
            }
        }
    }

    public static void orderDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(dis, dos, userInfo);
        viewAllStore(storeDTOs);
        System.out.print("주문 승인 / 거절할 가게를 선택하세요(범위 외 값 입력 시 종료) : ");
        int storeIdx = Integer.parseInt(keyInput.readLine());

        if(0 <= storeIdx && storeIdx < storeDTOs.size()) {
            ArrayList<OrdersDTO> OrderDTOs = getAllOrderDTOWithStore(dis, dos, storeDTOs.get(storeIdx));
            while (OrderDTOs.size() > 0) {
                viewOrder(OrderDTOs);

                System.out.print("승인 / 거절할 요청 선택(범위 외 값 입력 시 종료) : ");
                int idx = Integer.parseInt(keyInput.readLine());

                if (0 <= idx && idx < OrderDTOs.size()) {
                    while (true) {
                        System.out.println("승인 : Y/y, 거절 : N/n");
                        String ans = keyInput.readLine();

                        if (ans.equals("Y") || ans.equals("y")) {
                            System.out.println("승인되었습니다.\n");
                            Protocol orderAccept = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.ORDER | ProtocolCode.ACCEPT), 0, OrderDTOs.get(idx));
                            //전달한 DTO의 Status를 변경
                            dos.write(orderAccept.getBytes());
                            OrderDTOs.remove(idx);
                            break;
                        }

                        else if (ans.equals("N") || ans.equals("n")) {
                            System.out.println("거절되었습니다.\n");
                            Protocol registRefuse = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.ORDER | ProtocolCode.REFUSAL), 0, OrderDTOs.get(idx));
                            //전달한 DTO의 Status를 변경
                            OrderDTOs.remove(idx);
                            break;
                        }

                        else {
                            System.out.println(ErrorMessage.OUT_OF_BOUND);
                        }
                    }
                }

                else {
                    break;
                }
            }
        }
    }

    public static void viewStoreInUserRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        boolean iteration = true;
        while (iteration) {
            Viewer.searchStoreScreen();

            int searchStoreOption = Integer.parseInt(keyInput.readLine());
            switch (searchStoreOption) {
                case 1:
                    viewAllClassification(dis, dos);
                    System.out.println("카테고리명 입력 : ");
                    String classificationName = keyInput.readLine();
                    viewStoreWithClassification(dis, dos, classificationName);
                    break;

                case 2:
                    viewAllStore(dis, dos);
                    System.out.println("가게명 입력 : ");
                    String storeName = keyInput.readLine();
                    viewStoreWithName(dis, dos, storeName);
                    break;

                case 3:
                    iteration = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }
    }

    public static void viewAllClassification(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 카테고리 조회]");
        ArrayList<ClassificationDTO> DTOs = getAllClassificationDTO(dis, dos);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewAllStore(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTO(dis, dos);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewAllStore(ArrayList<StoreDTO> DTOs) {
        System.out.println("[전체 가게 조회]");

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }
    
    public static void viewStoreWithName(DataInputStream dis, DataOutputStream dos, String storeName) throws IOException {
        System.out.println("[이름으로 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithName(dis, dos, storeName);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewStoreWithClassification(DataInputStream dis, DataOutputStream dos, String classificationName) throws IOException {
        System.out.println("[카테고리로 가게 조회]");
        ClassificationDTO target = new ClassificationDTO();
        target.setName(classificationName);
        Protocol requestClassification = new Protocol(ProtocolType.SEARCH, ProtocolCode.CLASSIFICATION, 0, target);
        dos.write(requestClassification.getBytes());
        target = (ClassificationDTO) new Protocol(dis.readAllBytes()).getData();

        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithClassification(dis, dos, target);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewStoreWithUser(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        System.out.println("[가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(dis, dos, userInfo);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewAllMenu(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 메뉴 조회]");
        ArrayList<MenuDTO> DTOs = getAllMenuDTO(dis, dos);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewAllMenu(ArrayList<MenuDTO> DTOs) throws IOException {
        System.out.println("[전체 메뉴 조회]");

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewMenuWithUser(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(dis, dos, userInfo);

        for (int i = 0; i < storeDTOs.size(); i++) {
            System.out.println("[" + i + "] " + storeDTOs.get(i).toString());
        }
        System.out.println("메뉴를 조회할 가게 선택 : ");
        int idx = Integer.parseInt(keyInput.readLine());

        ArrayList<MenuDTO> MenuDTOs = getAllMenuDTOWithStore(dis, dos, storeDTOs.get(idx));
        for (int i = 0; i < MenuDTOs.size(); i++) {
            System.out.println(MenuDTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void viewOption(ArrayList<DetailsDTO> DTOs) {
        System.out.println("[전체 옵션 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void viewClassification(ArrayList<ClassificationDTO> DTOs) {
        System.out.println("[전체 분류 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void viewOwnerAndUser(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 점주 / 고객 조회]");
        ArrayList<UserDTO> DTOs = getAllOwnerAndUserDTO(dis, dos);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void viewOrderWithUser(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        System.out.println("[주문 조회]");
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(dis, dos, userInfo);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void viewOrder(ArrayList<OrdersDTO> DTOs) {
        System.out.println("[주문 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void viewAccountInfo(UserDTO userInfo) {
        Viewer.searchAccountScreen(userInfo);
    }

    public static void setRunningTime(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(dis, dos, userInfo);

        while(DTOs.size() > 0) {
            for (int i = 0; i < DTOs.size(); i++) {
                System.out.println("[" + i + "] " + DTOs.get(i).toString());
            }

            System.out.println("운영시간 변경 대상 선택(범위 외 값 입력 시 종료) : ");
            int idx = Integer.parseInt(keyInput.readLine());

            if (0 <= idx && idx < DTOs.size()) {
                StringTokenizer st;

                System.out.println("변경할 개점 시간 : ");
                st = new StringTokenizer(keyInput.readLine());
                int openHour = Integer.parseInt(st.nextToken());
                int openMinute = Integer.parseInt(st.nextToken());

                System.out.println("변경할 폐점 시간 : ");
                st = new StringTokenizer(keyInput.readLine());
                int closeHour = Integer.parseInt(st.nextToken());
                int closeMinute = Integer.parseInt(st.nextToken());

                LocalDateTime newOpenTime = LocalDateTime.of(1, 1, 1, openHour, openMinute);
                LocalDateTime newCloseTime = LocalDateTime.of(1, 1, 1, closeHour, closeMinute);
                DTOs.get(idx).setOpen_time(newOpenTime);
                DTOs.get(idx).setClose_time(newCloseTime);
                Protocol requestSetRunningTime = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.STORE, 0, DTOs.get(idx));
                //보내진 DTO의 내용으로 스토어 수정
                dos.write(requestSetRunningTime.getBytes());
            }

            else {
                break;
            }
        }
    }

    public static void modificationUser(DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        //개인정보 및 비밀번호 수정
        while(true) {
            Viewer.modificationUserScreen();

            int option = Integer.parseInt(keyInput.readLine());

            if(option == 1) {
                System.out.println("새로운 비밀번호를 입력하세요.");
                System.out.println("입력 : ");
                userInfo.setPw(keyInput.readLine());
                System.out.println();
            }

            else if(option == 2) {
                System.out.println("새로운 이름을 입력하세요.");
                System.out.println("입력 : ");
                userInfo.setName(keyInput.readLine());
                System.out.println();
            }

            else if(option == 3) {
                System.out.println("새로운 나이를 입력하세요.");
                System.out.println("입력 : ");
                userInfo.setAge(Integer.parseInt(keyInput.readLine()));
                System.out.println();
            }

            else if(option == 4) {
                break;
            }

            else {
                System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }

        Protocol userModification = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.USER, 0, userInfo);
        //데이터로 전달한 DTO로 변경, pk로 찾아오면 될것임.
        dos.write(userModification.getBytes());
        System.out.println("변경사항이 저장되었습니다.");
    }

    public static void orderCancel(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllHoldOrderDTOWithUser(dis, dos, userInfo);

        while(true) {
            System.out.println("[취소할 주문 선택]");
            for (int i = 0; i < DTOs.size(); i++) {
                System.out.println("[" + i + "] " + DTOs.get(i).toString());
            }
            System.out.println("입력(범위 외 값 입력 시 종료) : ");

            int select = Integer.parseInt(keyInput.readLine());

            if(0 <= select && select < DTOs.size()) {
                DTOs.get(select).setStatus(OrdersStatus.CANCEL.getCode());
                Protocol requestCancel = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.ORDER, 0, DTOs.get(select));
                //데이터로 전달한 DTO로 변경
                dos.write(requestCancel.getBytes());
            }

            else {
                break;
            }
        }

    }
}