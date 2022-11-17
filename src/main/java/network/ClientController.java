package network;

import persistence.dto.*;
import persistence.enums.OrdersStatus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientController {
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader keyInput;
    private Viewer viewer;

    public ClientController(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) {
        this.dis = dis;
        this.dos = dos;
        this.keyInput = keyInput;
        viewer = new Viewer();
    }

    public UserDTO login() throws IOException {
        UserDTO user = viewer.loginScreen(keyInput);

        Protocol findUser = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, user);
        dos.write(findUser.getBytes());
        UserDTO me = (UserDTO) new Protocol(dis.readAllBytes()).getData();

        return me;
    }

    public void showAdminScreen(UserDTO userInfo) {
        viewer.adminScreen(userInfo);
    }

    public void showOwnerScreen(UserDTO userInfo) {
        viewer.ownerScreen(userInfo);
    }

    public void showUserScreen(UserDTO userInfo) {
        viewer.userScreen(userInfo);
    }

    public void showLogoutMessage() {
        viewer.logout();
    }

    public ArrayList<StoreRegistDTO> getAllStoreRegistDTO() throws IOException {
        Protocol requestAllRegistStoreDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.REGIST), 0, null);
        dos.write(requestAllRegistStoreDTOs.getBytes());

        ArrayList<StoreRegistDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreRegistDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<OrdersDTO> getAllOrderDTO() throws IOException {
        Protocol requestAllOrderDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.ORDER, 0, null);
        dos.write(requestAllOrderDTOs.getBytes());

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<OrdersDTO> getAllOrderDTOWithUser(UserDTO userInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER), 0, userInfo);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //userInfo에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<OrdersDTO> getAllOrderDTOWithStore(StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER), 0, storeInfo);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //storeInfo에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<OrdersDTO> getAllHoldOrderDTOWithUser(UserDTO userInfo) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.ORDER, 0, userInfo);
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

    public ArrayList<StoreDTO> getAllStoreDTO() throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, null);
        dos.write(searchStoreInfo.getBytes());

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<StoreDTO> getAllStoreDTOWithName(String storeName) throws IOException {
        StoreDTO targetStore = new StoreDTO();
        targetStore.setName(storeName);
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, targetStore);
        //데이터로 전달한 DTO의 이름으로 검색
        dos.write(searchStoreInfo.getBytes());

        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        ArrayList<StoreDTO> DTOs = new ArrayList<>();

        for (int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<StoreDTO> getAllStoreDTOWithClassification(ClassificationDTO classificationInfo) throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, classificationInfo);
        //데이터로 전달한 DTO의 외래키를 기반으로 검색
        dos.write(searchStoreInfo.getBytes());

        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        ArrayList<StoreDTO> DTOs = new ArrayList<>();

        for (int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<StoreDTO> getAllStoreDTOWithUser(UserDTO userInfo) throws IOException {
        Protocol requestAllMyStoreDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, userInfo);
        dos.write(requestAllMyStoreDTOs.getBytes());
        //userInfo에 해당하는 모든 Store 리스트를 가져옴

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<MenuDTO> getAllMenuDTO() throws IOException {
        Protocol searchMenuInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.MENU, 0, null);
        dos.write(searchMenuInfo.getBytes());

        ArrayList<MenuDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((MenuDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<MenuDTO> getAllMenuDTOWithStore(StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyMenuDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, storeInfo);
        dos.write(requestAllMyMenuDTOs.getBytes());
        //storeInfo가 지닌 모든 menu 리스트를 가져옴

        ArrayList<MenuDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((MenuDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<DetailsDTO> getAllOptionDTOWithStore(StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyOptionDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.OPTION, 0, storeInfo);
        dos.write(requestAllMyOptionDTOs.getBytes());
        //storeInfo가 지닌 모든 DetailsDTO 리스트를 가져옴

        ArrayList<DetailsDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((DetailsDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<ClassificationDTO> getAllClassificationDTO() throws IOException {
        Protocol requestAllMyClassificationDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.CLASSIFICATION, 0, null);
        dos.write(requestAllMyClassificationDTOs.getBytes());
        //모든 카테고리DTO 리스트를 가져옴

        ArrayList<ClassificationDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public ArrayList<ClassificationDTO> getAllClassificationDTOWithStore(StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyClassificationDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.CLASSIFICATION, 0, storeInfo);
        dos.write(requestAllMyClassificationDTOs.getBytes());
        //storeInfo가 지닌 모든 카테고리DTO 리스트를 가져옴

        ArrayList<ClassificationDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }


    public ArrayList<UserDTO> getAllOwnerAndUserDTO() throws IOException {
        Protocol requestAllUserDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, null);
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

    public void registStoreDetermination() throws IOException {
        ArrayList<StoreRegistDTO> DTOs = getAllStoreRegistDTO();

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

    public void registStore(UserDTO userInfo) throws IOException {
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

    public void registMenu(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(userInfo);
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

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(storeInfo);
        ArrayList<ClassificationDTO> classificationDTOs = getAllClassificationDTOWithStore(storeInfo);
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

    public void registOrder(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO();
        viewAllStore(storeDTOs);
        System.out.print("주문할 가게를 선택하세요 : ");
        int storeIdx = Integer.parseInt(keyInput.readLine());

        ArrayList<MenuDTO> menuDTOs = getAllMenuDTOWithStore(storeDTOs.get(storeIdx));
        viewAllMenu(menuDTOs);
        System.out.print("메뉴를 선택하세요 : ");
        int menuIdx = Integer.parseInt(keyInput.readLine());

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(storeDTOs.get(storeIdx));
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

    public void registReview(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(userInfo);

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

    public void orderDetermination(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(userInfo);
        viewAllStore(storeDTOs);
        System.out.print("주문 승인 / 거절할 가게를 선택하세요(범위 외 값 입력 시 종료) : ");
        int storeIdx = Integer.parseInt(keyInput.readLine());

        if(0 <= storeIdx && storeIdx < storeDTOs.size()) {
            ArrayList<OrdersDTO> OrderDTOs = getAllOrderDTOWithStore(storeDTOs.get(storeIdx));
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

    public void viewStoreInUserRun() throws IOException {
        boolean iteration = true;
        while (iteration) {
            viewer.searchStoreScreen();

            int searchStoreOption = Integer.parseInt(keyInput.readLine());
            switch (searchStoreOption) {
                case 1:
                    viewAllClassification();
                    System.out.println("카테고리명 입력 : ");
                    String classificationName = keyInput.readLine();
                    viewStoreWithClassification(classificationName);
                    break;

                case 2:
                    viewAllStore();
                    System.out.println("가게명 입력 : ");
                    String storeName = keyInput.readLine();
                    viewStoreWithName(storeName);
                    break;

                case 3:
                    iteration = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }
    }

    public void viewAllClassification() throws IOException {
        System.out.println("[전체 카테고리 조회]");
        ArrayList<ClassificationDTO> DTOs = getAllClassificationDTO();

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewAllStore() throws IOException {
        System.out.println("[전체 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTO();

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewAllStore(ArrayList<StoreDTO> DTOs) {
        System.out.println("[전체 가게 조회]");

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewStoreWithName(String storeName) throws IOException {
        System.out.println("[이름으로 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithName(storeName);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewStoreWithClassification(String classificationName) throws IOException {
        System.out.println("[카테고리로 가게 조회]");
        ClassificationDTO target = new ClassificationDTO();
        target.setName(classificationName);
        Protocol requestClassification = new Protocol(ProtocolType.SEARCH, ProtocolCode.CLASSIFICATION, 0, target);
        dos.write(requestClassification.getBytes());
        target = (ClassificationDTO) new Protocol(dis.readAllBytes()).getData();

        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithClassification(target);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewStoreWithUser(UserDTO userInfo) throws IOException {
        System.out.println("[가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(userInfo);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewAllMenu() throws IOException {
        System.out.println("[전체 메뉴 조회]");
        ArrayList<MenuDTO> DTOs = getAllMenuDTO();

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewAllMenu(ArrayList<MenuDTO> DTOs) throws IOException {
        System.out.println("[전체 메뉴 조회]");

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewMenuWithUser(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(userInfo);

        for (int i = 0; i < storeDTOs.size(); i++) {
            System.out.println("[" + i + "] " + storeDTOs.get(i).toString());
        }
        System.out.println("메뉴를 조회할 가게 선택 : ");
        int idx = Integer.parseInt(keyInput.readLine());

        ArrayList<MenuDTO> MenuDTOs = getAllMenuDTOWithStore(storeDTOs.get(idx));
        for (int i = 0; i < MenuDTOs.size(); i++) {
            System.out.println(MenuDTOs.get(i).toString());
        }
        System.out.println();
    }

    public void viewOption(ArrayList<DetailsDTO> DTOs) {
        System.out.println("[전체 옵션 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public void viewClassification(ArrayList<ClassificationDTO> DTOs) {
        System.out.println("[전체 분류 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public void viewOwnerAndUser() throws IOException {
        System.out.println("[전체 점주 / 고객 조회]");
        ArrayList<UserDTO> DTOs = getAllOwnerAndUserDTO();
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public void viewOrderWithUser(UserDTO userInfo) throws IOException {
        System.out.println("[주문 조회]");
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(userInfo);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public void viewOrder(ArrayList<OrdersDTO> DTOs) {
        System.out.println("[주문 조회]");
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public void viewAccountInfo(UserDTO me) {
        viewer.searchAccountScreen(me);
    }

    public void setRunningTime(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(userInfo);

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

    public void modificationUser(UserDTO userInfo) throws IOException {
        //개인정보 및 비밀번호 수정
        while(true) {
            viewer.modificationUserScreen();

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

    public void orderCancel(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllHoldOrderDTOWithUser(userInfo);

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
