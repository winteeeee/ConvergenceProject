package network;

import persistence.dto.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Viewer {
    public void adminScreen(UserDTO userInfo) {
        System.out.println();
        System.out.println("관리자 " + userInfo.getName() + "님 환영합니다.");
        System.out.println("무엇을 하시겠습니까?");
        System.out.println("[1] 가게 등록 신청 승인 / 거절");
        System.out.println("[2] 가게 정보 조회");
        System.out.println("[3] 고객과 점주 정보 조회");
        System.out.println("[4] 로그아웃");
        System.out.print("입력 : ");
    }

    public void ownerScreen(UserDTO userInfo) {
        System.out.println();
        System.out.println("점주 " + userInfo.getName() + "님 환영합니다.");
        System.out.println("무엇을 하시겠습니까?");
        System.out.println("[1] 가게 등록 신청");
        System.out.println("[2] 메뉴 등록");
        System.out.println("[3] 운영 시간 설정");
        System.out.println("[4] 주문 접수 / 거절");
        System.out.println("[5] 가게 정보 조회");
        System.out.println("[6] 메뉴 정보 조회");
        System.out.println("[7] 로그아웃");
        System.out.print("입력 : ");
    }

    public void userScreen(UserDTO userInfo) {
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
        System.out.print("입력 : ");
    }

    public UserDTO loginScreen(BufferedReader keyInput) throws IOException {
        UserDTO userInfo = new UserDTO();

        System.out.println("********** 음식 주문 시스템 **********");
        System.out.print("ID : ");
        userInfo.setId(keyInput.readLine());
        System.out.print("PW : ");
        userInfo.setPw(keyInput.readLine());

        return userInfo;
    }

    public void searchStoreScreen() {
        System.out.println("[가게 조회]");
        System.out.println("[1] 카테고리로 검색");
        System.out.println("[2] 가게명으로 검색");
        System.out.println("[3] 종료");
        System.out.print("입력 : ");
    }

    public void searchAccountScreen(UserDTO userInfo) {
        System.out.println("[계정 정보 조회]");
        System.out.println("ID : " + userInfo.getId());
        System.out.println("이름 : " + userInfo.getName());
        System.out.println("나이 : " + userInfo.getAge());
    }

    public void modificationUserScreen() {
        System.out.println("변경할 정보를 선택해주세요.");
        System.out.println("[1] 비밀번호");
        System.out.println("[2] 이름");
        System.out.println("[3] 나이");
        System.out.println("[4] 종료");
        System.out.print("입력 : ");
    }

    public void logout() {
        System.out.println("로그아웃합니다.\n");
    }
}
