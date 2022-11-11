package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;


public abstract class DAO<T> {
    private final SqlSessionFactory sqlSessionFactory;

    public DAO(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<T> selectList(Object... arg) {
        List<T> dtos = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dtos = selectList(session, arg);
        } finally {
            session.close();
        }
        return dtos;
    }

    public T selectOne(Object... arg) {
        T dto = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dto = selectOne(session, arg);
        } finally {
            session.close();
        }
        return dto;
    }

    public int insert(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = insert(session, arg);
        } finally {
            session.close();
        }
        return sign;
    }

    public int update(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = update(session, arg);
        } finally {
            session.close();
        }
        return sign;
    }

    public int delete(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = delete(session, arg);
        } finally {
            session.close();
        }
        return sign;
    }


    protected abstract List<T> selectList(SqlSession session, Object[] arg);
    protected abstract T selectOne(SqlSession session, Object[] arg);
    protected abstract int insert(SqlSession session, Object[] arg);
    protected abstract int update(SqlSession session, Object[] arg);
    protected abstract int delete(SqlSession session, Object[] arg);
}
