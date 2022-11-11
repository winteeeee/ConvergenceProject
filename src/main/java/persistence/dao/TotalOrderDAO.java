package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.OrderDTO;
import persistence.dto.TotalOrderDTO;

import java.util.List;

public class TotalOrderDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public TotalOrderDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(OrderDTO order) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("", order);
        } finally {
            session.close();
        }
    }

    public List<TotalOrderDTO> selectAll(String user_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<TotalOrderDTO> list = null;
        try {
            list = session.selectList("", user_id);
        } finally {
            session.close();
        }
        return list;
    }
}