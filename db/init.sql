CREATE DATABASE api_doc;
USE api_doc;

DROP TABLE IF EXISTS `api_doc_project`;
CREATE TABLE `api_doc_project` (
  `id` varchar(32) NOT NULL,
  `create_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `update_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name_idx` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_doc_menu`;
CREATE TABLE `api_doc_menu` (
  `id` varchar(32) NOT NULL,
  `create_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `update_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT NULL DEFAULT '0',
  `desc` varchar(255) NOT NULL,
  `mapping` varchar(255) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `api_doc_project_id` varchar(255) NOT NULL,
  `api_doc_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id_idx` (`parent_id`),
  KEY `api_doc_project_id_idx` (`api_doc_project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_doc`;
CREATE TABLE `api_doc` (
  `id` varchar(32) NOT NULL,
  `create_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `update_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT NULL DEFAULT '0',
  `url` varchar(255) NOT NULL,
  `action` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `api_doc_project_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_doc_field`;
CREATE TABLE `api_doc_field` (
  `id` varchar(32) NOT NULL,
  `create_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `update_date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `required` bit(1) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `params_type` varchar(255) NOT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `api_doc_id` varchar(32) NOT NULL,
  `api_doc_project_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id_idx` (`parent_id`),
  KEY `api_doc_id_idx` (`api_doc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;