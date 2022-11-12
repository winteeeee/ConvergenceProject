package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.OrderDTO;
import persistence.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDAO extends DAO<OrderDTO> {
    private static OrderDAO orderDAO;
    static {
        if (orderDAO == null) {
            orderDAO = new OrderDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static OrderDAO getOrderDAO() { return orderDAO; }


    private OrderDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.OrderMapper.");
    }

    @Override
    protected List<OrderDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected OrderDTO selectOne(SqlSession session, Object[] arg) { return null; }
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


    public int insertOrder(LocalDateTime regdate, String option, Integer price, String comment, Long menu_id, Long total_order_id, Long store_id) {
        String stmt = "insertOrder";
        OrderDTO orderDTO = new OrderDTO(null, OrderStatus.HOLD.getCode(), regdate, option, price, comment, menu_id, total_order_id, store_id);

        return insert(stmt, orderDTO);
    }

    public List<OrderDTO> selectAllWithStore_id (Long store_id) {
        String stmt = "selectAllWithStore_id";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStore_id(store_id);

        return selectList(stmt, orderDTO);
    }

    public List<OrderDTO> selectAllWithTotal_Order_id (Long total_order_id) {
        String stmt = "selectAllWithTotal_Order_id";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotal_order_id(total_order_id);

        return selectList(stmt, orderDTO);
    }

    public int updateStatus(OrderStatus status, Long store_id) {
        String stmt = "updateStatus";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(status.getCode());
        orderDTO.setStore_id(store_id);

        return update(stmt, orderDTO);
    }
}