package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class DAO<T> {
    private final SqlSessionFactory sqlSessionFactory;
    protected final String sqlMapperPath;

    public DAO(SqlSessionFactory sqlSessionFactory, String sqlMapperPath) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlMapperPath = sqlMapperPath;
    }

    protected abstract List<T> selectList(SqlSession session, Object[] arg) throws Exception;
    protected abstract T selectOne(SqlSession session, Object[] arg) throws Exception;
    protected abstract int insert(SqlSession session, Object[] arg) throws Exception;
    protected abstract int update(SqlSession session, Object[] arg) throws Exception;
    protected abstract int delete(SqlSession session, Object[] arg) throws Exception;

    protected List<T> selectList(Object... arg) {
        List<T> dtos = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dtos = selectList(session, arg);
            session.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return dtos;
    }

    protected T selectOne(Object... arg) {
        T dto = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dto = selectOne(session, arg);
            session.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return dto;
    }

    protected int insert(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = insert(session, arg);
            session.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return sign;
    }

    protected int update(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = update(session, arg);
            session.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return sign;
    }

    protected int delete(Object... arg) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = delete(session, arg);
            session.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return sign;
    }
}
