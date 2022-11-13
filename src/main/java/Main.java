import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = UserDAO.getUserDAO();
        StoreDAO storeDAO = StoreDAO.getStoreDAO();
        DetailsDAO detailsDAO = DetailsDAO.getDetailsDAO();
        MenuDAO menuDAO = MenuDAO.getMenuDAO();
        TotalOrdersDAO totalOrdersDAO = TotalOrdersDAO.getTotalOrdersDAO();
        OrdersDAO ordersDAO = OrdersDAO.getOrdersDAO();
        ReviewDAO reviewDAO = ReviewDAO.getReviewDAO();
        StoreRegistDAO storeRegistDAO = StoreRegistDAO.getStoreRegistDAO();
/*
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
    }

    public static void storeRegistAndView() {

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