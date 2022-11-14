package service;

import persistence.dao.UserDAO;
import persistence.enums.Authority;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Long insertUser(String id, String pw, String name, String phone, Integer age) {
        return userDAO.insertUser(Authority.USER, id, pw, name, phone, age);
    }
}
