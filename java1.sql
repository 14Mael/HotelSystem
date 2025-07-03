/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : java1

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 04/07/2025 01:05:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cost
-- ----------------------------
DROP TABLE IF EXISTS `cost`;
CREATE TABLE `cost`  (
  `cost_id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` bigint NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `pay_time` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`cost_id`) USING BTREE,
  INDEX `record_id`(`record_id` ASC) USING BTREE,
  CONSTRAINT `record_id` FOREIGN KEY (`record_id`) REFERENCES `record` (`record_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `customer_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deposit` int NULL DEFAULT NULL,
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1016 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `record_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NULL DEFAULT NULL COMMENT '房间号',
  `room_num` int NULL DEFAULT NULL,
  `checkin_time` datetime(6) NOT NULL COMMENT '入住时间',
  `checkout_time` datetime(6) NULL DEFAULT NULL COMMENT '退房时间',
  `status` enum('已入住','已退房','已结算') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '已入住' COMMENT '订单状态（‘已结算’，‘已入住’,\'已退房\'）',
  `total_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '总费用',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `customer_id`(`customer_id` ASC) USING BTREE,
  INDEX `room_num`(`room_num` ASC) USING BTREE,
  CONSTRAINT `customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `room_num` FOREIGN KEY (`room_num`) REFERENCES `room` (`room_num`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 169 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_num` int NOT NULL,
  `status` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`room_num`) USING BTREE,
  INDEX `type_id`(`type_id` ASC) USING BTREE,
  CONSTRAINT `type_id` FOREIGN KEY (`type_id`) REFERENCES `room_type` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for room_type
-- ----------------------------
DROP TABLE IF EXISTS `room_type`;
CREATE TABLE `room_type`  (
  `type_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NOT NULL COMMENT '日价格',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for spare_rooms
-- ----------------------------
DROP VIEW IF EXISTS `spare_rooms`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `spare_rooms` AS select `room`.`room_num` AS `room_num`,`room`.`status` AS `status`,`room_type`.`type_name` AS `type_name`,`room_type`.`price` AS `Price` from (`room` join `room_type` on((`room_type`.`type_id` = `room`.`type_id`))) where (`room`.`status` = '空闲');

-- ----------------------------
-- Procedure structure for CalculateRoomRevenue
-- ----------------------------
DROP PROCEDURE IF EXISTS `CalculateRoomRevenue`;
delimiter ;;
CREATE PROCEDURE `CalculateRoomRevenue`(IN p_start_time DATETIME,
    IN p_end_time DATETIME)
BEGIN
    -- 临时存储计算结果
    DROP TEMPORARY TABLE IF EXISTS temp_revenue;
    CREATE TEMPORARY TABLE temp_revenue (
        type_id VARCHAR(30),
        type_name VARCHAR(20),
        total_hours DECIMAL(10,2),
        total_revenue DECIMAL(10,2)
    );

    -- 计算每种房型的总小时数和总收益
    INSERT INTO temp_revenue
    SELECT 
        rt.type_id,
        rt.type_name,
        SUM(
            TIMESTAMPDIFF(
                MINUTE,
                GREATEST(r.checkin_time, p_start_time),
                LEAST(IFNULL(r.checkout_time, p_end_time), p_end_time)
            ) / 60.0
        ) AS total_hours,
        SUM(
            CASE 
                WHEN r.status = '已结算' THEN 
                    IFNULL((SELECT amount FROM cost WHERE record_id = r.record_id ORDER BY pay_time DESC LIMIT 1), 0)
                ELSE 
                    (TIMESTAMPDIFF(
                        MINUTE,
                        GREATEST(r.checkin_time, p_start_time),
                        LEAST(IFNULL(r.checkout_time, p_end_time), p_end_time)
                    ) * (rt.`price` / 1440))
            END
        ) AS total_revenue
    FROM 
        record r
    JOIN 
        room rm ON r.room_num = rm.room_num
    JOIN 
        room_type rt ON rm.type_id = rt.type_id
    WHERE 
        (
            (r.checkin_time <= p_end_time) 
            AND 
            (r.checkout_time >= p_start_time OR r.checkout_time IS NULL)
        )
    GROUP BY 
        rt.type_id, rt.type_name;

    -- 返回结果集
    SELECT 
        IFNULL(type_id, 'N/A') AS '类型编号',
        IFNULL(type_name, 'N/A') AS '房型',
        IFNULL(ROUND(total_hours, 2), 0) AS '入住小时数',
        IFNULL(ROUND(total_revenue, 2), 0) AS '费用合计'
    FROM temp_revenue
    WHERE total_hours > 0 OR total_revenue > 0;
    
    -- 如果没有数据，返回提示行
    IF ROW_COUNT() = 0 THEN
        SELECT 
            '无数据' AS '类型编号',
            '请检查时间范围' AS '房型',
            0 AS '入住小时数',
            0 AS '费用合计';
    END IF;
    
    -- 清理临时表
    DROP TEMPORARY TABLE IF EXISTS temp_revenue;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_checkout
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_checkout`;
delimiter ;;
CREATE PROCEDURE `sp_checkout`(IN p_record_id INT)
BEGIN
    DECLARE v_room_num VARCHAR(20);
    DECLARE v_customer_id INT;
    DECLARE v_checkin_time DATETIME;
    DECLARE v_deposit DECIMAL(10,2);
    DECLARE v_base_fee DECIMAL(10,2);
    DECLARE v_total_fee DECIMAL(10,2);
    DECLARE v_price_per_day DECIMAL(10,2);  
    DECLARE v_checkout_time DATETIME;
    DECLARE v_minutes INT;
    
    -- 异常处理：回滚并抛出错误
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- 设置退房时间为当前时间
    SET v_checkout_time = NOW();

    -- 1. 查询入住记录
    SELECT 
        room_num, 
        customer_id, 
        checkin_time
    INTO 
        v_room_num, 
        v_customer_id, 
        v_checkin_time
    FROM record 
    WHERE record_id = p_record_id 
      AND status = '已入住';

    -- 若记录无效，抛出错误
    IF v_room_num IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '无效的入住记录';
    END IF;

    -- 2. 查询房型单价
    SELECT 
        `price`  
    INTO 
        v_price_per_day
    FROM room_type 
    JOIN room ON room_type.type_id = room.type_id
    WHERE room.room_num = v_room_num;

    -- 3. 查询客户押金（无值时默认 0）
    SELECT COALESCE(deposit, 0) INTO v_deposit 
    FROM customer 
    WHERE customer_id = v_customer_id;

    -- 4. 阶梯式计算费用（按分钟计算）
    SET v_minutes = TIMESTAMPDIFF(MINUTE, v_checkin_time, v_checkout_time);
    
    SET v_base_fee = CASE
        WHEN v_minutes <= 120 THEN v_price_per_day * 0.25  -- 2小时内25%
        WHEN v_minutes <= 240 THEN v_price_per_day * 0.5   -- 4小时内50%
        WHEN v_minutes <= 1440 THEN v_price_per_day        -- 24小时内100%
        ELSE v_price_per_day * CEIL(v_minutes/1440.0)     -- 超过24小时按整天计算
    END;
    
    -- 计算总费用（基础费用减去押金，但不低于0）
    SET v_total_fee = GREATEST(v_base_fee - v_deposit, 0);

    -- 5. 插入费用记录
    INSERT INTO cost (record_id, amount, pay_time) 
    VALUES (p_record_id, v_total_fee, v_checkout_time);

    -- 6. 更新入住记录状态
    UPDATE record 
    SET checkout_time = v_checkout_time,
        total_cost = v_total_fee,
        status = '已结算'
    WHERE record_id = p_record_id;

    -- 7. 扣除押金（如果押金未完全抵扣费用）
    IF v_deposit > 0 AND v_deposit > v_base_fee THEN
        UPDATE customer 
        SET deposit = deposit - v_base_fee  
        WHERE customer_id = v_customer_id;
    ELSEIF v_deposit > 0 THEN  -- 修改了这里，从ELSIF改为ELSEIF
        UPDATE customer 
        SET deposit = 0  
        WHERE customer_id = v_customer_id;
    END IF;

    COMMIT;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table record
-- ----------------------------
DROP TRIGGER IF EXISTS `checkin_update_status`;
delimiter ;;
CREATE TRIGGER `checkin_update_status` BEFORE INSERT ON `record` FOR EACH ROW BEGIN
    SET NEW.checkin_time = NOW();  -- 直接修改待插入的行，不触发新的 INSERT/UPDATE
    UPDATE room SET status = '已入住' WHERE room_num = NEW.room_num;  -- 操作其他表，安全
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table record
-- ----------------------------
DROP TRIGGER IF EXISTS `checkout_update_status`;
delimiter ;;
CREATE TRIGGER `checkout_update_status` BEFORE UPDATE ON `record` FOR EACH ROW BEGIN
    -- 关键点：通过 SET NEW.checkout_time 修改当前行，而非触发新的 UPDATE
    IF OLD.status = '已入住' AND NEW.status = '已结算' THEN
        SET NEW.checkout_time = NOW();
    END IF;
    
    -- 更新客房状态（不涉及 record 表，安全）
    IF OLD.status != '已结算' AND NEW.status = '已结算' THEN
        UPDATE room SET status = '空闲' WHERE room_num = NEW.room_num;
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
