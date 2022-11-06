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

    public UserDTO findUserWithId(String id) {
        UserDTO user = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            user = session.selectOne("");
        } finally {
            session.close();
        }
        return user;
    }

    public void insertUser(String id, String pw, Authority authority, String address) {

    }

    public void updateUser(UserDTO dto) {

    }

    public UserDTO readOneUser(String id) {
        return null;
    }

    public List<UserDTO> readAllUser() {
        return null;
    }
}
