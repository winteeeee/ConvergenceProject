package service;

import persistence.dao.StoreDAO;
import persistence.dao.StoreRegistDAO;
import persistence.dao.UserDAO;
import persistence.dto.StoreDTO;
import persistence.dto.StoreRegistDTO;
import persistence.dto.UserDTO;
import persistence.enums.RegistStatus;
import view.StoreView;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private StoreRegistDAO storeRegistDAO;
    private StoreDAO storeDAO;
    private UserDAO userDAO;

    private StoreView storeView;

    public AdminService(StoreRegistDAO storeRegistDAO, StoreDAO storeDAO, UserDAO userDAO, StoreView storeView) {
        this.storeRegistDAO = storeRegistDAO;
        this.storeDAO = storeDAO;
        this.userDAO = userDAO;
        this.storeView = storeView;
    }

    public void acceptStoreRegist(Long id) {
        storeRegistDAO.updateStatus(id, RegistStatus.ACCEPT);
        StoreRegistDTO registDTO = storeRegistDAO.selectOneWithId(id);

        storeDAO.insertStore(
                registDTO.getName(),
                registDTO.getComment(),
                registDTO.getPhone(),
                registDTO.getAddress(),
                null,
                null,
                registDTO.getUser_pk()
        );
    }

    public void viewStoreList() {
        List<StoreDTO> storeList = getStoreList();
        List<UserDTO> ownerList = new ArrayList<>();

        for (StoreDTO store : storeList) {
            ownerList.add(userDAO.selectOneWithPk(store.getUser_pk()));
        }

        storeView.storeViewForAdmin(storeList, ownerList);
    }

    public void rejectStoreRegist(Long id) {
        storeRegistDAO.updateStatus(id, RegistStatus.REJECT);
    }

    public List<StoreRegistDTO> getHoldList() {
        return storeRegistDAO.selectAllWithStatus(RegistStatus.HOLD);
    }

    public List<StoreDTO> getStoreList() {
        return storeDAO.selectAll();
    }
}
