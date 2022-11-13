import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public class Main {
    static StoreDAO storeDAO = StoreDAO.getStoreDAO();
    static DetailsDAO detailsDAO = DetailsDAO.getDetailsDAO();
    static MenuDAO menuDAO = MenuDAO.getMenuDAO();
    static TotalOrdersDAO totalOrdersDAO = TotalOrdersDAO.getTotalOrdersDAO();
    static OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
    static ReviewDAO reviewDAO = ReviewDAO.getReviewDAO();
    static UserDAO userDAO = UserDAO.getUserDAO();
    static StoreRegistDAO storeRegistDAO = StoreRegistDAO.getStoreRegistDAO();
    static ClassificationDAO classificationDAO = ClassificationDAO.getClassificationDAO();

    public static void main(String[] args) {
        test2_2();
    }

    public static void test1() {
        /* 사용자 추가 및 가게 등록 요청*/
        userDAO.insertUser(Authority.OWNER, "honsot", "honsotKIT123", "김선명", "010-1234-5678", 40);
        UserDTO userDTO = userDAO.selectOneWithId("honsot");
        storeRegistDAO.insertRegistration("한솥도시락 금오공대점", "맛과 정성을 담았습니다", "054-472-0615","경북 구미시 대학로 39", userDTO.getPk());

        /* 관리자 승인 */
        var list = storeRegistDAO.selectAll();
        StoreRegistDTO storeRegistDTO = list.get(0);
        storeRegistDAO.updateStatus(storeRegistDTO.getId(), RegistStatus.ACCEPT);

        /* 가게 추가 */
        storeDAO.insertStore(
                storeRegistDTO.getName(),
                storeRegistDTO.getComment(),
                storeRegistDTO.getPhone(),
                storeRegistDTO.getAddress(),
                null,
                null,
                storeRegistDTO.getUser_pk()
        );

        /* 모든 가게 조회 */
        var storeDTOList = storeDAO.selectAll();
        storeDTOList.stream().forEach( p -> {
            var user = userDAO.selectOneWithPk(p.getUser_pk());
            System.out.println(p.toString() + ", " + user.toString());
        } );
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

        /* 그룹 들고 오기 */
        List<ClassificationDTO> groups = classificationDAO.selectAllWithStore_id(honsot.getId());
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









    public static void viewAllMenu(Long store_id) {
        MenuDAO menuDAO = MenuDAO.getMenuDAO();
        List<MenuDTO> list = menuDAO.selectAllWithClassification_id(store_id);
        // 조건 추가 필요 -> 그룹별로 나눠서 출력
        list.stream().forEach( p -> {
            System.out.println(p.toString()); // 옵션 추가 필요
        } );
    }

    public static void changeMenuNameAndPrice(Long menu_id, String name, Integer price) {
        MenuDAO menuDAO = MenuDAO.getMenuDAO();
        MenuDTO menuDTO = menuDAO.selectOneWithId(menu_id);

        System.out.println(menuDTO.getName() + "의 이름과 가격을 각각 " + name + "와" + " " + price + "원으로 수정");
        menuDAO.updateNameAndPrice(menu_id, name, price);

        System.out.println("(수정 후 조회)");

        menuDTO = menuDAO.selectOneWithId(menu_id);
        System.out.println(menuDTO.toString());
    }

    public static void test3_1() {
        MenuDAO menuDAO = MenuDAO.getMenuDAO();
        Long user_pk = 1l;
        Long store_id = 2l;

        List<MenuDTO> menuList = menuDAO.selectAllWithClassification_id(0l); //TODO

        createOrders(user_pk, store_id, menuList.get(0), 0, 2);
        createOrders(user_pk, store_id, menuList.get(2), 0);
        createOrders(user_pk, store_id, menuList.get(2), 0);
        createOrders(user_pk, store_id, menuList.get(3), 3);
    }

    public static void createOrders(Long user_pk, Long store_id, MenuDTO menu, Integer... optionArr) {
        DetailsDAO detailsDAO = DetailsDAO.getDetailsDAO();
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();


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
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        List<OrdersDTO> list = ordersDAO.selectAllWithStore_id(store_id);

        for (OrdersDTO o : list) {
            System.out.println(o.toString());
        }
    }

    public static void acceptOrders(Long order_id) {
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        ordersDAO.updateStatus(OrdersStatus.IN_DELIVERY, order_id);
    }

    public static void cancelOrders(Long order_id) {
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        ordersDAO.updateStatus(OrdersStatus.CANCEL, order_id);
    }

    public static void deliveryFinish(Long order_id) {
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        ordersDAO.updateStatus(OrdersStatus.COMPLETE, order_id);
    }

    public static void writeReview() {

    }

    public static void viewReview() {
        // 페이징 처리
    }
}