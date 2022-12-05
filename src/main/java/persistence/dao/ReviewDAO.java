package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.dto.ReviewDTO;
import persistence.dto.StatisticsDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewDAO extends DAO<ReviewDTO> {
    public ReviewDAO(SqlSessionFactory sqlSessionFactory){
        super(sqlSessionFactory, "mapper.ReviewMapper.");
    }


    public int insertReview(ReviewDTO review) {
        String stmt = sqlMapperPath + "insertReview";
        ReviewDTO dto = ReviewDTO.builder()
                .comment(review.getComment())
                .regdate(LocalDateTime.now())
                .star_rating(review.getStar_rating())
                .user_pk(review.getUser_pk())
                .total_orders_id(review.getTotal_orders_id()).build();

        return insert((SqlSession session) -> {
            return session.insert(stmt, dto);
        });
    }

    public int updateOwnerComment(Long id, String owner_comment) {
        String stmt = sqlMapperPath + "updateOwnerComment";
        ReviewDTO dto = ReviewDTO.builder()
                .id(id)
                .owner_comment(owner_comment).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public int updateForInsert(Integer star_rating, Long total_orders_id) {
        String stmt = sqlMapperPath + "updateForInsert";
        ReviewDTO dto = ReviewDTO.builder()
                .star_rating(star_rating)
                .total_orders_id(total_orders_id).build();

        return update((SqlSession session) -> {
            return session.update(stmt, dto);
        });
    }

    public List<ReviewDTO> selectAllWithStoreId(Long store_id, Integer page) {
        String stmt = sqlMapperPath + "selectAllWithStoreId";

        page = (page - 1) * 2; // <- (page - 1) * pageSize
        Map<String, Object> map = new HashMap();
        map.put("store_id", store_id);
        map.put("page", page);

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, map);
        });
    }

    public List<ReviewDTO> selectAll(Long store_id) {
        String stmt = sqlMapperPath + "selectAll";

        Map<String, Object> map = new HashMap();
        map.put("store_id", store_id);

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, map);
        });
    }

    public List<ReviewDTO> selectAllWithUserPk(Long user_pk, Integer page) {  // TODO 페이지 사이즈도 인자로 받도록 만드는게 좋을 것 같음
        String stmt = sqlMapperPath + "selectAllWithUserPk";

        page = (page - 1) * 2; // <- (page - 1) * pageSize
        Map<String, Object> map = new HashMap();
        map.put("user_pk", user_pk);
        map.put("page", page);

        return selectList((SqlSession session) -> {
            return session.selectList(stmt, map);
        });
    }

    public ReviewDTO selectOneWithTotalOrdersId(Long total_orders_id) {
        String stmt = sqlMapperPath + "selectOneWithTotalOrdersId";
        ReviewDTO dto = ReviewDTO.builder()
                .user_pk(0l)
                .total_orders_id(total_orders_id).build();

        return selectOne((SqlSession session) -> {
            ReviewDTO temp = session.selectOne(stmt, dto);
            return (temp != null) ? temp : dto;
        });
    }
}