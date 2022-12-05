package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.DetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsDAO extends DAO<DetailsDTO>{
    public DetailsDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.DetailsMapper.");
    }


    public int insertDetails(DetailsDTO details) {
        String stmt = sqlMapperPath + "insertDetails";
        DetailsDTO dto = DetailsDTO.builder()
                .name(details.getName())
                .price(details.getPrice())
                .store_id(details.getStore_id()).build();

        return insert((SqlSession session) -> {
                return session.insert(stmt, dto);
            });
    }

    public List<DetailsDTO> selectAllWithStore_id(Long store_id) {
        String stmt = sqlMapperPath + "selectAllWithStore_id";
        DetailsDTO dto = DetailsDTO.builder()
                .store_id(store_id).build();

        return selectList((SqlSession session) -> {
                return session.selectList(stmt, dto);
            });
    }

    public List<DetailsDTO> selectAllWithMenu_id(Long menu_id) {
        String stmt = sqlMapperPath + "selectAllWithMenu_id";
        Map<String, Object> map = new HashMap<>();
        map.put("menu_id", menu_id);

        return selectList((SqlSession session) -> {
                return session.selectList(stmt, map);
            });
    }
}