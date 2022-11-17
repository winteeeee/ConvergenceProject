package service;

import persistence.dao.MenuDAO;
import persistence.dao.OrdersDAO;
import persistence.dao.StoreRegistDAO;
import persistence.dao.UserDAO;
import persistence.dto.OrdersDTO;
import persistence.enums.Authority;
import persistence.enums.OrdersStatus;

import java.util.List;

public class OwnerService {
    private UserDAO userDAO;
    private MenuDAO menuDAO;
    private StoreRegistDAO storeRegistDAO;
    private OrdersDAO ordersDAO;

    public OwnerService(UserDAO userDAO, MenuDAO menuDAO, StoreRegistDAO storeRegistDAO, OrdersDAO ordersDAO) {
        this.userDAO = userDAO;
        this.menuDAO = menuDAO;
        this.storeRegistDAO = storeRegistDAO;
        this.ordersDAO = ordersDAO;
    }

    public Long insertOwner(String id, String pw, String name, String phone, Integer age) {
        return userDAO.insertUser(Authority.OWNER, id, pw, name, phone, age);
    }

    public int insertStoreRegist(String name, String comment, String phone, String address, Long user_pk) {
        return  storeRegistDAO.insertRegistration(name, comment, phone, address, user_pk);
    }

    public void insertMenu(String name, Integer price, Integer stock, Long classification_id, List<Long> detailsList) {
        Long menu_id = menuDAO.insertMenu(name, price, stock, classification_id);
        for (Long details_id : detailsList) {
            menuDAO.insertMenuDetails(menu_id, details_id);
        }
    }

    public void updateMenu(Long menu_id, String name, Integer price) {
        menuDAO.updateNameAndPrice(menu_id, name, price);
    }

    public List<OrdersDTO> getOrdersWithStore_id(Long store_id) {
        return ordersDAO.selectAllWithStore_id(store_id);
    }

    public void acceptOrders(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.IN_DELIVERY, order_id);
    }

    public void cancelOrders(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.CANCEL, order_id);
    }

    public void deliveryFinish(Long order_id) {
        ordersDAO.updateStatus(OrdersStatus.COMPLETE, order_id);
    }
}
