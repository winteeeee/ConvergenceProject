package service;

import java.util.List;

public class Service {
    private final DAO boardDAO;

    public Service(DAO boardDAO) {
        this.DAO = dao;
    }

    public List<DTO> findAll() {
        List<DTO> all = dao.findAll();
        return all;
    }
}
