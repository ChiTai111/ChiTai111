/*
SQLyog v10.2 
MySQL - 5.5.40 : Database - kt_blog
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kt_blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `kt_blog`;

/*Table structure for table `kt_article` */

DROP TABLE IF EXISTS `kt_article`;

CREATE TABLE `kt_article` (
  `id` bigint(200) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '文章内容',
  `summary` varchar(1024) DEFAULT NULL COMMENT '文章摘要',
  `category_id` bigint(20) DEFAULT NULL COMMENT '所属分类id',
  `thumbnail` varchar(256) DEFAULT NULL COMMENT '缩略图',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶（0否，1是）',
  `status` char(1) DEFAULT '1' COMMENT '状态（0已发布，1草稿）',
  `view_count` bigint(200) DEFAULT '0' COMMENT '访问量',
  `is_comment` char(1) DEFAULT '1' COMMENT '是否允许评论 1是，0否',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

/*Data for the table `kt_article` */

insert  into `kt_article`(`id`,`title`,`content`,`summary`,`category_id`,`thumbnail`,`is_top`,`status`,`view_count`,`is_comment`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`) values(1,'苏州大学','## 学校简介\n\n## 简介\n\n​	**苏州大学**	是国家“211工程”“2011计划”首批入列高校，是教育部与江苏省人民政府共建“双一流”建设高校、国家航天局共建高校，是江苏省属重点综合性大学。苏州大学前身是Soochow University（东吴大学，1900年创办），开现代高等教育之先河，融中西文化之菁华，是中国最早以现代大学学科体系举办的大学。在中国高等教育史上，东吴大学是最早开展研究生教育并授予硕士学位、最先开展法学（英美法）专业教育，也是第一家创办学报的大学。','苏州大学简介',1,null,'1','0',105,'0',NULL,'2024-01-23 23:20:11',NULL,NULL,0),
                                                                                                                                                                                                        (2,'weq','adadaeqe','adad',2,NULL,'1','0',22,'0',NULL,'2024-01-21 14:58:30',NULL,NULL,1),
                                                                                                                                                                                                        (3,'dad','asdasda','sadad',1,NULL,'1','0',33,'0',NULL,'2024-01-18 14:58:34',NULL,NULL,1),
                                                                                                                                                                                                        (4,'sdad','## sda \r\n\r\n222\r\n### sdasd newnewnew',NULL,2,'','1','0',44,'0',NULL,'2024-01-17 14:58:37',NULL,NULL,0);


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
