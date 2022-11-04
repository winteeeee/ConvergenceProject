package persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PooledDataSource { // TODO 강의자료 소스임 삭제 및 수정 예정
    private static BasicDataSource basicDS;

    static {
        try {
            basicDS = new BasicDataSource();
            Properties properties = new Properties();

            //InputStream inputStream = new FileInputStream("src/main/resources/config/db.yml");
            Class<PooledDataSource> pooledDataSourceClass = PooledDataSource.class;
            InputStream inputStream = pooledDataSourceClass.getClassLoader().getResourceAsStream("config/db.yml"); //class path 최상위에서 부터 탐색
            properties.load(inputStream);

            basicDS.setDriverClassName(properties.getProperty("DRIVER_CLASS")); //loads the jdbc driver
            basicDS.setUrl(properties.getProperty("DB_CONNECTION_URL"));
            basicDS.setUsername(properties.getProperty("DB_USER"));
            basicDS.setPassword(properties.getProperty("DB_PWD"));
            // Parameters for connection pooling
            basicDS.setInitialSize(10);
            basicDS.setMaxTotal(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return basicDS;
    }
}