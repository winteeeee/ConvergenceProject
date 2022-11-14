package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.MenuDTO;

import java.util.HashMap;
import java.util.List;

public class MenuDAO extends DAO<MenuDTO>{
    public MenuDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.MenuMapper.");
    }

    @Override
    protected List<MenuDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected MenuDTO selectOne(SqlSession session, Object[] arg) {
        return session.selectOne(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int insert(SqlSession session, Object[] arg) throws Exception {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) {
        return session.update(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int delete(SqlSession session, Object[] arg) { return 0; }


    public Long insertMenu(String name, Integer price, Integer stock, Long classification_id) {
        String stmt = "insertMenu";
        MenuDTO menuDTO = new MenuDTO(null, name, price, stock, classification_id);
        insert(stmt, menuDTO);

        return menuDTO.getId();
    }

    public int insertMenuDetails(Long menuId, Long details_id) {
        String stmt = "insertMenuDetails";
        HashMap<String, Long> map = new HashMap<>();
        map.put("menu_id", menuId);
        map.put("details_id", details_id);

        return insert(stmt, map);
    }

    public List<MenuDTO> selectAllWithClassification_id(Long classification_id) {
        String stmt = "selectAllWithClassification_id";
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setClassification_id(classification_id);

        return selectList(stmt, menuDTO);
    }

    public MenuDTO selectOneWithId(Long id) {
        String stmt = "selectOneWithId";
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(id);

        return selectOne(stmt, menuDTO);
    }

    public int updateNameAndPrice(Long id, String name, Integer price) {
        String stmt = "updateNameAndPrice";
        MenuDTO menuDTO = new MenuDTO(id,name,price,null,null);

        return update(stmt, menuDTO);
    }
}