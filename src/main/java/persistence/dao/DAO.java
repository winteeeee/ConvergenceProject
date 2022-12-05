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

    protected List<T> selectList(Executable<List<T>> exec) {
        List<T> dtos = new ArrayList<>();
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dtos = exec.run(session);
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

    protected T selectOne(Executable<T> exec) {
        T dto = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            dto = exec.run(session);
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

    protected int insert(Executable<Integer> exec) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = exec.run(session);
            session.commit();
        }
        catch (Exception e) {
            return 0;
        }
        finally {
            session.close();
        }
        return sign;
    }

    protected int update(Executable<Integer> exec) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = exec.run(session);
            session.commit();
        }
        catch (Exception e) {
            return 0;
        }
        finally {
            session.close();
        }
        return sign;
    }

    protected int delete(Executable<Integer> exec) {
        int sign = 0;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            sign = exec.run(session);
            session.commit();
        }
        catch (Exception e) {
            return 0;
        }
        finally {
            session.close();
        }
        return sign;
    }
}
