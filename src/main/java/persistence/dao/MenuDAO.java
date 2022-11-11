package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.MenuDTO;
import persistence.dto.UserDTO;

import java.util.List;

public class MenuDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public MenuDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(MenuDTO menu) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("", menu);
        }finally {
            session.close();
        }
    }

    public void insertRelation(long store_id, long menu_id, int option_id) {}

    public List<MenuDTO> selectAll(long store_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<MenuDTO> list = null;
        try {
            list = session.selectList("", store_id);
        } finally {
            session.close();
        }
        return list;
    }

    public void update(MenuDTO menu) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("", menu);
        } finally {
            session.close();
        }
    }

    public MenuDTO selectOne(long menu_id) {
        SqlSession session = sqlSessionFactory.openSession();
        MenuDTO menu = null;
        try {
            menu = session.selectOne("", menu_id);
        } finally {
            session.close();
        }
        return menu;
    }
}