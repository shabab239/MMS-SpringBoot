/*
SQLyog Professional v13.1.1 (64 bit)
MySQL - 10.4.11-MariaDB : Database - mezz
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mezz` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `mezz`;

/*Table structure for table `core_bills` */

DROP TABLE IF EXISTS `core_bills`;

CREATE TABLE `core_bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `final_amount` double NOT NULL,
  `meal_cost` double NOT NULL,
  `mess_id` bigint(20) NOT NULL,
  `month` int(11) NOT NULL,
  `utility_cost` double NOT NULL,
  `year` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4u5862c4gj2ekqi47ey07reax` (`user_id`),
  CONSTRAINT `FK4u5862c4gj2ekqi47ey07reax` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `core_bills` */

/*Table structure for table `core_meals` */

DROP TABLE IF EXISTS `core_meals`;

CREATE TABLE `core_meals` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `day1` double NOT NULL DEFAULT 0,
  `day10` double NOT NULL DEFAULT 0,
  `day11` double NOT NULL DEFAULT 0,
  `day12` double NOT NULL DEFAULT 0,
  `day13` double NOT NULL DEFAULT 0,
  `day14` double NOT NULL DEFAULT 0,
  `day15` double NOT NULL DEFAULT 0,
  `day16` double NOT NULL DEFAULT 0,
  `day17` double NOT NULL DEFAULT 0,
  `day18` double NOT NULL DEFAULT 0,
  `day19` double NOT NULL DEFAULT 0,
  `day2` double NOT NULL DEFAULT 0,
  `day20` double NOT NULL DEFAULT 0,
  `day21` double NOT NULL DEFAULT 0,
  `day22` double NOT NULL DEFAULT 0,
  `day23` double NOT NULL DEFAULT 0,
  `day24` double NOT NULL DEFAULT 0,
  `day25` double NOT NULL DEFAULT 0,
  `day26` double NOT NULL DEFAULT 0,
  `day27` double NOT NULL DEFAULT 0,
  `day28` double NOT NULL DEFAULT 0,
  `day29` double NOT NULL DEFAULT 0,
  `day3` double NOT NULL DEFAULT 0,
  `day30` double NOT NULL DEFAULT 0,
  `day31` double NOT NULL DEFAULT 0,
  `day4` double NOT NULL DEFAULT 0,
  `day5` double NOT NULL DEFAULT 0,
  `day6` double NOT NULL DEFAULT 0,
  `day7` double NOT NULL DEFAULT 0,
  `day8` double NOT NULL DEFAULT 0,
  `day9` double NOT NULL DEFAULT 0,
  `mess_id` bigint(20) NOT NULL,
  `month` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqafp666obdpigcckkvm7nvdi3` (`user_id`),
  CONSTRAINT `FKqafp666obdpigcckkvm7nvdi3` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `core_meals` */

insert  into `core_meals`(`id`,`day1`,`day10`,`day11`,`day12`,`day13`,`day14`,`day15`,`day16`,`day17`,`day18`,`day19`,`day2`,`day20`,`day21`,`day22`,`day23`,`day24`,`day25`,`day26`,`day27`,`day28`,`day29`,`day3`,`day30`,`day31`,`day4`,`day5`,`day6`,`day7`,`day8`,`day9`,`mess_id`,`month`,`year`,`user_id`) values 
(1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,10,2024,1),
(2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,10,2024,2);

/*Table structure for table `core_purchases` */

DROP TABLE IF EXISTS `core_purchases`;

CREATE TABLE `core_purchases` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  `date` date NOT NULL,
  `description` varchar(255) NOT NULL,
  `mess_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9g7b7y8gmo72rf5b8yv94ykkl` (`user_id`),
  CONSTRAINT `FK9g7b7y8gmo72rf5b8yv94ykkl` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `core_purchases` */

insert  into `core_purchases`(`id`,`cost`,`date`,`description`,`mess_id`,`user_id`) values 
(1,100,'2024-10-22','Potol',1,1),
(2,210,'2024-10-22','Chicken',1,2);

/*Table structure for table `core_transactions` */

DROP TABLE IF EXISTS `core_transactions`;

CREATE TABLE `core_transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  `mess_id` bigint(20) NOT NULL,
  `type` enum('DEPOSIT','WITHDRAW') NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhakh2f2utjposadsadp1ji1e1` (`user_id`),
  CONSTRAINT `FKhakh2f2utjposadsadp1ji1e1` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `core_transactions` */

insert  into `core_transactions`(`id`,`amount`,`date`,`mess_id`,`type`,`user_id`) values 
(1,500,'2024-10-22',1,'DEPOSIT',2),
(2,500,'2024-10-22',1,'DEPOSIT',1);

/*Table structure for table `core_utilities` */

DROP TABLE IF EXISTS `core_utilities`;

CREATE TABLE `core_utilities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  `mess_id` bigint(20) NOT NULL,
  `month` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `core_utilities` */

insert  into `core_utilities`(`id`,`cost`,`mess_id`,`month`,`year`) values 
(1,100,1,10,2024),
(2,150,1,10,2024);

/*Table structure for table `sec_messes` */

DROP TABLE IF EXISTS `sec_messes`;

CREATE TABLE `sec_messes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balance` double NOT NULL DEFAULT 0,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4lgagivirw720ngbhxseh9e2b` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sec_messes` */

insert  into `sec_messes`(`id`,`balance`,`name`) values 
(1,440,'Shabab\'s Mess'),
(2,0,'Utso\'s Mess');

/*Table structure for table `sec_users` */

DROP TABLE IF EXISTS `sec_users`;

CREATE TABLE `sec_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `balance` double NOT NULL DEFAULT 0,
  `cell` varchar(11) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ROLE_ADMIN','ROLE_MANAGER','ROLE_MEMBER') NOT NULL,
  `mess_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjp9f1xtuq2lpe6ky4me6yib1w` (`cell`),
  UNIQUE KEY `UKcfu0ko0i9l8afdu520rvtf318` (`email`),
  KEY `FKsu596blwmb7k1fg41shrco8pa` (`mess_id`),
  CONSTRAINT `FKsu596blwmb7k1fg41shrco8pa` FOREIGN KEY (`mess_id`) REFERENCES `sec_messes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sec_users` */

insert  into `sec_users`(`id`,`avatar`,`balance`,`cell`,`email`,`name`,`password`,`role`,`mess_id`) values 
(1,NULL,500,'01700000000','test@email.com','Shabab Ahmed','$2a$12$fLpb.X9IDfLMZl0Az/pcqeSo97F79pzQ2GLBMoZsqdUOQJMkHV6sK','ROLE_ADMIN',1),
(2,NULL,500,'01900000000','test2@email.com','Shadman Labib','$2a$12$RjHxvAQEKpjKWbUxk3iLEeWeWUpe2691B.ojv.zN7JPGkuzQeTQmu','ROLE_MANAGER',1),
(3,NULL,0,'01600000000','test4@email.com','Amit Daa','$2a$12$.Mt6agF1UjDMezfPV1lLReHVp816vaMU/WfAEdU0ERPMb9juVNNoG','ROLE_MEMBER',2),
(4,NULL,0,'01800000000','test3@email.com','Ahamad Ullah','$2a$12$enhkoifp0kWBvev6e5qQLeIXYD.xDW4VJpXCaKWTDPFczo1alT.Sm','ROLE_ADMIN',2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
