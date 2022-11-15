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

            UserDTO userInfo = login(dis, dos, keyInput, ID, PW);

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

        while(login) {
            System.out.println();
            System.out.println("관리자 " + userInfo.getName() + "님 환영합니다.");
            System.out.println("무엇을 하시겠습니까?");
            System.out.println("[1] 가게 등록 신청 승인 / 거절");
            System.out.println("[2] 가게 정보 조회");
            System.out.println("[3] 메뉴 정보 조회");
            System.out.println("[4] 고객과 점주 정보 조회");
            System.out.println("[5] 가게별 매출 조회");
            System.out.println("[6] 로그아웃");
            System.out.println("입력 : ");

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case 1:
                    registStoreDetermination(dis, dos, keyInput);
                    break;

                case 2:
                    searchStore(dis, dos);
                    break;

                case 3:
                    searchAllMenu(dis, dos);
                    break;

                case 4:
                    searchOwnerAndUser(dis, dos);
                    break;

                case 5:
                    break;

                case 6:
                    logout();
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

        while(login) {
            System.out.println();
            System.out.println("점주 " + userInfo.getName() + "님 환영합니다.");
            System.out.println("무엇을 하시겠습니까?");
            System.out.println("[1] 가게 등록 신청");
            System.out.println("[2] 메뉴 등록 신청");
            System.out.println("[3] 할인 정책 설정");
            System.out.println("[4] 운영 시간 설정");
            System.out.println("[5] 주문 접수 / 거절");
            System.out.println("[6] 리뷰 답글 작성");
            System.out.println("[7] 통계 정보 열람");
            System.out.println("[8] 가게 정보 조회");
            System.out.println("[9] 메뉴 정보 조회");
            System.out.println("[10] 로그아웃");
            System.out.println("입력 : ");
            System.out.println();

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case 1:
                    registStore(dos, keyInput, userInfo);
                    break;

                case 2:
                    break; //옵션 관련 정보 업데이트 시 구현

                case 3:
                    break; //구현 안된듯

                case 4:
                    setRunningTime(dis, dos, keyInput, userInfo);
                    break;

                case 5:
                    orderDetermination(dis, dos, keyInput);
                    break;

                case 6:
                    break; //이야기 필요

                case 7:
                    break; //통계 정보 아직 구현안된걸로 보임

                case 8:
                    searchStore(dis, dos);
                    break;

                case 9:
                    searchMenu(dis, dos, keyInput, userInfo);
                    break;

                case 10:
                    logout();
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

        while(login) {
            System.out.println();
            System.out.println("고객 " + userInfo.getName() + "님 환영합니다.");
            System.out.println("무엇을 하시겠습니까?");
            System.out.println("[1] 개인 정보 및 비밀번호 수정");
            System.out.println("[2] 가게 조회");
            System.out.println("[3] 음식 주문");
            System.out.println("[4] 주문 취소");
            System.out.println("[5] 주문 조회");
            System.out.println("[6] 리뷰 등록");
            System.out.println("[7] 계정 정보 조회");
            System.out.println("[8] 로그아웃");
            System.out.println("입력 : ");
            System.out.println();

            int option = Integer.parseInt(keyInput.readLine());
            switch (option) {
                case 1:
                    modificationUser(dos, keyInput, userInfo);
                    break;

                case 2:
                    searchStoreInUserRun(dis, dos, keyInput);
                    break;

                case 3:
                    //임시, 구현안됐음.
                    registOrder(dis, dos, keyInput);
                    break;

                case 4:
                    orderCancel(dis, dos, keyInput, userInfo);
                    break;

                case 5:
                    searchOrder(dis, dos, userInfo);
                    break;

                case 6:
                    registReview(dis, dos, keyInput, userInfo);
                    break;

                case 7:
                    searchAccount(userInfo);
                    break;

                case 8:
                    logout();
                    login = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
                    break;
            }
        }
    }

    public static UserDTO login(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, String ID, String PW) throws IOException {
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

    public static void registMenu(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, StoreDTO storeInfo) throws IOException {
        String name;

        ArrayList<DetailsDTO> Options = getAllOptionDTOWithStore(dis, dos, storeInfo);
        ArrayList<Integer> selectedOption = new ArrayList<>();
        System.out.println("");
        //옵션 관련 정보 업데이트 시 마저 구현
    }

    public static void registOrder(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        //토탈 오더로 넣어야하는지?? 그냥 오더로 넣어야하는지??
        //정확한 매커니즘을 알아야할듯함
    }

    public static void registReview(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(dis, dos, userInfo);

        while(true) {
            System.out.println("[리뷰 등록]");
            for (int i = 0; i < DTOs.size(); i++) {
                System.out.println("[" + i + "] " + DTOs.get(i).toString());
            }
            System.out.println("리뷰를 등록할 주문을 선택하세요(범위 외 입력 시 종료) : ");
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

    public static void orderDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        ArrayList<OrdersDTO> DTOs = getAllOrderDTO(dis, dos);

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
                        Protocol orderAccept = new Protocol(ProtocolType.RESPONSE, (byte)(ProtocolCode.ORDER | ProtocolCode.ACCEPT), 0, DTOs.get(idx));
                        //전달한 DTO의 Status를 변경
                        dos.write(orderAccept.getBytes());
                        DTOs.remove(idx);
                        break;
                    }

                    else if (ans.equals("N") || ans.equals("n")) {
                        System.out.println("거절되었습니다.\n");
                        Protocol registRefuse = new Protocol(ProtocolType.RESPONSE, (byte) (ProtocolCode.ORDER | ProtocolCode.REFUSAL), 0, DTOs.get(idx));
                        //전달한 DTO의 Status를 변경
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

    public static void searchStoreInUserRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        boolean iteration = true;
        while (iteration) {
            System.out.println("[가게 조회]");
            System.out.println("[1] 카테고리로 검색");
            System.out.println("[2] 가게명으로 검색");
            System.out.println("[3] 종료");
            System.out.println("입력 : ");

            int searchStoreOption = Integer.parseInt(keyInput.readLine());
            switch (searchStoreOption) {
                case 1:
                    break; //카테고리로 검색은 추후 구현(현재는 테이블의 원소에 없음)

                case 2:
                    System.out.println("가게명 입력 : ");
                    String storeName = keyInput.readLine();
                    searchStoreWithName(dis, dos, storeName);
                    break;

                case 3:
                    iteration = false;
                    break;

                default:
                    System.out.println(ErrorMessage.OUT_OF_BOUND);
            }
        }
    }

    public static void searchStore(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTO(dis, dos);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }
    
    public static void searchStoreWithName(DataInputStream dis, DataOutputStream dos, String storeName) throws IOException {
        System.out.println("[이름으로 가게 조회]");
        ArrayList<StoreDTO> DTOs = getAllStoreDTOWithName(dis, dos, storeName);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void searchAllMenu(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 메뉴 조회]");
        ArrayList<MenuDTO> DTOs = getAllMenuDTO(dis, dos);

        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }

    public static void searchMenu(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
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

    public static void searchOption(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        System.out.println("[전체 옵션 조회]");
        ArrayList<DetailsDTO> DTOs = getAllOptionDTOWithStore(dis, dos, storeInfo);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void searchOwnerAndUser(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[전체 점주 / 고객 조회]");
        ArrayList<UserDTO> DTOs = getAllOwnerAndUserDTO(dis, dos);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void searchOrder(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        System.out.println("[전체 점주 / 고객 조회]");
        ArrayList<OrdersDTO> DTOs = getAllOrderDTOWithUser(dis, dos, userInfo);
        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();
    }

    public static void searchAccount(UserDTO userInfo) {
        System.out.println("[계정 정보 조회]");
        System.out.println("ID : " + userInfo.getId());
        System.out.println("이름 : " + userInfo.getName());
        System.out.println("나이 : " + userInfo.getAge());
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
            System.out.println("변경할 정보를 선택해주세요.");
            System.out.println("[1] 비밀번호");
            System.out.println("[2] 이름");
            System.out.println("[3] 나이");
            System.out.println("[4] 종료");
            System.out.println("입력 : ");

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

    public static void logout() {
        System.out.println("로그아웃합니다.\n");
    }
}