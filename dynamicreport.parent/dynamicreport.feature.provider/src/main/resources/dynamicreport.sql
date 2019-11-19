/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : dynamicreport

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2019-11-18 10:48:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dm_data_list
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_list`;
CREATE TABLE `dm_data_list` (
  `dl_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dl_identifier` varchar(100) NOT NULL,
  `dl_name` varchar(100) NOT NULL,
  `dl_query_sql` longtext NOT NULL,
  `dl_data_source` bigint(20) NOT NULL,
  `dl_record_cnt` varchar(10) DEFAULT NULL,
  `dl_description` varchar(500) DEFAULT NULL,
  `dl_status` varchar(20) DEFAULT NULL,
  `dl_created_date` datetime DEFAULT NULL,
  `dl_created_user` varchar(50) DEFAULT NULL,
  `dl_modified_date` datetime DEFAULT NULL,
  `dl_modified_user` varchar(50) DEFAULT NULL,
  `dl_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`dl_id`),
  KEY `FK_Reference_datasourcedatalist` (`dl_data_source`),
  CONSTRAINT `FK_Reference_datasourcedatalist` FOREIGN KEY (`dl_data_source`) REFERENCES `dm_data_source` (`ds_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据集';

-- ----------------------------
-- Records of dm_data_list
-- ----------------------------

-- ----------------------------
-- Table structure for dm_data_list_rel_param_group
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_list_rel_param_group`;
CREATE TABLE `dm_data_list_rel_param_group` (
  `dlrpg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dlrpg_data_list` bigint(20) NOT NULL,
  `dlrpg_data_parameter_group` bigint(20) NOT NULL,
  PRIMARY KEY (`dlrpg_id`),
  KEY `FK_Reference_datalistrelgroup` (`dlrpg_data_list`),
  KEY `FK_Reference_dataparametergrouplist` (`dlrpg_data_parameter_group`),
  CONSTRAINT `FK_Reference_datalistrelgroup` FOREIGN KEY (`dlrpg_data_list`) REFERENCES `dm_data_list` (`dl_id`),
  CONSTRAINT `FK_Reference_dataparametergrouplist` FOREIGN KEY (`dlrpg_data_parameter_group`) REFERENCES `dm_data_parameter_group` (`dpg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据集关联参数组';

-- ----------------------------
-- Records of dm_data_list_rel_param_group
-- ----------------------------

-- ----------------------------
-- Table structure for dm_data_parameter
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_parameter`;
CREATE TABLE `dm_data_parameter` (
  `dp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dp_identifier` varchar(100) NOT NULL,
  `dp_param_name` varchar(100) NOT NULL,
  `dp_name` varchar(100) NOT NULL,
  `dp_param_content` varchar(1000) NOT NULL,
  `dp_prefix` varchar(10) DEFAULT NULL,
  `dp_suffix` varchar(10) DEFAULT NULL,
  `dp_mandatory` varchar(1) DEFAULT NULL,
  `dp_description` varchar(500) DEFAULT NULL,
  `dp_status` varchar(20) DEFAULT NULL,
  `dp_created_date` datetime DEFAULT NULL,
  `dp_created_user` varchar(50) DEFAULT NULL,
  `dp_modified_date` datetime DEFAULT NULL,
  `dp_modified_user` varchar(50) DEFAULT NULL,
  `dp_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`dp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据参数';

-- ----------------------------
-- Records of dm_data_parameter
-- ----------------------------

-- ----------------------------
-- Table structure for dm_data_parameter_group
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_parameter_group`;
CREATE TABLE `dm_data_parameter_group` (
  `dpg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dpg_identifier` varchar(100) NOT NULL,
  `dpg_name` varchar(100) NOT NULL,
  `dpg_content` varchar(2000) NOT NULL,
  `dpg_description` varchar(500) DEFAULT NULL,
  `dpg_status` varchar(20) DEFAULT NULL,
  `dpg_created_date` datetime DEFAULT NULL,
  `dpg_created_user` varchar(50) DEFAULT NULL,
  `dpg_modified_date` datetime DEFAULT NULL,
  `dpg_modified_user` varchar(50) DEFAULT NULL,
  `dpg_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`dpg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据参数组';

-- ----------------------------
-- Records of dm_data_parameter_group
-- ----------------------------

-- ----------------------------
-- Table structure for dm_data_source
-- ----------------------------
DROP TABLE IF EXISTS `dm_data_source`;
CREATE TABLE `dm_data_source` (
  `ds_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ds_identifier` varchar(100) NOT NULL,
  `ds_name` varchar(100) NOT NULL,
  `ds_url` varchar(500) NOT NULL,
  `ds_driver_class` varchar(100) NOT NULL,
  `ds_username` varchar(50) NOT NULL,
  `ds_password` varchar(64) DEFAULT NULL,
  `ds_description` varchar(500) DEFAULT NULL,
  `ds_hive_kerberos1` varchar(1) DEFAULT NULL,
  `ds_hive_kerberos` varchar(1) DEFAULT NULL,
  `ds_hive_krb5` varchar(100) DEFAULT NULL,
  `ds_hive_keytab` varchar(100) DEFAULT NULL,
  `ds_status` varchar(20) DEFAULT NULL,
  `ds_created_date` datetime DEFAULT NULL,
  `ds_created_user` varchar(50) DEFAULT NULL,
  `ds_modified_date` datetime DEFAULT NULL,
  `ds_modified_user` varchar(50) DEFAULT NULL,
  `ds_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`ds_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源';

-- ----------------------------
-- Records of dm_data_source
-- ----------------------------

-- ----------------------------
-- Table structure for dm_param_group_rel_param
-- ----------------------------
DROP TABLE IF EXISTS `dm_param_group_rel_param`;
CREATE TABLE `dm_param_group_rel_param` (
  `pgrp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pgrp_data_parameter` bigint(20) NOT NULL,
  `pgrp_data_parameter_group` bigint(20) NOT NULL,
  PRIMARY KEY (`pgrp_id`),
  KEY `FK_Reference_dataparameter` (`pgrp_data_parameter`),
  KEY `FK_Reference_dataparameteregroup` (`pgrp_data_parameter_group`),
  CONSTRAINT `FK_Reference_dataparameter` FOREIGN KEY (`pgrp_data_parameter`) REFERENCES `dm_data_parameter` (`dp_id`),
  CONSTRAINT `FK_Reference_dataparameteregroup` FOREIGN KEY (`pgrp_data_parameter_group`) REFERENCES `dm_data_parameter_group` (`dpg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参数组关联参数';

-- ----------------------------
-- Records of dm_param_group_rel_param
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_chart_template
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_chart_template`;
CREATE TABLE `rpt_cfg_chart_template` (
  `t_id` varchar(32) NOT NULL,
  `t_name` varchar(40) NOT NULL,
  `t_content` text NOT NULL,
  `t_status` varchar(10) NOT NULL,
  `t_created_date` datetime NOT NULL,
  `t_created_user` varchar(20) NOT NULL,
  `t_modified_date` datetime DEFAULT NULL,
  `t_modified_user` varchar(20) DEFAULT NULL,
  `t_version_no` int(11) NOT NULL,
  PRIMARY KEY (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_chart_template
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_col_drill_param
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_col_drill_param`;
CREATE TABLE `rpt_cfg_col_drill_param` (
  `dp_id` varchar(32) NOT NULL,
  `dp_component_col` varchar(32) DEFAULT NULL,
  `dp_param_name` varchar(40) NOT NULL,
  `dp_tgt_param_name` varchar(40) NOT NULL,
  PRIMARY KEY (`dp_id`),
  KEY `FK_Reference_component_col_page_col` (`dp_component_col`),
  CONSTRAINT `FK_Reference_component_col_page_col` FOREIGN KEY (`dp_component_col`) REFERENCES `rpt_cfg_component_col` (`cc_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_col_drill_param
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_component
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_component`;
CREATE TABLE `rpt_cfg_component` (
  `cmpt_id` varchar(32) NOT NULL,
  `cmpt_name` varchar(40) NOT NULL,
  `cmpt_type` varchar(32) DEFAULT NULL,
  `cmpt_template` varchar(32) DEFAULT NULL,
  `cmpt_data_list_id` bigint(20) DEFAULT NULL,
  `cmpt_fixed_col` varchar(2) DEFAULT NULL,
  `cmpt_pagination` varchar(1) DEFAULT NULL,
  `cmpt_remark` varchar(200) DEFAULT NULL,
  `cmpt_status` varchar(10) NOT NULL,
  `cmpt_created_date` datetime NOT NULL,
  `cmpt_created_user` varchar(20) NOT NULL,
  `cmpt_modified_date` datetime DEFAULT NULL,
  `cmpt_modified_user` varchar(20) DEFAULT NULL,
  `cmpt_version_no` int(11) NOT NULL,
  PRIMARY KEY (`cmpt_id`),
  KEY `FK_Reference_component_component_type` (`cmpt_type`),
  KEY `FK_Reference_component_template` (`cmpt_template`),
  CONSTRAINT `FK_Reference_component_component_type` FOREIGN KEY (`cmpt_type`) REFERENCES `rpt_cfg_component_type` (`ct_id`),
  CONSTRAINT `FK_Reference_component_template` FOREIGN KEY (`cmpt_template`) REFERENCES `rpt_cfg_chart_template` (`t_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_component
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_component_col
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_component_col`;
CREATE TABLE `rpt_cfg_component_col` (
  `cc_id` varchar(32) NOT NULL,
  `cc_group` varchar(32) DEFAULT NULL,
  `cc_component` varchar(32) DEFAULT NULL,
  `cc_page` varchar(32) DEFAULT NULL,
  `cc_type` varchar(10) NOT NULL,
  `cc_name` varchar(32) NOT NULL,
  `cc_col_name` varchar(32) NOT NULL,
  `cc_col_func` varchar(10) DEFAULT NULL,
  `cc_col_alias` varchar(32) DEFAULT NULL,
  `cc_len` varchar(4) DEFAULT NULL,
  `cc_selected` varchar(1) DEFAULT NULL,
  `cc_row_merge` varchar(1) DEFAULT NULL,
  `cc_row_summary` varchar(1) DEFAULT NULL,
  `cc_pivot` varchar(1) DEFAULT NULL,
  `cc_pivot_selected` varchar(1) DEFAULT NULL,
  `cc_sequence` int(11) DEFAULT NULL,
  `cc_status` varchar(10) NOT NULL,
  `cc_created_date` datetime NOT NULL,
  `cc_created_user` varchar(20) NOT NULL,
  `cc_modified_date` datetime DEFAULT NULL,
  `cc_modified_user` varchar(20) DEFAULT NULL,
  `cc_version_no` int(11) NOT NULL,
  PRIMARY KEY (`cc_id`),
  KEY `FK_Reference_component_col_component` (`cc_component`),
  KEY `FK_Reference_component_col_group` (`cc_group`),
  CONSTRAINT `FK_Reference_component_col_component` FOREIGN KEY (`cc_component`) REFERENCES `rpt_cfg_component` (`cmpt_id`),
  CONSTRAINT `FK_Reference_component_col_group` FOREIGN KEY (`cc_group`) REFERENCES `rpt_cfg_component_col_group` (`ccg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_component_col
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_component_col_group
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_component_col_group`;
CREATE TABLE `rpt_cfg_component_col_group` (
  `ccg_id` varchar(32) NOT NULL,
  `ccg_name` varchar(20) NOT NULL,
  `ccg_display` varchar(1) DEFAULT NULL,
  `ccg_status` varchar(10) NOT NULL,
  `ccg_created_date` datetime NOT NULL,
  `ccg_created_user` varchar(20) NOT NULL,
  `ccg_modified_date` datetime DEFAULT NULL,
  `ccg_modified_user` varchar(20) DEFAULT NULL,
  `ccg_version_no` int(11) NOT NULL,
  PRIMARY KEY (`ccg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_component_col_group
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_component_type
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_component_type`;
CREATE TABLE `rpt_cfg_component_type` (
  `ct_id` varchar(32) NOT NULL,
  `ct_key` varchar(30) NOT NULL,
  `ct_name` varchar(20) NOT NULL,
  `ct_parent` varchar(32) DEFAULT NULL,
  `ct_remark` varchar(40) DEFAULT NULL,
  `ct_sequence` int(11) DEFAULT NULL,
  `ct_status` varchar(10) NOT NULL,
  `ct_created_date` datetime NOT NULL,
  `ct_created_user` varchar(20) NOT NULL,
  `ct_modified_date` datetime DEFAULT NULL,
  `ct_modified_user` varchar(20) DEFAULT NULL,
  `ct_version_no` int(11) NOT NULL,
  PRIMARY KEY (`ct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_component_type
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_page
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_page`;
CREATE TABLE `rpt_cfg_page` (
  `pg_id` varchar(32) NOT NULL,
  `pg_title` varchar(30) NOT NULL,
  `pg_remark` text,
  `pg_published` varchar(1) DEFAULT NULL,
  `pg_published_path` varchar(256) DEFAULT NULL,
  `pg_status` varchar(10) NOT NULL,
  `pg_created_date` datetime NOT NULL,
  `pg_created_user` varchar(20) NOT NULL,
  `pg_modified_date` datetime DEFAULT NULL,
  `pg_modified_user` varchar(20) DEFAULT NULL,
  `pg_version_no` int(11) NOT NULL,
  PRIMARY KEY (`pg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_page
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_page_component
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_page_component`;
CREATE TABLE `rpt_cfg_page_component` (
  `pc_id` varchar(32) NOT NULL,
  `pc_page` varchar(32) DEFAULT NULL,
  `pc_component` varchar(32) DEFAULT NULL,
  `pc_sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`pc_id`),
  KEY `FK_Reference_component_page` (`pc_page`),
  KEY `FK_Reference_page_component` (`pc_component`),
  CONSTRAINT `FK_Reference_component_page` FOREIGN KEY (`pc_page`) REFERENCES `rpt_cfg_page` (`pg_id`),
  CONSTRAINT `FK_Reference_page_component` FOREIGN KEY (`pc_component`) REFERENCES `rpt_cfg_component` (`cmpt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_page_component
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_page_param
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_page_param`;
CREATE TABLE `rpt_cfg_page_param` (
  `pp_id` varchar(32) NOT NULL,
  `pp_page` varchar(32) DEFAULT NULL,
  `pp_param` varchar(32) DEFAULT NULL,
  `pp_sequence` int(11) DEFAULT NULL,
  `pp_param_value` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`pp_id`),
  KEY `FK_Reference_page_param_page` (`pp_page`),
  KEY `FK_Reference_page_param_param` (`pp_param`),
  CONSTRAINT `FK_Reference_page_param_page` FOREIGN KEY (`pp_page`) REFERENCES `rpt_cfg_page` (`pg_id`),
  CONSTRAINT `FK_Reference_page_param_param` FOREIGN KEY (`pp_param`) REFERENCES `rpt_cfg_param` (`cp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_page_param
-- ----------------------------

-- ----------------------------
-- Table structure for rpt_cfg_param
-- ----------------------------
DROP TABLE IF EXISTS `rpt_cfg_param`;
CREATE TABLE `rpt_cfg_param` (
  `cp_id` varchar(32) NOT NULL,
  `cp_type` varchar(32) DEFAULT NULL,
  `cp_name` varchar(40) NOT NULL,
  `cp_param_name` varchar(40) NOT NULL,
  `cp_label` varchar(20) DEFAULT NULL,
  `cp_len` varchar(4) DEFAULT NULL,
  `cp_format` varchar(20) DEFAULT NULL,
  `cp_required` varchar(1) DEFAULT NULL,
  `cp_data_list_id` bigint(20) DEFAULT NULL,
  `cp_status` varchar(10) NOT NULL,
  `cp_created_date` datetime NOT NULL,
  `cp_created_user` varchar(20) NOT NULL,
  `cp_modified_date` datetime DEFAULT NULL,
  `cp_modified_user` varchar(20) DEFAULT NULL,
  `cp_version_no` int(11) NOT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `FK_Reference_parameter_component_type` (`cp_type`),
  CONSTRAINT `FK_Reference_parameter_component_type` FOREIGN KEY (`cp_type`) REFERENCES `rpt_cfg_component_type` (`ct_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rpt_cfg_param
-- ----------------------------

-- ----------------------------
-- Table structure for sys_cache_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_cache_service`;
CREATE TABLE `sys_cache_service` (
  `cs_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cs_server` bigint(20) NOT NULL,
  `cs_context_path` varchar(200) DEFAULT NULL,
  `cs_service_path` varchar(200) DEFAULT NULL,
  `cs_http_method` varchar(20) DEFAULT NULL,
  `cs_description` varchar(500) DEFAULT NULL,
  `cs_status` varchar(20) DEFAULT NULL,
  `cs_created_date` datetime DEFAULT NULL,
  `cs_created_user` varchar(40) DEFAULT NULL,
  `cs_modified_date` datetime DEFAULT NULL,
  `cs_modified_user` varchar(40) DEFAULT NULL,
  `cs_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`cs_id`),
  KEY `FK_Reference_contextpathrelserver` (`cs_server`),
  CONSTRAINT `FK_Reference_contextpathrelserver` FOREIGN KEY (`cs_server`) REFERENCES `sys_server_list` (`sc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='缓存服务';

-- ----------------------------
-- Records of sys_cache_service
-- ----------------------------

-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function` (
  `func_id` varchar(100) NOT NULL,
  `func_name` varchar(50) NOT NULL,
  `func_parent` varchar(100) DEFAULT NULL,
  `func_type` varchar(20) DEFAULT NULL,
  `func_sequence` int(11) DEFAULT NULL,
  `func_description` varchar(200) DEFAULT NULL,
  `func_status` varchar(20) DEFAULT NULL,
  `func_created_date` datetime DEFAULT NULL,
  `func_created_user` varchar(40) DEFAULT NULL,
  `func_modified_date` datetime DEFAULT NULL,
  `func_modified_user` varchar(40) DEFAULT NULL,
  `func_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`func_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能信息';

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES ('cfgChartTemplate:delete', '模板删除', 'cfgChartTemplateMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-03-22 09:28:15', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgChartTemplate:query', '模板查询', 'cfgChartTemplateMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-03-22 09:28:15', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgChartTemplate:saveOrUpdate', '模板新/修改', 'cfgChartTemplateMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-03-22 09:28:15', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgChartTemplateMgr', '模板配置', 'cfgReportMgr', 'menu', null, '备注', 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-09-29 15:25:17', 'admin', '3');
INSERT INTO `sys_function` VALUES ('cfgComponent', '组件配置', 'cfgReportMgr', 'MENU', null, '', 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-03-22 09:28:15', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponent:delete', '组件删除', 'cfgComponent', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponent:query', '组件查询', 'cfgComponent', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponent:saveOrUpdate', '组件新增/修改', 'cfgComponent', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentColGroup:delete', '列组别删除', 'cfgComponentColGroupMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentColGroup:query', '列组别查询', 'cfgComponentColGroupMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentColGroup:saveOrUpdate', '列组别保存', 'cfgComponentColGroupMgr', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentColGroupMgr', '列组别', 'cfgReportMgr', 'menu', '9', null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentType', '组件类型', 'cfgReportMgr', null, null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentType:delete', '组件类型删除', 'cfgComponentType', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentType:query', '组件类型查询', 'cfgComponentType', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgComponentType:saveOrUpdate', '组件类型新增/修改', 'cfgComponentType', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage', '报表配置', 'cfgReportMgr', null, null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:delete', '页面删除', 'cfgPage', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:deploy', '报表发布与取消', 'cfgPage', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:preview', '报表预览', 'cfgPage', 'button', null, null, 'ACTIVE', '2019-04-02 10:23:52', 'admin', '2019-04-02 10:23:52', 'admin', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:query', '页面查询', 'cfgPage', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:relateComponent', '关联组件', 'cfgPage', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:relateParam', '关联参数', 'cfgPage', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgPage:saveOrUpdate', '页面新增/修改', 'cfgPage', 'FUNCTION', null, '', 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgParam', '报表参数', 'cfgReportMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgParam:delete', '报表参数删除', 'cfgParam', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgParam:query', '报表参数查询', 'cfgParam', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgParam:saveOrUpdate', '报表参数保存', 'cfgParam', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:16', 'ADMIN', '2019-03-22 09:28:16', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('cfgReportMgr', '报表配置管理', null, 'menu', null, '备注', 'ACTIVE', '2019-03-22 09:28:15', 'ADMIN', '2019-09-18 17:18:26', 'admin', '1');
INSERT INTO `sys_function` VALUES ('dataList', '数据集', 'dataModelMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataList:delete', '数据集删除', 'dataList', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataList:query', '数据集查询', 'dataList', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataList:save', '数据集新增保存', 'dataList', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataModelMgr', '数据源管理', null, 'menu', '5', null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-10-21 17:29:54', 'admin', '1');
INSERT INTO `sys_function` VALUES ('dataParameter', '数据参数', 'dataModelMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameter:delete', '数据参数删除', 'dataParameter', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameter:query', '数据参数查询', 'dataParameter', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameter:save', '数据参数新增保存', 'dataParameter', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameterGroup', '数据参数组', 'dataModelMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameterGroup:delete', '数据参数组删除', 'dataParameterGroup', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameterGroup:query', '数据参数组查询', 'dataParameterGroup', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataParameterGroup:save', '数据参数组新增保存', 'dataParameterGroup', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataSource', '数据源', 'dataModelMgr', 'menu', '1', null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataSource:delete', '数据源删除', 'dataSource', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataSource:query', '数据源查询', 'dataSource', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('dataSource:save', '数据源保存更新', 'dataSource', 'button', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');
INSERT INTO `sys_function` VALUES ('loanBefore', '贷前', 'rptDisplay', 'menu', '1', null, 'ACTIVE', '2019-05-30 14:49:00', 'admin', '2019-05-30 14:49:00', 'admin', '0');
INSERT INTO `sys_function` VALUES ('menu', '栏目管理', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('menu:delete', '栏目删除', 'menu', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('menu:query', '栏目查询', 'menu', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('menu:saveOrUpdate', '栏目新增/修改', 'menu', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('riskApproval', 'ss', 'rptDisplay', 'menu', null, 'ewfqwefqf', 'ACTIVE', '2019-10-16 11:48:19', 'admin', '2019-10-16 11:48:19', 'admin', '0');
INSERT INTO `sys_function` VALUES ('role', '角色管理', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('role:delete', '角色删除', 'role', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('role:query', '角色查询', 'role', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('role:saveFunctions', '角色关联功能保存', 'role', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('role:saveOrUpdate', '角色新增/修改', 'role', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('rptDisplay', '报表展示', null, 'menu', null, null, 'ACTIVE', '2019-03-26 18:10:24', 'admin', '2019-03-26 18:10:24', 'admin', '0');
INSERT INTO `sys_function` VALUES ('serviceLog', '服务日志', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('systemMgr', '系统管理', null, null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_funtion', '功能管理', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_funtion:delete', '功能删除', 'system_funtion', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_funtion:query', '功能查询', 'system_funtion', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_funtion:saveOrUpdate', '功能新增/修改', 'system_funtion', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_user', '用户管理', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_user:delete', '用户删除', 'system_user', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_user:query', '用户查询', 'system_user', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_user:saveOrUpdate', '用户新增/修改', 'system_user', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('system_user:saveRoles', '用户修改角色', 'system_user', null, '0', '', 'ACTIVE', '2019-03-22 09:27:55', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_cache_service', '缓存服务管理', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_cache_service:delete', '缓存服务删除', 'sys_cache_service', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_cache_service:query', '缓存服务查询', 'sys_cache_service', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_cache_service:refresh', '缓存刷新服务', 'sys_cache_service', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_cache_service:saveOrUpdate', '缓存服务新增/修改', 'sys_cache_service', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_log', '日志管理', 'systemMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:27:56', 'admin', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_log:query', '日志查询', 'sys_log', 'button', null, null, 'ACTIVE', '2019-03-22 09:27:56', 'admin', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_server_list', '服务器配置', 'systemMgr', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_server_list:delete', '服务器配置删除', 'sys_server_list', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_server_list:query', '服务器配置查询', 'sys_server_list', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('sys_server_list:saveOrUpdate', '服务器配新增/修改', 'sys_server_list', null, '0', '', 'ACTIVE', '2019-03-22 09:27:56', 'ADMIN', null, null, '0');
INSERT INTO `sys_function` VALUES ('WebService', 'WebService服务', 'dataModelMgr', 'menu', null, null, 'ACTIVE', '2019-03-22 09:28:05', 'ADMIN', '2019-03-22 09:28:05', 'ADMIN', '0');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_user_name` varchar(50) DEFAULT NULL,
  `log_url` varchar(100) DEFAULT NULL,
  `log_params` varchar(50) DEFAULT NULL,
  `log_ip` varchar(20) DEFAULT NULL,
  `log_created_date` datetime DEFAULT NULL,
  `log_type` varchar(20) DEFAULT NULL,
  `log_message` text,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志信息';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(50) NOT NULL,
  `menu_function` varchar(100) NOT NULL,
  `menu_path` varchar(200) DEFAULT NULL,
  `menu_icon` varchar(100) DEFAULT NULL,
  `menu_parent_menu` bigint(20) DEFAULT NULL,
  `menu_sequence` int(11) DEFAULT NULL,
  `menu_description` varchar(200) DEFAULT NULL,
  `menu_status` varchar(20) DEFAULT NULL,
  `menu_created_date` datetime DEFAULT NULL,
  `menu_created_user` varchar(40) DEFAULT NULL,
  `menu_modified_date` datetime DEFAULT NULL,
  `menu_modified_user` varchar(40) DEFAULT NULL,
  `menu_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`),
  KEY `FK_Reference_menu_function` (`menu_function`),
  CONSTRAINT `FK_Reference_menu_function` FOREIGN KEY (`menu_function`) REFERENCES `sys_function` (`func_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='栏目信息';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', 'systemMgr', '', 'fa-gear', null, '99', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('2', '用户管理', 'system_user', 'page/user/index.html', '', '1', '1', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('3', '角色管理', 'role', 'page/role/index.html', '', '1', '2', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('4', '功能管理', 'system_funtion', 'page/sysFunction/function.html', '', '1', '3', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('5', '栏目管理', 'menu', 'page/menu/menu.html', '', '1', '4', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('6', '日志管理', 'serviceLog', 'page/sysLog/index.html', '', '1', '8', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('7', '服务器配置', 'sys_server_list', 'page/serverList/index.html', '', '1', '9', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('8', '缓存服务管理', 'sys_cache_service', 'page/cacheService/index.html', '', '1', '10', '', 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('9', '数据源管理', 'dataModelMgr', null, 'fa-database', null, '2', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('10', '数据源', 'dataSource', 'page/dataSource/index.html', null, '9', '1', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('11', '数据参数', 'dataParameter', 'page/dataParameter/index.html', null, '9', '2', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('12', '数据参数组', 'dataParameterGroup', 'page/dataParameterGroup/index.html', null, '9', '3', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('13', '数据集', 'dataList', 'page/dataList/index.html', null, '9', '4', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('14', 'WebService服务', 'WebService', 'page/webService/form.html', null, '9', '5', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('15', '报表配置管理', 'cfgReportMgr', null, 'fa-bar-chart', null, '3', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('16', '组件类型', 'cfgComponentType', 'page/cfgComponentType/index.html', null, '15', '1', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('17', '模板配置', 'cfgChartTemplateMgr', 'page/cfgChartTemplate/index.html', null, '15', '2', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('18', '组件配置', 'cfgComponent', 'page/cfgComponent/index.html', null, '15', '3', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('19', '报表参数', 'cfgParam', 'page/cfgParam/index.html', null, '15', '4', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('20', '报表配置', 'cfgPage', 'page/cfgPage/index.html', null, '15', '5', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('21', '列组别', 'cfgComponentColGroupMgr', 'page/cfgComponentColGroup/index.html', null, '15', '6', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');
INSERT INTO `sys_menu` VALUES ('22', '报表展示', 'rptDisplay', null, 'fa-bar-chart', null, '1', null, 'ACTIVE', '2019-11-18 10:45:36', 'admin', '2019-11-18 10:45:36', 'admin', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `role_key` varchar(50) NOT NULL,
  `role_description` varchar(500) DEFAULT NULL,
  `role_status` varchar(20) DEFAULT NULL,
  `role_created_date` datetime DEFAULT NULL,
  `role_created_user` varchar(40) DEFAULT NULL,
  `role_modified_date` datetime DEFAULT NULL,
  `role_modified_user` varchar(40) DEFAULT NULL,
  `role_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'admin', 'admin', 'ACTIVE', '2019-11-07 11:34:14', 'admin', '2019-11-07 13:28:08', 'admin', '1');

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function` (
  `rf_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rf_role` bigint(20) DEFAULT NULL,
  `rf_function` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`rf_id`),
  KEY `FK_Reference_role_function_function` (`rf_function`),
  KEY `FK_Reference_role_function_role` (`rf_role`),
  CONSTRAINT `FK_Reference_role_function_function` FOREIGN KEY (`rf_function`) REFERENCES `sys_function` (`func_id`),
  CONSTRAINT `FK_Reference_role_function_role` FOREIGN KEY (`rf_role`) REFERENCES `sys_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8 COMMENT='角色功能';

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES ('1', '1', 'cfgChartTemplate:delete');
INSERT INTO `sys_role_function` VALUES ('2', '1', 'cfgChartTemplate:query');
INSERT INTO `sys_role_function` VALUES ('3', '1', 'cfgChartTemplate:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('4', '1', 'cfgChartTemplateMgr');
INSERT INTO `sys_role_function` VALUES ('5', '1', 'cfgComponent');
INSERT INTO `sys_role_function` VALUES ('6', '1', 'cfgComponent:delete');
INSERT INTO `sys_role_function` VALUES ('7', '1', 'cfgComponent:query');
INSERT INTO `sys_role_function` VALUES ('8', '1', 'cfgComponent:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('9', '1', 'cfgComponentColGroup:delete');
INSERT INTO `sys_role_function` VALUES ('10', '1', 'cfgComponentColGroup:query');
INSERT INTO `sys_role_function` VALUES ('11', '1', 'cfgComponentColGroup:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('12', '1', 'cfgComponentColGroupMgr');
INSERT INTO `sys_role_function` VALUES ('13', '1', 'cfgComponentType');
INSERT INTO `sys_role_function` VALUES ('14', '1', 'cfgComponentType:delete');
INSERT INTO `sys_role_function` VALUES ('15', '1', 'cfgComponentType:query');
INSERT INTO `sys_role_function` VALUES ('16', '1', 'cfgComponentType:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('17', '1', 'cfgPage');
INSERT INTO `sys_role_function` VALUES ('18', '1', 'cfgPage:delete');
INSERT INTO `sys_role_function` VALUES ('19', '1', 'cfgPage:deploy');
INSERT INTO `sys_role_function` VALUES ('20', '1', 'cfgPage:preview');
INSERT INTO `sys_role_function` VALUES ('21', '1', 'cfgPage:query');
INSERT INTO `sys_role_function` VALUES ('22', '1', 'cfgPage:relateComponent');
INSERT INTO `sys_role_function` VALUES ('23', '1', 'cfgPage:relateParam');
INSERT INTO `sys_role_function` VALUES ('24', '1', 'cfgPage:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('25', '1', 'cfgParam');
INSERT INTO `sys_role_function` VALUES ('26', '1', 'cfgParam:delete');
INSERT INTO `sys_role_function` VALUES ('27', '1', 'cfgParam:query');
INSERT INTO `sys_role_function` VALUES ('28', '1', 'cfgParam:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('29', '1', 'cfgReportMgr');
INSERT INTO `sys_role_function` VALUES ('30', '1', 'dataList');
INSERT INTO `sys_role_function` VALUES ('31', '1', 'dataList:delete');
INSERT INTO `sys_role_function` VALUES ('32', '1', 'dataList:query');
INSERT INTO `sys_role_function` VALUES ('33', '1', 'dataList:save');
INSERT INTO `sys_role_function` VALUES ('34', '1', 'dataModelMgr');
INSERT INTO `sys_role_function` VALUES ('35', '1', 'dataParameter');
INSERT INTO `sys_role_function` VALUES ('36', '1', 'dataParameter:delete');
INSERT INTO `sys_role_function` VALUES ('37', '1', 'dataParameter:query');
INSERT INTO `sys_role_function` VALUES ('38', '1', 'dataParameter:save');
INSERT INTO `sys_role_function` VALUES ('39', '1', 'dataParameterGroup');
INSERT INTO `sys_role_function` VALUES ('40', '1', 'dataParameterGroup:delete');
INSERT INTO `sys_role_function` VALUES ('41', '1', 'dataParameterGroup:query');
INSERT INTO `sys_role_function` VALUES ('42', '1', 'dataParameterGroup:save');
INSERT INTO `sys_role_function` VALUES ('43', '1', 'dataSource');
INSERT INTO `sys_role_function` VALUES ('44', '1', 'dataSource:delete');
INSERT INTO `sys_role_function` VALUES ('45', '1', 'dataSource:query');
INSERT INTO `sys_role_function` VALUES ('46', '1', 'dataSource:save');
INSERT INTO `sys_role_function` VALUES ('47', '1', 'loanBefore');
INSERT INTO `sys_role_function` VALUES ('48', '1', 'menu');
INSERT INTO `sys_role_function` VALUES ('49', '1', 'menu:delete');
INSERT INTO `sys_role_function` VALUES ('50', '1', 'menu:query');
INSERT INTO `sys_role_function` VALUES ('51', '1', 'menu:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('52', '1', 'riskApproval');
INSERT INTO `sys_role_function` VALUES ('53', '1', 'role');
INSERT INTO `sys_role_function` VALUES ('54', '1', 'role:delete');
INSERT INTO `sys_role_function` VALUES ('55', '1', 'role:query');
INSERT INTO `sys_role_function` VALUES ('56', '1', 'role:saveFunctions');
INSERT INTO `sys_role_function` VALUES ('57', '1', 'role:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('58', '1', 'rptDisplay');
INSERT INTO `sys_role_function` VALUES ('59', '1', 'serviceLog');
INSERT INTO `sys_role_function` VALUES ('60', '1', 'systemMgr');
INSERT INTO `sys_role_function` VALUES ('61', '1', 'system_funtion');
INSERT INTO `sys_role_function` VALUES ('62', '1', 'system_funtion:delete');
INSERT INTO `sys_role_function` VALUES ('63', '1', 'system_funtion:query');
INSERT INTO `sys_role_function` VALUES ('64', '1', 'system_funtion:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('65', '1', 'system_user');
INSERT INTO `sys_role_function` VALUES ('66', '1', 'system_user:delete');
INSERT INTO `sys_role_function` VALUES ('67', '1', 'system_user:query');
INSERT INTO `sys_role_function` VALUES ('68', '1', 'system_user:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('69', '1', 'system_user:saveRoles');
INSERT INTO `sys_role_function` VALUES ('70', '1', 'sys_cache_service');
INSERT INTO `sys_role_function` VALUES ('71', '1', 'sys_cache_service:delete');
INSERT INTO `sys_role_function` VALUES ('72', '1', 'sys_cache_service:query');
INSERT INTO `sys_role_function` VALUES ('73', '1', 'sys_cache_service:refresh');
INSERT INTO `sys_role_function` VALUES ('74', '1', 'sys_cache_service:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('75', '1', 'sys_log');
INSERT INTO `sys_role_function` VALUES ('76', '1', 'sys_log:query');
INSERT INTO `sys_role_function` VALUES ('77', '1', 'sys_server_list');
INSERT INTO `sys_role_function` VALUES ('78', '1', 'sys_server_list:delete');
INSERT INTO `sys_role_function` VALUES ('79', '1', 'sys_server_list:query');
INSERT INTO `sys_role_function` VALUES ('80', '1', 'sys_server_list:saveOrUpdate');
INSERT INTO `sys_role_function` VALUES ('81', '1', 'WebService');

-- ----------------------------
-- Table structure for sys_server_list
-- ----------------------------
DROP TABLE IF EXISTS `sys_server_list`;
CREATE TABLE `sys_server_list` (
  `sc_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sc_ip` varchar(20) NOT NULL,
  `sc_port` int(11) DEFAULT NULL,
  `sc_description` varchar(500) DEFAULT NULL,
  `sc_status` varchar(20) DEFAULT NULL,
  `sc_created_date` datetime DEFAULT NULL,
  `sc_created_user` varchar(40) DEFAULT NULL,
  `sc_modified_date` datetime DEFAULT NULL,
  `sc_modified_user` varchar(40) DEFAULT NULL,
  `sc_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`sc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器配置';

-- ----------------------------
-- Records of sys_server_list
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `usr_id` varchar(50) NOT NULL,
  `usr_name` varchar(50) NOT NULL,
  `usr_nick_name` varchar(50) DEFAULT NULL,
  `usr_email` varchar(100) DEFAULT NULL,
  `usr_phone` varchar(30) DEFAULT NULL,
  `usr_sex` int(11) DEFAULT NULL,
  `usr_password` varchar(64) NOT NULL,
  `usr_address` varchar(300) DEFAULT NULL,
  `usr_birthday` datetime DEFAULT NULL,
  `usr_locked` int(11) DEFAULT NULL,
  `usr_department` bigint(20) DEFAULT NULL,
  `usr_remark` varchar(500) DEFAULT NULL,
  `usr_status` varchar(20) DEFAULT NULL,
  `usr_created_date` datetime DEFAULT NULL,
  `usr_created_user` varchar(40) DEFAULT NULL,
  `usr_modified_date` datetime DEFAULT NULL,
  `usr_modified_user` varchar(40) DEFAULT NULL,
  `usr_version_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`usr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('admin', 'admin', 'admin', null, null, '1', '43442676c74ae59f219c2d87fd6bad52', null, null, '0', null, null, 'ACTIVE', '2019-11-18 09:27:56', 'admin', '2019-11-18 17:43:02', 'admin', '1');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `ur_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ur_user` varchar(50) NOT NULL,
  `ur_role` bigint(20) NOT NULL,
  PRIMARY KEY (`ur_id`),
  KEY `FK_Reference_user_role_role` (`ur_role`),
  KEY `FK_Reference_user_role_user` (`ur_user`),
  CONSTRAINT `FK_Reference_user_role_role` FOREIGN KEY (`ur_role`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `FK_Reference_user_role_user` FOREIGN KEY (`ur_user`) REFERENCES `sys_user` (`usr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', 'admin', '1');
