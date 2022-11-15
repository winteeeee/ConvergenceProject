package controller;

import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.UserDTO;
import service.AdminService;
import service.OwnerService;
import service.StoreService;
import service.UserService;

public class UserController {
    StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    StoreRegistDAO storeRegistDAO = new StoreRegistDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());


    UserService userService = new UserService(userDAO, ordersDAO, menuDAO, reviewDAO);
    OwnerService ownerService = new OwnerService(userDAO, menuDAO, storeRegistDAO, ordersDAO);
    AdminService adminService = new AdminService(storeRegistDAO, storeDAO, userDAO);
    StoreService storeService = new StoreService(storeDAO, classificationDAO, menuDAO, detailsDAO);


    public boolean Login(UserDTO dto) {
        return userService.getUserWithId(dto.getId()).getPw().equals(dto.getPw());
    }

    public boolean insert(UserDTO dto) {
        return 0 != userService.insertUser(dto.getId(), dto.getPw(), dto.getName(), dto.getPhone(), dto.getAge());
    }

    public boolean update(UserDTO dto) {
        return 0 != userService.update(dto.getId(), dto.getPw(), dto.getName(), dto.getPhone(), dto.getAge());
    }
}
