package moe.ahao.transaction;

import com.mysql.cj.jdbc.Driver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTestUtils {
    public static final String MYSQL_HOST = "192.168.19.128:3306";
    public static final String MYSQL_PARAM = "?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&tinyInt1isBit=false&rewriteBatchedStatements=true&useAffectedRows=true";
    public static final String MYSQL_ROOT = "root";
    public static final String MYSQL_PW = "root";

    public static DataSource createH2DataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(org.h2.Driver.class.getName());
        ds.setPassword("root");
        ds.setUsername("root");
        ds.setUrl("jdbc:h2:mem:ahaodb;DB_CLOSE_DELAY=-1;MODE=MySQL;");

        return ds;
    }

    public static void clearDB(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet tables = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});//获取表名
            while (tables.next()) {
                statement.executeUpdate("DELETE FROM " + tables.getObject("TABLE_NAME"));
            }
        }
    }

    public static DataSource createMysqlDataSource(String database) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(Driver.class.getName());
        ds.setPassword(MYSQL_PW);
        ds.setUsername(MYSQL_ROOT);
        ds.setUrl(DBTestUtils.getMysqlJdbcUrl(database));
        return ds;
    }

    public static String getMysqlJdbcUrl(String database) {
        String jdbcUrl = "jdbc:mysql://" + MYSQL_HOST + "/" + (database != null ? database : "") + MYSQL_PARAM;
        return jdbcUrl;
    }
}
