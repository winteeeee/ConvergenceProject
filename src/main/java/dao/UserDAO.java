package dao;
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

    public boolean findUser(String id) {
        boolean isUser = false;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            if(session.selectOne("mapper.UserMapper.쿼리명", id) != null) {
                isUser = true;
            }
        } finally {
            session.close();
        }
        return isUser;
    }

    public void insertUser(String id, String pw, Authority authority, String address) {
        UserDTO user = new UserDTO(id, pw, 0, address);
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.insert("mapper.UserMapper.쿼리명", user);
        } finally {
            session.close();
        }
    }

    public void updateUser(UserDTO user) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            session.update("mapper.UserMapper.쿼리명", user);
        } finally {
            session.close();
        }
    }

    public UserDTO readOneUser(String id) {
        UserDTO user = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            user = session.selectOne("mapper.UserMapper.쿼리명", id);
        } finally {
            session.close();
        }
        return user;
    }

    public List<UserDTO> readAllUser() {
        List<UserDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.UserMapper.쿼리명");
        }
        finally {
            session.close();
        }
        return list;
    }
}
