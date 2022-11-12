package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.MenuDTO;

import java.util.List;

public class MenuDAO extends DAO<MenuDTO>{
    private static MenuDAO menuDAO;
    static {
        if (menuDAO == null) {
            menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static MenuDAO getMenuDAO() { return menuDAO; }


    private MenuDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.MenuMapper.");
    }

    @Override
    protected List<MenuDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected MenuDTO selectOne(SqlSession session, Object[] arg) {
        return null;
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
    protected int delete(SqlSession session, Object[] arg) { return 0; }


    public int insertMenu(String group, String name, Integer price, Integer stock, Long store_id, List<Integer> options) {
        String stmt = "insertMenu";
        MenuDTO menuDTO = new MenuDTO(null, group, name, price, stock, store_id);

        // TODO menu_option_map 관련 함수 필요함. 또한, 여러 쿼리를 실행시키는 만큼 원자성을 지녀야 함
        return insert(stmt, menuDTO);
    }

    public List<MenuDTO> selectAllWithStore_id(Long store_id) {
        String stmt = "selectAllWithStore_id";
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setStore_id(store_id);

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
        MenuDTO menuDTO = new MenuDTO(id,null,name,price,null,null);

        return update(stmt, menuDTO);
    }
}