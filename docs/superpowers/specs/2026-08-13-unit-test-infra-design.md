# 单测基建 + 单测 GitHub Action 设计

日期：2026-08-13
状态：已获用户批准

## 背景与目标

项目当前仅有 2 个纯单测（`MessageCodecTest`、`LightConfClientTest`），均不依赖外部服务。admin 模块（web/service/dal）零测试。CI（`.github/workflows/ci.yml`）只做 `mvn clean package` 构建，没有独立、明确呈现的单测环节。

目标：
1. 搭建可脱离 MySQL/Redis 外部服务的单测基建：
   - 依赖 DB 的测试用 **SQLite**（内存库）替代 MySQL；
   - 依赖 Redis 的测试用 **`InMemorySessionStore`**（本地内存实现）替代，无需改动（项目默认即内存实现）。
2. 用 1 个冒烟测试自证基建可用（MyBatis mapper XML + SQLite 链路）。
3. 新增独立 GitHub Actions 工作流跑单测（`mvn test`）。
4. 同步更新 README 中 4 处失效/过时内容。

本次只搭基建，不扩展 admin 各层测试范围（用户选定方案 B2）。

## 现状分析

| 项 | 现状 |
|---|---|
| 测试 | core/common 各 1 个纯单测（JUnit 4.12 + surefire），admin 零测试 |
| DB | MyBatis 3.4.5 + mybatis-spring-boot-starter 1.3.2 + MySQL（connector 5.1.45），由 lightconf-admin 父 pom 统一引入；mapper XML 位于 lightconf-admin-dal，无 MySQL 特有语法（无 `\``、LIMIT、ON DUPLICATE 等），SQLite 兼容性好 |
| Redis | 仅 `SessionStore` 抽象，已有 `InMemorySessionStore`（默认）与 `RedisSessionStore`（配置启用）两个实现 |
| Schema | `doc/db/light-conf-0.1.1V.sql`：6 张表，仅用反引号、`AUTO_INCREMENT`、`ENGINE=InnoDB ... CHARSET=utf8`、`COMMENT '...'`、`UNIQUE KEY` 等易转换语法 |
| CI | `.github/workflows/ci.yml`：JDK 11/17 矩阵，`mvn -B clean package`（GitHub Actions，workflow 名 CI） |
| 预览/外链 | README 中 www.whforever.cn 预览地址 503 失效、travis-ci.org badge 停服 301、img.hchstudio.cn 截图 503 失效 |

## 设计

### 1. SQLite 测试 schema

- 文件：`lightconf-admin/lightconf-admin-dal/src/test/resources/schema-sqlite.sql`
- 由 `doc/db/light-conf-0.1.1V.sql` 转换，含全部 6 张表：
  - `light_conf_app`
  - `light_conf_app_conf`
  - `light_conf_conf`
  - `light_conf_user`
  - `light_conf_log`
  - `light_conf_conf_log`
- 转换规则：
  - 去除反引号和 `CREATE DATABASE` 语句；
  - `int(11) NOT NULL AUTO_INCREMENT` → `INTEGER PRIMARY KEY AUTOINCREMENT`（SQLite 自增列必须是 `INTEGER PRIMARY KEY`）；
  - 去除 `ENGINE=InnoDB AUTO_INCREMENT=n DEFAULT CHARSET=utf8`、`COMMENT '...'`；
  - `UNIQUE KEY uk_x (col)` → `UNIQUE (col)`；
  - `tinyint(1)`、`datetime`、`varchar(n)` 原样保留（SQLite 动态类型，兼容）。

### 2. 依赖

- `lightconf-admin/lightconf-admin-dal/pom.xml` 新增（test scope）：
  - `org.xerial:sqlite-jdbc`（版本写入根 pom 的 `<dependencyManagement>` 或直接标注，选稳定版如 3.x 最新）
  - `junit`（版本走已有 `${junit.version}` 管理）
- MyBatis 依赖由 admin 父 pom 继承，dal 测试 classpath 已含 mybatis + spring-jdbc（可复用 `SingleConnectionDataSource` 保持内存库连接）。

### 3. 冒烟测试（自证基建）

- 文件：`lightconf-admin/lightconf-admin-dal/src/test/java/com/lightconf/admin/dal/SqliteSmokeTest.java`
- 形态：**纯 MyBatis，不启动 Spring**（用户批准的方案 1）。
- 步骤：
  1. 用 `SingleConnectionDataSource`（或等价单连接 DataSource）连接 `jdbc:sqlite::memory:`，保证测试全程同一连接（内存库每个连接是独立实例，必须单连接复用）；
  2. `SqlSessionFactoryBuilder` 加载 dal 模块的 MyBatis 配置（mapper XML 位置 + 上述数据源）；
  3. 执行 `schema-sqlite.sql` 建表；
  4. 插入 1 条 `light_conf_app` 记录；
  5. 通过 `AppMapper.selectByPrimaryKey` 查回并断言（主键、uuid、app_name 一致）。
- 命名空间/包与现有测试风格保持一致（JUnit 4）。

### 4. 单测 GitHub Actions 工作流

- 文件：`.github/workflows/unit-test.yml`
- 触发：push / pull_request → master（与现有 CI 一致）
- Job：JDK 11/17 矩阵（temurin），`mvn -B test`
- 失败时 `upload-artifact@v4` 上传 `**/target/surefire-reports/`
- 与现有 `ci.yml`（构建打包）职责分离

### 5. Redis 替代说明

- 不新增代码：`SessionStore` 默认实现即 `InMemorySessionStore`（内存）。
- 未来写依赖 Redis 的测试时，直接注入 `InMemorySessionStore` 即可，无需外部服务。

### 6. README 更新（范围已确认：1~4 项）

1. 删除"项目在线预览地址"小节（www.whforever.cn 已失效）；
2. Travis CI 徽章 → GitHub Actions 徽章（`https://github.com/haifeiWu/lightconf/actions/workflows/ci.yml/badge.svg`）；
3. 删除两处 img.hchstudio.cn 截图引用（保留说明文字）；
4. 环境要求更新为：`Maven 3+ / JDK 8+（推荐 11/17）/ Spring Boot 2.7（内嵌 Tomcat，无需独立部署）/ MySQL 5.5+（推荐 8.0）/ Redis 7（可选，多实例会话存储）`。

## 文件改动清单

| 操作 | 文件 |
|---|---|
| 新增 | `lightconf-admin/lightconf-admin-dal/src/test/resources/schema-sqlite.sql` |
| 新增 | `lightconf-admin/lightconf-admin-dal/src/test/java/com/lightconf/admin/dal/SqliteSmokeTest.java` |
| 修改 | `lightconf-admin/lightconf-admin-dal/pom.xml`（sqlite-jdbc test 依赖） |
| 新增 | `.github/workflows/unit-test.yml` |
| 修改 | `README.md`（4 处） |
| 可能修改 | 根 `pom.xml`（sqlite-jdbc 版本管理，如需要） |

## 验证方式

- 本地：`mvn -B test` 全绿（现有 2 测试 + 冒烟测试）；
- CI：推送后 unit-test.yml 在 JDK 11/17 下通过，surefire 报告可见；
- 不改动任何生产代码与现有 mapper XML。
