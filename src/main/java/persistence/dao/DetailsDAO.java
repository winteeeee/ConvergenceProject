package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.DetailsDTO;

import java.util.List;

public class DetailsDAO extends DAO<DetailsDTO>{
    private static DetailsDAO detailsDAO;
    static {
        if (detailsDAO == null) {
            detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static DetailsDAO getDetailsDAO() { return detailsDAO; }


    private DetailsDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.DetailsMapper.");
    }

    @Override
    protected List<DetailsDTO> selectList(SqlSession session, Object[] arg) { return null; }
    @Override
    protected DetailsDTO selectOne(SqlSession session, Object[] arg) { return null; }
    @Override
    protected int insert(SqlSession session, Object[] arg) {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) { return 0; }
    @Override
    protected int delete(SqlSession session, Object[] arg) { return 0; }

    public int insertDetails(String name, Integer price, Long store_id) {
        String stmt = "insertDetails";
        DetailsDTO detailsDTO = new DetailsDTO(null, name, price, store_id);

        return insert(stmt, detailsDTO);
    }
}