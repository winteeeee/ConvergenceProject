package service;

import persistence.dao.StoreDAO;
import persistence.dao.StoreRegistDAO;
import persistence.dao.UserDAO;
import persistence.dto.StoreDTO;
import persistence.dto.StoreRegistDTO;
import persistence.enums.RegistStatus;

import java.util.List;

public class AdminService {
    private StoreRegistDAO storeRegistDAO;
    private StoreDAO storeDAO;
    private UserDAO userDAO;

    public AdminService(StoreRegistDAO storeRegistDAO, StoreDAO storeDAO, UserDAO userDAO) {
        this.storeRegistDAO = storeRegistDAO;
        this.storeDAO = storeDAO;
        this.userDAO = userDAO;
    }

    public int acceptStoreRegist(Long id) {
        storeRegistDAO.updateStatus(id, RegistStatus.ACCEPT);
        StoreRegistDTO registDTO = storeRegistDAO.selectOneWithId(id);

        return storeDAO.insertStore(
                registDTO.getName(),
                registDTO.getComment(),
                registDTO.getPhone(),
                registDTO.getAddress(),
                null,
                null,
                registDTO.getUser_pk()
        );
    }

    public int rejectStoreRegist(Long id) {
        return storeRegistDAO.updateStatus(id, RegistStatus.REJECT);
    }

    public List<StoreRegistDTO> getHoldList() {
        return storeRegistDAO.selectAllWithStatus(RegistStatus.HOLD);
    }

    public List<StoreDTO> getStoreList() {
        return storeDAO.selectAll();
    }
}
