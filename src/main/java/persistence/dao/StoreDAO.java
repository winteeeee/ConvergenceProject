package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.StoreDTO;

public class StoreDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public StoreDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
}