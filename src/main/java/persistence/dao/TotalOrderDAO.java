package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.TotalOrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public class TotalOrderDAO extends DAO<TotalOrderDTO>{
    private static TotalOrderDAO totalOrderDAO;
    static {
        if (totalOrderDAO == null) {
            totalOrderDAO = new TotalOrderDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static TotalOrderDAO getTotalOrderDAO() { return totalOrderDAO; }

    private TotalOrderDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.TotalOrderMapper.");
    }

    @Override
    protected List<TotalOrderDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected TotalOrderDTO selectOne(SqlSession session, Object[] arg) {
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

    public int insertTotalOrder(LocalDateTime regdate, String address, Integer total_price, Long user_pk) {
        String stmt = "insertTotalOrder";
        TotalOrderDTO totalOrderDTO = new TotalOrderDTO(null, regdate, address, total_price, user_pk);

        return insert(stmt, totalOrderDTO);
    }

    public List<TotalOrderDTO> selectAllWithUser_pk(Long user_pk) {
        String stmt = "selectAllWithUser_pk";
        TotalOrderDTO totalOrderDTO = new TotalOrderDTO();
        totalOrderDTO.setUser_pk(user_pk);

        return selectList(stmt, totalOrderDTO);
    }
}