-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: shop
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customers` (
  `id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname`  VARCHAR(45) NOT NULL,
  `birthdate` DATE        NOT NULL,
  `sex`       VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers`
VALUES (1, 'Petr', 'Ivanov', '1990-12-01', 'male'), (2, 'Vasja', 'Petrov', '1976-03-03', 'male'),
  (3, 'Lena ', 'Veselova', '1980-03-04', 'female');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prices`
--

DROP TABLE IF EXISTS `prices`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prices` (
  `id`           INT(11)   NOT NULL AUTO_INCREMENT,
  `newPriceDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `idProduct`    INT(11)   NOT NULL,
  `price`        DOUBLE             DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prices`
--

LOCK TABLES `prices` WRITE;
/*!40000 ALTER TABLE `prices` DISABLE KEYS */;
INSERT INTO `prices`
VALUES (1, '2000-02-02 21:00:00', 1, 100), (2, '2004-04-02 21:00:00', 1, 120), (3, '2000-02-02 21:00:00', 2, 15),
  (4, '2011-07-05 21:00:00', 2, 16), (5, '2000-02-02 21:00:00', 3, 23), (6, '2003-02-08 21:00:00', 3, 76),
  (7, '2000-02-02 21:00:00', 4, 43), (8, '2012-01-02 21:00:00', 4, 40);
/*!40000 ALTER TABLE `prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productjournal`
--

DROP TABLE IF EXISTS `productjournal`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productjournal` (
  `id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `eventDate`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `idProduct`  INT(11)     NOT NULL,
  `idCustomer` VARCHAR(45) NOT NULL,
  `number`     INT(11)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productjournal`
--

LOCK TABLES `productjournal` WRITE;
/*!40000 ALTER TABLE `productjournal` DISABLE KEYS */;
INSERT INTO `productjournal` VALUES (1, '2000-12-31 21:00:00', 1, '1', 2), (2, '2001-02-01 21:00:00', 2, '2', 1),
  (3, '2004-02-04 21:00:00', 1, '2', 12), (4, '2008-12-31 21:00:00', 1, '3', 1), (5, '2008-12-31 21:00:00', 2, '3', 2),
  (6, '2008-12-31 21:00:00', 3, '3', 4), (7, '2010-01-31 21:00:00', 4, '2', 13), (8, '2011-01-01 21:00:00', 5, '3', 1),
  (9, '2011-01-01 21:00:00', 6, '3', 1), (10, '2011-01-02 21:00:00', 1, '1', 1),
  (11, '2011-04-03 21:00:00', 1, '2', 120), (12, '2012-06-05 21:00:00', 2, '1', 12),
  (13, '2012-12-31 21:00:00', 1, '2', 1), (14, '2012-12-31 21:00:00', 1, '3', 1),
  (15, '2012-12-31 21:00:00', 1, '1', 2), (16, '2012-12-31 21:00:00', 2, '2', 3),
  (17, '2013-01-01 21:00:00', 2, '4', 1), (18, '2013-01-02 21:00:00', 7, '3', 12);
/*!40000 ALTER TABLE `productjournal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `idproducts` INT(11)     NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(45) NOT NULL,
  `weight`     INT(11)     NOT NULL DEFAULT '0',
  `category`   VARCHAR(10) NOT NULL DEFAULT 'Toys',
  PRIMARY KEY (`idproducts`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products`
VALUES (1, 'ToyForBoy', 100, 'Toy'), (2, 'ToyForGirl', 200, 'Toy'), (3, 'tomato', 325, 'Vegetable'),
  (4, 'potato', 56, 'Vegetable'), (5, 'orange', 123, 'Fruit'), (6, 'socks', 24, 'Clothes'),
  (10, 'New Toy', 998, 'Toys'), (11, 'Superman', 777, 'Toys');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sellers`
--

DROP TABLE IF EXISTS `sellers`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sellers` (
  `id`   INT(11) NOT NULL,
  `name` VARCHAR(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sellers`
--

LOCK TABLES `sellers` WRITE;
/*!40000 ALTER TABLE `sellers` DISABLE KEYS */;
/*!40000 ALTER TABLE `sellers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id`   INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10)      DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2016-09-08 13:41:01
