package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.UserDTO;
import persistence.enums.Authority;

import java.util.List;


public class UserDAO extends DAO<UserDTO>{
    private static UserDAO userDAO;
    static {
        if (userDAO == null) {
            userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static UserDAO getUserDAO() { return userDAO; }


    private UserDAO(SqlSessionFactory sqlSessionFactory){
        super(sqlSessionFactory, "mapper.UserMapper.");
    }


    @Override
    protected List<UserDTO> selectList(SqlSession session, Object[] arg) {
        String stmt = (String) arg[0];
        UserDTO user;

        if (stmt.equals("selectAll")) {
            return session.selectList(sqlMapperPath + stmt);
        }
        else {
            user = (UserDTO) arg[1];
            return session.selectList(sqlMapperPath + stmt, user);
        }
    }
    @Override
    protected UserDTO selectOne(SqlSession session, Object[] arg) {
        String stmt = (String) arg[0];
        UserDTO user = (UserDTO) arg[1];

        return session.selectOne(sqlMapperPath + stmt, user);
    }
    @Override
    protected int insert(SqlSession session, Object[] arg) {
        String stmt = (String) arg[0];
        UserDTO user = (UserDTO) arg[1];
        return session.insert(sqlMapperPath + stmt, user);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) {
        return 0;
    }
    @Override
    protected int delete(SqlSession session, Object[] arg) {
        return 0;
    }



    public List<UserDTO> selectAll() {
        return selectList("selectAll");
    }

    public UserDTO findUserWithID(String id) {
        UserDTO user = new UserDTO();
        user.setId(id);

        return selectOne("findWithID", user);
    }

    public int insertUser(String id, String pw, Authority authority, String name, Integer age) {
        UserDTO user = new UserDTO(null, authority.getCode(), id, pw, name, age);
        return insert(user);
    }

/*
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
*/
}
