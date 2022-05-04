package moe.ahao.transaction;

import com.mysql.cj.jdbc.Driver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DBConstant {
    public static final String MYSQL_URL = "jdbc:mysql://192.168.153.134:3306/";
    public static final String MYSQL_PARAM = "?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&tinyInt1isBit=false&rewriteBatchedStatements=true&useAffectedRows=true";
    public static final String MYSQL_ROOT = "root";
    public static final String MYSQL_PW = "root";

    public static DataSource createH2DataSource() {
        // TODO 如何清理H2Database
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(org.h2.Driver.class.getName());
        // ds.setPassword("123456");
        // ds.setUsername("root");
        ds.setUrl("jdbc:p6spy:h2:mem:db1;DB_CLOSE_DELAY=-1;MODE=MySQL;");

        return ds;
    }

    public static DataSource createMysqlDataSource(String database) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(Driver .class.getName());
        ds.setPassword(MYSQL_PW);
        ds.setUsername(MYSQL_ROOT);
        ds.setUrl(MYSQL_URL + database + MYSQL_PARAM);
        return ds;
    }
}
