# 东软颐养中心 — 养老院综合管理系统

一个面向养老机构的综合信息管理系统，覆盖**客户（长者）管理、床位管理、护理管理、健康管家服务**与**系统用户管理**五大业务域。前端为 Vue 3 单页应用，后端为 Spring Boot 3 单体服务，数据存储于 MySQL。

本仓库为**整合后的完整工程**，前后端各一份，开箱即可运行（详见 [第 2 节](#二版本说明)）。

---

## 一、项目简介

系统以"长者入住"为主线，串联起从**入住登记 → 床位分配 → 护理级别设置 → 日常护理记录 → 退住/外出审批**的完整业务闭环，并为健康管家提供服务对象分配与日常体征记录能力。所有业务接口均受登录鉴权保护。

---

## 二、版本说明

| 项 | 说明 |
|---|---|
| 当前版本 |单一 `backend/` + 单一 `frontend/` + `sql/`，可直接编译运行 |

---

## 三、技术栈

### 后端
| 组件 | 版本 | 说明 |
|---|---|---|
| Spring Boot | 3.2.5 | `spring-boot-starter-web` |
| Java | 21 | `java.version=21`，编译 `release=21` |
| MyBatis-Plus | 3.5.6 | `mybatis-plus-spring-boot3-starter`，ORM |
| MySQL Connector/J | 随 Spring Boot 管理 | `com.mysql:mysql-connector-j` |
| Lombok | 随 Spring Boot 管理 | 简化实体类样板代码 |
| Maven | — | 坐标 `com.neusoft:neusoft-care:1.0.0` |

### 前端
| 组件 | 版本（package.json） |
|---|---|
| Vue | ^3.4.27 |
| Vue Router | ^4.3.2（History 模式） |
| Element Plus | ^2.7.6 |
| @element-plus/icons-vue | ^2.3.1 |
| Axios | ^1.7.2 |
| Vite | ^8.0.14 |
| @vitejs/plugin-vue | ^5.0.4 |

> 依赖安装建议使用 `npm ci`（仓库已附带 `package-lock.json`）。注意 `vite` 与 `@vitejs/plugin-vue` 主版本跨度较大，全新安装时留意 peerDependencies 冲突。

---

## 四、目录结构

```
elderly-care-management-system/
├── backend/                        # Spring Boot 单工程
│   ├── pom.xml
│   ├── src/main/java/com/neusoft/care/
│   │   ├── CareApplication.java    # 启动类，@MapperScan("com.neusoft.care.**.mapper")
│   │   ├── common/                 # Result、CorsConfig、WebMvcConfig、AuthInterceptor、
│   │   │                           # GlobalExceptionHandler、DashboardController
│   │   ├── auth/                   # 认证：controller / service / dto / TokenStore
│   │   ├── user/                   # 用户与角色
│   │   ├── customer/               # 客户、入住、退住、外出
│   │   ├── bed/                    # 房间、床位、换床
│   │   ├── nursing/                # 护理项目/级别/设置/记录
│   │   └── health/                 # 健康管家、服务对象、服务关注、日常护理
│   ├── src/main/resources/application.yml
│   └── target/                     # 构建产物（已入库，见第 9 节）
├── frontend/                       # Vue 3 单工程
│   ├── package.json  vite.config.js  index.html  .env.development
│   ├── dist/                       # 构建产物（已入库）
│   └── src/
│       ├── main.js  App.vue
│       ├── api/         # request.js（axios 实例）+ index.js（全量 API 方法）
│       ├── router/      # 路由表（全量）
│       ├── layout/      # MainLayout.vue（侧边菜单 + 顶栏）
│       ├── views/       # Login / Register / Dashboard
│       ├── modules/     # 业务页面：customer / bed / nursing / health / user
│       ├── components/  # PageCard.vue 等
│       ├── utils/       # auth.js、useListLoad.js
│       └── styles/
├── sql/
│   ├── neusoft_care.sql            # 全量建库建表 + 模拟数据
│   └── upgrade_user_auth.sql       # 增量升级脚本（可重复执行）
├── .gitattributes
└── .git/                           # 注意：无 .gitignore
```

---

## 五、功能模块

### 5.1 客户管理（`/api/customer`）
- **客户档案**：列表（按姓名模糊查询）、详情、新增/修改、删除。
- **入住登记**：
  - `POST /check-in` 直接登记入住记录；
  - `POST /check-in/register` **一站式入住**：新建客户 + 生成入住记录 + 自动占用床位（事务内完成），并按出生日期计算年龄、由房间楼层推导楼栋。
- **退住管理**：提交申请 → 审批（通过/拒绝）。
- **外出管理**：提交申请 → 审批；通过置为"外出"，返回后置回"在院"。
- **业务校验**：合同到期日不得早于入住日；床位不存在或已被占用时拒绝办理。

### 5.2 床位管理（`/api/bed`）
- 床位列表、房间列表、按楼层分组的房间视图（供床位示意图渲染）。
- 空床查询（按房间）、楼层床位统计（总量/占用/空闲）。
- **换床**：记录换床流水，并调整原床位（释放）与目标床位（占用）状态。
- 基础维护：房间/床位增删改、床位状态修改、换床记录删除。
- 状态含义：`0` 空闲、`1` 占用、`2` 维护。

### 5.3 护理模块（`/api/nursing`）
- **护理项目**（`nursing_item`）：编码、名称、分类、单位、价格、周期。
- **护理级别**（`nursing_level`）：级别编码、名称、频次、价格，并关联一组护理项目（`level_item`）。
- **客户护理设置**（`customer_nursing`）：为长者指定护理级别及生效日期。
- **护理记录**（`nursing_record`）：记录每次护理的项目、时间、执行人、状态。

### 5.4 健康管家（`/api/health`）
- **管家档案**（`health_steward`）：姓名、性别、电话、部门、状态。
- **服务对象分配**（`customer_steward`）：把长者分配给具体管家，含分配日期与状态。
- **服务关注**（`service_attention`）：增值服务的名称、类型、起止日期、价格、状态。
- **日常护理**（`daily_care`）：每日体征——血压、心率、血糖、体重、体温、情绪、饮食、睡眠、活动量及备注。

### 5.5 用户管理（`/api/auth` + `/api/user`）
- **登录认证**（`/api/auth`）：登录、注册、登出、获取当前用户。
- **系统用户**（`/api/user`）：用户列表（关联角色名）、角色字典、新增/修改、删除、**重置密码**。
- **角色**（`sys_role`）：`admin` 系统管理员、`steward` 健康管家、`nurse` 护理人员。
- **默认密码规则**：手机号后 6 位；手机号缺失或不足 6 位时回退为 `123456`。

---

## 六、数据库设计

数据库：`neusoft_care`（`utf8mb4` / `utf8mb4_unicode_ci`），共 **18 张表**。

| 归属 | 表名 | 用途 |
|---|---|---|
| 公共 | `sys_role` | 角色字典 |
| 公共 | `sys_user` | 系统用户（`status`：1 启用 / 0 禁用） |
| 客户管理 | `customer` | 长者档案（`status`：1 在院 / 2 外出 / 3 退住） |
| 客户管理 | `check_in_record` | 入住记录（合同号、到期日、押金） |
| 客户管理 | `check_out_request` | 退住申请（0 待审核 / 1 已通过 / 2 已拒绝） |
| 客户管理 | `outing_request` | 外出申请（0 待审核 / 1 已通过 / 2 已拒绝 / 3 已返回） |
| 床位管理 | `room` | 房间（房号、楼层、房型、床位数、价格） |
| 床位管理 | `bed` | 床位（`status`：0 空闲 / 1 占用 / 2 维护） |
| 床位管理 | `bed_exchange` | 换床流水 |
| 护理模块 | `nursing_item` | 护理项目 |
| 护理模块 | `nursing_level` | 护理级别 |
| 护理模块 | `level_item` | 级别-项目关联 |
| 护理模块 | `customer_nursing` | 客户护理设置 |
| 护理模块 | `nursing_record` | 护理记录 |
| 健康管家 | `health_steward` | 健康管家档案 |
| 健康管家 | `customer_steward` | 管家服务对象分配 |
| 健康管家 | `service_attention` | 服务关注 |
| 健康管家 | `daily_care` | 日常护理（体征）记录 |

### 初始化脚本
- **`sql/neusoft_care.sql`**：`CREATE DATABASE` + 建表 + 模拟数据（3 个角色、5 个用户、12 个房间、21 张床位、16 位长者及配套入住/护理/管家数据）。**含 `DROP TABLE`，仅用于首次初始化。**
- **`sql/upgrade_user_auth.sql`**：面向已有库的增量升级，基于 `information_schema` 判断后 `ALTER TABLE`，**可重复执行**。新增 `sys_user.status`、`customer.birth_date/blood_type/family_member/building`、`check_in_record.contract_expire_date`，并把种子用户密码从 `123456` 改写为手机号后 6 位。

---

## 七、接口规范

### 7.1 统一响应体
所有接口返回 `Result<T>`：

```json
{ "code": 200, "message": "success", "data": { } }
```

- `code = 200` 成功；`500` 失败（`message` 为原因）。
- 前端响应拦截器**直接返回 `data`**，业务代码无需再取 `.data`。
- 异常由 `GlobalExceptionHandler` 兜底转换为 `Result.fail(...)`。

### 7.2 认证机制
- 登录成功后返回 **UUID token**（32 位十六进制），保存在服务端内存 `ConcurrentHashMap`（`TokenStore`），**无过期时间，重启服务即失效**。
- 前端将 token 存入 `localStorage`（键 `care_token`，见 `utils/auth.js`），由 axios 请求拦截器写入 `Authorization` 请求头（**裸 token，无 `Bearer ` 前缀**）。
- `AuthInterceptor` 拦截 `/api/**`，放行 `/api/auth/**` 与 `/api/user/roles`；`OPTIONS` 预检直接放行。
- 校验失败抛出"请先登录"；前端拦截到该提示会清除本地凭证并跳转 `/login`。
- 前端 `request.js` 默认**直连** `http://localhost:8080/api`（`VITE_API_BASE_URL`），超时 15 秒；跨域由后端 `CorsConfig` 处理。

### 7.3 接口清单

**客户管理 `/api/customer`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/list?name=` | 客户列表（姓名模糊查询） |
| GET | `/{id}` | 客户详情 |
| POST | `/save` | 新增/修改客户 |
| DELETE | `/{id}` | 删除客户 |
| GET | `/check-in/list` | 入住记录列表 |
| POST | `/check-in` | 登记入住 |
| POST | `/check-in/register` | 一站式入住（建档+入住+占床） |
| DELETE | `/check-in/{id}` | 删除入住记录 |
| GET | `/check-out/list` | 退住申请列表 |
| POST | `/check-out/submit` | 提交退住申请 |
| POST | `/check-out/audit` | 审批退住（`{id, status, auditor}`） |
| DELETE | `/check-out/{id}` | 删除退住申请 |
| GET | `/outing/list` | 外出申请列表 |
| POST | `/outing/submit` | 提交外出申请 |
| POST | `/outing/audit` | 审批外出（`{id, status, auditor}`） |
| DELETE | `/outing/{id}` | 删除外出申请 |

**床位管理 `/api/bed`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/list` | 床位列表（含房间、客户信息） |
| GET | `/rooms` | 房间列表 |
| GET | `/rooms/grouped` | 按楼层分组的房间 |
| GET | `/vacant?roomId=` | 指定房间的空闲床位 |
| GET | `/stats` | 楼层床位统计 |
| GET | `/exchange/list` | 换床记录 |
| POST | `/exchange` | 换床 |
| PUT | `/{id}/status?status=` | 修改床位状态 |
| POST | `/rooms` | 新增/修改房间 |
| DELETE | `/rooms/{id}` | 删除房间 |
| POST | `/save` | 新增/修改床位 |
| DELETE | `/{id}` | 删除床位 |
| DELETE | `/exchange/{id}` | 删除换床记录 |

**护理模块 `/api/nursing`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET / POST | `/items` | 护理项目列表 / 保存 |
| DELETE | `/items/{id}` | 删除护理项目 |
| GET / POST | `/levels` | 护理级别列表（含项目）/ 保存 |
| DELETE | `/levels/{id}` | 删除护理级别 |
| GET / POST | `/customer-settings` | 客户护理设置列表 / 保存 |
| DELETE | `/customer-settings/{id}` | 删除客户护理设置 |
| GET / POST | `/records` | 护理记录列表 / 保存 |
| DELETE | `/records/{id}` | 删除护理记录 |

**健康管家 `/api/health`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET / POST | `/stewards` | 管家列表 / 保存 |
| DELETE | `/stewards/{id}` | 删除管家 |
| GET / POST | `/customer-stewards` | 服务对象分配列表 / 保存 |
| DELETE | `/customer-stewards/{id}` | 删除分配 |
| GET / POST | `/service-attention` | 服务关注列表 / 保存 |
| DELETE | `/service-attention/{id}` | 删除服务关注 |
| GET / POST | `/daily-care` | 日常护理列表 / 保存 |
| DELETE | `/daily-care/{id}` | 删除日常护理 |

**认证 `/api/auth`（免鉴权）**

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/login` | 登录，返回 token 与用户信息 |
| POST | `/register` | 注册，返回 token 与用户信息 |
| POST | `/logout` | 登出（使 token 失效） |
| GET | `/me` | 获取当前登录用户 |

**用户管理 `/api/user`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/list` | 用户列表（含角色名） |
| GET | `/roles` | 角色字典（**免鉴权**） |
| POST | `/save` | 新增/修改用户 |
| DELETE | `/{id}` | 删除用户 |
| POST | `/{id}/reset-password` | 重置密码为手机号后 6 位 |

**首页统计 `/api/dashboard`**

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/stats` | 客户总数、在院数、床位总数/占用/空闲、待审退住、待审外出、护理记录数、日常护理数 |

---

## 八、快速开始

### 8.1 环境要求
- JDK **21**、Maven 3.8+
- Node.js 18+ / npm
- MySQL 5.7 或 8.0

### 8.2 初始化数据库
```bash
# 全新初始化（会重建表）
mysql -uroot -p123456 < sql/neusoft_care.sql
```
若数据库已存在，改用增量脚本：
```bash
mysql -uroot -p123456 < sql/upgrade_user_auth.sql
```
默认连接信息见 `backend/src/main/resources/application.yml`：`localhost:3306/neusoft_care`，`root/123456`。

### 8.3 启动后端
```bash
cd backend
mvn spring-boot:run
```
服务地址：`http://localhost:8080`。

### 8.4 启动前端
```bash
cd frontend
npm ci          # 或 npm install
npm run dev
```
访问 `http://localhost:5173`。

前端默认直连 `http://localhost:8080/api`（`.env.development` 中的 `VITE_API_BASE_URL`）；`vite.config.js` 同时配置了 `/api` 代理到 `VITE_API_TARGET`（默认 `http://localhost:8080`）用于 `dev` 与 `preview`。

### 8.5 默认账号
初始密码为**手机号后 6 位**：

| 用户名 | 密码 | 角色 | 姓名 |
|---|---|---|---|
| `admin` | `000001` | 系统管理员 | 系统管理员 |
| `steward01` | `000002` | 健康管家 | 张管家 |
| `steward02` | `000003` | 健康管家 | 李管家 |
| `nurse01` | `000005` | 护理人员 | 刘护士 |
| `nurse02` | `000006` | 护理人员 | 陈护士 |

---

## 九、已知问题与改进建议

以下为代码审查结论，按影响程度排列。

### 9.1 安全（优先处理）
- **密码明文存储与明文比对**：`sys_user.password` 直接存明文；`AuthService` 登录用 `request.getPassword().equals(user.getPassword())` 比较；注册与 `UserService.resetPassword` 同样明文写入。**应改为 BCrypt 等加盐哈希。**（`AuthService` L23-59，`UserService` L33-68）
- **Token 机制过弱**：`TokenStore` 用 `ConcurrentHashMap<String, SysUser>` 存 UUID token，**无过期时间**，服务重启全部失效，且无法多实例共享。建议改用 JWT 或 Redis 并设置有效期。（`TokenStore` L24-40）
- **数据库凭证硬编码**：`application.yml` 明文写死 `root/123456`，建议改用环境变量或配置中心。
- **默认密码可预测**：由手机号后 6 位生成（不足则 `123456`），建议强制首次登录修改。
- **`/api/user/roles` 被排除在鉴权之外**：虽无敏感数据，可评估是否收回。

### 9.2 业务逻辑
- **退住审批未释放床位**：`auditCheckOut` 审批通过（status=1）时只把 `customer.status` 置为 `3` 并清空 `customer.bed_id`，**未将 `bed.status` 复位为 `0`、也未清空 `bed.customer_id`**，床位会一直显示"占用"，导致床位统计虚高。（`CustomerService` L187-203）**这是本版本最值得优先修复的缺陷。**
- **换床缺少校验**：`exchangeBed` 未校验目标床位是否存在、是否空闲、是否与原床位相同，直接覆盖状态，可能造成两个客户指向同一床位。（`BedService` L70-85）
- **删除操作缺少级联处理**：删除客户/房间/床位时未校验关联的在住记录、护理记录等，数据库层面也无外键约束，易产生悬空数据。
- **`deleteCheckIn` 未回滚床位占用状态**，与入住时的占用逻辑不对称。

### 9.3 工程实践
- **无 `.gitignore`**：`backend/target/`（构建产物）、`frontend/dist/`、`backend/.idea/` 均已入库，且存在 `package-lock.json` 之外的冗余。建议补充 `.gitignore` 并清理已跟踪的产物。
- **无统一分页**：列表接口一次性返回全表数据，数据量增大后存在性能隐患，建议引入 MyBatis-Plus 分页。
- **缺少接口文档**：未集成 Swagger/OpenAPI，建议补充。
- **无自动化测试**：未见单元/集成测试，建议对入住、退住、换床等核心流程补充测试。
- **异常粒度粗**：业务异常统一以 `IllegalArgumentException` 抛出并映射为 `code=500`，建议区分业务错误码（400 参数错误、401 未登录、403 无权限）。
- **依赖版本跨度**：`vite ^8.0.14` 与 `@vitejs/plugin-vue ^5.0.4` 主版本差异较大，全新安装时留意 peerDependencies 冲突。

---

## 十、开发约定（摘要）

- 后端分层：`controller` → `service` → `mapper`（MyBatis-Plus `BaseMapper`），实体在 `entity`，传输对象在 `dto`。
- MyBatis-Plus 配置：`map-underscore-to-camel-case: true`（下划线转驼峰），主键 `id` 自增（`id-type: auto`）。
- 前端页面统一放在 `src/modules/<业务>/`，页面外壳复用 `components/PageCard.vue`，列表加载复用 `utils/useListLoad.js`。
- 所有请求经 `src/api/request.js` 统一处理鉴权头与错误提示，业务代码只调用 `src/api/index.js` 导出的方法。