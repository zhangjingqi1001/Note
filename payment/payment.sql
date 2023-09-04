create database payment_demo;
USE `payment_demo`;
/*Table structure for table `t_order_info` */

DROP TABLE IF EXISTS `t_order_info`;
CREATE TABLE `t_order_info` (
	`id` BIGINT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单id',
	`title` VARCHAR(256) DEFAULT NULL COMMENT '订单标题',
	`order_no` VARCHAR(50) DEFAULT NULL COMMENT '商户订单编号',
	`user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
	`product_id` BIGINT(20) DEFAULT NULL COMMENT '支付产品id',
	`total_fee` INT(11) DEFAULT NULL COMMENT '订单金额(分)',
	`code_url` VARCHAR(50) DEFAULT NULL COMMENT '订单二维码连接',
	`order_status` VARCHAR(10) DEFAULT NULL COMMENT '订单状态',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
	 PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `t_payment_info`;
CREATE TABLE `t_payment_info`(
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录id',
	`order_no` VARCHAR (50) DEFAULT NULL COMMENT '商户订单编号',
	`transaction_id` VARCHAR(50) DEFAULT NULL COMMENT '支付系统交易编号',
	`payment_type` VARCHAR(20) DEFAULT NULL COMMENT '支付类型',
	`trade_type` VARCHAR (20) DEFAULT NULL COMMENT '交易类型',
	`trade_state` VARCHAR(50) DEFAULT NULL COMMENT '交易状态',
	`payer_total` INT(11) DEFAULT NULL COMMENT '支付金额(分)',
	`content` TEXT COMMENT '通知参数',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商Bid',
	`title` VARCHAR(20) DEFAULT NULL COMMENT '商品名称',
	`price` INT(11) DEFAULT NULL COMMENT '价格(分)',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*Data for the table 't_product' */
INSERT INTO `t_product` (`title`, `price`) VALUES ('Java程',1);
INSERT INTO `t_product` (`title`, `price`) VALUES ('大数据课程', 1);
INSERT INTO `t_product` (`title`,`price`) VALUES ('前端端程', 1);
INSERT INTO `t_product` (`title`, `price`) VALUES ('UI程' ,1);

/*Table structure for table it_refund_info' */
DROP TABLE IF EXISTS `t_refund_info`;
CREATE TABLE `t_refund_info`(
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '款单id',
	`order_no` VARCHAR (50) DEFAULT NULL COMMENT '商户订单编号',
	`refund_no` VARCHAR(50) DEFAULT NULL COMMENT '商户退款单编号',
	`refund_id` VARCHAR(50) DEFAULT NULL COMMENT '支付系统退款单号',
	`total_fee` INT(11) DEFAULT NULL COMMENT '原订单金额(分)',
	`refund` INT(11) DEFAULT NULL COMMENT '退款金额(分)',
	`reason` VARCHAR(50) DEFAULT NULL COMMENT '退款原因',
	`refund_status` VARCHAR(10) DEFAULT NULL COMMENT '退款状态',
	`content_return` TEXT COMMENT '申请退款返回参数',
	`content_notify` TEXT COMMENT '退款结果通知参数',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;