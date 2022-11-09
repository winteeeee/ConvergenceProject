package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.StoreRegistDTO;

public class StoreRegistDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public StoreRegistDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
}