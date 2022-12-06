import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;

import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main { // temp
    static StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static TotalOrdersDAO totalOrdersDAO = new TotalOrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static StatisticsDAO statisticsDAO = new StatisticsDAO(MyBatisConnectionFactory.getSqlSessionFactory());

    static UserService userService = new UserService(ordersDAO, storeDAO, userDAO, menuDAO, reviewDAO, totalOrdersDAO, classificationDAO, detailsDAO);
    static OwnerService ownerService = new OwnerService(userDAO, storeDAO, menuDAO, totalOrdersDAO, ordersDAO, reviewDAO, classificationDAO, statisticsDAO, detailsDAO);
    static AdminService adminService = new AdminService(storeDAO, userDAO, menuDAO, totalOrdersDAO, statisticsDAO);

    static UserDTO user;
    public static void main(String[] args) {
        userDAO.insertAdmin(UserDTO.builder()
                .id("admin")
                .pw("admin")
                .age(23)
                .name("ADMIN")
                .phone("010-4265-6669")
                .build()
        );
    }
}