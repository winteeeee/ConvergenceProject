/*
package persistence.dao;

import org.apache.ibatis.session.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAO {
    private final SqlSessionFactory sqlSessionFactory;

    public MyBoardDAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<DTO> selectAll() {
        List<DTO> dtos = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dtos = session.selectList("mapper.BoardMapper.selectAll");
        }
        finally {
            session.close();
        }
        return dtos;
    }

    public List<DTO> findPostWithTitleLike(String title) {
        List<DTO> dtos = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dtos = session.selectList("mapper.BoardMapper.findPostWithTitleLike", title);
        }
        finally {
            session.close();
        }
        return dtos;
    }

    public List<DTO> findPostWithTitleNameLike(Map<String, Object> params) {
        List<DTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.BoardMapper.findPostWithTitleNameLike", params);
        } finally {
            session.close();
        }
        return list;
    }

    public List<DTO> findPostWithTitleNameLike3(DTO dto){
        List<DTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            list = session.selectList("mapper.BoardMapper.selectWithTrim", dto);
        } finally {
            session.close();
        }
        return list;
    }
}
*/
