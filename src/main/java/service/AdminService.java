package service;

import persistence.dao.*;
import persistence.dto.*;
import persistence.enums.RegistStatus;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private StoreDAO storeDAO;
    private UserDAO userDAO;
    private MenuDAO menuDAO;
    private TotalOrdersDAO totalOrdersDAO;
    private StatisticsDAO statisticsDAO;

    public AdminService(StoreDAO storeDAO, UserDAO userDAO, MenuDAO menuDAO, TotalOrdersDAO totalOrdersDAO, StatisticsDAO statisticsDAO) {
        this.storeDAO = storeDAO;
        this.userDAO = userDAO;
        this.menuDAO = menuDAO;
        this.totalOrdersDAO = totalOrdersDAO;
        this.statisticsDAO = statisticsDAO;
    }

    public int acceptStore(Long id) {
        return storeDAO.updateStatus(id, RegistStatus.ACCEPT);
    }
    public int rejectStore(Long id) {
        return storeDAO.updateStatus(id, RegistStatus.REJECT);
    }

    public int acceptUser(Long pk) {
        return userDAO.updateStatus(pk, RegistStatus.ACCEPT);
    }
    public int rejectUser(Long pk) {
        return userDAO.updateStatus(pk, RegistStatus.REJECT);
    }

    public int acceptMenu(Long id) {
        return menuDAO.updateStatus(id, RegistStatus.ACCEPT);
    }
    public int rejectMenu(Long id) {
        return menuDAO.updateStatus(id, RegistStatus.REJECT);
    }

    public List<StoreDTO> getHoldStoreList() {
        return storeDAO.selectAllWithStatus(RegistStatus.HOLD);
    }

    public List<StoreDTO> getStoreList() {
        return storeDAO.selectAll();
    }

    public List<UserDTO> getHoldUserList() {
        return userDAO.selectAllWithStatus(RegistStatus.HOLD);
    }

    public List<MenuDTO> getHoldMenuList() {
        return menuDAO.selectAllWithStatus(RegistStatus.HOLD);
    }

    public List<StatisticsDTO> getStatistics() {
        List<StoreDTO> stores = storeDAO.selectAll();
        StatisticsDTO statistics;
        List<StatisticsDTO> result = new ArrayList<>();

        for (StoreDTO store : stores) {
            result.add(statisticsDAO.selectOneForAdmin(store.getId(), store.getName()));
        }

        return result;
    }
}
