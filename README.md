## 《多应用配置管理平台LIGHTCONF》

[![Build Status](https://travis-ci.org/haifeiWu/lightconf.svg?branch=master)](https://travis-ci.org/haifeiWu/lightconf)
[![GitHub release](https://img.shields.io/github/release/haifeiWu/lightconf.svg)](https://github.com/haifeiWu/lightconf/releases)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)


## 一、简介

### 1.1 概述
LIGHTCONF 是一个配置管理平台，其核心设计目标是“为业务提供统一的配置管理服务”。

### 1.2 特性
- 1、简单易用: 上手非常简单, 只需要引入maven依赖和一行配置即可;
- 2、在线管理: 提供配置管理中心, 支持在线管理配置信息;
- 3、实时推送: 配置信息更新后,实时推送配置信息, 项目中配置数据会实时更新并生效, 不需要重启线上机器;
- 4、配置备份: 配置数据会在MySQL中会对配置信息做备份, 保证配置数据的安全性;

### 1.3 背景

> why not properties

常规项目开发过程中, 通常会将配置信息位于在项目resource目录下的properties文件文件中, 配置信息通常包括有: jdbc地址配置、redis地址配置、活动开关、阈值配置、黑白名单……等等。使用properties维护配置信息将会导致以下几个问题:

- 1、需要手动修改properties文件; 
- 2、需要重新编译打包; 
- 3、需要重启线上服务器 (项目集群时,更加令人崩溃) ; 
- 4、配置生效不及时: 因为流程复杂, 新的配置生效需要经历比较长的时间才可以生效;
- 5、不同环境上线包不一致: 例如JDBC连接, 不同环境需要差异化配置;

> why LIGHTCONF

- 1、不需要 (手动修改properties文件) : 在配置管理中心提供的Web界面中, 定位到指定配置项, 输入新的配置的值, 点击更新按钮即可;
- 2、不需要 (重新编译打包) : 配置更新后, 实时推送新配置信息至项目中, 不需要编译打包;
- 3、不需要 (重启线上服务器) : 配置更新后, 实时推送新配置信息至项目中, 实时生效, 不需要重启线上机器; 
- 4、配置生效 "非常及时" : 点击更新按钮, 新的配置信息将会即可推送到项目中, 瞬间生效, 非常及时。比如一些开关类型的配置, 配置变更后, 将会立刻推送至项目中并生效, 相对常规配置修改繁琐的流程, 及时性可谓天壤之别; 

### 1.4 项目在线预览地址
配置中心预览 | Release Download
--- | ---
http://58.87.84.211/lightconf-admin-web/toLogin | http://58.87.84.211/lightconf-sample/


#### 源码仓库地址

源码仓库地址 | 接入LIGHTCONF的Demo项目预览
--- | ---
 
### 1.5 环境
- Maven3+
- Jdk1.7+
- Tomcat7+
- Mysql5.5+

## 二、快速入门

### 2.1 初始化“数据库”
请下载项目源码并解压，获取 "调度数据库初始化SQL脚本" 并执行即可。脚本位置如下：
 
    lightconf/doc/db/light-conf-0.1.1V.sql

### 2.2 编译源码
解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可

- lightconf-admin：配置管理中心
- lightconf-core：公共依赖
- lightconf-common：公共依赖
- lightconf-sample: 接入LIGHTCONF的Demo项目

### 2.3 “配置管理中心” 项目配置

    项目：lightconf-admin
    作用：管理线上配置信息
    
配置文件位置：

    lightconf/lightconf-admin/lightconf-admin-web/src/main/resources/light-conf.properties
    
配置项目说明：
    
    # 配置登录lightconf的用户名，密码
    light.conf.login.username=admin
    light.conf.login.password=123456
    
    # mysql database setting
    jdbc.type=mysql
    jdbc.driver=com.mysql.jdbc.Driver
    
    jdbc.url=jdbc:mysql://localhost:3306/light-conf?useUnicode=true&characterEncoding=utf-8
    jdbc.username=root
    jdbc.password=root_pwd
    
    # pool settings
    jdbc.pool.init=2
    jdbc.pool.minIdle=3
    jdbc.pool.maxActive=20
    
    # jdbc.testSql=SELECT 'x'
    jdbc.testSql=SELECT 'x' FROM DUAL
    
    # 服务端启动监听端口
    netty.server.port=9998

### 2.4 “接入LIGHTCONF的示例项目” 项目配置

    项目：lightconf-sample
    作用：接入LIGHTCONF的示例项目，供用户参考学习
    
#### A、引入maven依赖

    <!-- lightconf-client -->
    <dependency>
        <groupId>com.lightconf</groupId>
        <artifactId>lightconf-core</artifactId>
        <version>${project.parent.version}</version>
    </dependency>
    
#### B、添加 LIGHTCONF 配置文件

    可参考配置文件：
    lightconf/lightconf-sample/src/main/resources/light-conf.properties
    
    配置项说明:
    
    # 连接light-conf-admin的IP地址
    light.conf.host=127.0.0.1
    # 连接light-conf-admin的端口号
    light.conf.port=9998
    
    ## 接入应用的uuid
    application.uuid=8705d6c8-bbe0-420c-9853-f780de4cb5ea
 
#### C、LIGHTCONF 配置初始化[必须]
    可参考配置文件：
    
    lightconf/lightconf-sample/src/main/resources/spring/applicationcontext-light-conf.xml
    
    配置项说明：
    
    <!-- ********************************* 核心配置[必须]：LIGHTCONF 配置 ********************************* -->
    <bean id="xxlConf" class="com.lightconf.core.spring.LightConfFactory" init-method="init" destroy-method="destroy" />

    <!-- ********************************* 核心配置[必须]：LIGHTCONF netty client监听 ********************************* -->
    <bean id="lightConfListener" class="com.lightconf.core.listener.LightConfClientListener"></bean>