package moe.ahao.tree;

import com.mysql.cj.jdbc.Driver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

class TreeTest {
    @ComponentScan("moe.ahao.tree")
    @MapperScan("moe.ahao.tree")
    static class TestConfig {
        @Bean
        public DataSource dataSource() {
            // DriverManagerDataSource ds = new DriverManagerDataSource();
            // ds.setDriverClassName(org.h2.Driver.class.getName());
            // // ds.setPassword("123456");
            // // ds.setUsername("root");
            // ds.setUrl("jdbc:p6spy:h2:mem:db1;DB_CLOSE_DELAY=-1;MODE=MySQL;");

            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName(Driver.class.getName());
            ds.setPassword("root");
            ds.setUsername("root");
            ds.setUrl("jdbc:mysql://192.168.19.128:3306/ahaodb?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&tinyInt1isBit=false&rewriteBatchedStatements=true&useAffectedRows=true");
            return ds;
        }

        @Bean
        public SqlSessionFactory sqlSessionFactory() throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource());
            return factoryBean.getObject();
        }
    }

    interface TreeRepositoryImpl extends TreeRepository<TreeNode> {
    }

    static class TreeServiceImpl extends TreeService<TreeNode> {
        public TreeServiceImpl(TreeRepository<TreeNode> treeRepository) {
            super(treeRepository);
        }
    }

    TreeTest() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

        DataSource ds = context.getBean(DataSource.class);
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + TreeNode.TABLE_NAME);
            statement.executeUpdate("CREATE TABLE " + TreeNode.TABLE_NAME + "(" +
                "`id`        BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "`parent_id` BIGINT(20) NOT NULL," +
                "`left_val`  BIGINT(20) NOT NULL," +
                "`right_val` BIGINT(20) NOT NULL," +
                "`level` INT NOT NULL" +
                ");");
            statement.executeUpdate("insert into " + TreeNode.TABLE_NAME + "(id, parent_id, left_val, right_val, level) values(1, 0, 1, 2, 1)");
        }

        TreeRepositoryImpl mapper = context.getBean(TreeRepositoryImpl.class);
        service = new TreeServiceImpl(mapper);
    }

    private TreeServiceImpl service;

    @BeforeEach
    void beforeEach() {
        service.save(1, new TreeNode()); // id:2
        service.save(1, new TreeNode()); // id:3
        service.save(2, new TreeNode()); // id:4
        service.save(3, new TreeNode()); // id:5
        service.save(5, new TreeNode()); // id:6
        service.save(2, new TreeNode()); // id:7
        //              (1)1(14)
        //      (2)2(7)                (8)3(13)
        // (3)4(4)   (5)7(6)           (9)5(12)
        //                             (10)6(11)
    }

    @Test
    void testChildren() {
        List<TreeNode> children = service.getChildren(1);
        int[][] expect = {{2, 7}, {8, 13}};
        Assertions.assertEquals(expect.length, children.size());
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(1, children.get(i).getParentId());
            Assertions.assertEquals(expect[i][0], children.get(i).getLeft());
            Assertions.assertEquals(expect[i][1], children.get(i).getRight());
        }

        this.defaultAssertTree();
    }

    @Test
    void testDescendant() {
        List<TreeNode> descendant = service.getDescendant(1);
        int[][] expect = {{1, 2, 7}, {2, 3, 4}, {2, 5, 6}, {1, 8, 13}, {3, 9, 12}, {5, 10, 11}};
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.defaultAssertTree();
    }

    @Test
    void testDescendantCount() {
        long descendantCount = service.getDescendantCount(1);
        Assertions.assertEquals(6, descendantCount);

        this.defaultAssertTree();
    }

    @Test
    void testAncestorLink() {
        List<TreeNode> ancestorLink = service.getAncestorLink(6);
        int[][] expect = {{0, 1, 14}, {1, 8, 13}, {3, 9, 12}};
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], ancestorLink.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], ancestorLink.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], ancestorLink.get(i).getRight());
        }

        this.defaultAssertTree();
    }

    @Test
    void testIsLeaf() {
        Assertions.assertFalse(service.isLeaf(1));
        Assertions.assertFalse(service.isLeaf(2));
        Assertions.assertFalse(service.isLeaf(3));
        Assertions.assertTrue(service.isLeaf(4));
        Assertions.assertFalse(service.isLeaf(5));
        Assertions.assertTrue(service.isLeaf(6));
        Assertions.assertTrue(service.isLeaf(7));

        this.defaultAssertTree();
    }

    @Test
    void delete2() {
        service.delete(2);
        int[][] expect = {{1, 2, 7}, {3, 3, 6}, {5, 4, 5}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void delete3() {
        service.delete(3);
        int[][] expect = {{1, 2, 7}, {2, 3, 4}, {2, 5, 6}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void delete4() {
        service.delete(4);
        int[][] expect = {{1, 2, 5}, {2, 3, 4}, {1, 6, 11}, {3, 7, 10}, {5, 8, 9}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void delete5() {
        service.delete(5);
        int[][] expect = {{1, 2, 7}, {2, 3, 4}, {2, 5, 6}, {1, 8, 9}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void delete6() {
        service.delete(6);
        int[][] expect = {{1, 2, 7}, {2, 3, 4}, {2, 5, 6}, {1, 8, 11}, {3, 9, 10}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void delete7() {
        service.delete(7);
        int[][] expect = {{1, 2, 5}, {2, 3, 4}, {1, 6, 11}, {3, 7, 10}, {5, 8, 9}};

        List<TreeNode> descendant = service.getDescendant(1);
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], descendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], descendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], descendant.get(i).getRight());
        }

        this.assertTree(expect);
    }

    @Test
    void deleteAll() {
        List<TreeNode> descendant = service.getDescendant(1);
        int[][][] expect = {
            {{1, 2, 7}, {3, 3, 6}, {5, 4, 5}}, // (1)1(8) (2)3(7) (3)5(6) (4)6(5)
            {{1, 2, 7}, {3, 3, 6}, {5, 4, 5}}, // (1)1(8) (2)3(7) (3)5(6) (4)6(5)
            {{1, 2, 7}, {3, 3, 6}, {5, 4, 5}}, // (1)1(8) (2)3(7) (3)5(6) (4)6(5)
            {}, // (1)1(8)
            {}, // (1)1(8)
            {}, // (1)1(8)
        };

        for (int i = 0; i < descendant.size(); i++) {
            service.delete(descendant.get(i).getId());

            this.assertTree(expect[i]);
        }
    }


    void defaultAssertTree() {
        int[][] except = {{1, 2, 7}, {2, 3, 4}, {2, 5, 6}, {1, 8, 13}, {3, 9, 12}, {5, 10, 11}};
        assertTree(except);
    }

    void assertTree(int[][] expect) {
        List<TreeNode> afterDeleteDescendant = service.getDescendant(1);
        if (afterDeleteDescendant.size() <= 0) {
            return;
        }
        for (int i = 0; i < expect.length; i++) {
            Assertions.assertEquals(expect[i][0], afterDeleteDescendant.get(i).getParentId());
            Assertions.assertEquals(expect[i][1], afterDeleteDescendant.get(i).getLeft());
            Assertions.assertEquals(expect[i][2], afterDeleteDescendant.get(i).getRight());
        }
    }

}
