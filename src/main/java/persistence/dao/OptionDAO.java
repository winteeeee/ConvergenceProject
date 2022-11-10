package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.OptionDTO;
import persistence.dto.StoreDTO;

import java.util.List;

public class OptionDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public OptionDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(OptionDTO option) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("", option);
        } finally {
            session.close();
        }
    }
}