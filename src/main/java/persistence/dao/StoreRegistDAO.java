package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.StoreRegistDTO;
import persistence.dto.UserDTO;
import persistence.enums.Authority;

import java.util.List;

public class StoreRegistDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public StoreRegistDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(StoreRegistDTO storeRegist) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("", storeRegist);
        } finally {
            session.close();
        }
    }

    public List<StoreRegistDTO> selectAll() {
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreRegistDTO> list = null;
        try{
           list = session.selectList("");
        } finally {
            session.close();
        }
        return list;
    }

    public List<StoreRegistDTO> selectAll(Authority authority) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreRegistDTO> list = null;
        try{
            list = session.selectList("", authority);
        } finally {
            session.close();
        }
        return list;
    }

    public void update(StoreRegistDTO storeRegist) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("", storeRegist);
        } finally {
            session.close();
        }
    }
}