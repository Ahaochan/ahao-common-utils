package moe.ahao.transaction;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractUserTest {
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void beforeEach() throws Exception {
        String sql = FileUtils.readFileToString(new ClassPathResource("sql/user.sql").getFile(), StandardCharsets.UTF_8);
        try (Connection conn = this.getBeforeEachConnection();
             Statement s = conn.createStatement();) {
            s.execute(sql);
        }
    }

    protected Connection getBeforeEachConnection() throws SQLException {
        if(dataSource != null) {
            return dataSource.getConnection();
        } else {
            return DriverManager.getConnection(DBTestUtils.getMysqlJdbcUrl("ahaodb"), "root", "root");
        }
    }
}
