package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.TotalOrderDTO;

public class TotalOrderDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public TotalOrderDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
}