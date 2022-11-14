package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.TotalOrdersDTO;

import java.time.LocalDateTime;
import java.util.List;

public class TotalOrdersDAO extends DAO<TotalOrdersDTO>{
    private static TotalOrdersDAO totalOrdersDAO;
    static {
        if (totalOrdersDAO == null) {
            totalOrdersDAO = new TotalOrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static TotalOrdersDAO getTotalOrdersDAO() { return totalOrdersDAO; }

    private TotalOrdersDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.TotalOrdersMapper.");
    }

    @Override
    protected List<TotalOrdersDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected TotalOrdersDTO selectOne(SqlSession session, Object[] arg) {
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

    public int insertTotalOrders(LocalDateTime regdate, String address, Integer total_price, Long user_pk) {
        String stmt = "insertTotalOrders";
        TotalOrdersDTO totalOrdersDTO = new TotalOrdersDTO(null, regdate, address, total_price, user_pk);

        return insert(stmt, totalOrdersDTO);
    }

    public List<TotalOrdersDTO> selectAllWithUser_pk(Long user_pk) {
        String stmt = "selectAllWithUser_pk";
        TotalOrdersDTO totalOrdersDTO = new TotalOrdersDTO();
        totalOrdersDTO.setUser_pk(user_pk);

        return selectList(stmt, totalOrdersDTO);
    }
}