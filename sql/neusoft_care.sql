-- 东软颐养中心 数据库初始化脚本
-- 数据库密码: 123456

CREATE DATABASE IF NOT EXISTS neusoft_care DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE neusoft_care;

-- ==================== 公共表 ====================
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code   VARCHAR(20)  NOT NULL UNIQUE,
    role_name   VARCHAR(50)  NOT NULL,
    description VARCHAR(200)
);

CREATE TABLE sys_user (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    real_name   VARCHAR(50)  NOT NULL,
    role        VARCHAR(20)  NOT NULL COMMENT '关联 sys_role.role_code',
    phone       VARCHAR(20),
    status      TINYINT DEFAULT 1 COMMENT '1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==================== 模块1: 客户管理 ====================
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(50)  NOT NULL,
    gender       TINYINT      NOT NULL COMMENT '1男 2女',
    age          INT          NOT NULL,
    birth_date   DATE,
    blood_type   VARCHAR(10)  COMMENT 'A/B/AB/O',
    id_card      VARCHAR(18),
    phone        VARCHAR(20),
    family_member VARCHAR(50) COMMENT '家属',
    emergency_contact VARCHAR(50),
    emergency_phone   VARCHAR(20),
    building     VARCHAR(20)  COMMENT '楼栋',
    address      VARCHAR(200),
    check_in_date DATE,
    bed_id       BIGINT,
    status       TINYINT DEFAULT 1 COMMENT '1在院 2外出 3退住',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS check_in_record;
CREATE TABLE check_in_record (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    bed_id       BIGINT       NOT NULL,
    check_in_date DATE        NOT NULL,
    contract_expire_date DATE COMMENT '合同到期时间',
    contract_no  VARCHAR(50),
    deposit      DECIMAL(10,2) DEFAULT 0,
    remark       VARCHAR(500),
    operator     VARCHAR(50),
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS check_out_request;
CREATE TABLE check_out_request (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    reason       VARCHAR(500) NOT NULL,
    status       TINYINT DEFAULT 0 COMMENT '0待审核 1已通过 2已拒绝',
    apply_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    audit_time   DATETIME,
    auditor      VARCHAR(50)
);

DROP TABLE IF EXISTS outing_request;
CREATE TABLE outing_request (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    reason       VARCHAR(500) NOT NULL,
    outing_date  DATE         NOT NULL,
    return_date  DATE,
    status       TINYINT DEFAULT 0 COMMENT '0待审核 1已通过 2已拒绝 3已返回',
    apply_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    audit_time   DATETIME,
    auditor      VARCHAR(50)
);

-- ==================== 模块2: 床位管理 ====================
DROP TABLE IF EXISTS room;
CREATE TABLE room (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_no      VARCHAR(20)  NOT NULL UNIQUE,
    floor        INT          NOT NULL,
    room_type    VARCHAR(20)  NOT NULL COMMENT '单人间/双人间/多人间/VIP',
    bed_count    INT          NOT NULL,
    price        DECIMAL(10,2) NOT NULL,
    remark       VARCHAR(200)
);

DROP TABLE IF EXISTS bed;
CREATE TABLE bed (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_id      BIGINT       NOT NULL,
    bed_no       VARCHAR(20)  NOT NULL,
    status       TINYINT DEFAULT 0 COMMENT '0空闲 1占用 2维护',
    customer_id  BIGINT,
    UNIQUE KEY uk_room_bed (room_id, bed_no)
);

DROP TABLE IF EXISTS bed_exchange;
CREATE TABLE bed_exchange (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    from_bed_id  BIGINT       NOT NULL,
    to_bed_id    BIGINT       NOT NULL,
    reason       VARCHAR(500),
    operator     VARCHAR(50),
    exchange_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==================== 模块3: 护理模块 ====================
DROP TABLE IF EXISTS nursing_item;
CREATE TABLE nursing_item (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_code    VARCHAR(20)  NOT NULL UNIQUE,
    item_name    VARCHAR(100) NOT NULL,
    category     VARCHAR(30)  NOT NULL COMMENT '生活照料/医疗护理/康复心理',
    unit         VARCHAR(10)  DEFAULT '次',
    price        DECIMAL(10,2) NOT NULL,
    cycle_desc   VARCHAR(50),
    description  VARCHAR(500)
);

DROP TABLE IF EXISTS nursing_level;
CREATE TABLE nursing_level (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    level_code   VARCHAR(20)  NOT NULL UNIQUE,
    level_name   VARCHAR(50)  NOT NULL,
    description  VARCHAR(500),
    frequency    VARCHAR(100),
    price        DECIMAL(10,2) NOT NULL
);

DROP TABLE IF EXISTS level_item;
CREATE TABLE level_item (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    level_id     BIGINT NOT NULL,
    item_id      BIGINT NOT NULL,
    UNIQUE KEY uk_level_item (level_id, item_id)
);

DROP TABLE IF EXISTS customer_nursing;
CREATE TABLE customer_nursing (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT NOT NULL,
    level_id     BIGINT NOT NULL,
    start_date   DATE   NOT NULL,
    end_date     DATE,
    status       TINYINT DEFAULT 1 COMMENT '1生效 0失效',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS nursing_record;
CREATE TABLE nursing_record (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    item_id      BIGINT       NOT NULL,
    nursing_time DATETIME     NOT NULL,
    staff_name   VARCHAR(50),
    status       TINYINT DEFAULT 1 COMMENT '1已完成 0未完成',
    remark       VARCHAR(500),
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==================== 模块4: 健康管家 ====================
DROP TABLE IF EXISTS health_steward;
CREATE TABLE health_steward (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(50)  NOT NULL,
    gender       TINYINT      NOT NULL,
    phone        VARCHAR(20),
    department   VARCHAR(50),
    status       TINYINT DEFAULT 1 COMMENT '1在职 0离职',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS customer_steward;
CREATE TABLE customer_steward (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT NOT NULL,
    steward_id   BIGINT NOT NULL,
    assign_date  DATE   NOT NULL,
    status       TINYINT DEFAULT 1 COMMENT '1服务中 0已结束',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS service_attention;
CREATE TABLE service_attention (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    service_type VARCHAR(50),
    start_date   DATE         NOT NULL,
    end_date     DATE,
    price        DECIMAL(10,2),
    status       TINYINT DEFAULT 1 COMMENT '1进行中 0已结束',
    remark       VARCHAR(500)
);

DROP TABLE IF EXISTS daily_care;
CREATE TABLE daily_care (
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id  BIGINT       NOT NULL,
    steward_id   BIGINT       NOT NULL,
    care_date    DATE         NOT NULL,
    blood_pressure VARCHAR(20),
    heart_rate   INT,
    blood_sugar  DECIMAL(5,2),
    weight       DECIMAL(5,2),
    temperature  DECIMAL(4,1),
    mood         VARCHAR(50),
    diet         VARCHAR(200),
    sleep        VARCHAR(100),
    activity     VARCHAR(200),
    remark       VARCHAR(500),
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ==================== 模拟数据 ====================

-- 系统角色
INSERT INTO sys_role (role_code, role_name, description) VALUES
('admin', '系统管理员', '拥有全部管理权限'),
('steward', '健康管家', '健康管家与客户服务'),
('nurse', '护理人员', '护理项目与护理记录');

-- 系统用户（默认密码为手机号后6位）
INSERT INTO sys_user (username, password, real_name, role, phone, status) VALUES
('admin', '000001', '系统管理员', 'admin', '13800000001', 1),
('steward01', '000002', '张管家', 'steward', '13800000002', 1),
('steward02', '000003', '李管家', 'steward', '13800000003', 1),
('nurse01', '000005', '刘护士', 'nurse', '13800000005', 1),
('nurse02', '000006', '陈护士', 'nurse', '13800000006', 1);

-- 房间
INSERT INTO room (room_no, floor, room_type, bed_count, price, remark) VALUES
('101', 1, '单人间', 1, 3500.00, '一楼南向'),
('102', 1, '双人间', 2, 2800.00, '一楼东向'),
('103', 1, '双人间', 2, 2800.00, '一楼西向'),
('201', 2, '单人间', 1, 3800.00, '二楼南向'),
('202', 2, '双人间', 2, 3000.00, '二楼东向'),
('203', 2, '多人间', 4, 2200.00, '二楼北向'),
('301', 3, 'VIP', 1, 5800.00, '三楼VIP套房'),
('302', 3, '单人间', 1, 4000.00, '三楼南向'),
('303', 3, '双人间', 2, 3200.00, '三楼东向'),
('401', 4, '单人间', 1, 4200.00, '四楼景观房'),
('402', 4, '双人间', 2, 3400.00, '四楼东向'),
('403', 4, '多人间', 4, 2400.00, '四楼北向');

-- 床位
INSERT INTO bed (room_id, bed_no, status, customer_id) VALUES
(1, 'A', 1, 1), (2, 'A', 1, 2), (2, 'B', 1, 3),
(3, 'A', 0, NULL), (3, 'B', 1, 4), (4, 'A', 1, 5),
(5, 'A', 1, 6), (5, 'B', 0, NULL), (6, 'A', 1, 7),
(6, 'B', 1, 8), (6, 'C', 0, NULL), (6, 'D', 1, 9),
(7, 'A', 1, 10), (8, 'A', 1, 11), (9, 'A', 1, 12),
(9, 'B', 0, NULL), (10, 'A', 1, 13), (11, 'A', 1, 14),
(11, 'B', 1, 15), (12, 'A', 0, NULL), (12, 'B', 1, 16),
(12, 'C', 1, 17), (12, 'D', 0, NULL);

-- 客户
INSERT INTO customer (name, gender, age, id_card, phone, emergency_contact, emergency_phone, address, check_in_date, bed_id, status) VALUES
('王建国', 1, 78, '210102194601011234', '13911110001', '王强', '13922220001', '沈阳市和平区', '2024-01-15', 1, 1),
('李秀英', 2, 82, '210102194201021235', '13911110002', '李芳', '13922220002', '沈阳市沈河区', '2024-02-20', 2, 1),
('张德明', 1, 75, '210102194901031236', '13911110003', '张华', '13922220003', '沈阳市大东区', '2024-03-10', 3, 1),
('赵玉兰', 2, 80, '210102194401041237', '13911110004', '赵明', '13922220004', '沈阳市皇姑区', '2024-04-05', 5, 1),
('刘大海', 1, 85, '210102193901051238', '13911110005', '刘洋', '13922220005', '沈阳市铁西区', '2024-05-12', 6, 1),
('陈桂芬', 2, 79, '210102194501061239', '13911110006', '陈磊', '13922220006', '沈阳市浑南区', '2024-06-18', 7, 1),
('孙志远', 1, 72, '210102195201071240', '13911110007', '孙丽', '13922220007', '沈阳市于洪区', '2024-07-22', 9, 1),
('周美玲', 2, 76, '210102194801081241', '13911110008', '周杰', '13922220008', '沈阳市苏家屯区', '2024-08-30', 10, 1),
('吴国强', 1, 81, '210102194301091242', '13911110009', '吴敏', '13922220009', '沈阳市沈北新区', '2024-09-15', 12, 1),
('郑淑华', 2, 77, '210102194701101243', '13911110010', '郑伟', '13922220010', '沈阳市辽中区', '2024-10-08', 13, 1),
('马文斌', 1, 74, '210102195001111244', '13911110011', '马丽', '13922220011', '大连市中山区', '2024-11-20', 14, 1),
('黄秀兰', 2, 83, '210102194101121245', '13911110012', '黄强', '13922220012', '大连市西岗区', '2025-01-05', 15, 1),
('林永福', 1, 86, '210102193801131246', '13911110013', '林芳', '13922220013', '大连市沙河口区', '2025-02-14', 16, 1),
('杨桂英', 2, 78, '210102194601141247', '13911110014', '杨明', '13922220014', '大连市甘井子区', '2025-03-01', 17, 1),
('徐德成', 1, 73, '210102195101151248', '13911110015', '徐华', '13922220015', '鞍山市铁东区', '2025-03-20', 18, 1),
('何玉珍', 2, 80, '210102194401161249', '13911110016', '何强', '13922220016', '鞍山市铁西区', '2025-04-10', 19, 1),
('高志勇', 1, 79, '210102194501171250', '13911110017', '高丽', '13922220017', '抚顺市新抚区', '2025-04-25', 20, 1),
('罗淑芬', 2, 84, '210102194001181251', '13911110018', '罗明', '13922220018', '本溪市平山区', '2025-05-01', 21, 1),
('梁国栋', 1, 71, '210102195301191252', '13911110019', '梁芳', '13922220019', '丹东市振兴区', '2025-05-10', 22, 1),
('谢秀梅', 2, 77, '210102194701201253', '13911110020', '谢强', '13922220020', '锦州市古塔区', '2025-05-15', NULL, 2);

-- 入住记录
INSERT INTO check_in_record (customer_id, bed_id, check_in_date, contract_no, deposit, remark, operator) VALUES
(1, 1, '2024-01-15', 'HT202401001', 5000.00, '首批入住客户', '管理员'),
(2, 2, '2024-02-20', 'HT202402001', 5000.00, '', '管理员'),
(3, 3, '2024-03-10', 'HT202403001', 5000.00, '', '管理员'),
(4, 5, '2024-04-05', 'HT202404001', 5000.00, '', '管理员'),
(5, 6, '2024-05-12', 'HT202405001', 6000.00, '需特级护理', '管理员'),
(6, 7, '2024-06-18', 'HT202406001', 5000.00, '', '管理员'),
(7, 9, '2024-07-22', 'HT202407001', 5000.00, '', '管理员'),
(8, 10, '2024-08-30', 'HT202408001', 5000.00, '', '管理员'),
(9, 12, '2024-09-15', 'HT202409001', 5000.00, '', '管理员'),
(10, 13, '2024-10-08', 'HT202410001', 8000.00, 'VIP客户', '管理员'),
(11, 14, '2024-11-20', 'HT202411001', 5000.00, '', '管理员'),
(12, 15, '2024-12-05', 'HT202412001', 5000.00, '', '管理员'),
(13, 16, '2025-01-05', 'HT202501001', 5000.00, '', '管理员'),
(14, 17, '2025-02-14', 'HT202502001', 5000.00, '', '管理员'),
(15, 18, '2025-03-01', 'HT202503001', 5000.00, '', '管理员'),
(16, 19, '2025-03-20', 'HT202503002', 5000.00, '', '管理员'),
(17, 20, '2025-04-10', 'HT202504001', 5000.00, '', '管理员'),
(18, 21, '2025-04-25', 'HT202504002', 5000.00, '', '管理员'),
(19, 22, '2025-05-01', 'HT202505001', 5000.00, '', '管理员');

-- 退住申请
INSERT INTO check_out_request (customer_id, reason, status, apply_time, audit_time, auditor) VALUES
(3, '由儿女接出，回家休养', 1, '2025-04-01 10:00:00', '2025-04-02 09:00:00', '管理员'),
(8, '身体恢复良好，回家居住', 0, '2025-05-20 14:30:00', NULL, NULL),
(12, '转其他养老机构', 0, '2025-05-22 11:00:00', NULL, NULL),
(15, '家庭原因需要照顾', 2, '2025-05-18 16:00:00', '2025-05-19 10:00:00', '管理员'),
(6, '费用原因退住', 0, '2025-05-23 09:30:00', NULL, NULL);

-- 外出申请
INSERT INTO outing_request (customer_id, reason, outing_date, return_date, status, apply_time, audit_time, auditor) VALUES
(1, '子女接回家过周末', '2025-05-24', '2025-05-26', 1, '2025-05-23 08:00:00', '2025-05-23 09:00:00', '管理员'),
(2, '医院复查', '2025-05-25', '2025-05-25', 0, '2025-05-24 10:00:00', NULL, NULL),
(5, '参加家族聚会', '2025-05-26', '2025-05-27', 0, '2025-05-24 14:00:00', NULL, NULL),
(10, '外出购物', '2025-05-20', '2025-05-20', 3, '2025-05-19 09:00:00', '2025-05-19 10:00:00', '管理员'),
(14, '探亲', '2025-05-22', '2025-05-24', 1, '2025-05-21 11:00:00', '2025-05-21 14:00:00', '管理员'),
(20, '医院体检', '2025-05-23', NULL, 1, '2025-05-22 08:30:00', '2025-05-22 09:00:00', '管理员');

-- 床位调换
INSERT INTO bed_exchange (customer_id, from_bed_id, to_bed_id, reason, operator) VALUES
(4, 4, 5, '原床位邻居噪音大', '管理员'),
(7, 8, 9, '希望换到靠窗位置', '管理员'),
(11, 13, 14, '升级到更好房间', '管理员');

-- 护理项目
INSERT INTO nursing_item (item_code, item_name, category, unit, price, cycle_desc, description) VALUES
('LZ-001', '晨间护理', '生活照料', '次', 15.00, '每日', '协助洗漱、整理床铺'),
('LZ-002', '晚间护理', '生活照料', '次', 10.00, '每日', '协助就寝、整理物品'),
('LZ-003', '床上擦浴', '生活照料', '次', 20.00, '隔日', '全身清洁护理'),
('LZ-004', '协助进食', '生活照料', '次', 10.00, '每餐', '协助或鼻饲进食'),
('LZ-005', '协助翻身', '生活照料', '次', 5.00, '每2小时', '预防压疮'),
('LZ-006', '剪指甲/洗发', '生活照料', '次', 15.00, '每周', '个人卫生护理'),
('LZ-007', '翻身拍背', '生活照料', '次', 5.00, '每2小时', '排痰护理'),
('YL-001', '生命体征监测', '医疗护理', '次', 5.00, '每日1-2次', '血压、血糖等监测'),
('YL-002', '药物管理', '医疗护理', '次', 10.00, '遵医嘱', '按时喂药并记录'),
('YL-003', '伤口换药', '医疗护理', '次', 50.00, '隔日', '按医嘱换药'),
('YL-004', '导管护理', '医疗护理', '次', 60.00, '每周', '导尿管/胃管护理'),
('YL-005', '吸氧', '医疗护理', '小时', 5.00, '按需', '吸氧并记录'),
('KF-001', '肢体功能训练', '康复心理', '次', 30.00, '每日', '预防肌肉萎缩'),
('KF-002', '认知训练', '康复心理', '次', 20.00, '隔日', '延缓认知衰退'),
('KF-003', '心理疏导', '康复心理', '次', 40.00, '每周', '一对一心理疏导');

-- 护理级别
INSERT INTO nursing_level (level_code, level_name, description, frequency, price) VALUES
('ZL', '自理型', '健康自理，无需特殊护理', '每日巡查1次', 500.00),
('HL-1', '一级护理', '部分失能，需部分协助', '每日2-3次', 1200.00),
('HL-2', '二级护理', '中度失能，日常生活需较多帮助', '夜间每2小时1次', 2500.00),
('HL-3', '三级护理', '重度失能，卧床需全护理', '夜间每1小时1次', 4000.00),
('YZ', '医疗专护', '病危需医疗监护', '24小时医护值班', 6000.00);

-- 级别-项目关联
INSERT INTO level_item (level_id, item_id) VALUES
(1,1),(1,2),(1,8),
(2,1),(2,2),(2,3),(2,6),(2,8),(2,9),
(3,1),(3,2),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(3,9),(3,13),
(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,9),(4,10),(4,12),(4,13),
(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,9),(5,10),(5,11),(5,12),(5,13);

-- 客户护理设置
INSERT INTO customer_nursing (customer_id, level_id, start_date, status) VALUES
(1,1,'2024-01-15',1),(2,1,'2024-02-20',1),(3,2,'2024-03-10',1),
(4,2,'2024-04-05',1),(5,4,'2024-05-12',1),(6,2,'2024-06-18',1),
(7,1,'2024-07-22',1),(8,2,'2024-08-30',1),(9,3,'2024-09-15',1),
(10,5,'2024-10-08',1),(11,1,'2024-11-20',1),(12,2,'2024-12-05',1),
(13,2,'2025-01-05',1),(14,1,'2025-02-14',1),(15,2,'2025-03-01',1),
(16,3,'2025-03-20',1),(17,2,'2025-04-10',1),(18,4,'2025-04-25',1),
(19,1,'2025-05-01',1),(20,1,'2025-05-15',1);

-- 护理记录
INSERT INTO nursing_record (customer_id, item_id, nursing_time, staff_name, status, remark) VALUES
(1,1,'2025-05-24 07:30:00','刘护士',1,'正常'),
(1,2,'2025-05-24 20:00:00','刘护士',1,'正常'),
(2,1,'2025-05-24 07:45:00','陈护士',1,'正常'),
(2,8,'2025-05-24 09:00:00','陈护士',1,'血压120/80'),
(5,1,'2025-05-24 07:00:00','刘护士',1,'需协助'),
(5,4,'2025-05-24 08:00:00','刘护士',1,'早餐'),
(5,5,'2025-05-24 10:00:00','陈护士',1,''),
(5,7,'2025-05-24 10:00:00','陈护士',1,''),
(5,9,'2025-05-24 08:30:00','刘护士',1,'已服药'),
(9,1,'2025-05-24 07:30:00','陈护士',1,''),
(9,3,'2025-05-24 14:00:00','刘护士',1,'擦浴完成'),
(10,8,'2025-05-24 08:00:00','刘护士',1,'VIP监测'),
(10,12,'2025-05-24 15:00:00','陈护士',1,'吸氧2小时'),
(3,1,'2025-05-23 07:30:00','刘护士',1,''),
(3,2,'2025-05-23 20:00:00','陈护士',1,''),
(4,1,'2025-05-23 07:30:00','刘护士',1,''),
(6,1,'2025-05-23 07:30:00','陈护士',1,''),
(7,1,'2025-05-23 07:30:00','刘护士',1,''),
(8,1,'2025-05-23 07:30:00','陈护士',0,'客户外出未完成'),
(11,1,'2025-05-23 07:30:00','刘护士',1,''),
(12,1,'2025-05-23 07:30:00','陈护士',1,''),
(13,1,'2025-05-23 07:30:00','刘护士',1,''),
(14,1,'2025-05-23 07:30:00','陈护士',1,''),
(15,1,'2025-05-23 07:30:00','刘护士',1,''),
(16,1,'2025-05-23 07:30:00','陈护士',1,''),
(17,1,'2025-05-23 07:30:00','刘护士',1,''),
(18,1,'2025-05-23 07:30:00','陈护士',1,''),
(19,1,'2025-05-23 07:30:00','刘护士',1,'');

-- 健康管家
INSERT INTO health_steward (name, gender, phone, department, status) VALUES
('张管家', 1, '13800000002', '健康管家部', 1),
('李管家', 2, '13800000003', '健康管家部', 1),
('王管家', 1, '13800000004', '健康管家部', 1),
('赵管家', 2, '13800000007', '健康管家部', 1),
('孙管家', 1, '13800000008', '健康管家部', 1);

-- 客户-管家关系
INSERT INTO customer_steward (customer_id, steward_id, assign_date, status) VALUES
(1,1,'2024-01-15',1),(2,1,'2024-02-20',1),(3,1,'2024-03-10',1),
(4,2,'2024-04-05',1),(5,2,'2024-05-12',1),(6,2,'2024-06-18',1),
(7,3,'2024-07-22',1),(8,3,'2024-08-30',1),(9,3,'2024-09-15',1),
(10,1,'2024-10-08',1),(11,2,'2024-11-20',1),(12,2,'2024-12-05',1),
(13,3,'2025-01-05',1),(14,3,'2025-02-14',1),(15,1,'2025-03-01',1),
(16,2,'2025-03-20',1),(17,3,'2025-04-10',1),(18,1,'2025-04-25',1),
(19,2,'2025-05-01',1),(20,3,'2025-05-15',1);

-- 服务关注
INSERT INTO service_attention (customer_id, service_name, service_type, start_date, end_date, price, status, remark) VALUES
(1,'基础生活照料','生活照料','2024-01-15',NULL,500.00,1,''),
(2,'基础生活照料','生活照料','2024-02-20',NULL,500.00,1,''),
(5,'特级护理服务','医疗护理','2024-05-12',NULL,4000.00,1,'24小时护理'),
(5,'康复训练','康复服务','2024-06-01','2025-06-01',900.00,1,'每日训练'),
(10,'VIP专属服务','VIP服务','2024-10-08',NULL,2000.00,1,'专属管家'),
(10,'营养配餐','餐饮服务','2024-10-08',NULL,800.00,1,''),
(9,'认知训练','康复服务','2024-10-01',NULL,600.00,1,''),
(18,'医疗专护','医疗护理','2025-04-25',NULL,6000.00,1,''),
(3,'基础生活照料','生活照料','2024-03-10','2025-04-01',500.00,0,'已退住'),
(14,'心理疏导','心理服务','2025-03-01','2025-09-01',160.00,1,'每周一次');

-- 日常护理
INSERT INTO daily_care (customer_id, steward_id, care_date, blood_pressure, heart_rate, blood_sugar, weight, temperature, mood, diet, sleep, activity, remark) VALUES
(1,1,'2025-05-24','125/82',72,5.6,65.5,36.5,'良好','正常','7小时','晨练30分钟',''),
(2,1,'2025-05-24','130/85',75,6.2,58.0,36.4,'良好','少盐','6小时','棋牌活动',''),
(5,2,'2025-05-24','145/90',80,7.1,72.0,36.6,'一般','流食','5小时','卧床休息','需关注血压'),
(10,1,'2025-05-24','118/75',68,5.4,62.0,36.3,'良好','营养餐','8小时','花园散步',''),
(9,3,'2025-05-24','135/88',78,6.8,70.5,36.5,'一般','正常','6小时','康复训练',''),
(3,1,'2025-05-23','128/80',70,5.8,63.0,36.4,'良好','正常','7小时','看电视',''),
(4,2,'2025-05-23','122/78',74,5.5,60.0,36.5,'良好','正常','7小时','手工活动',''),
(6,2,'2025-05-23','132/84',76,6.0,55.5,36.4,'良好','正常','6小时','阅读',''),
(7,3,'2025-05-23','120/76',70,5.3,68.0,36.3,'良好','正常','8小时','太极拳',''),
(11,2,'2025-05-23','126/82',73,5.9,64.0,36.5,'良好','正常','7小时','音乐欣赏',''),
(12,2,'2025-05-23','134/86',77,6.5,59.0,36.4,'一般','少糖','6小时','休息',''),
(13,3,'2025-05-23','121/79',71,5.7,61.5,36.5,'良好','正常','7小时','聊天',''),
(14,3,'2025-05-23','129/83',74,6.1,57.0,36.4,'良好','正常','7小时','书法',''),
(15,1,'2025-05-23','127/81',72,5.6,66.0,36.5,'良好','正常','7小时','散步',''),
(16,2,'2025-05-23','138/89',79,6.9,71.0,36.6,'一般','正常','5小时','康复训练',''),
(17,3,'2025-05-23','124/80',73,5.8,62.5,36.4,'良好','正常','7小时','棋牌',''),
(18,1,'2025-05-23','150/95',85,8.2,75.0,36.7,'较差','流食','4小时','卧床','需密切观察'),
(19,2,'2025-05-23','119/77',69,5.4,67.5,36.3,'良好','正常','8小时','晨练',''),
(1,1,'2025-05-23','123/80',71,5.5,65.3,36.4,'良好','正常','7小时','晨练',''),
(2,1,'2025-05-23','128/84',74,6.0,57.8,36.4,'良好','少盐','6小时','棋牌','');