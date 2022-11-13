import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        /*
        StoreDAO storeDAO = StoreDAO.getStoreDAO();
        DetailsDAO detailsDAO = DetailsDAO.getDetailsDAO();
        MenuDAO menuDAO = MenuDAO.getMenuDAO();
        TotalOrdersDAO totalOrdersDAO = TotalOrdersDAO.getTotalOrdersDAO();
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        ReviewDAO reviewDAO = ReviewDAO.getReviewDAO();
        userDAO.insertUser(Authority.ADMIN, "nm090909", "0014", "백대환", 23);
        storeDAO.insertStore("맘터", "밥줘", "123-4567-8901", "오름 27", null, null, 1l);
        detailsDAO.insertDetails("곱빼기", 1000, 1l);
        menuDAO.insertMenu("tlqkf", "noname", 1, 999, 1l, null);
        totalOrdersDAO.insertTotalOrders(LocalDateTime.now(), "오름 27", 1000, 1l);
        ordersDAO.insertOrders(LocalDateTime.now(), "살려줘(+999)", 999, "치킨무는 필요없어요",1l, 1l, 1l);
        ordersDAO.updateStatus(OrdersStatus.CANCEL, 1l);
        List<OrdersDTO> a = ordersDAO.selectAllWithStore_id(1l);
        reviewDAO.insertReview("맛없어요", LocalDateTime.now(), 5, 1l, 1l);
        List<ReviewDTO> b = reviewDAO.selectAllWithUser_pk(1l);
        storeRegistDAO.updateStatus(1l, RegistStatus.ACCEPT);
        List<StoreRegistDTO> d = storeRegistDAO.selectAllWithStatus(RegistStatus.ACCEPT);
        List<TotalOrdersDTO> e = TotalOrdersDAO.getTotalOrdersDAO().selectAllWithUser_pk(1l);
        */
        storeRegistAndView();
        //백대환 바보
    }

    public static void storeRegistAndView() {
        UserDAO userDAO = UserDAO.getUserDAO();
        StoreRegistDAO storeRegistDAO = StoreRegistDAO.getStoreRegistDAO();

        UserDTO userDTO = null;

        //userDAO.insertUser(Authority.OWNER, "honsot", "honsotKIT123", "김선명", 40); // phone : 010-1234-5678
        //userDTO = userDAO.selectOneWithId("honsot");
        //storeRegistDAO.insertRegistration("한솥도시락 금오공대점", "맛과 정성을 담았습니다", "054-472-0615","경북 구미시 대학로 39", userDTO.getPk());

        var list = storeRegistDAO.selectAll();
        list.stream().forEach( p -> {
            var user = userDAO.selectOneWithPk(p.getUser_pk());
            System.out.println(p.toString() + ", " + user.toString());
        } );

        storeRegistDAO.updateStatus(1L, RegistStatus.ACCEPT); // 관리자가 가게 등록 승인/거절 하는 함수

        list = storeRegistDAO.selectAll();
        list.stream().forEach( p -> {
            var user = userDAO.selectOneWithPk(p.getUser_pk());
            System.out.println(p.toString() + ", " + user.toString());
        } );
    }

    public static void insertOptionAll() {

    }

    public static void viewAllMenu(Long store_id) {

    }

    public static void changeMenuNameAndPrice(Long menu_id, String name, Integer price) {

    }

    public static void createOrders() {
        // TotalOrders, 재고 소진시 반려 기능
    }

    public static void viewOrders() {

    }

    public static void acceptOrders() {

    }

    public static void cancelOrders() {

    }

    public static void deliveryFinish(Long order_id) {

    }

    public static void writeReview() {

    }

    public static void viewReview() {
        // 페이징 처리
    }
}