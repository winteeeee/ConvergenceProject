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
        final int LOGIN = 1;
        final int OWNER_REGIST = 1;
        final int REGIST = 2;
        final int USER_REGIST = 2;

        while (true) {
            int option = viewer.initScreen(keyInput);

            if(option == LOGIN) {
                UserDTO user = viewer.loginScreen(keyInput);
                Protocol findUser = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, user);
                dos.write(findUser.getBytes());
                UserDTO me = (UserDTO) new Protocol(dis.readAllBytes()).getData();

                return me;
            }

            else if(option == REGIST) {
                int registOption = viewer.registScreen(keyInput);
                UserDTO userInfo = new UserDTO();

                if(registOption == OWNER_REGIST) {
                    userInfo = viewer.ownerRegistScreen(keyInput);
                }

                else if(registOption == USER_REGIST) {
                    userInfo = viewer.userRegistScreen(keyInput);
                }

                else {
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                }

                if(registOption == OWNER_REGIST || registOption == USER_REGIST) {
                    Protocol registUser = new Protocol(ProtocolType.REGISTER, (byte)(ProtocolCode.USER | ProtocolCode.REGIST), 0, userInfo);
                    dos.write(registUser.getBytes());
                    viewer.showRegistUserCompleteMessage();
                }
            }

            else {
                System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }
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

    public <T> ArrayList<OrdersDTO> getAllOrderDTO(T info) throws IOException {
        Protocol requestAllMyOrderDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.ORDER), 0, (DTO) info);
        dos.write(requestAllMyOrderDTOs.getBytes());
        //info에 해당하는 모든 Orders 리스트를 가져옴

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public <T> ArrayList<OrdersDTO> getAllHoldOrderDTO(T info) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTO(info);
        ArrayList<OrdersDTO> result = new ArrayList<>();

        for(int i = 0; i < DTOs.size(); i++) {
            OrdersDTO cur = DTOs.get(i);

            if(cur.getStatusEnum().getName().equals("HOLD")) {
                result.add(cur);
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

    public <T> ArrayList<StoreDTO> getAllStoreDTO(T info) throws IOException {
        Protocol requestAllMyStoreDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, (DTO) info);
        dos.write(requestAllMyStoreDTOs.getBytes());
        //info에 해당하는 모든 Store 리스트를 가져옴

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

    public <T> ArrayList<MenuDTO> getAllMenuDTO(T info) throws IOException {
        Protocol requestAllMyMenuDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.MENU, 0, (DTO) info);
        dos.write(requestAllMyMenuDTOs.getBytes());
        //info가 지닌 모든 menu 리스트를 가져옴

        ArrayList<MenuDTO> DTOs = new ArrayList<>();
        int listLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        for(int i = 0; i < listLength; i++) {
            DTOs.add((MenuDTO) new Protocol(dis.readAllBytes()).getData());
        }

        return DTOs;
    }

    public <T> ArrayList<DetailsDTO> getAllOptionDTO(T info) throws IOException {
        Protocol requestAllMyOptionDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.OPTION, 0, (DTO) info);
        dos.write(requestAllMyOptionDTOs.getBytes());
        //info가 지닌 모든 DetailsDTO 리스트를 가져옴

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

    public <T> ArrayList<ClassificationDTO> getAllClassificationDTO(T info) throws IOException {
        Protocol requestAllMyClassificationDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.CLASSIFICATION, 0, (DTO) info);
        dos.write(requestAllMyClassificationDTOs.getBytes());
        //info가 지닌 모든 카테고리DTO 리스트를 가져옴

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
            int idx = viewer.getIdx(DTOs);

            if (0 <= idx && idx < DTOs.size()) {
                while (true) {
                    String ans = viewer.getDetermination();

                    if (ans.equals("Y") || ans.equals("y")) {
                        viewer.showAcceptMessage();
                        Protocol registAccept = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.STORE | ProtocolCode.ACCEPT), 0, DTOs.get(idx));
                        //전달한 가게 등록 DTO의 정보를 가게 등록 테이블에서 삭제하고 가게 테이블에 등록하시오.
                        dos.write(registAccept.getBytes());
                        DTOs.remove(idx);
                        break;
                    }

                    else if (ans.equals("N") || ans.equals("n")) {
                        viewer.showRefusalMessage();
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
        String[] storeInfo = viewer.getStoreInfo();

        StoreRegistDTO newStore = new StoreRegistDTO();
        newStore.setName(storeInfo[0]);
        newStore.setComment(storeInfo[1]);
        newStore.setAddress(storeInfo[2]);
        newStore.setPhone(storeInfo[3]);
        newStore.setUser_pk(userInfo.getPk());

        Protocol requestStoreRegist = new Protocol(ProtocolType.REGISTER, (byte) (ProtocolCode.STORE | ProtocolCode.REGIST), 0, newStore);
        dos.write(requestStoreRegist.getBytes());
    }

    public void registMenu(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO(userInfo);
        StoreDTO storeInfo = viewer.selectStore(storeDTOs);

        ArrayList<ClassificationDTO> classificationDTOs = getAllClassificationDTO(storeInfo);
        ClassificationDTO selectedClass = viewer.selectClassification(classificationDTOs);

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTO(storeInfo);
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

        ArrayList<MenuDTO> menuDTOs = getAllMenuDTO(storeDTOs.get(storeIdx));
        int menuIdx = viewer.getIdx(menuDTOs);

        ArrayList<DetailsDTO> optionDTOs = getAllOptionDTO(storeDTOs.get(storeIdx));
        ArrayList<Integer> optionIdxes = viewer.getOptionIdxes(optionDTOs);
        String details = null;
        for(int i = 0; i < optionIdxes.size() - 1; i++) {
            details += (optionIdxes.get(i) + ", ");
        }
        details += optionIdxes.get(optionIdxes.size() - 1);

        OrdersDTO newOrder = new OrdersDTO();
        newOrder.setStatus(OrdersStatus.HOLD.getCode());
        newOrder.setRegdate(LocalDateTime.now());
        newOrder.setStore_id(storeDTOs.get(storeIdx).getId());
        newOrder.setMenu_id(menuDTOs.get(menuIdx).getId());
        newOrder.setUser_pk(userInfo.getPk());
        newOrder.setDetails(details);

        Protocol registOrder = new Protocol(ProtocolType.REGISTER, ProtocolCode.ORDER, 0, newOrder);
        dos.write(registOrder.getBytes());
        //등록할 주문 정보를 보냄
        viewer.showOrderCompleteMessage();
    }

    public void registReview(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTO(userInfo);

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

    public void viewReview(UserDTO userInfo) throws IOException {
        int curPage = 1;

        while(true) {
            Protocol requestReview = new Protocol(ProtocolType.SEARCH, ProtocolCode.REVIEW, 0, userInfo);
            dos.write(requestReview.getBytes());
            dos.write(Serializer.intToByteArray(curPage));

            final int MAX_PAGE = Deserializer.byteArrayToInt(dis.readAllBytes());
            int classificationListLength = Deserializer.byteArrayToInt(dis.readAllBytes());
            ArrayList<ClassificationDTO> classificationDTOs = new ArrayList<>();
            for (int i = 0; i < classificationListLength; i++) {
                classificationDTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());

                int reviewListLength = Deserializer.byteArrayToInt(dis.readAllBytes());
                ArrayList<ReviewDTO> reviewDTOs = new ArrayList<>();
                for (int j = 0; j < reviewListLength; j++) {
                    reviewDTOs.add((ReviewDTO) new Protocol(dis.readAllBytes()).getData());
                }

                viewer.viewDTO(classificationDTOs.get(i));
                viewer.viewDTOs(reviewDTOs);
            }
            viewer.viewPage(curPage, MAX_PAGE, 5);
            curPage = viewer.getNextPage();

            if (!(1 <= curPage && curPage <= MAX_PAGE)) {
                break;
            }
        }
    }

    public void orderDetermination(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO(userInfo);
        int storeIdx = viewer.getIdx(storeDTOs);

        if(0 <= storeIdx && storeIdx < storeDTOs.size()) {
            ArrayList<OrdersDTO> orderDTOs = getAllHoldOrderDTO(storeDTOs.get(storeIdx));
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

    public <T> void modificationMenu(T info) throws IOException {
        ArrayList<MenuDTO> menuDTOs = viewMenu(info);
        int idx = viewer.getIdx();
        MenuDTO selected = menuDTOs.get(idx);
        Pair<String, Integer> modificationInfo = viewer.modificationMenuScreen(selected);
        selected.setName(modificationInfo.first());
        selected.setPrice(modificationInfo.second());

        Protocol requestModification = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.MENU, 0, selected);
        //전달한 메뉴 DTO를 update
        dos.write(requestModification.getBytes());
    }

    public void setRunningTime(UserDTO userInfo) throws IOException {
        ArrayList<StoreDTO> DTOs = getAllStoreDTO(userInfo);

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

    public void orderCancel(UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllHoldOrderDTO(userInfo);

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

    public void viewStore() throws IOException {
        viewer.viewDTOs(getAllStoreDTO());
    }

    public <T> ArrayList<MenuDTO> viewMenu(T info) throws IOException {
        ArrayList<StoreDTO> storeDTOs = getAllStoreDTO(info);
        int idx = viewer.getIdx(storeDTOs);

        Protocol requestMenu = new Protocol(ProtocolType.SEARCH, ProtocolCode.MENU, 0, storeDTOs.get(idx));
        dos.write(requestMenu.getBytes());

        int classificationListLength = Deserializer.byteArrayToInt(dis.readAllBytes());
        ArrayList<ClassificationDTO> classificationDTOs = new ArrayList<>();
        ArrayList<MenuDTO> result = new ArrayList<>();
        for(int i = 0; i < classificationListLength; i++) {
            classificationDTOs.add((ClassificationDTO) new Protocol(dis.readAllBytes()).getData());

            int menuListLength = Deserializer.byteArrayToInt(dis.readAllBytes());
            ArrayList<MenuDTO> menuDTOs = new ArrayList<>();
            for (int j = 0; j < menuListLength; j++) {
                MenuDTO cur = (MenuDTO) new Protocol(dis.readAllBytes()).getData();
                menuDTOs.add(cur);
                result.add(cur);
            }

            viewer.viewDTO(classificationDTOs.get(i));
            viewer.viewDTOs(menuDTOs, result.size());
        }

        return result;
    }

    public <T> void viewOrder(T info) throws IOException {
        viewer.viewDTOs(getAllOrderDTO(info));
    }
}
