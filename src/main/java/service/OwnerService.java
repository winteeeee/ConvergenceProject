package service;

import persistence.dao.StoreRegistDAO;
import persistence.dao.UserDAO;
import persistence.enums.Authority;

public class OwnerService {
    private UserDAO userDAO;
    private StoreRegistDAO storeRegistDAO;

    public OwnerService(UserDAO userDAO, StoreRegistDAO storeRegistDAO) {
        this.userDAO = userDAO;
        this.storeRegistDAO = storeRegistDAO;
    }

    public Long insertOwner(String id, String pw, String name, String phone, Integer age) {
        return userDAO.insertUser(Authority.OWNER, id, pw, name, phone, age);
    }

    public void insertStoreRegist(String name, String comment, String phone, String address, Long user_pk) {
        storeRegistDAO.insertRegistration(name, comment, phone, address, user_pk);
    }
}
