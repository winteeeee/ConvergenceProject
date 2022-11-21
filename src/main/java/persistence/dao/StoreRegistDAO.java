package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.StoreRegistDTO;
import persistence.enums.RegistStatus;

import java.util.ArrayList;
import java.util.List;

public class StoreRegistDAO extends DAO<StoreRegistDTO>{
    public StoreRegistDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.StoreRegistMapper.");
    }


    @Override
    protected List<StoreRegistDTO> selectList(SqlSession session, Object[] arg) {
        switch ((String) arg[0]) {
            case "selectAll":
                return session.selectList(sqlMapperPath + arg[0]);
            case "selectAllWithStatus":
                return session.selectList(sqlMapperPath + arg[0], arg[1]);
        }
        return new ArrayList<StoreRegistDTO>();
    }
    @Override
    protected StoreRegistDTO selectOne(SqlSession session, Object[] arg) {
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

    public int insertRegistration(String name, String comment, String phone, String address, Long user_pk) {
        String stmt = "insertRegistration";
        StoreRegistDTO storeRegistDTO = new StoreRegistDTO(null, RegistStatus.HOLD.getCode(), name, comment, phone, address, user_pk);

        return insert(stmt, storeRegistDTO);
    }

    public List<StoreRegistDTO> selectAll() {
        String stmt = "selectAll";

        return selectList(stmt);
    }

    public List<StoreRegistDTO> selectAllWithStatus(RegistStatus status) {
        String stmt = "selectAllWithStatus";
        StoreRegistDTO storeRegistDTO = new StoreRegistDTO();
        storeRegistDTO.setStatus(status.getCode());

        return selectList(stmt, storeRegistDTO);
    }

    public int updateStatus(Long id, RegistStatus status) {
        String stmt = "updateStatus";
        StoreRegistDTO storeRegistDTO = new StoreRegistDTO();
        storeRegistDTO.setId(id);
        storeRegistDTO.setStatus(status.getCode());

        return update(stmt, storeRegistDTO);
    }

    public StoreRegistDTO selectOneWithId(Long id) {
        String stmt = "selectOneWithId";
        StoreRegistDTO storeRegistDTO = new StoreRegistDTO();
        storeRegistDTO.setId(id);

        return selectOne(stmt, storeRegistDTO);
    }
}