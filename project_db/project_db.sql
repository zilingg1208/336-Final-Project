-- MySQL dump 10.13  Distrib 8.0.46, for macos15 (x86_64)
--
-- Host: 127.0.0.1    Database: project_db
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'dd3fa478-c56d-11f0-919f-f92ef64b9300:1-2208';

--
-- Table structure for table `Aircraft`
--

DROP TABLE IF EXISTS `Aircraft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aircraft` (
  `aircraft_id` int NOT NULL,
  `aid` char(2) NOT NULL,
  `capacity` int DEFAULT NULL,
  PRIMARY KEY (`aircraft_id`),
  KEY `aid` (`aid`),
  CONSTRAINT `aircraft_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `Airlines` (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aircraft`
--

LOCK TABLES `Aircraft` WRITE;
/*!40000 ALTER TABLE `Aircraft` DISABLE KEYS */;
INSERT INTO `Aircraft` VALUES (1,'AA',2),(2,'DL',3);
/*!40000 ALTER TABLE `Aircraft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Airlines`
--

DROP TABLE IF EXISTS `Airlines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Airlines` (
  `aid` char(2) NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Airlines`
--

LOCK TABLES `Airlines` WRITE;
/*!40000 ALTER TABLE `Airlines` DISABLE KEYS */;
INSERT INTO `Airlines` VALUES ('AA'),('DL'),('UA');
/*!40000 ALTER TABLE `Airlines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Airports`
--

DROP TABLE IF EXISTS `Airports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Airports` (
  `airport_id` char(3) NOT NULL,
  PRIMARY KEY (`airport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Airports`
--

LOCK TABLES `Airports` WRITE;
/*!40000 ALTER TABLE `Airports` DISABLE KEYS */;
INSERT INTO `Airports` VALUES ('JFK'),('LAX'),('ORD');
/*!40000 ALTER TABLE `Airports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Employees`
--

DROP TABLE IF EXISTS `Employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Employees` (
  `eid` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `role` enum('admin','rep') DEFAULT NULL,
  PRIMARY KEY (`eid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Employees`
--

LOCK TABLES `Employees` WRITE;
/*!40000 ALTER TABLE `Employees` DISABLE KEYS */;
INSERT INTO `Employees` VALUES (1,'admin1','123','admin'),(2,'rep1','123','rep');
/*!40000 ALTER TABLE `Employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Flights`
--

DROP TABLE IF EXISTS `Flights`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Flights` (
  `aid` char(2) NOT NULL,
  `flight_number` int NOT NULL,
  `aircraft_id` int NOT NULL,
  `departure_airport` char(3) NOT NULL,
  `destination_airport` char(3) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `stops` int DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `days_of_week` varchar(20) DEFAULT NULL,
  `departure_datetime` datetime DEFAULT NULL,
  `arrival_datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`aid`,`flight_number`),
  KEY `aircraft_id` (`aircraft_id`),
  KEY `departure_airport` (`departure_airport`),
  KEY `destination_airport` (`destination_airport`),
  CONSTRAINT `flights_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `Airlines` (`aid`),
  CONSTRAINT `flights_ibfk_2` FOREIGN KEY (`aircraft_id`) REFERENCES `Aircraft` (`aircraft_id`),
  CONSTRAINT `flights_ibfk_3` FOREIGN KEY (`departure_airport`) REFERENCES `Airports` (`airport_id`),
  CONSTRAINT `flights_ibfk_4` FOREIGN KEY (`destination_airport`) REFERENCES `Airports` (`airport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Flights`
--

LOCK TABLES `Flights` WRITE;
/*!40000 ALTER TABLE `Flights` DISABLE KEYS */;
INSERT INTO `Flights` VALUES ('AA',101,1,'JFK','LAX',300.00,0,NULL,NULL,'2026-05-01 08:00:00','2026-05-01 11:00:00'),('AA',102,1,'JFK','LAX',350.00,1,NULL,NULL,'2026-05-02 09:00:00','2026-05-02 12:00:00'),('DL',201,2,'LAX','JFK',320.00,0,NULL,NULL,'2026-05-05 10:00:00','2026-05-05 18:00:00'),('UA',301,2,'JFK','ORD',200.00,0,NULL,NULL,'2026-05-01 07:00:00','2026-05-01 09:00:00');
/*!40000 ALTER TABLE `Flights` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Includes`
--

DROP TABLE IF EXISTS `Includes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Includes` (
  `ticket_id` int NOT NULL,
  `aid` char(2) NOT NULL,
  `flight_number` int NOT NULL,
  `departure_datetime` datetime DEFAULT NULL,
  `seat_number` varchar(10) DEFAULT NULL,
  `class` varchar(20) DEFAULT NULL,
  `special_meal` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ticket_id`,`aid`,`flight_number`),
  KEY `aid` (`aid`,`flight_number`),
  CONSTRAINT `includes_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `Tickets` (`ticket_id`),
  CONSTRAINT `includes_ibfk_2` FOREIGN KEY (`aid`, `flight_number`) REFERENCES `Flights` (`aid`, `flight_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Includes`
--

LOCK TABLES `Includes` WRITE;
/*!40000 ALTER TABLE `Includes` DISABLE KEYS */;
INSERT INTO `Includes` VALUES (1,'AA',101,'2026-05-01 08:00:00','12A','economy','none'),(2,'AA',101,'2026-05-01 08:00:00','12B','business','vegan'),(3,'DL',201,'2026-04-29 13:47:14','12A','economy','none'),(4,'AA',101,'2026-04-29 14:49:59','12B','economy','none'),(7,'AA',102,'2026-05-01 11:25:13','13A','economy','none'),(10,'AA',102,'2026-05-01 11:45:48','15A','business','none');
/*!40000 ALTER TABLE `Includes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Questions`
--

DROP TABLE IF EXISTS `Questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Questions` (
  `qid` int NOT NULL AUTO_INCREMENT,
  `cid` int DEFAULT NULL,
  `eid` int DEFAULT NULL,
  `question` text,
  `response` text,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`qid`),
  KEY `cid` (`cid`),
  KEY `eid` (`eid`),
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Users` (`cid`),
  CONSTRAINT `questions_ibfk_2` FOREIGN KEY (`eid`) REFERENCES `Employees` (`eid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Questions`
--

LOCK TABLES `Questions` WRITE;
/*!40000 ALTER TABLE `Questions` DISABLE KEYS */;
INSERT INTO `Questions` VALUES (1,1,NULL,'Can I change my seat?',NULL,'pending'),(2,2,NULL,'Is food included?','Yes','answered'),(3,1,NULL,'can i upgrade?',NULL,'pending');
/*!40000 ALTER TABLE `Questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tickets`
--

DROP TABLE IF EXISTS `Tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tickets` (
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `cid` int NOT NULL,
  `total_fare` decimal(10,2) DEFAULT NULL,
  `booking_fee` decimal(10,2) DEFAULT NULL,
  `purchase_time` datetime DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'active',
  PRIMARY KEY (`ticket_id`),
  KEY `cid` (`cid`),
  CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Users` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tickets`
--

LOCK TABLES `Tickets` WRITE;
/*!40000 ALTER TABLE `Tickets` DISABLE KEYS */;
INSERT INTO `Tickets` VALUES (1,1,300.00,20.00,'2026-04-29 13:06:38','one-way','active'),(2,2,320.00,20.00,'2026-04-29 13:06:38','one-way','cancelled'),(3,1,300.00,20.00,'2026-04-29 13:47:14','one-way','active'),(4,3,300.00,20.00,'2026-04-29 14:46:52','one-way','waiting'),(6,1,300.00,20.00,'2026-05-01 11:24:36','one-way','waiting'),(7,1,350.00,20.00,'2026-05-01 11:25:10','one-way','active'),(9,1,300.00,20.00,'2026-05-01 11:45:21','one-way','waiting'),(10,1,350.00,20.00,'2026-05-01 11:45:46','one-way','active');
/*!40000 ALTER TABLE `Tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `cid` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'Alice','alice@test.com','alice','123'),(2,'Bob','bob@test.com','bob','123'),(3,'Charlie','charlie@test.com','charlie','123');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Waiting_List`
--

DROP TABLE IF EXISTS `Waiting_List`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Waiting_List` (
  `ticket_id` int NOT NULL,
  `aid` char(2) NOT NULL,
  `flight_number` int NOT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`ticket_id`,`aid`,`flight_number`),
  KEY `aid` (`aid`,`flight_number`),
  CONSTRAINT `waiting_list_ibfk_1` FOREIGN KEY (`ticket_id`) REFERENCES `Tickets` (`ticket_id`),
  CONSTRAINT `waiting_list_ibfk_2` FOREIGN KEY (`aid`, `flight_number`) REFERENCES `Flights` (`aid`, `flight_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Waiting_List`
--

LOCK TABLES `Waiting_List` WRITE;
/*!40000 ALTER TABLE `Waiting_List` DISABLE KEYS */;
INSERT INTO `Waiting_List` VALUES (6,'AA',101,1),(9,'AA',101,2);
/*!40000 ALTER TABLE `Waiting_List` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-01 12:38:01
