package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.StoreDTO;
import persistence.dto.StoreRegistDTO;
import persistence.enums.Authority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO extends DAO<StoreDTO>{
    private static StoreDAO storeDao;
    static {
        if (storeDao == null) {
            storeDao = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static StoreDAO getStoreDAO() { return storeDao; }

    private StoreDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.StoreMapper.");
    }

    @Override
    protected List<StoreDTO> selectList(SqlSession session, Object[] arg) {
        switch ((String) arg[0]) {
            case "selectAll":
                return session.selectList(sqlMapperPath + arg[0]);
            case "selectAllWithUser_pk":
                return session.selectList(sqlMapperPath + arg[0], arg[1]);
        }
        return new ArrayList<StoreDTO>();
    }
    @Override
    protected StoreDTO selectOne(SqlSession session, Object[] arg) {
        return null;
    }
    @Override
    protected int insert(SqlSession session, Object[] arg) {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) {
        return 0;
    }
    @Override
    protected int delete(SqlSession session, Object[] arg) {
        return 0;
    }

    public int insertStore(String name, String comment, String phone, String address, LocalDateTime open_time, LocalDateTime close_time, Long user_pk) {
        String stmt = "insertStore";
        StoreDTO storeDTO = new StoreDTO(null, name, comment, phone, address, 0, 0, open_time, close_time, user_pk);

        return insert(stmt, storeDTO);
    }

    public List<StoreDTO> selectAll() {
        String stmt = "selectAll";
        return selectList(stmt);
    }

    public List<StoreDTO> selectAllWithUser_pk(Long user_pk) {
        String stmt = "selectAllWithUser_pk";
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setUser_pk(user_pk);

        return selectList(stmt, storeDTO);
    }
}