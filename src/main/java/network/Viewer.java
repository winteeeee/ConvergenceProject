package network;

import org.testng.internal.collections.Pair;
import persistence.dto.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Viewer {
    private BufferedReader keyInput;

    public Viewer(BufferedReader keyInput) {
        this.keyInput = keyInput;
    }
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

    public int searchStoreScreenAndGetOption() throws IOException {
        System.out.println("[가게 조회]");
        System.out.println("[1] 카테고리로 검색");
        System.out.println("[2] 가게명으로 검색");
        System.out.println("[3] 종료");
        System.out.print("입력 : ");

        return Integer.parseInt(keyInput.readLine());
    }

    public void searchAccountScreen(UserDTO userInfo) {
        System.out.println("[계정 정보 조회]");
        System.out.println("ID : " + userInfo.getId());
        System.out.println("이름 : " + userInfo.getName());
        System.out.println("나이 : " + userInfo.getAge());
    }

    public int modifiUserScreenAndGetOption() throws IOException {
        System.out.println("변경할 정보를 선택해주세요.");
        System.out.println("[1] 비밀번호");
        System.out.println("[2] 이름");
        System.out.println("[3] 나이");
        System.out.println("[4] 종료");
        System.out.print("입력 : ");

        return Integer.parseInt(keyInput.readLine());
    }

    public void changeUserPW(UserDTO userInfo) throws IOException {
        System.out.println("새로운 비밀번호를 입력하세요.");
        System.out.println("입력 : ");
        userInfo.setPw(keyInput.readLine());
        System.out.println();
    }

    public void changeUserName(UserDTO userInfo) throws IOException {
        System.out.println("새로운 이름을 입력하세요.");
        System.out.println("입력 : ");
        userInfo.setName(keyInput.readLine());
        System.out.println();
    }

    public void changeUserAge(UserDTO userInfo) throws IOException {
        System.out.println("새로운 나이를 입력하세요.");
        System.out.println("입력 : ");
        userInfo.setAge(Integer.parseInt(keyInput.readLine()));
        System.out.println();
    }

    public void logout() {
        System.out.println("로그아웃합니다.\n");
    }

    public StoreDTO selectStore(ArrayList<StoreDTO> storeDTOs) throws IOException {
        viewDTOs(storeDTOs);
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

        return storeInfo;
    }

    public ClassificationDTO selectClassification(ArrayList<ClassificationDTO> classificationDTOs) throws IOException {
        viewDTOs(classificationDTOs);
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

        return selectedClass;
    }

    public ArrayList<Integer> selectOption(ArrayList<DetailsDTO> optionDTOs) throws IOException {
        ArrayList<Integer> selectedOption = new ArrayList<>();

        viewDTOs(optionDTOs);
        System.out.println("등록할 옵션을 모두 선택하세요");
        System.out.println("범위 바깥 값을 입력하거나 모든 옵션을 선택하면 입력이 종료됩니다.");
        while(selectedOption.size() < optionDTOs.size()) {
            System.out.print("입력 : ");
            selectedOption.add(Integer.parseInt(keyInput.readLine()));
            System.out.println("등록되었습니다.");
        }

        return selectedOption;
    }

    public MenuDTO setNewMenu(ClassificationDTO selectedClass) throws IOException {
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

        return newMenu;
    }

    public <T> int getIdx(ArrayList<T> DTOs) throws IOException {
        viewDTOs(DTOs);
        System.out.print("대상을 선택하세요 : ");
        return Integer.parseInt(keyInput.readLine());
    }

    public ArrayList<Integer> getOptionIdxes(ArrayList<DetailsDTO> optionDTOs) throws IOException {
        viewDTOs(optionDTOs);
        ArrayList<Integer> optionIdxes = new ArrayList<>();

        System.out.println("옵션을 선택하세요");
        System.out.println("범위 바깥 값을 입력하거나 모든 옵션을 선택하면 입력이 종료됩니다.");
        while(optionIdxes.size() < optionDTOs.size()) {
            System.out.print("입력 : ");
            optionIdxes.add(Integer.parseInt(keyInput.readLine()));
        }

        return optionIdxes;
    }

    public Pair<String, Integer> getReviewInfo() throws IOException {
        System.out.println("리뷰 내용 입력 : ");
        String contents = keyInput.readLine();
        System.out.println("별점 입력(1 ~ 5) : ");
        int starRank = Integer.parseInt(keyInput.readLine());

        return new Pair<>(contents, starRank);
    }

    public String getDetermination() throws IOException {
        System.out.println("승인 : Y/y, 거절 : N/n");
        return keyInput.readLine();
    }

    public int[] getChangeTimeInfo() throws IOException {
        int[] changeTimeInfo = new int[4];
        StringTokenizer st;

        System.out.println("변경할 개점 시간 : ");
        st = new StringTokenizer(keyInput.readLine());
        changeTimeInfo[0] = Integer.parseInt(st.nextToken());
        changeTimeInfo[1] = Integer.parseInt(st.nextToken());

        System.out.println("변경할 폐점 시간 : ");
        st = new StringTokenizer(keyInput.readLine());
        changeTimeInfo[2] = Integer.parseInt(st.nextToken());
        changeTimeInfo[3] = Integer.parseInt(st.nextToken());

        return changeTimeInfo;
    }

    public String[] getStoreInfo() throws IOException {
        String[] result = new String[4];

        System.out.println("[가게 등록]");
        System.out.println("상호명 : ");
        result[0] = keyInput.readLine();
        System.out.println("간단한 가게 소개 : ");
        result[1] = keyInput.readLine();
        System.out.println("주소 : ");
        result[2] = keyInput.readLine();
        System.out.println("가게 전화번호 : ");
        result[3] = keyInput.readLine();

        return result;
    }

    public String getClassificationName(ArrayList<ClassificationDTO> DTOs) throws IOException {
        viewDTOs(DTOs);
        System.out.println("카테고리명 입력 : ");
        return keyInput.readLine();
    }

    public String getStoreName(ArrayList<StoreDTO> DTOs) throws IOException {
        viewDTOs(DTOs);
        System.out.println("가게명 입력 : ");
        return keyInput.readLine();
    }

    public void showOrderCompleteMessage() {
        System.out.println("주문이 정상적으로 등록되었습니다.");
        System.out.println();
    }

    public void showReviewCompleteMessage() {
        System.out.println("리뷰가 등록되었습니다.");
        System.out.println();
    }

    public void showAcceptMessage() {
        System.out.println("승인되었습니다.\n");
    }

    public void showRefusalMessage() {
        System.out.println("거절되었습니다.\n");
    }

    public void showSaveMessage() {
        System.out.println("변경사항이 저장되었습니다.");
    }

    public <T> void viewDTOs(ArrayList<T> DTOs) throws IOException {
        for(int i = 0; i < DTOs.size(); i++) {
            System.out.println("[" + i + "] " + DTOs.get(i).toString());
        }
        System.out.println();
    }
}
