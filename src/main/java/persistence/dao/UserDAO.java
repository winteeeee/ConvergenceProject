package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.UserDTO;
import persistence.enums.Authority;

import java.util.List;


public class UserDAO extends DAO<UserDTO>{
    public UserDAO(SqlSessionFactory sqlSessionFactory){
        super(sqlSessionFactory, "mapper.UserMapper.");
    }

    @Override
    protected List<UserDTO> selectList(SqlSession session, Object[] arg) {
        return null;
    }
    @Override
    protected UserDTO selectOne(SqlSession session, Object[] arg) {
        return session.selectOne(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int insert(SqlSession session, Object[] arg) {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) {
        return session.update(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int delete(SqlSession session, Object[] arg) {
        return 0;
    }

    public Long insertUser(Authority authority, String id, String pw, String name, String phone, Integer age) {
        String stmt = "insertUser";
        UserDTO user = new UserDTO(null, authority.getCode(), id, pw, name, phone, age);
        insert(stmt, user);

        return user.getPk();
    }

    public UserDTO selectOneWithPk(Long pk) {
        String stmt = "selectOneWithUserPk";
        UserDTO userDTO = new UserDTO();
        userDTO.setPk(pk);

        return selectOne(stmt, userDTO);
    }

    public UserDTO selectOneWithId(String id) {
        String stmt = "selectOneWithUserId";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        return selectOne(stmt, userDTO);
    }

    public int update(String id, String pw, String name, String phone, Integer age) {
        String stmt = "update";
        UserDTO userDTO = new UserDTO(null, null, id, pw, name, phone, age);

        return update(stmt, userDTO);
    }
}
