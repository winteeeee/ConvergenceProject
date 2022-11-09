package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.MenuDTO;
import persistence.dto.UserDTO;

import java.util.List;

public class MenuDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public MenuDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<MenuDTO> readAllMenu(String store_id) {
        return null;
    }

    public MenuDTO readOneMenu(String menu_id) {
        return null;
    }

    public void insertMenu() {

    }

    public void updateMenu() {

    }
}