package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import persistence.MyBatisConnectionFactory;
import persistence.dto.OptionDTO;

public class OptionDAO extends DAO<OptionDTO>{
    private static OptionDAO optionDAO;
    static {
        if (optionDAO == null) {
            optionDAO = new OptionDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        }
    }
    public static OptionDAO getOptionDAO() { return optionDAO; }


    private OptionDAO(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, "mapper.OptionMapper.");
    }

    @Override
    protected int insert(SqlSession session, Object[] arg) {
        return session.insert(sqlMapperPath + arg[0], arg[1]);
    }

    public int insertOption(String name, Integer price, Long store_id) {
        String stmt = "insertOption";
        OptionDTO optionDTO = new OptionDTO(null, name, price, store_id);

        return insert(stmt, optionDTO);
    }
}