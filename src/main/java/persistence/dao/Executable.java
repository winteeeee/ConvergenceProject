package persistence.dao;

import org.apache.ibatis.session.SqlSession;

public interface Executable<T> {
    T run(SqlSession session) throws Exception;
}
