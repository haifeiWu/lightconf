CREATE DATABASE  IF NOT EXISTS `xxl-conf` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `xxl-conf`;
-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: xxl-conf
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `XXL_CONF_GROUP`
--

DROP TABLE IF EXISTS `XXL_CONF_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `XXL_CONF_GROUP` (
  `group_name` varchar(100) NOT NULL,
  `group_title` varchar(100) NOT NULL COMMENT '描述',
  PRIMARY KEY (`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `XXL_CONF_GROUP`
--

LOCK TABLES `XXL_CONF_GROUP` WRITE;
/*!40000 ALTER TABLE `XXL_CONF_GROUP` DISABLE KEYS */;
INSERT INTO `XXL_CONF_GROUP` VALUES ('aliyun-ecs','?????'),('default','默认分组');
/*!40000 ALTER TABLE `XXL_CONF_GROUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `XXL_CONF_NODE`
--

DROP TABLE IF EXISTS `XXL_CONF_NODE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `XXL_CONF_NODE` (
  `node_group` varchar(100) NOT NULL COMMENT '分组',
  `node_key` varchar(100) NOT NULL COMMENT '配置Key',
  `node_value` varchar(512) DEFAULT NULL COMMENT '配置Value',
  `node_desc` varchar(100) DEFAULT NULL COMMENT '配置简介',
  UNIQUE KEY `u_group_key` (`node_group`,`node_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `XXL_CONF_NODE`
--

LOCK TABLES `XXL_CONF_NODE` WRITE;
/*!40000 ALTER TABLE `XXL_CONF_NODE` DISABLE KEYS */;
INSERT INTO `XXL_CONF_NODE` VALUES ('default','db.test.password','qweqwe','????'),('default','key01','123411','??????'),('default','key03','ams1233','????');
/*!40000 ALTER TABLE `XXL_CONF_NODE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `light_conf_app`
--

DROP TABLE IF EXISTS `light_conf_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `light_conf_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) DEFAULT NULL,
  `app_name` varchar(45) DEFAULT NULL,
  `app_desc` varchar(45) DEFAULT NULL,
  `private_key` longtext,
  `public_key` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `light_conf_app`
--

LOCK TABLES `light_conf_app` WRITE;
/*!40000 ALTER TABLE `light_conf_app` DISABLE KEYS */;
/*!40000 ALTER TABLE `light_conf_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `light_conf_app_conf`
--

DROP TABLE IF EXISTS `light_conf_app_conf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `light_conf_app_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(45) DEFAULT NULL,
  `conf_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `light_conf_app_conf`
--

LOCK TABLES `light_conf_app_conf` WRITE;
/*!40000 ALTER TABLE `light_conf_app_conf` DISABLE KEYS */;
/*!40000 ALTER TABLE `light_conf_app_conf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `light_conf_conf`
--

DROP TABLE IF EXISTS `light_conf_conf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `light_conf_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conf_key` varchar(255) DEFAULT NULL,
  `conf_value` varchar(255) DEFAULT NULL,
  `conf_desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `light_conf_conf`
--

LOCK TABLES `light_conf_conf` WRITE;
/*!40000 ALTER TABLE `light_conf_conf` DISABLE KEYS */;
/*!40000 ALTER TABLE `light_conf_conf` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-28 17:01:26
