-- 已有库增量升级（兼容 MySQL 5.7 / 8.0，可重复执行）
-- root 密码: 123456

USE neusoft_care;

CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code   VARCHAR(20)  NOT NULL UNIQUE,
    role_name   VARCHAR(50)  NOT NULL,
    description VARCHAR(200)
);

INSERT IGNORE INTO sys_role (role_code, role_name, description) VALUES
('admin', '系统管理员', '拥有全部管理权限'),
('steward', '健康管家', '健康管家与客户服务'),
('nurse', '护理人员', '护理项目与护理记录');

-- sys_user.status
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'sys_user' AND COLUMN_NAME = 'status');
SET @sql = IF(@exist = 0,
    'ALTER TABLE sys_user ADD COLUMN status TINYINT DEFAULT 1 COMMENT ''1启用 0禁用''',
    'SELECT ''skip sys_user.status'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- customer.birth_date
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'customer' AND COLUMN_NAME = 'birth_date');
SET @sql = IF(@exist = 0, 'ALTER TABLE customer ADD COLUMN birth_date DATE NULL', 'SELECT ''skip'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- customer.blood_type
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'customer' AND COLUMN_NAME = 'blood_type');
SET @sql = IF(@exist = 0, 'ALTER TABLE customer ADD COLUMN blood_type VARCHAR(10) NULL', 'SELECT ''skip'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- customer.family_member
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'customer' AND COLUMN_NAME = 'family_member');
SET @sql = IF(@exist = 0, 'ALTER TABLE customer ADD COLUMN family_member VARCHAR(50) NULL', 'SELECT ''skip'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- customer.building
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'customer' AND COLUMN_NAME = 'building');
SET @sql = IF(@exist = 0, 'ALTER TABLE customer ADD COLUMN building VARCHAR(20) NULL', 'SELECT ''skip'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- check_in_record.contract_expire_date
SET @exist = (SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'check_in_record' AND COLUMN_NAME = 'contract_expire_date');
SET @sql = IF(@exist = 0, 'ALTER TABLE check_in_record ADD COLUMN contract_expire_date DATE NULL', 'SELECT ''skip'' AS info');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE sys_user SET status = 1 WHERE status IS NULL;

UPDATE sys_user SET password = '000001' WHERE username = 'admin' AND password = '123456';
UPDATE sys_user SET password = '000002' WHERE username = 'steward01' AND password = '123456';
UPDATE sys_user SET password = '000003' WHERE username = 'steward02' AND password = '123456';
UPDATE sys_user SET password = '000005' WHERE username = 'nurse01' AND password = '123456';
UPDATE sys_user SET password = '000006' WHERE username = 'nurse02' AND password = '123456';

SELECT 'upgrade_user_auth 执行完成' AS message;
