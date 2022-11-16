import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;

import service.AdminService;
import service.OwnerService;
import service.StoreService;
import service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Main {
    static StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static StoreRegistDAO storeRegistDAO = new StoreRegistDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());


    static UserService userService = new UserService(userDAO, ordersDAO, menuDAO, reviewDAO);
    static OwnerService ownerService = new OwnerService(userDAO, menuDAO, storeRegistDAO, ordersDAO);
    static AdminService adminService = new AdminService(storeRegistDAO, storeDAO, userDAO);
    static StoreService storeService = new StoreService(storeDAO, classificationDAO, menuDAO, detailsDAO);

    public static void main(String[] args) {
        System.out.println("test1");
        test1();

        System.out.println("\ntest2_1");
        test2_1();

        System.out.println("\ntest2_2");
        test2_2();

        System.out.println("\ntest2_3");
        test2_3();

        System.out.println("\ntest3_1");
        test3_1();

        System.out.println("\ntest3_2");
        test3_2();

        System.out.println("\ntest3_3");
        test3_3();

        System.out.println("\ntest3_4");
        test3_4();

        System.out.println("\ntest3_5");
        test3_5();

        System.out.println("\ntest3_6");
        test3_6();

        System.out.println("\ntest4_1");
        test4_1();

        System.out.println("\ntest4_2");
        test4_2();
    }

    public static void test1() {
        /* 사용자 추가 및 가게 등록 요청*/
        Long ownerKey = ownerService.insertOwner("honsot", "honsotKIT123", "김선명", "010-1234-5678", 40);
        ownerService.insertStoreRegist("한솥도시락 금오공대점", "맛과 정성을 담았습니다", "054-472-0615","경북 구미시 대학로 39", ownerKey);

        /* 관리자 승인 및 가게 추가 */
        var list = adminService.getHoldList();
        StoreRegistDTO storeRegist = list.get(0);
        adminService.acceptStoreRegist(storeRegist.getId());

        /* 모든 가게 조회 */
        viewStoreList();
    }

    public static void viewStoreList() {
        List<StoreDTO> storeList = adminService.getStoreList();
        List<UserDTO> ownerList = new ArrayList<>();

        for (StoreDTO store : storeList) {
            ownerList.add(userDAO.selectOneWithPk(store.getUser_pk()));
        }

        storeViewForAdmin(storeList, ownerList);
    }

    public static void storeViewForAdmin(List<StoreDTO> storeList, List<UserDTO> ownerList) {
        for (int idx = 0; idx < storeList.size(); idx++) {
            StoreDTO store = storeList.get(idx);
            UserDTO owner = ownerList.get(idx);
            System.out.println( (idx + 1) + ". " +
                    store.getName() + ", " +
                    store.getComment() + ", " +
                    store.getAddress() + ", " +
                    store.getPhone() + ", " +
                    owner.getName() + ", " +
                    owner.getPhone() + ", " +
                    owner.getId() + ", " +
                    owner.getPw()
            );
        }
    }








    public static void test2_1() {
        /* 가게 들고 오기 */
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        /* 옵션 등록 */
        insertOptionAll(honsot.getId());
        List<DetailsDTO> details = detailsDAO.selectAllWithStore_id(honsot.getId());

        /* 메뉴 등록 */
        classificationDAO.insertClassification("고기고기시리즈", honsot.getId());
        ClassificationDTO group1 = classificationDAO.selectAllWithStore_id(honsot.getId()).get(0);
        insertMenu(group1.getId(), "돈까스도련님고기고기", 6000, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
        insertMenu(group1.getId(), "탕수육도련님고기고기", 5800, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
        insertMenu(group1.getId(), "새치 고기고기", 6700, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
        insertMenu(group1.getId(), "돈치 고기고기", 5800, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());

        classificationDAO.insertClassification("정식시리즈", honsot.getId());
        ClassificationDTO group2 = classificationDAO.selectAllWithStore_id(honsot.getId()).get(1);
        insertMenu(group2.getId(), "제육 김치찌개 정식", 8200, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
        insertMenu(group2.getId(), "제육 김치 부대찌개 정식", 8500, 10, details.get(0).getId(), details.get(1).getId());
        insertMenu(group2.getId(), "돈치스팸 김치 부대찌개 정식", 8500, 1, details.get(0).getId(), details.get(1).getId());
    }

    public static void insertOptionAll(Long store_id) {
        detailsDAO.insertDetails("한솥밥 곱빼기", 400, store_id);
        detailsDAO.insertDetails("현미밥 교체", 1000, store_id);
        detailsDAO.insertDetails("계란후라이", 1000, store_id);
        detailsDAO.insertDetails("청양고추", 300, store_id);
    }

    public static void insertMenu(Long group_id, String name, Integer price, Integer stock, Long... options) {
        ownerService.insertMenu(name, price, stock, group_id, Arrays.asList(options));
    }









    public static void test2_2() {
        /* 가게 들고 오기 */
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        /* 출력 */
        viewAllMenu(honsot.getId());
    }

    public static void viewAllMenu(Long store_id) {
        /* 그룹 들고 오기 */
        List<ClassificationDTO> groups = storeService.getMenuGroups(store_id);
        List<MenuDTO> menus;
        List<DetailsDTO> detailsList;

        /* 출력 */
        int menuIdx = 1;
        for (ClassificationDTO group : groups) {
            menus = storeService.getMenusWithGroup_id(group.getId());
            System.out.println("[" + group.getName() + "]");
            for (MenuDTO menu : menus) {
                detailsList = storeService.getDetailsWithMenuId(menu.getId());

                System.out.print(menuIdx + ". " + menu.getName() + ", " + menu.getPrice() + "원");
                for (DetailsDTO details : detailsList) {
                    System.out.print(", " + details.getName());
                }
                System.out.println();
                menuIdx++;
            }
        }
    }






    public static void test2_3() {
        /* 가게 들고 오기*/
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        /* 매뉴 그룹 들고 오기 */
        ClassificationDTO classification = storeService.getMenuGroups(honsot.getId()).get(0);

        /* 수정할 메뉴 들고 오기 */
        MenuDTO menu = storeService.getMenusWithGroup_id(classification.getId()).get(0);

        /* 메뉴 수정 */
        changeMenuNameAndPrice(menu.getId(), "돈까스고기고기", 6500);
    }

    public static void changeMenuNameAndPrice(Long menu_id, String name, Integer price) {
        MenuDTO menu = storeService.getMenu(menu_id);

        ownerService.updateMenu(menu_id, name, price);

        System.out.println(menu.getName() + "의 이름과 가격을 각각 " + name + "와" + " " + price + "원으로 수정");
        System.out.println("(수정 후 조회)");

        ClassificationDTO classification = storeService.getGroupWithId(menu.getClassification_id());
        menu = storeService.getMenu(menu_id);
        System.out.println("[" + classification.getName() + "]");
        List<DetailsDTO> detailsList = storeService.getDetailsWithMenuId(menu.getId());

        System.out.print(menu.getName() + " | " + menu.getPrice());
        for (DetailsDTO details : detailsList) {
            System.out.print(", " + details.getName());
        }
        System.out.println();
    }





    public static void test3_1() {
        Long user2pk = userService.insertUser("hello", "world", "테크모", "010-4265-6669", 23);

        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        List<MenuDTO> menuList = storeService.getAllMenus(honsot.getId());

        createOrders(user.getPk(), honsot.getId(), menuList.get(0), 0, 2);
        createOrders(user.getPk(), honsot.getId(), menuList.get(2), 0);
        createOrders(user.getPk(), honsot.getId(), menuList.get(2), 0);
        createOrders(user2pk, honsot.getId(), menuList.get(3), 3);

    }

    public static void createOrders(Long user_pk, Long store_id, MenuDTO menu, Integer... optionArr) {
        List<DetailsDTO> options = storeService.getDetailsWithMenuId(menu.getId());

        String details = "";
        Integer price = menu.getPrice();

        if (0 < optionArr.length) {
            details += options.get(0).getName();
        }
        for (int idx = 1; idx < optionArr.length; idx++) {
            details += ", " + options.get(idx).getName();
            price += options.get(idx).getPrice();
        }

        userService.order(details, price, "무 많이요", menu.getId(), user_pk, store_id);
    }







    public static void test3_2() {
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        viewOrders(ownerService.getOrdersWithStore_id(honsot.getId()));
    }

    public static void viewOrders(List<OrdersDTO> list) {

        System.out.println("[주문내역]");
        String menu_name = null;

        for (OrdersDTO order : list) {
            menu_name = storeService.getMenu(order.getMenu_id()).getName();
            System.out.println("회원" + order.getUser_pk() + ", " + menu_name + ", " + order.getDetails() + ", " + order.getPrice() + ", " + order.getStatusEnum().getTitle());
        }
    }




    public static void test3_3() {
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        List<OrdersDTO> list = ownerService.getOrdersWithStore_id(honsot.getId());

        ownerService.acceptOrders(list.get(0).getId());
        ownerService.acceptOrders(list.get(1).getId());
        ownerService.acceptOrders(list.get(2).getId());
    }




    public static void test3_4() {
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        List<OrdersDTO> list = ownerService.getOrdersWithStore_id(honsot.getId());

        userService.cancelOrder(list.get(0).getId());
        userService.cancelOrder(list.get(3).getId());

        viewOrders(ownerService.getOrdersWithStore_id(honsot.getId()));
    }




    public static void test3_5() {
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        List<MenuDTO> menuList = storeService.getAllMenus(honsot.getId());

        createOrders(user.getPk(), honsot.getId(), menuList.get(6));
        createOrders(user.getPk(), honsot.getId(), menuList.get(6));

        viewOrders(ownerService.getOrdersWithStore_id(honsot.getId()));
    }




    public static void test3_6() {
        UserDTO user = userService.getUserWithId("honsot");
        StoreDTO honsot = storeService.getStoreWithUser_pk(user.getPk()).get(0);

        List<OrdersDTO> list = ownerService.getOrdersWithStore_id(honsot.getId());

        ownerService.deliveryFinish(list.get(0).getId());
        ownerService.deliveryFinish(list.get(1).getId());
        ownerService.deliveryFinish(list.get(2).getId());

        viewOrders(userService.getEndedOrders(user.getPk()));
    }




    public static void test4_1() {
        UserDTO user = userService.getUserWithId("honsot");
        List<OrdersDTO> list = userService.getEndedOrders(user.getPk());

        writeReview("test1", 1, user.getPk(), list.get(0).getId());
        writeReview("test2", 1, user.getPk(), list.get(1).getId());
        writeReview("test3", 1, user.getPk(), list.get(2).getId());

        /* 취소된 주문에는 리뷰 작성X 예시 */
        UserDTO user2 = userService.getUserWithId("hello");
        List<OrdersDTO> list2 = userService.getEndedOrders(user2.getPk());

        writeReview("test4 comment1", 1, user2.getPk(), list2.get(0).getId());
    }

    public static void writeReview(String contents, Integer star_rating, Long user_pk, Long orders_id) {
        userService.writeReview(contents, star_rating, user_pk, orders_id);
    }




    public static void test4_2() {
        UserDTO user = userService.getUserWithId("honsot");
        char input = '1';
        int page;

        while(input != 'Q' && input != 'q') {
            page = input - '0';
            List<ReviewDTO> list = userService.getReviewList(user.getPk(), page);
            input = viewReview(list, page, 2);
        }
    }

    public static char viewReview(List<ReviewDTO> list, int page, int maxPage) {
        Scanner sc = new Scanner(System.in);
        if(1 <= page && page <= maxPage) {
            System.out.println("현재 " + page + " page 입니다.");

            for(ReviewDTO review : list) {
                System.out.println(review.getOrders_id() + ", " + review.getContents() + ", " + review.getRegdate());
            }

            System.out.print("Page ");
            for(int i = 1; i <= maxPage; i++) {
                System.out.print(i + " ");
            }
            System.out.print('\t' + "종료: q or Q" + '\n');
            System.out.print("입력 : ");

        }
        else {
            System.out.println("없는 page 입니다. 다시 입력하세요.");
            System.out.print("입력 : ");
        }
        return sc.next().charAt(0);
    }
}