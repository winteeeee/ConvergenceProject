package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.OrdersDTO;
import persistence.enums.OrdersStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrdersDAO extends DAO<OrdersDTO> {
    private static OrdersDAO ordersDAO;
    static {
        if (ordersDAO == null) {
            ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static OrdersDAO getOrdersDAO() { return ordersDAO; }


    private OrdersDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.OrdersMapper.");
    }

    @Override
    protected List<OrdersDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected OrdersDTO selectOne(SqlSession session, Object[] arg) {
        return session.selectOne(sqlMapperPath + arg[0], arg[1]);
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


    public int insertOrders(LocalDateTime regdate, String details, Integer price, String comment, Long menu_id, Long user_pk, Long store_id) {
        String stmt = "insertOrders";
        OrdersDTO ordersDTO = new OrdersDTO(null, OrdersStatus.HOLD.getCode(), regdate, details, price, comment, menu_id, user_pk, store_id);

        return insert(stmt, ordersDTO);
    }

    public List<OrdersDTO> selectAllWithStore_id (Long store_id) {
        String stmt = "selectAllWithStore_id";
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStore_id(store_id);

        return selectList(stmt, ordersDTO);
    }

    public List<OrdersDTO> selectAllWithUser_pk (Long user_pk) {
        String stmt = "selectAllWithTotal_Orders_id";
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setUser_pk(user_pk);

        return selectList(stmt, ordersDTO);
    }

    public OrdersDTO selectOneWithId (Long id) {
        String stmt = "selectOneWithId";
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setId(id);

        return selectOne(stmt, ordersDTO);
    }

    public int updateStatus(OrdersStatus status, Long id) {
        String stmt = "updateStatus";
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(status.getCode());
        ordersDTO.setId(id);

        return update(stmt, ordersDTO);
    }
}