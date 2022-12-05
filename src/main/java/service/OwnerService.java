package service;

import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.OrdersStatus;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OwnerService {
    private UserDAO userDAO;
    private StoreDAO storeDAO;
    private MenuDAO menuDAO;
    private TotalOrdersDAO totalOrdersDAO;
    private OrdersDAO ordersDAO;
    private ReviewDAO reviewDAO;
    private ClassificationDAO classificationDAO;
    private StatisticsDAO statisticsDAO;
    private DetailsDAO detailsDAO;

    public OwnerService(UserDAO userDAO, StoreDAO storeDAO, MenuDAO menuDAO, TotalOrdersDAO totalOrdersDAO, OrdersDAO ordersDAO, ReviewDAO reviewDAO, ClassificationDAO classificationDAO, StatisticsDAO statisticsDAO, DetailsDAO detailsDAO) {
        this.userDAO = userDAO;
        this.storeDAO = storeDAO;
        this.menuDAO = menuDAO;
        this.totalOrdersDAO = totalOrdersDAO;
        this.ordersDAO = ordersDAO;
        this.reviewDAO = reviewDAO;
        this.classificationDAO = classificationDAO;
        this.statisticsDAO = statisticsDAO;
        this.detailsDAO = detailsDAO;
    }

    public Long insertOwner(UserDTO user) {
        return userDAO.insertOwner(user);
    }

    public int insertStore(StoreDTO store) {
        return storeDAO.insertStore(store);
    }

    public List<StoreDTO> getStoreWithUser_pk(Long user_pk) {
        return storeDAO.selectAllWithUserPk(user_pk);
    }


    public void insertMenu(MenuDTO menu, List<Long> detailsList) {
        Long menu_id = menuDAO.insertMenu(menu);
        for (Long details_id : detailsList) {
            menuDAO.insertMenuDetails(menu_id, details_id);
        }
    }

    public int updateTime(Long store_id, LocalDateTime open_time, LocalDateTime close_time) {
        return storeDAO.updateTime(store_id, open_time, close_time);
    }

    public int updateMenu(Long menu_id, String name, Integer price) {
        return menuDAO.updateNameAndPrice(menu_id, name, price);
    }

    public List<TotalOrdersDTO> getTotalOrders(Long store_id) {
        return totalOrdersDAO.selectAllWithStoreId(store_id);
    }
    
    public List<OrdersDTO> getOrders(Long total_order_id) {
        return ordersDAO.selectAllWithTotal_orders_id(total_order_id);
    }

    public int acceptOrders(Long id) {
        if (totalOrdersDAO.selectOneWithId(id).getStatusEnum() != OrdersStatus.CANCEL) {
            return totalOrdersDAO.updateStatus(id, OrdersStatus.IN_DELIVERY);
        }
        else {
            return 0;
        }
    }

    public int cancelOrders(Long id) {
        return totalOrdersDAO.updateStatus(id, OrdersStatus.CANCEL);
    }

    public int deliveryFinish(Long id) {
        return totalOrdersDAO.updateStatus(id, OrdersStatus.COMPLETE);
    }

    public List<ReviewDTO> getReviewList(Long store_id, Integer page) {
        return reviewDAO.selectAllWithStoreId(store_id, page);
    }

    public int updateOwnerComment(Long review_id, String comment) {
        return reviewDAO.updateOwnerComment(review_id, comment);
    }

    public List<StatisticsDTO> getStatistics(Long store_id) {
        List<ClassificationDTO> groups = classificationDAO.selectAllWithStore_id(store_id);
        List<MenuDTO> menus;
        List<StatisticsDTO> result = new ArrayList<>();

        for (ClassificationDTO group : groups) {
            menus = menuDAO.selectAllWithClassification_id(group.getId(), RegistStatus.ACCEPT);

            for(MenuDTO dto : menus) {
                result.add(statisticsDAO.selectOneForOwner(dto.getId(), dto.getName()));
            }
        }

        return result;
    }

    public void insertDetails(DetailsDTO details) {
        detailsDAO.insertDetails(details);
    }

    public void insertClassification(ClassificationDTO classification) {
        classificationDAO.insertClassification(classification);
    }
}
