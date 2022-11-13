package network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import persistence.dto.*;
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
                    /*
                    ● 개인 정보 및 비밀번호 수정
                    ● 음식점 조회
                        - 카테고리로 조회
                        - 가게명으로 조회
                    ● 음식 주문
                    ● 주문 취소
                        - 주문 접수 후 n초 이후에는 주문 취소가 불가능
                    ● 주문 내역 조회
                    ● 리뷰와 별점 등록
                    ● 가게 정보 조회
                    ● 메뉴 정보 조회
                    ● 본인의 계정 정보 조회
                    ● 주문 내역 조회
                     */
            }
        }
    }

    public static void storeRegistDetermination(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput) throws IOException {
        Protocol requestAllStoreRegistDTOs = new Protocol(ProtocolType.SEARCH, (byte)(ProtocolCode.STORE | ProtocolCode.REGIST), 0, null);
        dos.write(requestAllStoreRegistDTOs.getBytes());

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
                        Protocol registAccept = new Protocol(ProtocolType.REGISTER, (byte) (ProtocolCode.STORE | ProtocolCode.REGIST), 0, DTOs.get(idx));
                        //전달한 가게 등록 DTO의 정보를 가게 등록 테이블에서 삭제하고 가게 테이블에 등록하시오.
                        dos.write(registAccept.getBytes());
                        DTOs.remove(idx);
                        break;
                    }

                    else if (ans.equals("N") || ans.equals("n")) {
                        System.out.println("거절되었습니다.\n");
                        Protocol registRefuse = new Protocol(ProtocolType.DELETE, (byte) (ProtocolCode.STORE | ProtocolCode.REGIST), 0, DTOs.get(idx));
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

    public static void searchStore(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[가게 정보 조회]");
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

    public static void searchMenu(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[메뉴 정보 조회]");
        Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.MENU, 0, null);
        dos.write(searchStoreInfo.getBytes());

        int listLength = 0;
        for (int i = 0; i < listLength; i++) {
            MenuDTO cur = (MenuDTO) new Protocol(dis.readAllBytes()).getData();
            System.out.println(cur.toString());
        }
        System.out.println();
    }

    public static void searchOwnerAndUser(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[고객과 점주 정보 조회]");
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

    public static void logout() {
        System.out.println("로그아웃합니다.\n");
    }

    public static void adminRun(DataInputStream dis, DataOutputStream dos, BufferedReader keyInput, UserDTO userInfo) throws IOException {
        while(true) {
            System.out.println("관리자 " + userInfo.getName() + "님 환영합니다.");
            System.out.println("무엇을 하시겠습니까?");
            System.out.println("[1] 가게 등록 신청 승인 / 거절");
            System.out.println("[2] 가게 정보 조회");
            System.out.println("[3] 메뉴 정보 조회");
            System.out.println("[4] 고객과 점주 정보 조회");
            System.out.println("[5] 가게별 매출 조회");
            System.out.println("[6] 로그아웃");
            int option = Integer.parseInt(keyInput.readLine());

            if (option == 1) {
                storeRegistDetermination(dis, dos, keyInput);
            }

            else if (option == 2) {
                searchStore(dis, dos);
            }

            else if (option == 3) {
                searchMenu(dis, dos);
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
            int option = Integer.parseInt(keyInput.readLine());

            if (option == 1) {

            }

            else if (option == 2) {

            }

            else if (option == 3) {

            }

            else if (option == 4) {

            }

            else if (option == 5) {

            }

            else if (option == 6) {

            }

            else if (option == 7) {

            }

            else if (option == 8) {

            }

            else if (option == 9) {

            }

            else if (option == 10) {
                System.out.println("로그아웃합니다.\n");
                break;
            }

            else {
                System.out.println("[경고] 잘못된 입력");
            }
        }

                    /*
                    ● 가게 등록 신청
                    ● 메뉴 등록 신청
                    ● 할인 정책 설정
                    ● 가게운영 시간설정
                    ● 고객의 주문 접수/거절
                    ● 고객 리뷰에 대한 답글 등록
                    ● 주문건수, 매출현황 등 통계정보 열람
                    ● 가게 정보 조회
                    ● 메뉴 정보 조회
                    - (주문한 고객을 포함한) 주문 정보 조회
                    - (메뉴별)주문 건수, 매출 현황 등 통계정보 조회
                     */
    }
}