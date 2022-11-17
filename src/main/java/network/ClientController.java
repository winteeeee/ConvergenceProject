package network;

import org.testng.internal.collections.Pair;
import persistence.dto.*;
import persistence.enums.OrdersStatus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ClientController {
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader keyInput;
    private Viewer viewer;

    public ClientController(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) {
        this.dis = dis;
        this.dos = dos;
        this.keyInput = keyInput;
        viewer = new Viewer(keyInput);
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
        StoreDTO storeInfo = viewer.selectStore(storeDTOs);

        ArrayList<ClassificationDTO> classificationDTOs = getAllClassificationDTOWithStore(storeInfo);
        ClassificationDTO selectedClass = viewer.selectClassification(classificationDTOs);

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(storeInfo);
        ArrayList<Integer> selectedOption = viewer.selectOption(optionDTOs);

        MenuDTO newMenu = viewer.setNewMenu(selectedClass);

        Protocol registMenu = new Protocol(ProtocolType.REGISTER, ProtocolCode.MENU, 0, newMenu);
        dos.write(registMenu.getBytes());
        //옵션을 제외한 정보들을 먼저 보내고

        dos.write(Serializer.intToByteArray(selectedOption.size()));
        //보낼 옵션 리스트의 크기
        for(int i = 0; i < selectedOption.size(); i++) {
            dos.write(optionDTOs.get(selectedOption.get(i)).getBytes());
            //옵션들의 정보들을 쭉 보낸다.
        }
    }

    public void registOrder(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO();
        int storeIdx = viewer.getIdx(storeDTOs);

        ArrayList<MenuDTO> menuDTOs = getAllMenuDTOWithStore(storeDTOs.get(storeIdx));
        int menuIdx = viewer.getIdx(menuDTOs);

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTOWithStore(storeDTOs.get(storeIdx));
        ArrayList<Integer> optionIdxes = viewer.getOptionIdxes(optionDTOs);

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

        viewer.showOrderCompleteMessage();
    }

    public void registReview(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(userInfo);

        while(true) {
            int select = viewer.getIdx(DTOs);

            if (0 <= select && select < DTOs.size()) {
                Pair<String, Integer> reviewInfo = viewer.getReviewInfo();

                ReviewDTO newReivew = new ReviewDTO();
                newReivew.setContents(reviewInfo.first());
                newReivew.setRegdate(LocalDateTime.now());
                newReivew.setStar_rating(reviewInfo.second());
                newReivew.setUser_pk(userInfo.getPk());
                newReivew.setOrders_id(DTOs.get(select).getId());

                Protocol registReview = new Protocol(ProtocolType.REGISTER, ProtocolCode.REVIEW, 0, newReivew);
                //데이터로 전달한 녀석을 리뷰 테이블에 insert
                dos.write(registReview.getBytes());
                viewer.showReviewCompleteMessage();
            }

            else {
                break;
            }
        }
    }

    public void orderDetermination(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTOWithUser(userInfo);
        int storeIdx = viewer.getIdx(storeDTOs);

        if(0 <= storeIdx && storeIdx < storeDTOs.size()) {
            ArrayList<OrdersDTO> orderDTOs = getAllOrderDTOWithStore(storeDTOs.get(storeIdx));
            while (orderDTOs.size() > 0) {
                int idx = viewer.getIdx(orderDTOs);

                if (0 <= idx && idx < orderDTOs.size()) {
                    while (true) {
                        String ans = viewer.getDetermination();

                        if (ans.equals("Y") || ans.equals("y")) {
                            viewer.showAcceptMessage();
                            Protocol orderAccept = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.ORDER | ProtocolCode.ACCEPT), 0, orderDTOs.get(idx));
                            //전달한 DTO의 Status를 변경
                            dos.write(orderAccept.getBytes());
                            orderDTOs.remove(idx);
                            break;
                        }

                        else if (ans.equals("N") || ans.equals("n")) {
                            viewer.showRefusalMessage();
                            Protocol registRefuse = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.ORDER | ProtocolCode.REFUSAL), 0, orderDTOs.get(idx));
                            //전달한 DTO의 Status를 변경
                            dos.write(registRefuse.getBytes());
                            orderDTOs.remove(idx);
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

    public void setRunningTime(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(userInfo);

        while(DTOs.size() > 0) {
            viewer.viewDTOs(DTOs);
            int idx = viewer.getIdx(DTOs);

            if (0 <= idx && idx < DTOs.size()) {
                int[] changeTimeInfo = viewer.getChangeTimeInfo();

                LocalDateTime newOpenTime = LocalDateTime.of(1, 1, 1, changeTimeInfo[0], changeTimeInfo[1]);
                LocalDateTime newCloseTime = LocalDateTime.of(1, 1, 1, changeTimeInfo[2], changeTimeInfo[3]);
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
        boolean escapeFlag = true;
        while(!escapeFlag) {
            int option = viewer.modifiUserScreenAndGetOption();

            switch (option) {
                case 1:
                    viewer.changeUserPW(userInfo);
                    break;

                case 2:
                    viewer.changeUserName(userInfo);
                    break;

                case 3:
                    viewer.changeUserAge(userInfo);
                    break;

                case 4:
                    escapeFlag = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }

        Protocol userModification = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.USER, 0, userInfo);
        //데이터로 전달한 DTO로 변경, pk로 찾아오면 될것임.
        dos.write(userModification.getBytes());
        viewer.showSaveMessage();
    }

    public void orderCancel(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllHoldOrderDTOWithUser(userInfo);

        while(true) {
            int select = viewer.getIdx(DTOs);

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

    public void viewAllStore() throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTO();
        viewer.viewDTOs(DTOs);
    }

    public void viewOwnerAndUser() throws IOException {
        ArrayList<UserDTO> DTOs = getAllOwnerAndUserDTO();
        viewer.viewDTOs(DTOs);
    }

    public void viewStoreWithUser(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithUser(userInfo);
        viewer.viewDTOs(DTOs);
    }

    public void viewMenuWithUser(UserDTO userInfo) throws IOException {
        viewer.viewMenuWithUser(userInfo);
    }

    public void viewStoreInUserRun() throws IOException {
        viewer.viewStoreInUserRun();
    }

    public void viewOrderWithUser(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(userInfo);
        viewer.viewDTOs(DTOs);
    }

    public void viewAccountInfo(UserDTO userInfo) {
        viewer.searchAccountScreen(userInfo);
    }
}
