CREATE DATABASE  IF NOT EXISTS `to_do_list_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `to_do_list_db`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: to_do_list_db
-- ------------------------------------------------------
-- Server version	8.0.43

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

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `ac_id` bigint NOT NULL AUTO_INCREMENT,
  `ac_completed` bit(1) DEFAULT NULL,
  `ac_description` varchar(255) NOT NULL,
  `ac_due_date` date NOT NULL,
  `ac_tag_id` bigint DEFAULT NULL,
  `ac_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`ac_id`),
  KEY `fk_tag` (`ac_tag_id`),
  KEY `FK8y0s2ofu67x4s512xphasn7ok` (`ac_user_id`),
  CONSTRAINT `FK8y0s2ofu67x4s512xphasn7ok` FOREIGN KEY (`ac_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_tag` FOREIGN KEY (`ac_tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` VALUES (9,_binary '\0','CP372 Review','2026-04-18',7,NULL),(14,_binary '\0','My birthday kkk','2026-06-01',9,NULL),(20,_binary '\0','CP372 Review','2026-04-19',7,NULL),(21,_binary '\0','HTML/CSS','2026-04-18',8,NULL),(22,_binary '\0','HTML/CSS','2026-04-19',8,NULL),(23,_binary '\0','CP372 Exam - LH 3094 - 7pm','2026-04-20',7,NULL),(24,_binary '\0','CP312 - US - 8:30am','2026-04-23',7,NULL),(25,_binary '\0','CP312 Review','2026-04-21',7,NULL),(26,_binary '\0','CP312 Review','2026-04-22',7,NULL),(27,_binary '\0','HTML/CSS','2026-04-21',8,NULL),(28,_binary '\0','Go to Toronto','2026-04-24',9,NULL),(29,_binary '\0','HTML/CSS','2026-04-22',8,NULL),(31,_binary '\0','Interview Questions','2026-04-25',8,NULL),(33,_binary '\0','Back-end knowledge','2026-04-26',8,NULL);
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `tag_id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (7,'Schoolwork'),(8,'Work'),(9,'Other');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_email` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_role` varchar(255) NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK33uo7vet9c79ydfuwg1w848f` (`user_email`),
  UNIQUE KEY `UKk8d0f2n7n88w1a16yhua64onx` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'to_do_list_db'
--

--
-- Dumping routines for database 'to_do_list_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-04 19:41:49
