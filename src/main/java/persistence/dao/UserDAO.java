package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.UserDTO;
import persistence.enums.Authority;

public class UserDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public UserDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public UserDTO findUserWithId(String id) {
        UserDTO user = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            user = session.selectOne("", id);
        } finally {
            session.close();
        }
        return user;
    }

    public void insertUser(String id, String pw, Authority authority, String address) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            // 세션을 사용해서 DB에 유저 넣기
        } finally {
            session.close();
        }
    }
}
