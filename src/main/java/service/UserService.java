package service;

import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserService {
    private OrdersDAO ordersDAO;
    private StoreDAO storeDAO;
    private UserDAO userDAO;
    private MenuDAO menuDAO;
    private ReviewDAO reviewDAO;
    private TotalOrdersDAO totalOrdersDAO;
    private ClassificationDAO classificationDAO;
    private DetailsDAO detailsDAO;

    public UserService(OrdersDAO ordersDAO, StoreDAO storeDAO, UserDAO userDAO, MenuDAO menuDAO, ReviewDAO reviewDAO, TotalOrdersDAO totalOrdersDAO, ClassificationDAO classificationDAO, DetailsDAO detailsDAO) {
        this.ordersDAO = ordersDAO;
        this.storeDAO = storeDAO;
        this.userDAO = userDAO;
        this.menuDAO = menuDAO;
        this.reviewDAO = reviewDAO;
        this.totalOrdersDAO = totalOrdersDAO;
        this.classificationDAO = classificationDAO;
        this.detailsDAO = detailsDAO;
    }

    public Long insertUser(UserDTO user) {
        return userDAO.insertUser(user);
    }

    public UserDTO getUserWithId(String id) {
        return userDAO.selectOneWithId(id);
    }

    public UserDTO update(UserDTO user) {
        userDAO.update(user);
        return userDAO.selectOneWithPk(user.getPk());
    }

    public List<StoreDTO> getAllStore() {
        return storeDAO.selectAllWithStatus(RegistStatus.ACCEPT);
    }

    public List<ReviewDTO> getStoreReview(Long store_id, Integer page) {
        return reviewDAO.selectAllWithStoreId(store_id, page);
    }

    public synchronized int order(TotalOrdersDTO totalOrders, List<OrdersDTO> ordersList) {
        Integer sumPrice = 0;
        for (OrdersDTO orders : ordersList) {
            sumPrice += orders.getPrice();
        }
        totalOrders.setPrice(sumPrice);

        Long total_order_id = totalOrdersDAO.insertTotalOrders(totalOrders);
        boolean flag = true;

        for (OrdersDTO orders : ordersList) {
            orders.setTotal_orders_id(total_order_id);
            ordersDAO.insertOrders(orders);
        }

        HashMap<Long, Integer> map = new HashMap<>();
        for (OrdersDTO orders : ordersList) {
            if (map.containsKey(orders.getMenu_id())) {
                map.put(orders.getMenu_id(), map.get(orders.getMenu_id()) + 1);
            }
            else {
                map.put(orders.getMenu_id(), 1);
            }
        }

        for (OrdersDTO orders : ordersList) {
            if (menuDAO.selectOneWithId(orders.getMenu_id()).getStock() < map.get(orders.getMenu_id())) {
                flag = false;
                break;
            }
        }


        if (flag) {
            for (OrdersDTO orders : ordersList) {
                orders.setTotal_orders_id(total_order_id);
                ordersDAO.insertOrders(orders);
                menuDAO.updateForInsert(orders.getMenu_id());
            }
            return 1;
        }
        else {
            totalOrdersDAO.updateStatus(total_order_id, OrdersStatus.CANCEL);
            return 0;
        }
    }

    public int cancelOrder(Long total_order_id) {
        TotalOrdersDTO totalOrders = totalOrdersDAO.selectOneWithId(total_order_id);

        if(totalOrders.getStatusEnum() == OrdersStatus.HOLD) {
            totalOrdersDAO.updateStatus(total_order_id, OrdersStatus.CANCEL);
            return 1;
        }
        else {
            return 0; // 배달중 이거나 이미 취소된 경우
        }
    }

    public List<TotalOrdersDTO> getOrders(Long user_pk) {
        return totalOrdersDAO.selectAllWithUserPk(user_pk);
    }

    public int writeReview(ReviewDTO review) {
        TotalOrdersDTO totalOrders = totalOrdersDAO.selectOneWithId(review.getTotal_orders_id());

        if(totalOrders.getStatusEnum() == OrdersStatus.COMPLETE && (1 <= review.getStar_rating() && review.getStar_rating() <= 5)) {
            reviewDAO.insertReview(review);
            reviewDAO.updateForInsert(review.getStar_rating(), review.getTotal_orders_id());
            return 1;
        }
        else {
            return 0;
        }
    }


    public List<StoreDTO> getStoreWithUser_pk(Long user_pk) {
        return storeDAO.selectAllWithUserPk(user_pk);
    }

    public List<ClassificationDTO> getMenuGroups(Long store_id) {
        return classificationDAO.selectAllWithStore_id(store_id);
    }

    public List<MenuDTO> getMenusWithGroup_id(Long classification_id) {
        return menuDAO.selectAllWithClassification_id(classification_id, RegistStatus.ACCEPT);
    }



    public MenuDTO getMenu(Long menu_id) {
        return menuDAO.selectOneWithId(menu_id);
    }

    public List<DetailsDTO> getDetailsWithMenuId(Long menu_id) {
        return detailsDAO.selectAllWithMenu_id(menu_id);
    }

    public ClassificationDTO getGroupWithId(Long store_id) {
        return classificationDAO.selectOneWithId(store_id);
    }
}
