package service;

import persistence.dao.ClassificationDAO;
import persistence.dao.DetailsDAO;
import persistence.dao.MenuDAO;
import persistence.dao.StoreDAO;
import persistence.dto.ClassificationDTO;
import persistence.dto.DetailsDTO;
import persistence.dto.MenuDTO;
import persistence.dto.StoreDTO;

import java.util.ArrayList;
import java.util.List;

public class StoreService {
    private StoreDAO storeDAO;
    private ClassificationDAO classificationDAO;
    private MenuDAO menuDAO;
    private DetailsDAO detailsDAO;

    public StoreService(StoreDAO storeDAO, ClassificationDAO classificationDAO, MenuDAO menuDAO, DetailsDAO detailsDAO) {
        this.storeDAO = storeDAO;
        this.classificationDAO = classificationDAO;
        this.menuDAO = menuDAO;
        this.detailsDAO = detailsDAO;
    }

    public List<StoreDTO> getStoreWithUser_pk(Long user_pk) {
        return storeDAO.selectAllWithUser_pk(user_pk);
    }

    public List<ClassificationDTO> getMenuGroups(Long store_id) {
        return classificationDAO.selectAllWithStore_id(store_id);
    }

    public List<MenuDTO> getMenusWithGroup_id(Long classification_id) {
        return menuDAO.selectAllWithClassification_id(classification_id);
    }

    public List<MenuDTO> getAllMenus(Long store_id) {
        List<MenuDTO> menus = new ArrayList<>();

        List<ClassificationDTO> groups = getMenuGroups(store_id);
        for (ClassificationDTO group : groups) {
            menus.addAll(getMenusWithGroup_id(group.getId()));
        }

        return menus;
    }

    public MenuDTO getMenu(Long menu_id) {
        return menuDAO.selectOneWithId(menu_id);
    }

    public List<DetailsDTO> getDetailsWithMenuId(Long menu_id) {
        return detailsDAO.selectAllWithMenu_id(menu_id);
    }

    public ClassificationDTO getGroupWithId(Long id) {
        return classificationDAO.selectOneWithId(id);
    }
}
