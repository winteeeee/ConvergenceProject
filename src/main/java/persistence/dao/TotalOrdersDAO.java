package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.TotalOrdersDTO;
import persistence.enums.OrdersStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TotalOrdersDAO extends DAO<TotalOrdersDTO> {     // TODO 전부 매핑, 쿼리 완성해야함
    public TotalOrdersDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.TotalOrdersMapper.");
    }

    public Long insertTotalOrders(TotalOrdersDTO totalOrders) {
        String stmt = sqlMapperPath + "insertTotalOrders";
        TotalOrdersDTO dto = TotalOrdersDTO.builder()
                .status(OrdersStatus.HOLD)
                .regdate(LocalDateTime.now())
                .price(totalOrders.getPrice())
                .store_id(totalOrders.getStore_id())
                .user_pk(totalOrders.getUser_pk())
                .build();

        insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });

        return dto.getId();
    }

    public List<TotalOrdersDTO> selectAllWithStoreId(Long store_id) {
        String stmt = sqlMapperPath + "selectAllWithStoreId";
        TotalOrdersDTO dto = TotalOrdersDTO.builder()
                .store_id(store_id)
                .build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public List<TotalOrdersDTO> selectAllWithUserPk(Long user_pk) {
        String stmt = sqlMapperPath + "selectAllWithUserPk";
        TotalOrdersDTO dto = TotalOrdersDTO.builder()
                .user_pk(user_pk)
                .build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public int updateStatus(Long id, OrdersStatus status) {
        String stmt = sqlMapperPath + "updateStatus";
        TotalOrdersDTO dto = TotalOrdersDTO.builder()
                .id(id)
                .status(status)
                .build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public TotalOrdersDTO selectOneWithId(Long id) {
        String stmt = sqlMapperPath + "selectOneWithId";
        TotalOrdersDTO dto = TotalOrdersDTO.builder()
                .id(id)
                .build();

        return selectOne((SqlSession session) -> {
            return session.selectOne(stmt, dto);
        });
    }
}