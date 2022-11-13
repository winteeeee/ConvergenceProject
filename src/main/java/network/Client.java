package network;

import java.io.*;
import java.net.*;
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

                if(userInfo.getAuthorityEnum().getName().equals("ADMIN")) {
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
                        /*
                        ArrayList에다가 선택된 모든 DTO들을 담고
                        가게 등록 신청 목록을 쭉 출력하고(인덱스 포함해서)
                        인덱스 선택 후 서버에 등록 승인 / 거절 응답을 보냄
                        이후 목록 재출력

                        List가 없으면 없다고 출력하고 초기 화면으로 돌아감
                         */
                        }

                        else if (option == 2) {
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

                        else if (option == 3) {
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

                        else if (option == 4) {
                            System.out.println("[고객과 점주 정보 조회]");
                            Protocol searchStoreInfo = new Protocol(ProtocolType.SEARCH, ProtocolCode.USER, 0, null);
                            dos.write(searchStoreInfo.getBytes());

                            int listLength = 0;
                            for (int i = 0; i < listLength; i++) {
                                UserDTO cur = (UserDTO) new Protocol(dis.readAllBytes()).getData();
                                String authority = cur.getAuthorityEnum().getName();
                                if (authority.equals("OWNER") || authority.equals("USER"))
                                    System.out.println(cur.toString());
                            }
                            System.out.println();
                        }

                        else if (option == 5) {
                            //아직 매출관련 메소드는 구현안된듯?
                        }

                        else if (option == 6) {
                            System.out.println("로그아웃합니다.");
                            break;
                        }
                    }
                }

                else if(userInfo.getAuthorityEnum().getName().equals("OWNER")) {
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

                else if(userInfo.getAuthorityEnum().getName().equals("USER")) {
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
}