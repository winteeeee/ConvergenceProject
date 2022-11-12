import persistence.dao.*;
import persistence.dto.OrdersDTO;
import persistence.dto.ReviewDTO;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;

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
/*
        userDAO.insertUser(Authority.ADMIN, "nm090909", "0014", "백대환", 23);
        storeDAO.insertStore("맘터", "밥줘", "123-4567-8901", "오름 27", 9999, 5, LocalDateTime.now(), LocalDateTime.now(), 1l);
        detailsDAO.insertDetails("곱빼기", 1000, 1l);
        menuDAO.insertMenu("tlqkf", "noname", 1, 999, 1l, null);
        totalOrdersDAO.insertTotalOrders(LocalDateTime.now(), "오름 27", 1000, 1l);
        ordersDAO.insertOrders(LocalDateTime.now(), "살려줘(+999)", 999, "치킨무는 필요없어요",1l, 1l, 1l);
        ordersDAO.updateStatus(OrdersStatus.CANCEL, 1l);
        List<OrdersDTO> a = ordersDAO.selectAllWithStore_id(1l);
        reviewDAO.insertReview("맛없어요", LocalDateTime.now(), 5, 1l, 1l);
        List<ReviewDTO> b = reviewDAO.selectAllWithUser_pk(1l);
*/
    }
}