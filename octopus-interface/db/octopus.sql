/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : octopus

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2013-12-29 19:38:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `article`
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_type` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pack` double DEFAULT NULL,
  `customerGroup` varchar(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('1', 'article', 'CAJA PEPSI 2Lts 6Uni', '6', '', null, null);
INSERT INTO `article` VALUES ('2', 'promotion', 'PROMOCION PODEROSA', '6', '', '2013-06-06 00:00:00', '2013-12-12 00:00:00');
INSERT INTO `article` VALUES ('3', 'article', 'CAJA CERVEZA 2Lts 2Uni', '2', '', null, null);

-- ----------------------------
-- Table structure for `article_price`
-- ----------------------------
DROP TABLE IF EXISTS `article_price`;
CREATE TABLE `article_price` (
  `articleId` bigint(20) NOT NULL,
  `price` double DEFAULT NULL,
  `priceId` int(11) NOT NULL,
  PRIMARY KEY (`articleId`,`priceId`),
  KEY `FK2748D0C06556300E` (`articleId`),
  CONSTRAINT `FK2748D0C06556300E` FOREIGN KEY (`articleId`) REFERENCES `article` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article_price
-- ----------------------------
INSERT INTO `article_price` VALUES ('1', '5.5', '0');
INSERT INTO `article_price` VALUES ('1', '5.6', '1');
INSERT INTO `article_price` VALUES ('2', '25.5', '0');
INSERT INTO `article_price` VALUES ('3', '6.9', '0');
INSERT INTO `article_price` VALUES ('3', '6.95', '1');
INSERT INTO `article_price` VALUES ('3', '7.02', '2');

-- ----------------------------
-- Table structure for `audit_entry`
-- ----------------------------
DROP TABLE IF EXISTS `audit_entry`;
CREATE TABLE `audit_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `moduleId` int(11) DEFAULT NULL,
  `messageType` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `remoteIp` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `sessionId` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `deviceId` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `messageId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2711 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `pricesList` int(11) DEFAULT NULL,
  `observation` varchar(255) DEFAULT NULL,
  `cust_group` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES ('1', 'ENRIQUE CARLOS', '0', null, 'A');
INSERT INTO `customer` VALUES ('2', 'OBDULIO VARELA', '0', null, 'A');

-- ----------------------------
-- Table structure for `order_item`
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article` bigint(20) DEFAULT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `toCredit` tinyint(1) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2D110D6418558873` (`article`),
  KEY `FK2D110D64C16A647E` (`orderId`),
  CONSTRAINT `FK2D110D6418558873` FOREIGN KEY (`article`) REFERENCES `article` (`code`),
  CONSTRAINT `FK2D110D64C16A647E` FOREIGN KEY (`orderId`) REFERENCES `order_m` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item
-- ----------------------------

-- ----------------------------
-- Table structure for `order_m`
-- ----------------------------
DROP TABLE IF EXISTS `order_m`;
CREATE TABLE `order_m` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) DEFAULT NULL,
  `customer` bigint(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FKB80CF7BCE7C0CE6F` (`user`),
  KEY `FKB80CF7BCCD1CE215` (`customer`),
  CONSTRAINT `FKB80CF7BCCD1CE215` FOREIGN KEY (`customer`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKB80CF7BCE7C0CE6F` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_m
-- ----------------------------

-- ----------------------------
-- Table structure for `promo_item`
-- ----------------------------
DROP TABLE IF EXISTS `promo_item`;
CREATE TABLE `promo_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article` bigint(20) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `toCredit` tinyint(1) DEFAULT NULL,
  `promotionId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3A1ABC4318558873` (`article`),
  KEY `FK3A1ABC4313CFD268` (`promotionId`),
  CONSTRAINT `FK3A1ABC4313CFD268` FOREIGN KEY (`promotionId`) REFERENCES `article` (`code`),
  CONSTRAINT `FK3A1ABC4318558873` FOREIGN KEY (`article`) REFERENCES `article` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of promo_item
-- ----------------------------
INSERT INTO `promo_item` VALUES ('1', '1', '1', null, null, '2');
INSERT INTO `promo_item` VALUES ('2', '3', '2', null, null, '2');

-- ----------------------------
-- Table structure for `sales_user_customer`
-- ----------------------------
DROP TABLE IF EXISTS `sales_user_customer`;
CREATE TABLE `sales_user_customer` (
  `customerId` bigint(20) NOT NULL,
  `salesUserId` varchar(255) NOT NULL,
  `daysToVisit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customerId`,`salesUserId`),
  KEY `FK3B0A2FDF4ABC6FB0` (`customerId`),
  KEY `FK3B0A2FDF9E472DF0` (`salesUserId`),
  CONSTRAINT `FK3B0A2FDF4ABC6FB0` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK3B0A2FDF9E472DF0` FOREIGN KEY (`salesUserId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sales_user_customer
-- ----------------------------
INSERT INTO `sales_user_customer` VALUES ('1', 'dela', '1,1,1,1,1,1,1');
INSERT INTO `sales_user_customer` VALUES ('1', 'Guest-User', '1,1,1,1,1,1,1');
INSERT INTO `sales_user_customer` VALUES ('1', 'maqui', '1,1,1,1,1,1,1');
INSERT INTO `sales_user_customer` VALUES ('2', 'dela', '1,1,1,1,1,1,1');
INSERT INTO `sales_user_customer` VALUES ('2', 'Guest-User', '1,1,1,1,1,1,1');
INSERT INTO `sales_user_customer` VALUES ('2', 'maqui', '1,1,1,1,1,1,1');

-- ----------------------------
-- Table structure for `stock`
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `article` bigint(20) NOT NULL,
  `stock` double DEFAULT NULL,
  PRIMARY KEY (`article`),
  KEY `FK68AF71618558873` (`article`),
  CONSTRAINT `FK68AF71618558873` FOREIGN KEY (`article`) REFERENCES `article` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of stock
-- ----------------------------
INSERT INTO `stock` VALUES ('1', '1');
INSERT INTO `stock` VALUES ('2', '14');
INSERT INTO `stock` VALUES ('3', '12');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `canCreateCreditNotes` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('Guest-User', 'sales', 'Guest-User', '0', '1');
INSERT INTO `user` VALUES ('dela', 'sales', 'a', '0', '1');
INSERT INTO `user` VALUES ('maqui', 'sales', 'a', '0', '1');
