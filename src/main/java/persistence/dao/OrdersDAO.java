package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.MenuDTO;
import persistence.dto.OrdersDTO;
import java.util.List;

public class OrdersDAO extends DAO<OrdersDTO> {
    public OrdersDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.OrdersMapper.");
    }


    public int insertOrders(OrdersDTO orders) {
        String stmt = sqlMapperPath + "insertOrders";
        OrdersDTO dto = OrdersDTO.builder()
                .details(orders.getDetails())
                .price(orders.getPrice())
                .menu_id(orders.getMenu_id())
                .total_orders_id(orders.getTotal_orders_id()).build();

        return insert((SqlSession session) -> {
                return session.insert(stmt, dto);
        });
    }

    public List<OrdersDTO> selectAllWithTotal_orders_id (Long total_orders_id) {
        String stmt = sqlMapperPath + "selectAllWithTotalOrdersId";
        OrdersDTO dto = OrdersDTO.builder()
                .total_orders_id(total_orders_id).build();

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, dto);
        });
    }
}