package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.StatisticsDTO;

public class StatisticsDAO extends DAO<StatisticsDTO>{
    public StatisticsDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.StatisticsMapper.");
    }

    public StatisticsDTO selectOneForOwner(Long menu_id, String name) {
        String stmt = sqlMapperPath + "selectOneForOwner";
        StatisticsDTO dto = StatisticsDTO.builder()
                .id(menu_id)
                .count(0)
                .price(0)
                .name(name)
                .build();

        return selectOne((SqlSession session) -> {
            StatisticsDTO temp = session.selectOne(stmt, dto);
            return (temp != null) ? temp : dto;
        });
    }

    public StatisticsDTO selectOneForAdmin(Long store_id, String name) {
        String stmt = sqlMapperPath + "selectOneForAdmin";
        StatisticsDTO dto = StatisticsDTO.builder()
                .id(store_id)
                .count(0)
                .price(0)
                .name(name)
                .build();

        return selectOne((SqlSession session) -> {
            StatisticsDTO temp = session.selectOne(stmt, dto);
            return (temp != null) ? temp : dto;
        });
    }
}
