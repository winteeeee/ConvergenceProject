package persistence.dao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.StoreDTO;
import persistence.enums.RegistStatus;

import java.time.LocalDateTime;
import java.util.List;

public class StoreDAO extends DAO<StoreDTO>{
    public StoreDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.StoreMapper.");
    }

    public int insertStore(StoreDTO store) {
        String stmt = sqlMapperPath + "insertStore";
        StoreDTO dto = StoreDTO.builder()
                .status(RegistStatus.HOLD)
                .name(store.getName())
                .comment(store.getComment())
                .phone(store.getPhone())
                .address(store.getAddress())
                .open_time(store.getOpen_time())
                .close_time(store.getClose_time())
                .user_pk(store.getUser_pk()).build();

        return insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });
    }

    public int updateStatus(Long id, RegistStatus status) {
        String stmt = sqlMapperPath + "updateStatus";
        StoreDTO dto = StoreDTO.builder()
                .id(id)
                .status(status).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public List<StoreDTO> selectAll() {
        String stmt = sqlMapperPath + "selectAll";
        return selectList((SqlSession session) -> {
            return session.selectList(stmt);
        });
    }

    public List<StoreDTO> selectAllWithStatus(RegistStatus status) {
        String stmt = sqlMapperPath + "selectAllWithStatus";
        StoreDTO dto = StoreDTO.builder()
                .status(status).build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public List<StoreDTO> selectAllWithUserPk(Long user_pk) {
        String stmt = sqlMapperPath + "selectAllWithUserPk";
        StoreDTO dto = StoreDTO.builder()
                .user_pk(user_pk).build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public int updateTime(Long id, LocalDateTime open_time, LocalDateTime close_time) {
        String stmt = sqlMapperPath + "updateTime";
        StoreDTO dto = StoreDTO.builder()
                .id(id)
                .open_time(open_time)
                .close_time(close_time).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }
}