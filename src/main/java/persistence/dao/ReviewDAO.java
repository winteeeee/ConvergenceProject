package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.ReviewDTO;

import java.util.List;

public class ReviewDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public ReviewDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(ReviewDTO review) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            session.insert("", review);
        } finally {
            session.close();
        }
    }

    public List<ReviewDTO> selectAll(String user_id) {
        SqlSession session = sqlSessionFactory.openSession();
        List<ReviewDTO> list = null;
        try {
            list = session.selectList("", user_id);
        } finally {
            session.close();
        }
        return list;
    }
 }