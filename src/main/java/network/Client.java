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

            UserDTO user = new UserDTO();
            user.setId(ID);
            user.setPw(PW);

            Protocol loginInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, user);
            dos.write(loginInfo.getBytes());

            UserDTO userInfo = (UserDTO) new Protocol(dis.readAllBytes()).getData();
            String userAuthority = userInfo.getAuthorityEnum().getName();
            if(userAuthority.equals("ADMIN")) {
                adminRun(dis, dos, keyInput, userInfo);
            }

            else if(userAuthority.equals("OWNER")) {
                ownerRun(dis, dos, keyInput, userInfo);
            }

            else if(userAuthority.equals("USER")) {
                userRun(dis, dos, keyInput, userInfo);
            }
        }
    }

    public static void registStoreDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        Protocol requestAllregistStoreDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.REGIST), 0, null);
        dos.write(requestAllregistStoreDTOs.getBytes());

        ArrayList<StoreRegistDTO> DTOs = new ArrayList<>();
        int listLength = 0;
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
                        System.out.println("[경고] 잘못된 입력");
                    }
                }
            }

            else {
                break;
            }
        }
    }

    public static void orderDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        Protocol requestAllOrderDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.ORDER, 0, null);
        dos.write(requestAllOrderDTOs.getBytes());

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
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
                        System.out.println("[경고] 잘못된 입력");
                    }
                }
            }

            else {
                break;
            }
        }
    }

    public static void searchStore(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, null);
        dos.write(searchStoreInfo.getBytes());

        //먼저 몇 개만큼 받을 것인가가 필요한듯
        //프로토콜 수정 필요, 응답으로 리스트 몇개인지
        int listLength = 0; //임시로 0으로 설정
        for (int i = 0; i < listLength; i++) {
            StoreDTO cur = (StoreDTO) new Protocol(dis.readAllBytes()).getData();
            System.out.println(cur.toString());
        }
        System.out.println();
    }
    
    public static void searchStoreWithName(DataInputStream dis, DataOutputStream dos, String storeName) throws IOException {
        StoreDTO targetStore = new StoreDTO();
        targetStore.setName(storeName);
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, targetStore);
        //데이터로 전달한 DTO의 이름으로 검색
        dos.write(searchStoreInfo.getBytes());
        
        int listLength = 0;
        for (int i = 0; i < listLength; i++) {
            StoreDTO cur = (StoreDTO) new Protocol(dis.readAllBytes()).getData();
            System.out.println(cur.toString());
        }
        System.out.println();
    }

    public static void searchAllMenu(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol searchMenuInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, null);
        dos.write(searchMenuInfo.getBytes());

        //먼저 몇 개만큼 받을 것인가가 필요한듯
        //프로토콜 수정 필요, 응답으로 리스트 몇개인지
        int listLength = 0; //임시로 0으로 설정
        for (int i = 0; i < listLength; i++) {
            MenuDTO cur = (MenuDTO) new Protocol(dis.readAllBytes()).getData();
            System.out.println(cur.toString());
        }
        System.out.println();
    }

    public static void searchMenu(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        Protocol requestAllMyStoreDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, userInfo);
        dos.write(requestAllMyStoreDTOs.getBytes());
        //userInfo에 해당하는 모든 Store 리스트를 가져옴

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println("메뉴를 조회할 가게 선택 : ");
        int idx = Integer.parseInt(keyInput.readLine());

        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.MENU, 0, DTOs.get(idx));
        dos.write(searchStoreInfo.getBytes());

        int menuDTOSize = 0;
        for (int i = 0; i < menuDTOSize; i++) {
            MenuDTO cur = (MenuDTO) new Protocol(dis.readAllBytes()).getData();
            System.out.println(cur.toString());
        }
        System.out.println();
    }

    public static ArrayList<DetailsDTO> searchOption(DataInputStream dis, DataOutputStream dos, StoreDTO storeInfo) throws IOException {
        Protocol requestAllMyOptionDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, storeInfo);
        dos.write(requestAllMyOptionDTOs.getBytes());
        //storeInfo에 해당하는 모든 Option 리스트를 가져옴

        ArrayList<DetailsDTO> DTOs = new ArrayList<>();
        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            DTOs.add((DetailsDTO) new Protocol(dis.readAllBytes()).getData());
        }

        for (int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }

        System.out.println();

        return DTOs;
    }

    public static void searchOwnerAndUser(DataInputStream dis, DataOutputStream dos) throws IOException {
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, null);
        dos.write(searchStoreInfo.getBytes());

        int listLength = 0;
        for (int i = 0; i < listLength; i++) {
            UserDTO cur = (UserDTO) new Protocol(dis.readAllBytes()).getData();
            String curAuthority = cur.getAuthorityEnum().getName();
            if (curAuthority.equals("OWNER") || curAuthority.equals("USER"))
                System.out.println(cur.toString());
        }
        System.out.println();
    }

    public static ArrayList<OrdersDTO> searchOrder(DataInputStream dis, DataOutputStream dos, UserDTO userInfo) throws IOException {
        Protocol requestMyAllOrder = new Protocol(ProtocolType.SEARCH, ProtocolCode.ORDER, 0, userInfo);
        dos.write(requestMyAllOrder.getBytes());

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();

        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            DTOs.add((OrdersDTO) new Protocol(dis.readAllBytes()).getData());
            System.out.println(DTOs.get(i).toString());
        }

        return DTOs;
    }

    public static void searchAccount(UserDTO userInfo) {
        System.out.println("ID : " + userInfo.getId());
        System.out.println("이름 : " + userInfo.getName());
        System.out.println("나이 : " + userInfo.getAge());
    }

    public static void setRunningTime(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        Protocol requestAllMyStoreDTOs = new Protocol(ProtocolType.SEARCH, ProtocolCode.STORE, 0, userInfo);
        dos.write(requestAllMyStoreDTOs.getBytes());
        //userInfo에 해당하는 모든 Store 리스트를 가져옴

        ArrayList<StoreDTO> DTOs = new ArrayList<>();
        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            DTOs.add((StoreDTO) new Protocol(dis.readAllBytes()).getData());
        }

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

        Protocol requestregistStore = new Protocol(ProtocolType.REGISTER, ProtocolCode.STORE, 0, newStore);
        dos.write(requestregistStore.getBytes());
    }

    public static void registMenu(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, StoreDTO storeInfo) throws IOException {
        String name;

        ArrayList<DetailsDTO> Options = searchOption(dis, dos, storeInfo);
        ArrayList<Integer> selectedOption = new ArrayList<>();
        System.out.println("");
        //옵션 관련 정보 업데이트 시 마저 구현
    }

    public static void registOrder(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        //토탈 오더로 넣어야하는지?? 그냥 오더로 넣어야하는지??
        //정확한 매커니즘을 알아야할듯함
    }

    public static void registReview(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        ArrayList<OrdersDTO> DTOs = searchOrder(dis, dos, userInfo);

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
                System.out.println("[경고] 잘못된 입력");
            }
        }

        Protocol userModification = new Protocol(ProtocolType.MODIFICATION, ProtocolCode.USER, 0, userInfo);
        //데이터로 전달한 DTO로 변경, pk로 찾아오면 될것임.
        dos.write(userModification.getBytes());
        System.out.println("변경사항이 저장되었습니다.");
    }

    public static void orderCancel(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        Protocol requestAllMyOrder = new Protocol(ProtocolType.SEARCH, ProtocolCode.ORDER, 0, userInfo);
        dos.write(requestAllMyOrder.getBytes());

        ArrayList<OrdersDTO> DTOs = new ArrayList<>();
        int listLength = 0;
        for(int i = 0; i < listLength; i++) {
            OrdersDTO temp = (OrdersDTO) new Protocol(dis.readAllBytes()).getData();

            if(temp.getStatusEnum().getName().equals("HOLD")) {
                DTOs.add(temp);
            }
        }

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

    public static void adminRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        while(true) {
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

            if (option == 1) {
                registStoreDetermination(dis, dos, keyInput);
            }

            else if (option == 2) {
                searchStore(dis, dos);
            }

            else if (option == 3) {
                searchAllMenu(dis, dos);
            }

            else if (option == 4) {
                searchOwnerAndUser(dis, dos);
            }

            else if (option == 5) {
                //아직 매출관련 메소드는 구현안된듯?
            }

            else if (option == 6) {
                logout();
                break;
            }

            else {
                System.out.println("[경고] 잘못된 입력");
            }
        }
    }

    public static void ownerRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        while(true) {
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

            if (option == 1) {
                registStore(dos, keyInput, userInfo);
            }

            else if (option == 2) {
                //옵션 관련 정보 업데이트 시 구현
            }

            else if (option == 3) {
                //구현 안된듯
            }

            else if (option == 4) {
                setRunningTime(dis, dos, keyInput, userInfo);
            }

            else if (option == 5) {
                orderDetermination(dis, dos, keyInput);
            }

            else if (option == 6) {
                //이야기 필요
            }

            else if (option == 7) {
                //통계 정보 아직 구현안된걸로 보임
            }

            else if (option == 8) {
                searchStore(dis, dos);
            }

            else if (option == 9) {
                searchMenu(dis, dos, keyInput, userInfo);
            }

            else if (option == 10) {
                logout();
                break;
            }

            else {
                System.out.println("[경고] 잘못된 입력");
            }
        }
    }

    public static void userRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        while(true) {
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

            if (option == 1) {
                modificationUser(dos, keyInput, userInfo);
            }

            else if (option == 2) {
                while(true) {
                    System.out.println("[가게 조회]");
                    System.out.println("[1] 카테고리로 검색");
                    System.out.println("[2] 가게명으로 검색");
                    System.out.println("[3] 종료");
                    System.out.println("입력 : ");

                    int searchStoreOption = Integer.parseInt(keyInput.readLine());
                    
                    if(searchStoreOption == 1) {
                        //카테고리로 검색은 추후 구현(현재는 테이블의 원소에 없음)
                    }
                    
                    else if(searchStoreOption == 2) {
                        System.out.println("가게명 입력 : ");
                        String storeName = keyInput.readLine();
                        searchStoreWithName(dis, dos, storeName);
                    }
                    
                    else if(searchStoreOption == 3) {
                        break;
                    }
                    
                    else {
                        System.out.println("[경고] 잘못된 입력");
                    }
                }
            }

            else if (option == 3) {
                //임시, 구현안됐음.
                registOrder(dis, dos, keyInput);
            }

            else if (option == 4) {
                orderCancel(dis, dos, keyInput, userInfo);
            }

            else if (option == 5) {
                searchOrder(dis, dos, userInfo);
            }

            else if (option == 6) {
                registReview(dis, dos, keyInput, userInfo);
            }

            else if (option == 7) {
                searchAccount(userInfo);
            }

            else if (option == 8) {
                logout();
                break;
            }

            else {
                System.out.println("[경고] 잘못된 입력");
            }
        }
    }
}