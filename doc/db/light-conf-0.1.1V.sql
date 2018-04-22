/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.6.17 : Database - light-conf
*********************************************************************
*/

CREATE DATABASE /*!32312 IF NOT EXISTS*/`light-conf` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `light-conf`;

/*Table structure for table `light_conf_app` */

DROP TABLE IF EXISTS `light_conf_app`;

CREATE TABLE `light_conf_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) DEFAULT NULL,
  `app_name` varchar(45) DEFAULT NULL,
  `app_desc` varchar(45) DEFAULT NULL,
  `private_key` longtext,
  `public_key` longtext,
  `is_connected` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Data for the table `light_conf_app` */

insert  into `light_conf_app`(`id`,`uuid`,`app_name`,`app_desc`,`private_key`,`public_key`,`is_connected`) values (10,'b3144bcc-491f-464b-a862-08640cdd0d76','test3','测试应用add',NULL,NULL,0),(11,'8d9eb3aa-e80a-4b81-b219-e41296964422','test4','测试应用add',NULL,NULL,0),(14,'7ad410af-1106-420d-b893-9b38ec1801af','test','哈哈',NULL,NULL,0),(15,'8705d6c8-bbe0-420c-9853-f780de4cb5ea','testSample','测试sample',NULL,NULL,0);

/*Table structure for table `light_conf_app_conf` */

DROP TABLE IF EXISTS `light_conf_app_conf`;

CREATE TABLE `light_conf_app_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(45) DEFAULT NULL,
  `conf_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `light_conf_app_conf` */

insert  into `light_conf_app_conf`(`id`,`app_id`,`conf_id`) values (2,'10','2'),(3,'10','3'),(5,'11','5'),(13,'15','13'),(14,'15','14'),(19,'15','19'),(26,'15','26');

/*Table structure for table `light_conf_conf` */

DROP TABLE IF EXISTS `light_conf_conf`;

CREATE TABLE `light_conf_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conf_key` varchar(255) DEFAULT NULL,
  `conf_value` varchar(255) DEFAULT NULL,
  `conf_desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `light_conf_conf` */

insert  into `light_conf_conf`(`id`,`conf_key`,`conf_value`,`conf_desc`) values (2,'dbsize','1234','测试'),(3,'key1','1234','test'),(5,'dbsize','890','890'),(13,'default.key01','请问而退','测试'),(14,'key1','去玩儿体育欧派','测试'),(19,'key02','sdfgjkl12345','测试'),(26,'key01','微软微软','234');

/*light-conf v0.1.x end */

/*light-conf v0.2.0 start */
CREATE TABLE `light_conf_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `permission` tinyint(4) NOT NULL DEFAULT '0' COMMENT '权限：0-普通用户、1-管理员',
  `permission_projects` varchar(1000) DEFAULT NULL COMMENT '权限项目列表，多个逗号分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `light_conf_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conf_key` varchar(200) NOT NULL COMMENT '配置Key',
  `conf_desc` varchar(100) NOT NULL COMMENT '配置描述',
  `conf_value` varchar(2000) DEFAULT NULL COMMENT '配置Value',
  `opt_time` datetime NOT NULL COMMENT '操作时间',
  `opt_user` varchar(100) NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `light_conf_conf_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` int(11) DEFAULT NULL COMMENT 'log表的id',
  `conf_id` int(11) DEFAULT NULL COMMENT '配置Key的id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

ALTER TABLE `light_conf_user` ADD UNIQUE(`user_name`);