package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.UserDTO;

public class UserDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public UserDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
    public UserDTO findUserWithId() {
        UserDTO user = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            user = session.selectOne("");
        } finally {
            session.close();
        }
        return user;
    }
}
