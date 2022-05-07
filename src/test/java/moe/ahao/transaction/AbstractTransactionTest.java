package moe.ahao.transaction;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.sql.*;

import static moe.ahao.transaction.bank.transfer.service.BankTransferAccountMybatisService.SELECT_SQL;

public abstract class AbstractTransactionTest {
    @BeforeEach
    public void beforeEach() throws Exception {
        String sql = FileUtils.readFileToString(new ClassPathResource("sql/bank_transfer.sql").getFile(), StandardCharsets.UTF_8);
        try (Connection conn = this.getBeforeEachConnection();
             Statement s = conn.createStatement();) {
            s.execute(sql);
        }
    }
    protected Connection getBeforeEachConnection() throws SQLException {
        return DriverManager.getConnection(DBTestUtils.getMysqlJdbcUrl("ahaodb"), "root", "root");
    }

    @Test
    void commit() throws Exception {
        try {
            this.doCommit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.assertCommit();
    }
    protected void doCommit() throws Exception {
        this.execute(false);
    }

    @Test
    void rollback() throws Exception {
        try {
            this.doRollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.assertRollback();
    }
    protected void doRollback() throws Exception {
        this.execute(true);
    }
    protected void execute(boolean rollback) throws Exception {
        Assertions.fail("空实现");
    }

    void assertCommit() throws Exception {
        try (Connection conn = DriverManager.getConnection(DBTestUtils.getMysqlJdbcUrl("ahaodb"), "root", "root");
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(SELECT_SQL);) {

            rs.next();
            Assertions.assertEquals("1", rs.getString("account_id"));
            Assertions.assertEquals(0, rs.getInt("amount"));
            rs.next();
            Assertions.assertEquals("2", rs.getString("account_id"));
            Assertions.assertEquals(200, rs.getInt("amount"));
        }
    }

    void assertRollback() throws Exception {
        try (Connection conn = DriverManager.getConnection(DBTestUtils.getMysqlJdbcUrl("ahaodb"), "root", "root");
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(SELECT_SQL);) {

            rs.next();
            Assertions.assertEquals("1", rs.getString("account_id"));
            Assertions.assertEquals(100, rs.getInt("amount"));
            rs.next();
            Assertions.assertEquals("2", rs.getString("account_id"));
            Assertions.assertEquals(100, rs.getInt("amount"));
        }
    }
}
