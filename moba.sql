/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26-log : Database - moba
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`moba` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `moba`;

/*Table structure for table `character` */

DROP TABLE IF EXISTS `character`;

CREATE TABLE `character` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cId` int(11) DEFAULT NULL COMMENT '英雄配置id',
  `level` int(11) DEFAULT '1' COMMENT '等级',
  `exp` int(64) DEFAULT '0' COMMENT '经验',
  `userId` int(10) DEFAULT NULL COMMENT '所属user',
  `attSpot` int(11) DEFAULT NULL COMMENT '攻击点',
  `defSpot` int(11) DEFAULT NULL COMMENT '防御点',
  `hpSpot` int(11) DEFAULT NULL COMMENT '生命点',
  `criSpot` int(11) DEFAULT NULL COMMENT '暴击点',
  `resuSpot` int(11) DEFAULT NULL COMMENT '复活点',
  `speedSpot` int(11) DEFAULT NULL COMMENT '速度点',
  `cdSpot` int(11) DEFAULT NULL COMMENT 'cd点',
  `att` float DEFAULT NULL COMMENT '攻击',
  `def` float DEFAULT NULL COMMENT '防御',
  `hp` float DEFAULT NULL COMMENT '血量',
  `cri` float DEFAULT NULL COMMENT '暴击率',
  `resu` float DEFAULT NULL COMMENT '复活率',
  `speed` float DEFAULT NULL COMMENT '速度提升率',
  `cd` float DEFAULT NULL COMMENT '降低cd率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `character` */

insert  into `character`(`id`,`cId`,`level`,`exp`,`userId`,`attSpot`,`defSpot`,`hpSpot`,`criSpot`,`resuSpot`,`speedSpot`,`cdSpot`,`att`,`def`,`hp`,`cri`,`resu`,`speed`,`cd`) values (13,1,1,0,12,64,24,0,10,0,0,0,382,114,352,1.02,0,0,0),(15,1,1,0,13,0,0,0,0,0,0,0,62,66,352,0.02,0,300,0),(16,2,1,0,13,0,0,0,0,0,0,0,77,41,252,0.02,0,400,0),(34,3,1,0,12,21,60,0,0,0,0,0,182,161,252,0.02,0,0,0),(35,3,1,0,12,5,9,0,0,0,0,0,107,69,202,0.02,0,0,0),(36,3,1,0,12,0,0,0,0,0,0,0,72,46,282,0.02,0,0,0),(43,1,1,0,17,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(44,2,1,0,17,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(47,1,1,0,19,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(48,2,1,0,19,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(49,1,1,0,20,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(50,2,1,0,20,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(51,1,1,0,21,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(52,2,1,0,21,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(53,3,1,0,20,0,0,0,0,0,0,0,82,51,202,0.02,0,0,0),(54,1,1,0,22,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(55,2,1,0,22,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(56,1,1,0,24,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(57,2,1,0,24,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(58,1,1,0,25,0,0,0,0,0,0,0,62,66,352,0.02,0,0,0),(59,2,1,0,25,0,0,0,0,0,0,0,77,41,252,0.02,0,0,0),(60,2,1,0,12,0,0,0,0,0,0,0,77,41,252,0.2,0,0,0);

/*Table structure for table `follow` */

DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `followId` int(11) DEFAULT NULL COMMENT '关注者id',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf32 COMMENT='关注表';

/*Data for the table `follow` */

insert  into `follow`(`id`,`followId`,`userId`) values (24,12,13),(25,12,13),(30,12,22),(62,19,12),(64,20,12),(68,22,12),(72,19,22),(74,21,22),(75,12,24),(76,22,24),(77,12,20),(78,13,20),(79,17,20),(80,19,20);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '昵称',
  `coin` int(64) DEFAULT '0' COMMENT '金币',
  `characterId` int(11) DEFAULT NULL COMMENT '已选角色id',
  `bag` text COMMENT '背包',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`username`,`password`,`nickname`,`coin`,`characterId`,`bag`) values (12,'123','123','泥皑刺',1098010254,34,'[{\"itemId_\":16,\"count_\":30,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":1,\"count_\":35,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":11,\"count_\":1,\"isTran_\":true,\"money_\":77,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":8,\"memoizedHashCode\":0},{\"itemId_\":12,\"count_\":2,\"isTran_\":true,\"money_\":33,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":8,\"memoizedHashCode\":0},{\"itemId_\":2,\"count_\":4,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":9,\"count_\":2,\"isTran_\":true,\"money_\":555,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":9,\"memoizedHashCode\":0},{\"itemId_\":14,\"count_\":1,\"isTran_\":true,\"money_\":999,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":9,\"memoizedHashCode\":0},{\"itemId_\":10,\"count_\":2,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(13,'1234','1234','但摹拭',789876682,16,'[{\"itemId_\":9,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":16,\"count_\":10,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":5,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":8,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":12,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":2,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(17,'12345','12345','星呸泼',789876682,43,'[{\"itemId_\":1,\"count_\":5,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":7,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(19,'1','1','睫坯薛',789874182,47,'[{\"itemId_\":16,\"count_\":10,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":1,\"count_\":5,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(20,'12','12','顶沦市',689866694,50,'[{\"itemId_\":2,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":1,\"count_\":5,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":7,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":16,\"count_\":10,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":3,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":9,\"count_\":2,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":17,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(21,'123456','123456','蒸夫痒',789876682,51,'[]'),(22,'11','11','滩运曝',789874082,54,'[{\"itemId_\":6,\"count_\":1,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0},{\"itemId_\":16,\"count_\":10,\"isTran_\":false,\"money_\":0,\"memoizedIsInitialized\":1,\"unknownFields\":{\"fields\":{},\"fieldsDescending\":{}},\"memoizedSize\":4,\"memoizedHashCode\":0}]'),(24,'111','111','季箍僳',789876482,56,'[]');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
