package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.ClassificationDTO;

import java.util.List;

public class ClassificationDAO extends DAO<ClassificationDTO>{
    public ClassificationDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.ClassificationMapper.");
    }


    public List<ClassificationDTO> selectAllWithStore_id(Long store_id) {
        String stmt = sqlMapperPath + "selectAllWithStore_id";
        ClassificationDTO dto = ClassificationDTO.builder()
                .store_id(store_id).build();

        return selectList((SqlSession session) -> {
                return session.selectList(stmt, dto);
            });
    }

    public int insertClassification(ClassificationDTO Classification) {
        String stmt = sqlMapperPath + "insertClassification";
        ClassificationDTO dto = ClassificationDTO.builder()
                .name(Classification.getName())
                .store_id(Classification.getStore_id()).build();

        return insert((SqlSession session) -> {
                return session.insert(stmt, dto);
            });
    }

    public ClassificationDTO selectOneWithId(Long id) {
        String stmt = sqlMapperPath + "selectOneWithId";
        ClassificationDTO dto = ClassificationDTO.builder()
                .id(id).build();

        return selectOne((SqlSession session) -> {
                return session.selectOne(stmt, dto);
            });
    }
}
