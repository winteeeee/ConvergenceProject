package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.MenuDTO;

import java.util.HashMap;
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
        return session.selectOne(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int insert(SqlSession session, Object[] arg) throws Exception {
        session.insert(sqlMapperPath + arg[0], arg[1]);
        session.commit();

        MenuDTO menuDTO = (MenuDTO) arg[1];
        for (MenuDTO temp : selectList("selectAllWithClassification_id", menuDTO)) {
            if (menuDTO.getName().equals(temp.getName())) {
                menuDTO = temp;
                break;
            }
        }
        List<Long> details = (List) arg[2];

        for (int i = 0; i < details.size(); i++) {
            HashMap<String, Long> map = new HashMap<>();
            map.put("menu_id", menuDTO.getId());
            map.put("details_id", details.get(i));
            session.insert(sqlMapperPath + "insertMenuDetails", map);
        }

        return 1;
    }
    @Override
    protected int update(SqlSession session, Object[] arg) {
        return session.update(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int delete(SqlSession session, Object[] arg) { return 0; }


    public int insertMenu(String name, Integer price, Integer stock, Long classification_id, List<Long> details) {
        String stmt = "insertMenu";
        MenuDTO menuDTO = new MenuDTO(null, name, price, stock, classification_id);
        return insert(stmt, menuDTO, details);
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