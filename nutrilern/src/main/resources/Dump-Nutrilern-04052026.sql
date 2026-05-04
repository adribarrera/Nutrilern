CREATE DATABASE  IF NOT EXISTS `nutrilern` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `nutrilern`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: gateway01.eu-central-1.prod.aws.tidbcloud.com    Database: nutrilern
-- ------------------------------------------------------
-- Server version	8.0.11-TiDB-v8.5.3-serverless

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
-- Table structure for table `Alimento`
--

DROP TABLE IF EXISTS `Alimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Alimento` (
  `id_alimento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `marca` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `kcal` double DEFAULT NULL,
  `grasas` double DEFAULT NULL,
  `grasas_saturadas` double DEFAULT NULL,
  `hidratos_carbono` double DEFAULT NULL,
  `azucares` double DEFAULT NULL,
  `proteinas` double DEFAULT NULL,
  `sal` double DEFAULT NULL,
  `id_categoria_fk` int DEFAULT NULL,
  PRIMARY KEY (`id_alimento`) /*T![clustered_index] CLUSTERED */,
  KEY `fk_1` (`id_categoria_fk`),
  CONSTRAINT `fk_1` FOREIGN KEY (`id_categoria_fk`) REFERENCES `Categoria_Alimento` (`id_categoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=60001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Alimento`
--

LOCK TABLES `Alimento` WRITE;
/*!40000 ALTER TABLE `Alimento` DISABLE KEYS */;
INSERT INTO `Alimento` VALUES (1,'Salteado Rústico','Mercadona',76,0.1,0,15,6.5,2.2,0.09,5),(2,'Queso Mozzarella Rallado','Hacendado',283,21,13,2.5,0.8,21,1,4),(3,'Arroz Redondo','Hacendado',344,1,0.2,75,0.5,8.2,0.01,8),(4,'Leche Entera UHT','Hacendado',63,3.6,2.4,4.6,4.6,3.1,0.13,4),(5,'Filetes Lomo Duroc','Mercadona',152,8.9,3.2,0,0,18,0.15,1),(6,'Huevos Frescos','Hacendado',150,11.1,3.1,0.5,0.5,12.5,0.36,3),(30001,'Aceite de Oliva Virgen Extra','Hacendado',822,91.4,0,0,0,0,0,10),(30002,'Cereales con chocolate blanco rellenos de leche','Mercadona',488,22,0,65,0,7.2,0,8),(30003,'Zanahorias','Horcaol',20,1,0,2,0,4,0,5);
/*!40000 ALTER TABLE `Alimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Categoria_Alimento`
--

DROP TABLE IF EXISTS `Categoria_Alimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Categoria_Alimento` (
  `id_categoria` int NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_categoria`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Categoria_Alimento`
--

LOCK TABLES `Categoria_Alimento` WRITE;
/*!40000 ALTER TABLE `Categoria_Alimento` DISABLE KEYS */;
INSERT INTO `Categoria_Alimento` VALUES (1,'Carnes y Aves'),(2,'Pescados y Mariscos'),(3,'Huevos'),(4,'Lácteos y Derivados'),(5,'Verduras y Hortalizas'),(6,'Frutas'),(7,'Legumbres'),(8,'Cereales y Tubérculos'),(9,'Frutos Secos y Semillas'),(10,'Aceites y Grasas'),(11,'Dulces y Azúcares'),(12,'Panadería y Bollería'),(13,'Salsas y Condimentos'),(14,'Bebidas (Sin Alcohol)'),(15,'Bebidas Alcohólicas'),(16,'Embutidos y Fiambres'),(17,'Snacks y Aperitivos'),(18,'Platos Preparados / Fast Food'),(19,'Suplementos Deportivos'),(20,'Otras categorías'),(100,'Sin Categoria');
/*!40000 ALTER TABLE `Categoria_Alimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Historial_Peso`
--

DROP TABLE IF EXISTS `Historial_Peso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Historial_Peso` (
  `id_historial` int NOT NULL AUTO_INCREMENT,
  `id_usuario_fk` int NOT NULL,
  `peso` double NOT NULL,
  `fecha` date DEFAULT (CURRENT_DATE),
  PRIMARY KEY (`id_historial`) /*T![clustered_index] CLUSTERED */,
  KEY `fk_1` (`id_usuario_fk`),
  CONSTRAINT `fk_1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `Usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=150001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Historial_Peso`
--

LOCK TABLES `Historial_Peso` WRITE;
/*!40000 ALTER TABLE `Historial_Peso` DISABLE KEYS */;
INSERT INTO `Historial_Peso` VALUES (1,90001,90,'2026-04-30'),(2,90001,130,'2026-04-30'),(3,90001,80,'2026-04-30'),(4,90001,85,'2026-04-30'),(5,90001,135,'2026-04-30'),(60001,150001,80,'2026-05-04'),(90001,90001,95,'2026-05-04'),(120001,150001,70,'2026-05-04'),(120002,150001,70,'2026-05-04');
/*!40000 ALTER TABLE `Historial_Peso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Objetivo_Usuario`
--

DROP TABLE IF EXISTS `Objetivo_Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Objetivo_Usuario` (
  `id_objetivo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_objetivo`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=30001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Objetivo_Usuario`
--

LOCK TABLES `Objetivo_Usuario` WRITE;
/*!40000 ALTER TABLE `Objetivo_Usuario` DISABLE KEYS */;
INSERT INTO `Objetivo_Usuario` VALUES (1,'Perder Grasa','Déficit calórico para reducir tejido adiposo.'),(2,'Mantener','Equilibrio calórico para mantener el peso actual.'),(3,'Ganar Volumen','Superávit calórico para aumentar masa muscular.');
/*!40000 ALTER TABLE `Objetivo_Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Registro_Diario`
--

DROP TABLE IF EXISTS `Registro_Diario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Registro_Diario` (
  `id_registro` int NOT NULL AUTO_INCREMENT,
  `cantidad_gramos` double DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `tipo_comida` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_usuario_fk` int DEFAULT NULL,
  `id_alimento_fk` int DEFAULT NULL,
  `kcal` double DEFAULT NULL,
  `grasas` double DEFAULT NULL,
  `grasas_saturadas` double DEFAULT NULL,
  `hidratos_carbono` double DEFAULT NULL,
  `azucares` double DEFAULT NULL,
  `proteinas` double DEFAULT NULL,
  `sal` double DEFAULT NULL,
  PRIMARY KEY (`id_registro`) /*T![clustered_index] CLUSTERED */,
  KEY `fk_1` (`id_alimento_fk`),
  KEY `fk_2` (`id_usuario_fk`),
  CONSTRAINT `fk_1` FOREIGN KEY (`id_alimento_fk`) REFERENCES `Alimento` (`id_alimento`),
  CONSTRAINT `fk_2` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=210001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Registro_Diario`
--

LOCK TABLES `Registro_Diario` WRITE;
/*!40000 ALTER TABLE `Registro_Diario` DISABLE KEYS */;
INSERT INTO `Registro_Diario` VALUES (1,100,'2026-04-30','General',90001,3,344,1,0.2,75,0.5,8.2,0.01),(2,100,'2026-04-30','General',90001,5,152,8.9,3.2,0,0,18,0.15),(30001,100,'2026-05-04','General',150001,1,76,0.1,0,15,6.5,2.2,0.09),(30002,100,'2026-05-04','General',150001,5,152,8.9,3.2,0,0,18,0.15),(90001,0,'2026-05-04','General',150001,5,152,8.9,3.2,0,0,18,0.15),(120001,0,'2026-05-04','General',90001,3,344,1,0.2,75,0.5,8.2,0.01),(150001,1000,'2026-05-04','General',90001,5,1520,89,32,0,0,180,1.5),(180001,3000,'2026-05-04','General',90001,5,4560,267,96,0,0,540,4.5);
/*!40000 ALTER TABLE `Registro_Diario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `passwd` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellidos` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `edad` int DEFAULT NULL,
  `altura` int DEFAULT NULL,
  `peso_inicial` decimal(10,2) DEFAULT NULL,
  `rol` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'Cliente',
  `id_objetivo_fk` int NOT NULL,
  `sexo` varchar(1) COLLATE utf8mb4_general_ci DEFAULT 'M',
  PRIMARY KEY (`id_usuario`) /*T![clustered_index] CLUSTERED */,
  UNIQUE KEY `email` (`email`),
  KEY `fk_1` (`id_objetivo_fk`),
  CONSTRAINT `fk_1` FOREIGN KEY (`id_objetivo_fk`) REFERENCES `Objetivo_Usuario` (`id_objetivo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=270001;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES (90001,'jabeji@alumnos.ilerna.com','$2a$12$51e8JcCCu5LOCVW9iAORLuTbVwvzucuSriRIwSNNFiN32fE1dTASO','Adrián','Barrera Fernández',24,190,95.00,'ADMIN',2,'M'),(150001,'dariorumi5@gmail.com','$2a$12$k6F4QxVKNdysLPqx/qNjbOagkscgdntW9zimJtLaEHraB0leUrOma','Darío','Rumí',20,170,70.00,'USUARIO',1,'H');
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'nutrilern'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-04 22:31:59
