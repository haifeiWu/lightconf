# 单测基建 + 单测 GitHub Action 实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 搭建脱离 MySQL/Redis 外部服务的单测基建（SQLite 替代 DB、InMemorySessionStore 替代 Redis），用冒烟测试自证 MyBatis+SQLite 链路，新增独立单测 GitHub Actions 工作流，并更新 README 4 处失效/过时内容。

**Architecture:** 在 lightconf-admin-dal 模块 test 目录新增 SQLite 版 schema（6 张表，由 MySQL DDL 转换）与冒烟测试 `SqliteSmokeTest`（纯 MyBatis + 内存 SQLite 单连接 DataSource，不启动 Spring）。新增 `.github/workflows/unit-test.yml` 独立跑 `mvn -B test`。README 清理失效外链、替换徽章、更新环境要求。不修改任何生产代码与现有 mapper XML。

**Tech Stack:** Java 8 / Maven 3.9 / JUnit 4.12 / MyBatis 3.4.5 / sqlite-jdbc 3.53.2.1 / GitHub Actions

## Global Constraints

- 不修改生产代码、现有 mapper XML、`.github/workflows/ci.yml`（构建 CI 保持不变）
- 测试代码兼容 JDK 8（`java.version=1.8`）：禁用 `var`、`List.of`、`String.isBlank` 等 JDK9+ API
- 测试框架保持 JUnit 4（`org.junit.Test` + `org.junit.Assert`），junit 版本走已有 `junit.version=4.12` 属性
- sqlite-jdbc 版本固定 `3.53.2.1`（Maven Central 最新稳定版，2025 年发布）
- MyBatis 3.4.5 / Spring Boot 2.7.18 / MyBatis Generator 产物不动
- commit message 遵循仓库现有风格（`feat:`/`docs:`/`chore:` 前缀 + 中文说明）
- 仓库根目录：`/Users/chenzhiyun/work/opensource/lightconf`

---

### Task 1: SQLite 测试 schema 与 dal 测试依赖

**Files:**
- Create: `lightconf-admin/lightconf-admin-dal/src/test/resources/schema-sqlite.sql`
- Modify: `lightconf-admin/lightconf-admin-dal/pom.xml`（dependencies 新增 sqlite-jdbc、junit）

**Interfaces:**
- Consumes: `doc/db/light-conf-0.1.1V.sql`（MySQL DDL 源）
- Produces: `schema-sqlite.sql`（Task 2 的 SqliteSmokeTest 在 `@BeforeClass` 中读取并逐条执行）

- [ ] **Step 1: 修改 dal 模块 pom，添加测试依赖**

在 `lightconf-admin/lightconf-admin-dal/pom.xml` 的 `<dependencies>` 内（`lightconf-admin-model` 依赖之后）新增：

```xml
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.53.2.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
```

- [ ] **Step 2: 创建 SQLite 测试 schema**

创建 `lightconf-admin/lightconf-admin-dal/src/test/resources/schema-sqlite.sql`，内容为 6 张表的 DDL（由 `doc/db/light-conf-0.1.1V.sql` 转换：去反引号/`CREATE DATABASE`、`AUTO_INCREMENT` 列 → `INTEGER PRIMARY KEY AUTOINCREMENT`、去 `ENGINE=... CHARSET=...` 与 `COMMENT '...'`、`UNIQUE KEY uk_x (cols)` → `UNIQUE (cols)`；**不保留源文件中的 seed INSERT 数据**，冒烟测试自行插入）：

```sql
-- SQLite 版测试 schema（由 doc/db/light-conf-0.1.1V.sql 转换，仅 DDL，无数据）
-- 转换规则：去反引号 / AUTO_INCREMENT -> INTEGER PRIMARY KEY AUTOINCREMENT / 去 ENGINE、COMMENT / UNIQUE KEY -> UNIQUE

CREATE TABLE light_conf_app (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  uuid TEXT,
  app_name TEXT,
  app_desc TEXT,
  private_key TEXT,
  public_key TEXT,
  is_connected INTEGER DEFAULT 0,
  is_change INTEGER DEFAULT 0,
  is_push_conf INTEGER DEFAULT 0
);

CREATE TABLE light_conf_app_conf (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  app_id TEXT,
  conf_id TEXT
);

CREATE TABLE light_conf_conf (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  app_id INTEGER,
  conf_key TEXT,
  conf_value TEXT,
  conf_desc TEXT,
  UNIQUE (app_id, conf_key)
);

CREATE TABLE light_conf_user (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_name TEXT NOT NULL,
  password TEXT NOT NULL,
  permission INTEGER NOT NULL DEFAULT 0,
  permission_projects TEXT,
  UNIQUE (user_name)
);

CREATE TABLE light_conf_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  conf_key TEXT NOT NULL,
  conf_desc TEXT NOT NULL,
  conf_value TEXT,
  opt_time TEXT NOT NULL,
  opt_user TEXT NOT NULL
);

CREATE TABLE light_conf_conf_log (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  log_id INTEGER,
  conf_id INTEGER
);
```

- [ ] **Step 3: 验证依赖解析与编译**

Run: `cd /Users/chenzhiyun/work/opensource/lightconf && mvn -q -pl lightconf-admin/lightconf-admin-dal -am compile`
Expected: BUILD SUCCESS（sqlite-jdbc 3.53.2.1 从 Maven Central 下载成功，dal 模块编译通过）

- [ ] **Step 4: Commit**

```bash
cd /Users/chenzhiyun/work/opensource/lightconf
git add lightconf-admin/lightconf-admin-dal/pom.xml lightconf-admin/lightconf-admin-dal/src/test/resources/schema-sqlite.sql
git commit -m "chore: 单测基建 — dal 模块添加 sqlite-jdbc 测试依赖与 SQLite 版测试 schema"
```

---

### Task 2: 冒烟测试 SqliteSmokeTest（自证 MyBatis + SQLite 链路）

**Files:**
- Create: `lightconf-admin/lightconf-admin-dal/src/test/java/com/lightconf/admin/dal/SqliteSmokeTest.java`
- Test: 同上（唯一测试类，含 1 个断言方法）

**Interfaces:**
- Consumes: `schema-sqlite.sql`（Task 1）、`mappings/AppMapper.xml` 与 `AppMapper.java`（生产 mapper）、`com.lightconf.admin.model.dataobj.AppWithBLOBs`（生产 model）
- Produces: 验证结论——SQLite 上 MyBatis mapper XML 查询链路可用，供后续 admin 测试照抄

- [ ] **Step 1: 编写冒烟测试（先写完整测试，此时预期运行失败）**

创建 `lightconf-admin/lightconf-admin-dal/src/test/java/com/lightconf/admin/dal/SqliteSmokeTest.java`：

```java
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
import java.io.PrintWriter;
import java.io.Reader;
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
        String schema = readResource("schema-sqlite.sql");
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : schema.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
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
        try (Reader reader = Resources.getResourceAsReader(path, "UTF-8")) {
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
```

- [ ] **Step 2: 运行测试验证失败**

Run: `cd /Users/chenzhiyun/work/opensource/lightconf && mvn -q -pl lightconf-admin/lightconf-admin-dal -am test -Dtest=SqliteSmokeTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: FAIL——`SingleConnectionDataSource` 是测试类自身方法，首个失败点应是 `Resources.getResourceAsStream("mappings/AppMapper.xml")` 报错或 mapper XML 解析异常（确认测试确实被执行且暴露了问题）。若测试意外通过，则说明实现已可用，跳到 Step 4。

> 注意：`-Dsurefire.failIfNoSpecifiedTests=false` 必不可少——`-am` 会连带构建 lightconf-admin-model 模块，该模块无匹配测试，surefire 2.22.2 默认会因 "No tests were executed" 报错。

> 说明：此任务测试与实现同文件编写（冒烟测试无独立"实现类"），TDD 的失败验证以"测试能被 surefire 发现并运行、且验证了 SQLite 链路"为准。

- [ ] **Step 3: 修正并运行至通过**

若 Step 2 暴露具体问题（如 mapper XML 缺失资源、SQLite 驱动未注册），逐一修复后重跑：

Run: `cd /Users/chenzhiyun/work/opensource/lightconf && mvn -q -pl lightconf-admin/lightconf-admin-dal -am test -Dtest=SqliteSmokeTest -Dsurefire.failIfNoSpecifiedTests=false`
Expected: PASS——Tests run: 1, Failures: 0, Errors: 0

- [ ] **Step 4: 全量测试回归**

Run: `cd /Users/chenzhiyun/work/opensource/lightconf && mvn -q test`
Expected: BUILD SUCCESS——现有 2 个纯单测（MessageCodecTest、LightConfClientTest）+ 新增冒烟测试全部通过

- [ ] **Step 5: Commit**

```bash
cd /Users/chenzhiyun/work/opensource/lightconf
git add lightconf-admin/lightconf-admin-dal/src/test/java/com/lightconf/admin/dal/SqliteSmokeTest.java
git commit -m "feat: 单测基建 — SqliteSmokeTest 冒烟测试验证 MyBatis+内存 SQLite 链路"
```

---

### Task 3: 单测 GitHub Actions 工作流

**Files:**
- Create: `.github/workflows/unit-test.yml`

**Interfaces:**
- Consumes: Task 2 验证过的 `mvn -B test`（全模块单测命令）
- Produces: CI 单测 job——JDK 11/17 矩阵跑 `mvn -B test`，失败上传 surefire 报告

- [ ] **Step 1: 创建工作流文件**

创建 `.github/workflows/unit-test.yml`：

```yaml
name: Unit Tests

on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java: [ 11, 17 ]
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK ${{ matrix.java }}
        uses: actions/setup-java@v4
        with:
          java-version: ${{ matrix.java }}
          distribution: temurin
          cache: maven

      - name: Run unit tests
        run: mvn -B test

      - name: Upload surefire reports on failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: surefire-reports-${{ matrix.java }}
          path: '**/target/surefire-reports/'
```

- [ ] **Step 2: 验证 YAML 语法**

Run: `python3 -c "import yaml,sys; yaml.safe_load(open('.github/workflows/unit-test.yml')); print('YAML OK')"`
Expected: `YAML OK`

- [ ] **Step 3: 本地全量测试确认 CI 命令可执行**

Run: `cd /Users/chenzhiyun/work/opensource/lightconf && mvn -B test -q`
Expected: BUILD SUCCESS（与 Task 2 Step 4 相同，确认 `-B` 参数下无交互挂起）

- [ ] **Step 4: Commit**

```bash
cd /Users/chenzhiyun/work/opensource/lightconf
git add .github/workflows/unit-test.yml
git commit -m "feat: 新增单测 GitHub Actions 工作流（unit-test.yml，JDK 11/17 矩阵）"
```

---

### Task 4: README 更新（4 处失效/过时内容）

**Files:**
- Modify: `README.md`

**Interfaces:**
- Consumes: `.github/workflows/ci.yml`（badge 指向现有构建工作流）
- Produces: 更新后的 README，无失效外链

- [ ] **Step 1: 替换 Travis CI 徽章为 GitHub Actions 徽章**

在 `README.md` 第 2-3 行，将：

```markdown
[![Build Status](https://travis-ci.org/haifeiWu/lightconf.svg?branch=master)](https://travis-ci.org/haifeiWu/lightconf)
```

替换为：

```markdown
[![CI](https://github.com/haifeiWu/lightconf/actions/workflows/ci.yml/badge.svg)](https://github.com/haifeiWu/lightconf/actions/workflows/ci.yml)
```

- [ ] **Step 2: 删除"项目在线预览地址"小节**

删除 README 中以下整段（含表头）：

```markdown
#### 项目在线预览地址
配置中心预览 | 接入LIGHTCONF的Demo项目预览
--- | ---
http://www.whforever.cn/lightconf-admin-web/ | http://www.whforever.cn/lightconf-sample/

```

（保留紧随其后的"源码仓库地址"小节）

- [ ] **Step 3: 删除两处失效截图引用**

删除以下两行（保留所在小节的其他文字）：

```markdown
![light-conf-app](http://img.hchstudio.cn/light-conf-app.png "light-conf-app")
```

和

```markdown
![light-conf-conf](http://img.hchstudio.cn/light-conf-conf.png "light-conf-conf")
```

- [ ] **Step 4: 更新环境要求**

在 `README.md` 的"### 1.5 环境"小节，将：

```markdown
- Maven3+
- Jdk8+（推荐 11/17）
- Tomcat7+
- Mysql5.5+
```

替换为：

```markdown
- Maven 3+
- JDK 8+（推荐 11/17）
- Spring Boot 2.7（内嵌 Tomcat，无需独立部署）
- MySQL 5.5+（推荐 8.0）
- Redis 7（可选，多实例水平扩展时用于会话存储）
```

- [ ] **Step 5: 验证 README 更新结果**

Run:
```bash
cd /Users/chenzhiyun/work/opensource/lightconf
grep -n 'travis\|whforever\|hchstudio\|Tomcat7\|Mysql5.5' README.md || echo "无失效内容残留"
grep -n 'actions/workflows/ci.yml' README.md
```
Expected: 第一条 grep 无输出（无残留），第二条输出 badge 行

- [ ] **Step 6: Commit**

```bash
cd /Users/chenzhiyun/work/opensource/lightconf
git add README.md
git commit -m "docs: 更新 README — 徽章换 GitHub Actions、移除失效预览地址与截图、修正环境要求"
```

---

## 最终验收

```bash
cd /Users/chenzhiyun/work/opensource/lightconf
mvn -B test -q                        # 全量单测通过（含冒烟测试）
python3 -c "import yaml; yaml.safe_load(open('.github/workflows/unit-test.yml'))"  # YAML 合法
git log --oneline -6                  # 4 个任务 commit 各就各位
```

预期输出：BUILD SUCCESS；YAML 无异常；log 显示 4 个新 commit（Task1~Task4）。

## 风险与回退

- **sqlite-jdbc 与 MyBatis 3.4.5 兼容性**：JDBC 标准接口，风险低；若 mapper XML 出现 SQLite 不支持的 SQL，冒烟测试会立即暴露（当前仅测 `selectByPrimaryKey`，为标准 SELECT）
- **CI 单测首次运行**：push 后观察 GitHub Actions 的 Unit Tests workflow，若失败上传 surefire-reports artifact 排查
- **回退**：4 个 commit 均为独立可回滚提交（`git revert` 或 `git reset` 单个 commit 即可），不涉及生产代码
