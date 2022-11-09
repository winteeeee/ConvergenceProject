package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.UserDTO;
import persistence.enums.Authority;
import java.util.List;

public class UserDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public UserDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void insert(UserDTO user) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("", user);
        } finally {
            session.close();
        }
    }
}
