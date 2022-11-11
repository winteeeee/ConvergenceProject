package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.StoreDTO;
import persistence.dto.StoreRegistDTO;
import persistence.enums.Authority;

import java.util.List;

public class StoreDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public StoreDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /*public void insert(StoreRegistDTO storeRegist) {
        SqlSession session = sqlSessionFactory.openSession();
        StoreDTO store = new StoreDTO(); // storeRegist 에서 storeDTO 생성에 필요한 get
        try{
            session.selectList("", store);
        } finally {
            session.close();
        }
    }*/

    public List<StoreDTO> selectAll() {
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreDTO> list = null;
        try {
            list = session.selectList("");
        } finally {
            session.close();
        }
        return list;
    }

    public List<StoreDTO> selectAll(String user_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<StoreDTO> list = null;
        try {
            list = session.selectList("", user_id);
        } finally {
            session.close();
        }
        return list;
    }
}