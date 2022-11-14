import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.OrdersStatus;
import service.AdminService;
import service.OwnerService;
import service.UserService;
import view.StoreView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public class Main {
    static StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static TotalOrdersDAO totalOrdersDAO = new TotalOrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static StoreRegistDAO storeRegistDAO = new StoreRegistDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());

    static StoreView storeView = new StoreView();

    static UserService userService = new UserService(userDAO);
    static OwnerService ownerService = new OwnerService(userDAO, storeRegistDAO);
    static AdminService adminService = new AdminService(storeRegistDAO, storeDAO, userDAO, storeView);

    public static void main(String[] args) {
        test1();
        // 복구용
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
        adminService.viewStoreList();
    }



    public static void test2_1() {
        /* 가게 들고 오기 */
        UserDTO user = userDAO.selectOneWithId("honsot");
        StoreDTO honsot = storeDAO.selectAllWithUser_pk(user.getPk()).get(0);

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
        menuDAO.insertMenu(name, price, stock, group_id, Arrays.asList(options));
    }



    public static void test2_2() {
        /* 가게 들고 오기 */
        UserDTO user = userDAO.selectOneWithId("honsot");
        StoreDTO honsot = storeDAO.selectAllWithUser_pk(user.getPk()).get(0);

        /* 출력 */
        viewAllMenu(honsot.getId());
    }

    public static void viewAllMenu(Long store_id) {
        /* 그룹 들고 오기 */
        List<ClassificationDTO> groups = classificationDAO.selectAllWithStore_id(store_id);
        List<MenuDTO> menus;
        List<DetailsDTO> detailsList;

        /* 출력 */
        int menuIdx = 1;
        for (ClassificationDTO group : groups) {
            menus = menuDAO.selectAllWithClassification_id(group.getId());
            System.out.println("[" + group.getName() + "]");
            for (MenuDTO menu : menus) {
                detailsList = detailsDAO.selectAllWithMenu_id(menu.getId());

                System.out.print(menuIdx + ". " + menu.getName() + ", " + menu.getPrice() + "원");
                for (DetailsDTO details : detailsList) {
                    System.out.print(", " + details.getName());
                }
                System.out.println();
                menuIdx++;
            }
        }
    }







    public static void changeMenuNameAndPrice(Long menu_id, String name, Integer price) {
        MenuDTO menuDTO = menuDAO.selectOneWithId(menu_id);

        System.out.println(menuDTO.getName() + "의 이름과 가격을 각각 " + name + "와" + " " + price + "원으로 수정");
        menuDAO.updateNameAndPrice(menu_id, name, price);

        System.out.println("(수정 후 조회)");

        menuDTO = menuDAO.selectOneWithId(menu_id);
        System.out.println(menuDTO.toString());
    }

    public static void test3_1() {
        Long user_pk = 1l;
        Long store_id = 2l;

        List<MenuDTO> menuList = menuDAO.selectAllWithClassification_id(0l); //TODO

        createOrders(user_pk, store_id, menuList.get(0), 0, 2);
        createOrders(user_pk, store_id, menuList.get(2), 0);
        createOrders(user_pk, store_id, menuList.get(2), 0);
        createOrders(user_pk, store_id, menuList.get(3), 3);
    }

    public static void createOrders(Long user_pk, Long store_id, MenuDTO menu, Integer... optionArr) {
        List<DetailsDTO> options = detailsDAO.selectAllWithMenu_id(menu.getId());

        String details = "";
        Integer price = menu.getPrice();
        for (int idx : optionArr) {
            details += ", " + options.get(idx).getName() + "(+" + options.get(idx).getPrice() + ")";
            price += options.get(idx).getPrice();
        }

        ordersDAO.insertOrders(LocalDateTime.now(), details, price, "무 많이요", menu.getId(), user_pk, store_id);
    }

    public static void viewOrders(Long store_id) {
        List<OrdersDTO> list = ordersDAO.selectAllWithStore_id(store_id);

        for (OrdersDTO o : list) {
            System.out.println(o.toString());
        }
    }

    public static void acceptOrders(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.IN_DELIVERY, order_id);
    }

    public static void cancelOrders(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.CANCEL, order_id);
    }

    public static void deliveryFinish(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.COMPLETE, order_id);
    }

    public static void writeReview() {

    }

    public static void viewReview() {
        // 페이징 처리
    }
}