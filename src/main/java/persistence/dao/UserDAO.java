package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.StoreDTO;
import persistence.dto.UserDTO;
import persistence.enums.Authority;
import persistence.enums.RegistStatus;

import java.util.List;


public class UserDAO extends DAO<UserDTO>{
    public UserDAO(SqlSessionFactory sqlSessionFactory){
        super(sqlSessionFactory, "mapper.UserMapper.");
    }

    public Long insertAdmin(UserDTO owner) {
        String stmt = sqlMapperPath + "insertUser";
        UserDTO dto = UserDTO.builder()
                .status(RegistStatus.ACCEPT)
                .authority(Authority.ADMIN)
                .id(owner.getId())
                .pw(owner.getPw())
                .name(owner.getName())
                .phone(owner.getPhone())
                .age(owner.getAge())
                .build();

        insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });

        return dto.getPk();
    }

    public Long insertOwner(UserDTO owner) {
        String stmt = sqlMapperPath + "insertUser";
        UserDTO dto = UserDTO.builder()
                .status(RegistStatus.HOLD)
                .authority(Authority.OWNER)
                .id(owner.getId())
                .pw(owner.getPw())
                .name(owner.getName())
                .phone(owner.getPhone())
                .age(owner.getAge())
                .build();

        insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });

        return dto.getPk();
    }

    public int insertUser(UserDTO user) {
        String stmt = sqlMapperPath + "insertUser";
        UserDTO dto = UserDTO.builder()
                .status(RegistStatus.ACCEPT)
                .authority(Authority.USER)
                .id(user.getId())
                .pw(user.getPw())
                .name(user.getName())
                .phone(user.getPhone())
                .age(user.getAge())
                .build();



        return insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });
    }

    public List<UserDTO> selectAllWithStatus(RegistStatus status) {
        String stmt = sqlMapperPath + "selectAllWithStatus";
        StoreDTO dto = StoreDTO.builder()
                .status(status)
                .build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public UserDTO selectOneWithPk(Long pk) {
        String stmt = sqlMapperPath + "selectOneWithPk";
        UserDTO dto = UserDTO.builder()
                .pk(pk)
                .build();

        return selectOne((SqlSession session) -> {
            return session.selectOne(stmt, dto);
        });
    }

    public UserDTO selectOneWithId(String id) {
        String stmt = sqlMapperPath + "selectOneWithId";
        UserDTO dto = UserDTO.builder()
                .id(id)
                .build();

        return selectOne((SqlSession session) -> {
            return session.selectOne(stmt, dto);
        });
    }

    public int update(UserDTO user) {
        String stmt = sqlMapperPath + "update";
        UserDTO dto = UserDTO.builder()
                .pk(user.getPk())
                .pw(user.getPw())
                .name(user.getName())
                .phone(user.getPhone())
                .age(user.getAge())
                .build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public int updateStatus(Long pk, RegistStatus status) {
        String stmt = sqlMapperPath + "updateStatus";
        UserDTO dto = UserDTO.builder()
                .pk(pk)
                .status(status)
                .build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

}
