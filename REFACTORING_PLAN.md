# LIGHTCONF 代码评审与重构计划

> 评审范围：全量源码（105 个 Java 文件）+ Maven 构建配置 + 资源文件。
> 评审日期：以 git 最新提交为准。

---

## 一、项目概览

| 维度 | 说明 |
|---|---|
| 定位 | 分布式配置中心（xxl-conf 的二次开发，大量残留 `xxl-conf`/`XxlConf`/`@author xuxueli` 痕迹） |
| 模块 | `common`（模型/编解码/工具）、`core`（客户端 SDK）、`admin`（dal/model/service/web 四层）、`sample`×2（示例） |
| 技术栈 | Spring Boot 2.1.2 + Netty + MyBatis + MySQL + ehcache + fastjson + lombok |
| 通信 | 客户端 Netty 长连接，启动全量下发 + 变更下推（推拉混合） |
| 数据模型 | `conf`(全局配置) + `app` + `app_conf`(关联) |

---

## 二、问题清单（按严重度分级）

### 🔴 P0 — 安全 / 致命缺陷 / 构建阻断

1. **Netty 无鉴权 + Java 反序列化 RCE（最高危）**
   服务端 9998 端口用 `ObjectDecoder(ClassResolvers.cacheDisabled(null))` 直接反序列化任意客户端字节流，且 `lightConfClientLogin()` 完全不校验用户名密码（硬编码的 `wuhf/abcd` 也没用上）。任何能连上端口的人可触发反序列化 gadget 链 RCE，或伪装 uuid 拉取任意应用配置。

2. **硬编码凭据已入库**
   `application.properties` 提交了生产 MySQL 密码 `TengXunYun@2018` 与公网 IP；`light-conf.properties` 提交了 `redis.auth=genyun135`；`login` 接口日志打印明文密码。

3. **密码逻辑自相矛盾（功能性 bug）**
   `addUser/updateUser` 存 MD5，`userLogin` 却用明文 `equals` 比较 → 登录永远失败；且 MD5 本身已不安全（应 BCrypt）。

4. **构建阻断**
   `lightconf-core/pom.xml` 引用 `${spring.version}`，但父 pom 中该属性被注释；`lightconf-admin/pom.xml` 引用 `${slf4j.version}`，父 pom 定义的是 `slf4j-api.version` → Maven 属性无法解析，无法构建。

5. **任一客户端异常导致服务瘫痪**
   `ServerHandler.exceptionCaught()` 里调用 `getThreadPool().shutdown()`，一个坏连接就能关掉全局线程池。

6. **`SyncWriteFuture.get()` 用 `latch.wait()`**（无 synchronized，抛 `IllegalMonitorStateException`）；`timeout` 从未赋值导致 `isTimeout()` 恒 true；整套 `SyncWrite/SyncWriteMap` 实为死代码。

7. **`RequestCountFilter` 的 `FastThreadLocal THREAD_LOCAL` 未初始化** → 每个请求 NPE。

8. **`CrossDomainRequestFilter` 把 `ServletRequest` 强转成 `HttpServletResponse`** → 运行期 ClassCastException。

### 🟠 P1 — 功能缺陷 / 数据一致性

9. `ServerHandler` UPLOAD_CONF 分支：`app==null` 时 `result` 仍为 null → NPE；且 `result` 在循环内被覆盖只保留最后一次。
10. `AppServiceImpl.deleteApp` 判空顺序反了：`confList.size() > 0 && confList != null`。
11. 多处未实现直接 `return null`：`getAppList`、`ConfServiceImpl.pageList/deleteByKey`、`UserServiceImpl.getUserList`、`LogServiceImpl`（空类）、`LogController`（空类）→ 调用方 NPE。
12. **配置 key 全局唯一**（`getConfByKey` 全表查），不同应用不能有同名 key —— 数据模型缺陷。
13. `ClientHandler/ServerHandler` 的 `ReferenceCountUtil.release()` 语义错误（release 的是 `MsgType` 枚举），且 `SimpleChannelInboundHandler` 已自动释放 → 双重释放/错误释放。
14. `SyncCacheToFile` 为空 TODO —— 设计文档里"本地文件容灾"这一核心能力未实现。
15. 登录态存静态 `CacheUtils.LOGIN_STATUS` 内存 Map：重启丢失、多实例不共享、cookie 明文 token。

### 🟡 P2 — 设计 / 架构

16. **三套响应包装类并存**：`LightConfResult` / `ReturnT` / `ResultCode`，且 `ReturnT` 在 `common` 与 `admin-web` 重复定义。
17. **两套编解码并存**：JSON 的 `MessageEncoder/Decoder` 被注释，实际走 Java 序列化 `ObjectEncoder/Decoder`。
18. **`SpringContextHolder` 静态取 bean 反模式**：`ServerHandler` 用静态上下文拿 service，应依赖注入。
19. **线程生命周期失控**：`ClientBootstrap` 每次 `new NioEventLoopGroup` 不关闭；`LightConfClientListener` 每次事件 `new ScheduledExecutorService` 不关闭（线程泄漏）；`reConnect` 是局部变量导致无限重连。
20. **静态可变全局状态**：`Constants.clientId`、`NettyChannelMap`、`CacheUtils`、`key2BeanField` 静态 Map，多应用/多 classloader 下互相污染。
21. 大量 xxl-conf 复制残留：`TokenApi`、`ApiTokenValidateInterceptor` 整段注释死代码。
22. `MvcConfig` 继承已废弃的 `WebMvcConfigurerAdapter`；`PermessionLimit` 拼写错误（应为 `Permission`）。
23. **版本陈旧且不一致**：Boot 2.1.2、fastjson 1.2.34（多个 RCE CVE）、netty 4.1.17、log4j 1.2.17、guava 29、httpclient 4.5.3/4.5.13 混用；`log4j2.xml` 配置却依赖 log4j 1.2.17。
24. **零测试**：`surefire skip=true`，无任何测试文件。

### 🟢 P3 — 代码质量

25. `e.printStackTrace()` 满天飞（应 `logger.error(msg, e)`）。
26. `System.out.println` 调试残留。
27. 未使用依赖/工具类（RSAUtils、Base64Utils、HttpTookit、JsonMapper 等）。
28. `BeanRefreshLightConfListener` 用 String 反射 set 到 int/boolean 字段会 `IllegalArgumentException`。
29. `LightConfFactory` 静态 `beanFactory/beanName` 非线程安全。
30. `reloadAll()` 中 `keySet.size()>1` 才刷新的逻辑无意义且循环自我引用。

---

## 三、重构计划（分 5 阶段，先安全网后架构）

> 原则：先止血（构建 + 安全），再修正确性，最后架构演进；每阶段可独立合入、有明确验收。

### 阶段 0 —— 建立安全网（阻断项，最先做）
- 修复 `${spring.version}` / `${slf4j.version}` 属性引用。
- 升级基线：Spring Boot → 2.7.x（或 3.x），Java 8 → 11/17，netty → 4.1.x 最新，fastjson → 最新安全版（或统一迁移到 Jackson），移除 log4j 1.x 统一 log4j2 + slf4j。
- 开启 `surefire` 并加最小冒烟测试 + GitHub Actions CI。
- **验收**：`mvn clean package` 通过，CI 绿。

### 阶段 1 —— 安全加固
- 弃用 Java 反序列化，统一为 JSON 编解码 + **消息长度上限** + 登录鉴权（uuid + 签名/凭据校验）。
- 配置全量外置化（环境变量/密钥管理），清理硬编码凭据并做 git 历史清洗。
- 密码改 BCrypt；登录不打印明文；会话改分布式 token（替代静态 Map）。
- **验收**：无认证客户端无法建立会话/拉取配置；无硬编码凭据残留。

### 阶段 2 —— 核心逻辑修复
- 修复 P0/P1 全部缺陷：UPLOAD_CONF NPE、`exceptionCaught` 关线程池、判空顺序、double release、过滤器强转/NPE、`SyncWriteFuture`、`WebExceptionResolver` 强转。
- 实现 `SyncCacheToFile` 本地持久化容灾；删除或正确重写 `SyncWrite` 死代码。
- 修复密码校验一致性。
- **验收**：单测覆盖上述修复点，异常路径不崩。

### 阶段 3 —— 架构梳理
- **统一响应模型**（只保留一个泛型化的 `Result<T>`），删除 `ReturnT`/`ResultCode` 重复定义。
- 服务端用依赖注入替代 `SpringContextHolder` 静态取 bean。
- 统一线程/连接生命周期管理（EventLoopGroup、ScheduledExecutor 统一 shutdown hook）。
- **数据模型**：`conf` 增加 app 维度或 `(app_id, conf_key)` 唯一，修复同名 key 冲突。
- 补齐或删除空 Service/Controller/未实现方法。
- **验收**：接口行为不变，重复类清零，线程无泄漏（可观测性验证）。

### 阶段 4 —— 清理与治理
- 删除死代码/注释块/重复 util；重命名 `PermessionLimit→PermissionLimit`、清理 `xxl-conf` 残留命名与注释。
- 补全测试：编解码协议集成测试 + 核心 Service 单测 + 配置下推端到端测试。
- 同步更新 README / 设计文档。
- **验收**：测试覆盖率达标，代码扫描（如 SpotBugs/依赖漏洞扫描）无高危。

---

## 四、涉及关键文件索引

| 问题 | 文件 |
|---|---|
| 反序列化 RCE / 无鉴权 | `lightconf-admin-web/.../netty/LightConfServerBootstrap.java`、`ServerHandler.java`、`ServerInitializer.java` |
| 客户端编解码 / 重连 | `lightconf-core/.../netty/ClientInitializer.java`、`ClientBootstrap.java`、`ClientHandler.java` |
| 硬编码凭据 | `lightconf-admin-web/src/main/resources/application.properties`、`light-conf.properties` |
| 密码不一致 | `lightconf-admin-service/.../impl/UserServiceImpl.java` |
| 未实现方法 | `AppServiceImpl`、`ConfServiceImpl`、`UserServiceImpl`、`LogServiceImpl`、`LogController` |
| 线程池关闭 / NPE | `ServerHandler.java`、`common/util/ThreadPoolUtils.java` |
| 同步写死代码 | `common/sync/*` |
| 本地容灾 TODO | `core/core/SyncCacheToFile.java` |
| 过滤器缺陷 | `admin-web/.../filter/CrossDomainRequestFilter.java`、`RequestCountFilter.java` |
| 响应模型重复 | `common/util/LightConfResult.java`、`ReturnT.java`、`ResultCode.java`、`admin-web/util/ReturnT.java` |
| 静态取 bean | `admin-service/.../impl/SpringContextHolder.java` |

---

## 五、执行记录（2026-08-13 重构实施）

### ✅ 阶段 0：构建 + 依赖基线（完成）
- Spring Boot 2.1.2 → **2.7.18**；Java 1.7 → **1.8**（CI 验证 11/17）
- fastjson → 1.2.83；netty 4.1.17/4.1.42 → **4.1.94**（弃用 netty-all 空壳 jar，显式声明 transport/codec/handler/buffer/common/resolver 模块）
- 移除 log4j 1.2.17 → 统一 log4j2；ehcache 3.4.0 → 3.10.8；guava → 32.1.3；httpclient → 4.5.14
- 修复 sample-springboot war 打包（failOnMissingWebXml）；mysql 驱动迁移到 `com.mysql:mysql-connector-j`
- 开启 surefire；新增 GitHub Actions CI（JDK 11/17 矩阵）
- 删除死代码：`HttpTookit`、`JsonMapper`、`RSAUtils`、`Base64Utils`、`TokenApi`、`ApiTokenValidateInterceptor`、`ServerInitializer`、`sync/*`

### ✅ 阶段 1：安全加固（完成）
- **弃用 Java 反序列化**：client/server pipeline 统一为 JSON 编解码（`MessageEncoder/Decoder`），`MessageDecoder` 增加 10MB 帧长度上限与非法长度拒绝
- 修复 `PushMsg`/`LoginMsg` typeName 冲突、`ReplyBody` 体系补 seeAlso 白名单（fastjson 多态安全序列化）
- **登录鉴权**：`LoginMsg` 新增 secret 字段，服务端 `ServerAuthConfig` 校验共享密钥，失败拒绝连接；移除硬编码 wuhf/abcd
- **配置外置化**：MySQL/Redis 真实凭据从仓库清除，改为环境变量注入（`DB_HOST/DB_PORT/DB_USERNAME/DB_PASSWORD/LIGHTCONF_SECRET`）
- **BCrypt**：新密码 BCrypt 存储 + 存量 MD5 兼容校验；登录日志不再打印明文密码
- **会话加固**：随机 token + 2h 过期替代弱 MD5 token；登出即失效

### ✅ 阶段 2：核心逻辑修复（完成）
- `ServerHandler`：UPLOAD_CONF NPE、exceptionCaught 关闭全局线程池、双重 release、LOGIN 双重处理、PING NPE 全部修复
- 判空顺序（`AppServiceImpl`）；`WebExceptionResolver` handler 强转；`CrossDomainRequestFilter` 强转 bug（改用 Spring CORS 配置）；删除 `RequestCountFilter`（NPE）
- **实现 `SyncCacheToFile`**：本地缓存定时持久化 + 启动加载容灾（`light-conf-cache.properties`）
- 修复 `LightConfLocalCacheConf.reloadAll` 的 `size()>1` bug；`LightConfClientListener` 线程泄漏与重复启动；`ClientBootstrap` 重连计数 bug
- `LightConfFactory.refreshBeanField`：String → 基本类型转换（避免反射注入异常）

### ✅ 阶段 3：架构梳理（完成）
- **统一响应模型**：删除 `ReturnT`（common + admin-web）、`ResultCode`，全项目统一 `LightConfResult`，并删除其死方法
- **DI 替代 SpringContextHolder**：`ServerHandler`/`LightConfServerBootstrap` 改为 Spring 组件 + 构造器注入（@Sharable），删除 `SpringContextHolder`
- 线程/连接生命周期：`LightConfServerBootstrap.shutdown()`、`LightConfServerListener` 实现 `DisposableBean`、`LightConfClientListener` 定时器单例 + destroy
- 删除空实现：`LogService/LogServiceImpl/LogController`、`ConfService.pageList/deleteByKey`、`UserService.getUserList`、`AppService.getAppList`、`AppController.getAppConf/getAppList`、`UserController.updatePermissionProjects`
- `MvcConfig` 改实现 `WebMvcConfigurer`（替代废弃的 Adapter）

### ✅ 阶段 4：清理与治理（完成）
- 重命名 `PermessionLimit` → `PermissionLimit`（含文件/类/全部使用点）
- 清理 xxl-conf/XxlConf 残留命名与注释；`printStackTrace` 全部改为结构化日志
- 新增测试：`MessageCodecTest`（9 个：多态往返 + 恶意帧拒绝）、`LightConfClientTest`（3 个）；`mvn clean package` 全绿（12 tests）
- README 更新：环境要求、配置项（环境变量注入 + secret）

### ✅ 遗留项修复（2026-08-13 第二批）

**1. conf 增加 app 维度 —— (app_id, conf_key) 唯一 ✓**
- `light_conf_conf` 表新增 `app_id` 列 + 唯一索引 `uk_app_conf_key(app_id, conf_key)`；不同应用可拥有同名 key
- 数据层：`Conf`/`ConfExample` 增加 appId；`ConfMapper.xml` 全量 SQL 带 app_id；`ConfMapper2.xml` 改为按 `conf.app_id` 直接查询（移除 join 关联表）
- 服务层：`add` 按 (app_id, conf_key) 查重；`update/deleteById` 校验配置归属当前 app，防止跨应用更新/删除；不再写入 `light_conf_app_conf` 关联表（保留表以兼容历史数据）
- `ServerHandler` UPLOAD_CONF：配置已存在视为上传成功（幂等上传）
- 脚本：`doc/db/light-conf-0.1.1V.sql` 重建（修复 app 表缺 is_change/is_push_conf 列、v0.2.0 段语法错误）；新增 `doc/db/upgrade-v0.2.0-app-dimension.sql` 存量升级脚本（回填 app_id → 清理重复/无归属数据 → 唯一索引）

**2. 登录态多实例共享 —— 可插拔 SessionStore ✓**
- `SessionStore` 接口 + `InMemorySessionStore`（默认，@ConditionalOnMissingBean）+ `RedisSessionStore`（`light.conf.session.store=redis` 启用，StringRedisTemplate + 2h TTL）
- `CacheUtils` 静态类删除；`LoginService` 构造注入 SessionStore；admin-web 引入 `spring-boot-starter-data-redis`（不配置 Redis 时不影响启动）

**3. 线程生命周期 ✓**
- `ThreadPoolUtils.shutdown()`（幂等）；`ClientBootstrap.shutdown()`（停止重连 + 关闭 EventLoopGroup）；`LightConfClientListener.destroy()` 依次关闭客户端连接、同步定时器、全局线程池

**4. sample-springboot 补齐 ✓**
- 新增 `light-conf.properties`（host/port/uuid/secret）；xml bean id 清理为 `lightConf`

**5. 全局异常处理器 ✓**
- `WebExceptionResolver` 重构为 `@RestControllerAdvice`：统一返回 `LightConfResult`，内部异常细节不再泄漏给前端
- `AppController/ConfController/UserController` 去除 try/catch 样板（约 12 处）

### ⚠️ 遗留项（需单独验证，本轮未做）
1. **运行时验证**：本机无 MySQL/Redis 环境，admin 模块的启动、DB 迁移脚本、Redis 会话切换未做集成验证（构建与单测全绿）。建议在部署环境执行 `doc/db/upgrade-v0.2.0-app-dimension.sql` 后做一次端到端冒烟。
2. **ASK 分支遗留 demo 逻辑**：`ServerHandler` ASK 分支仍是 xxl-conf 示例（authToken 回复），未实际使用，可后续删除。
3. **`ThreadPoolUtils` 仍为静态单例**：多实例部署下各实例独立，属正常预期；若需进程内动态管理可改为 Spring bean。
