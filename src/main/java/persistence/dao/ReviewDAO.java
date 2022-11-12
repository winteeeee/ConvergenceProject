package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.ReviewDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDAO extends DAO<ReviewDTO> {
    private static ReviewDAO reviewDAO;
    static {
        if (reviewDAO == null) {
            reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static ReviewDAO getReviewDAO() { return reviewDAO; }

    public ReviewDAO(SqlSessionFactory sqlSessionFactory){
        super(sqlSessionFactory, "mapper.ReviewMapper.");
    }

    @Override
    protected List<ReviewDTO> selectList(SqlSession session, Object[] arg) {
        return session.selectList(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected ReviewDTO selectOne(SqlSession session, Object[] arg) { return null; }
    @Override
    protected int insert(SqlSession session, Object[] arg) {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }
    @Override
    protected int update(SqlSession session, Object[] arg) { return 0; }
    @Override
    protected int delete(SqlSession session, Object[] arg) { return 0; }

    public int insertReview(String contents, LocalDateTime regdate, Integer star_rating, Long user_pk, Long order_id) {
        String stmt = "insertReview";
        ReviewDTO reviewDTO = new ReviewDTO(null, contents, regdate, star_rating, user_pk, order_id);

        return insert(stmt, reviewDTO);
    }

    public List<ReviewDTO> selectAllWithUser_pk(Long user_pk) {
        String stmt = "selectAllWithUser_pk";
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUser_pk(user_pk);

        return selectList(stmt, reviewDTO);
    }
}