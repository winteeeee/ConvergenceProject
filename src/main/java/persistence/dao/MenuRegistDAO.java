package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.MenuRegistDTO;

public class MenuRegistDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public MenuRegistDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
}