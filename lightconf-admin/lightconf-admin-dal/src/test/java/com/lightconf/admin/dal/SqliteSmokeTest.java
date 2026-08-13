package com.lightconf.admin.dal;

import com.lightconf.admin.dal.dao.AppMapper;
import com.lightconf.admin.model.dataobj.AppWithBLOBs;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 单测基建冒烟测试：验证 MyBatis mapper XML 可跑在内存 SQLite 上。
 *
 * <p>纯 MyBatis（不启动 Spring），单连接 DataSource 保持内存库生命周期；
 * 数据插入走原生 SQL 并显式指定 id，绕开 AppMapper.xml 中 MySQL 特有的
 * LAST_INSERT_ID()。</p>
 */
public class SqliteSmokeTest {

    private static SqlSession session;

    @BeforeClass
    public static void setUp() throws Exception {
        // 1. 单连接 DataSource：所有 getConnection 返回同一连接，内存库不丢失
        DataSource dataSource = new SingleConnectionDataSource();

        // 2. 执行 SQLite 测试 schema 建表
        //    逐行过滤注释后按分号拆分执行；Connection 保持打开（内存库生命周期）
        String schema = readResource("schema-sqlite.sql");
        Connection conn = dataSource.getConnection();
        try (Statement stmt = conn.createStatement()) {
            StringBuilder ddl = new StringBuilder();
            for (String line : schema.split("\n")) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith("--")) {
                    ddl.append(line).append('\n');
                }
            }
            for (String sql : ddl.toString().split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }

        // 3. 编程式构建 MyBatis 工厂，加载生产 mapper XML（mappings/AppMapper.xml）
        Environment env = new Environment("test", new JdbcTransactionFactory(), dataSource);
        Configuration config = new Configuration(env);
        try (InputStream is = Resources.getResourceAsStream("mappings/AppMapper.xml")) {
            XMLMapperBuilder builder =
                    new XMLMapperBuilder(is, config, "mappings/AppMapper.xml", config.getSqlFragments());
            builder.parse();
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
        session = factory.openSession();
    }

    @AfterClass
    public static void tearDown() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    public void appMapperSelectByPrimaryKeyWorksOnSqlite() throws Exception {
        // 直接 SQL 插入（显式 id=1），绕开 MySQL 特有 LAST_INSERT_ID()
        try (Statement stmt = session.getConnection().createStatement()) {
            stmt.execute("INSERT INTO light_conf_app "
                    + "(id, uuid, app_name, app_desc, is_connected, is_change, is_push_conf) "
                    + "VALUES (1, 'uuid-smoke', 'demo-app', 'smoke', 0, 0, 0)");
        }

        AppMapper mapper = session.getMapper(AppMapper.class);
        AppWithBLOBs app = mapper.selectByPrimaryKey(1);

        assertNotNull("SQLite 上应能查回 App", app);
        assertEquals(Integer.valueOf(1), app.getId());
        assertEquals("uuid-smoke", app.getUuid());
        assertEquals("demo-app", app.getAppName());
    }

    private static String readResource(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[4096];
        int n;
        try (Reader reader = new InputStreamReader(Resources.getResourceAsStream(path), StandardCharsets.UTF_8)) {
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
        }
        return sb.toString();
    }

    /** 单连接 DataSource：缓存同一 Connection，保证 jdbc:sqlite::memory: 内存库全程共享。 */
    private static final class SingleConnectionDataSource implements DataSource {

        private Connection connection;

        private synchronized Connection connection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            }
            return connection;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return connection();
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return connection();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            if (iface.isInstance(this)) {
                return iface.cast(this);
            }
            throw new SQLException("SqliteSmokeTest.SingleConnectionDataSource 不支持 unwrap 到 " + iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) {
            return iface.isInstance(this);
        }

        @Override
        public PrintWriter getLogWriter() {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) {
        }

        @Override
        public void setLoginTimeout(int seconds) {
        }

        @Override
        public int getLoginTimeout() {
            return 0;
        }

        @Override
        public Logger getParentLogger() {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }
}
