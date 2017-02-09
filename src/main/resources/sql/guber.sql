-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: guber
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
-- Table structure for table `cab`
--

DROP TABLE IF EXISTS `cab`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cab` (
  `id`               INT(11)     NOT NULL AUTO_INCREMENT,
  `manufacture_year` DATE                 DEFAULT NULL,
  `car_make`         VARCHAR(45)          DEFAULT NULL,
  `licence_plate`    VARCHAR(30) NOT NULL,
  `capacity`         VARCHAR(45)          DEFAULT NULL,
  `has_baby_chair`   TINYINT(1)           DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cab`
--

LOCK TABLES `cab` WRITE;
/*!40000 ALTER TABLE `cab` DISABLE KEYS */;
INSERT INTO `cab` VALUES (1, '1968-01-01', 'Audi', 'A007AA', '4', 1), (2, '2010-01-01', 'Cadillac', 'C000CC', '2', 0),
  (3, '1999-01-01', 'Chevrolet', 'C000CC', '10', 1), (4, '1908-01-01', 'Ford', 'F888CK', '3', 0),
  (5, '1996-01-01', 'BMW', 'BMWBMW', '1', 1), (6, '2018-01-01', 'Nissan', 'O000OO', '24', 1),
  (7, '1968-01-01', 'Batmobile', 'B777AT', '2', 0);
/*!40000 ALTER TABLE `cab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driver` (
  `id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname`  VARCHAR(45) NOT NULL,
  `birthdate` VARCHAR(45) NOT NULL,
  `sex`       VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1, 'Alex', 'Ivanov', '1988-03-14', 'M'), (2, 'Sam', 'Smith', '1980-03-07', 'F'),
  (3, 'Duke', 'Javov', '1995-01-01', 'M'), (4, 'Michael', 'Schumacher', '1969-01-03', 'M'),
  (5, 'Robert', 'De Niro', '1943-08-17', 'M'), (6, 'Yuri', 'Detochkin', '1968-01-01', 'M'),
  (7, 'Robin', 'Round', '2012-01-01', 'M');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shift` (
  `id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `cab_id`     INT(11)     NOT NULL,
  `driver_id`  INT(11)     NOT NULL,
  `company_id` INT(11)     NOT NULL,
  `date`       VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cab_idx` (`cab_id`),
  KEY `company_idx` (`company_id`),
  KEY `driver_idx` (`driver_id`),
  CONSTRAINT `cab` FOREIGN KEY (`cab_id`) REFERENCES `cab` (`id`)
    ON UPDATE CASCADE,
  CONSTRAINT `company` FOREIGN KEY (`company_id`) REFERENCES `taxi_company` (`id`)
    ON UPDATE CASCADE,
  CONSTRAINT `driver` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
INSERT INTO `shift`
VALUES (1, 2, 1, 2, '2018-01-01'), (2, 1, 1, 2, '2018-01-02'), (3, 3, 2, 2, '2018-01-01'), (4, 2, 2, 2, '2018-01-02'),
  (5, 6, 3, 2, '2018-01-01'), (6, 4, 4, 2, '2018-01-01'), (7, 5, 5, 1, '2018-01-01'), (8, 5, 5, 1, '2018-01-02'),
  (9, 5, 5, 1, '2018-01-03'), (10, 7, 7, 3, '2018-01-01');
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxi_company`
--

DROP TABLE IF EXISTS `taxi_company`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxi_company` (
  `id`      INT(11)     NOT NULL AUTO_INCREMENT,
  `name`    VARCHAR(45) NOT NULL,
  `address` VARCHAR(45)          DEFAULT NULL,
  `rate`    INT(11)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxi_company`
--

LOCK TABLES `taxi_company` WRITE;
/*!40000 ALTER TABLE `taxi_company` DISABLE KEYS */;
INSERT INTO `taxi_company`
VALUES (1, 'Taxi Scorsese', 'New York', 1), (2, 'Landyshi', 'Moscow', 10), (3, 'OOO Batman', 'Gotham City', 1000);
/*!40000 ALTER TABLE `taxi_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxi_ride`
--

DROP TABLE IF EXISTS `taxi_ride`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxi_ride` (
  `id`          INT(11)     NOT NULL AUTO_INCREMENT,
  `shift_id`    INT(11)     NOT NULL,
  `status`      VARCHAR(45) NOT NULL,
  `fare`        INT(11)              DEFAULT NULL,
  `start_time`  DATETIME             DEFAULT NULL,
  `end_time`    DATETIME             DEFAULT NULL,
  `start_point` VARCHAR(45)          DEFAULT NULL,
  `end_point`   VARCHAR(45)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `shift_idx` (`shift_id`),
  CONSTRAINT `shift` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxi_ride`
--

LOCK TABLES `taxi_ride` WRITE;
/*!40000 ALTER TABLE `taxi_ride` DISABLE KEYS */;
INSERT INTO `taxi_ride` VALUES (1, 1, 'Cancelled', 20, NULL, NULL, 'Railway Station', 'Pulkovo'),
  (2, 2, 'Cancelled', 50, NULL, NULL, 'Railway Station', 'Supermarket'),
  (3, 3, 'Finished', 100, '2018-01-01 23:01:01', '2018-01-01 23:53:53', 'Nevsky Prospect', 'Pulkovo'),
  (4, 4, 'Finished', 300, '2018-01-02 15:21:01', '2018-01-02 18:21:01', 'Kremlin', 'Duma'),
  (5, 6, 'Finished', 1000, '2018-01-01 00:00:00', '2018-01-01 00:01:00', 'Moscow', 'St.Petersburg'),
  (6, 6, 'Finished', 10000, '2018-01-01 00:01:00', '2018-01-01 00:01:10', 'Moscow', 'London'),
  (7, 6, 'Finished', 1000000, '2018-01-01 00:01:10', '2018-01-01 00:01:11', 'Moscow', 'Moon'),
  (8, 7, 'Cancelled', 2, NULL, NULL, '14 Av', '16 Av'), (9, 8, 'Cancelled', 3, NULL, NULL, '14 Av', '17 Av'),
  (10, 9, 'Finished', 4, '2018-01-03 12:01:10', '2018-01-03 12:21:10', '14 Av', '18 Av');
/*!40000 ALTER TABLE `taxi_ride` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2017-02-09 19:05:19
