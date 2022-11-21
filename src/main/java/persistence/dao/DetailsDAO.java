package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.DetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsDAO extends DAO<DetailsDTO>{
    public DetailsDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.DetailsMapper.");
    }

    @Override
    protected List<DetailsDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected DetailsDTO selectOne(SqlSession session, Object[] arg) {
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

    public int insertDetails(String name, Integer price, Long store_id) {
        String stmt = "insertDetails";
        DetailsDTO detailsDTO = new DetailsDTO(null, name, price, store_id);

        return insert(stmt, detailsDTO);
    }

    public List<DetailsDTO> selectAllWithStore_id(Long store_id) {
        String stmt = "selectAllWithStore_id";
        DetailsDTO detailsDTO = new DetailsDTO();
        detailsDTO.setStore_id(store_id);

        return selectList(stmt, detailsDTO);
    }

    public List<DetailsDTO> selectAllWithMenu_id(Long menu_id) {
        String stmt = "selectAllWithMenu_id";
        Map<String, Object> map = new HashMap<>();
        map.put("menu_id", menu_id);

        return selectList(stmt, map);
    }
}