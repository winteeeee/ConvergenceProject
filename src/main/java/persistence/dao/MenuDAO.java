package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.MenuDTO;
import persistence.dto.StoreDTO;
import persistence.enums.RegistStatus;

import java.util.HashMap;
import java.util.List;

public class MenuDAO extends DAO<MenuDTO>{
    public MenuDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.MenuMapper.");
    }


    public Long insertMenu(MenuDTO menu) {
        String stmt = sqlMapperPath + "insertMenu";
        MenuDTO dto = MenuDTO.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .status(RegistStatus.HOLD)
                .classification_id(menu.getClassification_id()).build();

        insert((SqlSession session) -> {
                return session.insert(stmt, dto);
            });

        return dto.getId();
    }

    public int updateStatus(Long id, RegistStatus status) { // TODO 쿼리 만들고 매핑하기
        String stmt = sqlMapperPath + "updateStatus";
        MenuDTO dto = MenuDTO.builder()
                .id(id)
                .status(status).build();

        return update((SqlSession session) -> {
            return session.insert(stmt, dto);
        });
    }

    public int insertMenuDetails(Long menu_id, Long details_id) {
        String stmt = sqlMapperPath + "insertMenuDetails";
        HashMap<String, Object> map = new HashMap<>();
        map.put("menu_id", menu_id);
        map.put("details_id", details_id);

        return insert((SqlSession session) -> {
                return session.insert(stmt, map);
            });
    }

    public List<MenuDTO> selectAllWithClassification_id(Long classification_id) {
        String stmt = sqlMapperPath + "selectAllWithClassification_id";
        MenuDTO dto = MenuDTO.builder()
                .classification_id(classification_id).build();

        return selectList((SqlSession session) -> {
                return session.selectList(stmt, dto);
            });
    }

    public List<MenuDTO> selectAllWithStatus(RegistStatus status) {
        String stmt = sqlMapperPath + "selectAllWithStatus";
        StoreDTO dto = StoreDTO.builder()
                .status(status).build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }

    public MenuDTO selectOneWithId(Long id) {
        String stmt = sqlMapperPath + "selectOneWithId";
        MenuDTO dto = MenuDTO.builder()
                .id(id).build();

        return selectOne((SqlSession session) -> {
                return session.selectOne(stmt, dto);
            });
    }

    public int updateNameAndPrice(Long id, String name, Integer price) {
        String stmt = sqlMapperPath + "updateNameAndPrice";
        MenuDTO dto = MenuDTO.builder()
                .id(id)
                .name(name)
                .price(price).build();

        return update((SqlSession session) -> {
                return session.update(stmt, dto);
            });
    }

    public int updateForInsert(Long id) {
        String stmt = sqlMapperPath + "updateForInsert";
        MenuDTO dto = MenuDTO.builder()
                .id(id).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public int updateStock(Integer stock) {
        String stmt = sqlMapperPath + "updateStock";
        MenuDTO dto = MenuDTO.builder()
                .stock(stock).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }
}