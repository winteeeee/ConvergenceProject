package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.OrderDTO;
import java.util.List;

public class OrderDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public OrderDAO(SqlSessionFactory sqlSessionFactory){
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

    public List<OrderDTO> selectAllForStore(long store_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> list = null;
        try {
            list = session.selectList("", store_id);
        }finally {
            session.close();
        }
        return list;
    }

    public List<OrderDTO> selectAllForReview(long total_order_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<OrderDTO> list = null;
        try {
            list = session.selectList("", total_order_id);
        }finally {
            session.close();
        }
        return list;
    }

    public void update(OrderDTO order) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.update("", order);
        }finally {
            session.close();
        }
    }
}