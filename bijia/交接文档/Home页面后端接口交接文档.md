# Home 页面后端接口交接文档

## 📋 文档信息
- **项目名称**: bi-jia-net（笔架地图 - 海洋海岸带遥感影像智能解译云平台）
- **功能模块**: Home 页面 - 遥感数据检索
- **文档版本**: v1.0
- **更新日期**: 2026-04-13
- **对应前端页面**: `FrontEnd/src/views/Home.vue`

---

## 一、功能概述

Home 页面是平台的核心功能页面，提供遥感影像数据的检索、查看和处理分析功能。主要功能包括：

1. **遥感数据检索**：按数据类型、区域、时间范围筛选卫星影像
2. **区域选择**：支持省市选择和自定义绘制多边形区域
3. **数据展示**：在地图上显示卫星影像覆盖范围
4. **数据处理**：支持加入个人数据和处理分析功能
5. **智能问答**：提供 AI 智能问答助手

---

## 二、核心接口清单

### 2.1 卫星数据检索接口（核心接口）

**接口描述**: 根据查询条件检索卫星影像数据，支持按省份/城市、卫星类型、时间范围筛选

**请求方式**: `GET`

**接口路径**: `/map/satellite/list1`

**请求参数**:
```javascript
{
  // 分页参数
  pageNum: 1,              // 页码
  pageSize: 10,            // 每页条数
  
  // 区域检索（二选一）
  province: "山东省",      // 省份名称（可选）
  city: "青岛市",          // 城市名称（可选）
  points: [],              // 自定义多边形区域坐标数组，格式：["lat,lng", "lat,lng", ...]
  
  // 卫星类型筛选
  satelliteTypes: ["高分系列", "资源系列"], // 卫星类型数组
  
  // 时间范围
  startCollectTime: "2024-01-01 00:00:00", // 起始采集时间
  endCollectTime: "2024-12-31 23:59:59"    // 结束采集时间
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| province | String | 否 | 省份名称，模糊查询 |
| city | String | 否 | 城市名称，模糊查询 |
| points | Array | 否 | 自定义区域坐标数组，与province/city互斥 |
| satelliteTypes | Array | 否 | 卫星类型列表，IN查询 |
| startCollectTime | String | 否 | 采集时间起始，格式：yyyy-MM-dd HH:mm:ss |
| endCollectTime | String | 否 | 采集时间结束，格式：yyyy-MM-dd HH:mm:ss |

**响应示例**:
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "id": 1,
      "satelliteType": "高分系列",
      "sensorType": "PMS",
      "collectTime": "2024-03-15 10:30:00",
      "province": "山东省",
      "city": "青岛市",
      "image": "http://111.15.191.198:8066/profile/upload/2024/03/15/xxx.png",
      "sourcefilepath": "http://111.15.191.198:8066/profile/upload/2024/03/15/xxx.tif",
      "sourcefilename": "GF6_PMS_20240315.tif",
      "leftupLatitude": 36.123456789012,
      "leftupLongitude": 120.123456789012,
      "leftdownLatitude": 35.987654321098,
      "leftdownLongitude": 120.234567890123,
      "rightupLatitude": 36.234567890123,
      "rightupLongitude": 120.345678901234,
      "rightdownLatitude": 36.098765432109,
      "rightdownLongitude": 120.456789012345,
      "centerlatitude": 36.111111111111,
      "centerlongitude": 120.222222222222,
      "resolution": "2米",
      "region": "胶州湾区域",
      "title": "胶州湾高分六号影像",
      "exInfo": "其他信息"
    }
  ]
}
```

**后端实现**:
- **Controller**: `SatelliteController.list1()` - `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-admin\src\main\java\com\skzh\web\controller\map\SatelliteController.java`
- **Service**: `SatelliteServiceImpl.selectSatelliteList1()` - `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-biz\src\main\java\com\skzh\map\service\impl\SatelliteServiceImpl.java`
- **Mapper**: `SatelliteMapper.selectSatelliteList1()` - `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-biz\src\main\java\com\skzh\map\mapper\SatelliteMapper.java`
- **XML**: `SatelliteMapper.xml` - `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-biz\src\main\resources\mapper\map\SatelliteMapper.xml`

**核心逻辑**:
1. **省市检索**：当 `points` 为空时，按 province、city、satelliteTypes、时间范围查询
2. **自定义区域检索**：当 `points` 不为空时，先查询所有符合条件的数据，再用射线法判断卫星影像中心点是否在多边形内
3. **路径转换**：将本地文件路径转换为 HTTP 访问路径
4. **中心点计算**：如果影像没有中心点坐标，自动计算四个角点的中心并更新数据库

---

### 2.2 获取卫星详情接口

**接口描述**: 根据 ID 获取单条卫星影像的详细信息

**请求方式**: `GET`

**接口路径**: `/map/satellite/{id}`

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 卫星影像ID |

**响应示例**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "satelliteType": "高分系列",
    "sensorType": "PMS",
    "collectTime": "2024-03-15 10:30:00",
    "image": "http://111.15.191.198:8066/profile/upload/2024/03/15/xxx.png",
    "sourcefilepath": "http://111.15.191.198:8066/profile/upload/2024/03/15/xxx.tif",
    // ... 其他字段
  }
}
```

---

### 2.3 智能问答接口

**接口描述**: 调用 AI 智能问答服务

**请求方式**: `GET`

**接口路径**: `/answer/getByContent`

**请求参数**:
```javascript
{
  content: "请介绍这个区域的地形特征"  // 用户提问内容
}
```

**后端实现**:
- **Controller**: `AnswerController.getByContent()` - `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-admin\src\main\java\com\skzh\web\controller\answer\AnswerController.java`

**配置信息** (`application.yml`):
```yaml
subjectToken:
  url: https://iam-pub.cn-east-317.qdrgznjszx.com/v3/auth/tokens
  name: cn-east-317
  ak: HYL4UG6XKXQSZWFQO6RU
  sk: f8wT60YJUFh1taLV1QqJ4waG3UcHDR0JLMWiECJG

answer:
  url: https://24fdf11523a54e97b2dbe15a4cddeed5.infer.qdrgznjszx.com/v1/infers/2ad15b54-ce72-48e2-a868-3497d21e3d3b/glm2_bot
```

---

## 三、数据库表结构

### 3.1 satellite 表（卫星影像表）

**表名**: `satellite`

**建表语句位置**: `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\sql\bijia.sql`

**关键字段**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键，自增 |
| satellite_type | VARCHAR(64) | 卫星类型（高分系列、资源系列、吉林1号、无人机） |
| sensor_type | VARCHAR(64) | 传感器类型 |
| image | VARCHAR(500) | 卫星PNG/JPG图片路径 |
| sourcefilepath | VARCHAR(1000) | TIFF源文件路径 |
| sourcefilename | VARCHAR(255) | 源文件名称 |
| collect_time | DATETIME | 采集时间 |
| province | VARCHAR(64) | 省份 |
| city | VARCHAR(64) | 城市 |
| region | VARCHAR(64) | 区域描述 |
| leftup_latitude | DECIMAL(18,12) | 左上角纬度 |
| leftup_longitude | DECIMAL(18,12) | 左上角经度 |
| leftdown_latitude | DECIMAL(18,12) | 左下角纬度 |
| leftdown_longitude | DECIMAL(18,12) | 左下角经度 |
| rightup_latitude | DECIMAL(18,12) | 右上角纬度 |
| rightup_longitude | DECIMAL(18,12) | 右上角经度 |
| rightdown_latitude | DECIMAL(18,12) | 右下角纬度 |
| rightdown_longitude | DECIMAL(18,12) | 右下角经度 |
| centerlatitude | DECIMAL(18,12) | 中心点纬度 |
| centerlongitude | DECIMAL(18,12) | 中心点经度 |
| resolution | VARCHAR(255) | 分辨率 |
| title | VARCHAR(255) | 标题 |
| ex_info | VARCHAR(255) | 其他信息 |
| starttime | DATETIME | 开始时间 |
| endtime | DATETIME | 结束时间 |
| acquisitiontime | DATETIME | 采集时间 |
| receivedtime | DATETIME | 接收时间 |
| orbitid | VARCHAR(255) | 轨道ID |
| receivestationid | VARCHAR(255) | 接收站ID |
| create_by | VARCHAR(64) | 创建人 |
| create_time | DATETIME | 创建时间 |
| update_by | VARCHAR(64) | 更新人 |
| update_time | DATETIME | 更新时间 |

---

## 四、核心业务逻辑

### 4.1 检索流程

```
用户输入查询条件
    ↓
前端调用 listSatellite1(queryParams)
    ↓
后端 SatelliteController.list1()
    ↓
判断 points 是否为空
    ├─ 是（省市检索）
    │   └─ 调用 selectSatelliteList1(satellite)
    │       └─ SQL 按 province, city, satelliteTypes, collect_time 查询
    │
    └─ 否（自定义区域检索）
        └─ 调用 selectSatelliteList1(satellite) 获取所有数据
            └─ 遍历每条数据
                ├─ 如果有 centerlatitude/centerlongitude
                │   └─ 判断中心点是否在多边形内（射线法）
                └─ 如果没有
                    ├─ 计算四个角点的中心点
                    ├─ 更新数据库 centerlatitude/centerlongitude
                    └─ 判断中心点是否在多边形内
            └─ 返回符合条件的数据
    ↓
转换文件路径（本地路径 → HTTP路径）
    ↓
分页处理
    ↓
返回数据给前端
```

### 4.2 区域判断算法

**算法**: 射线法（Ray Casting Algorithm）

**实现位置**: `H:\IDEA_Project\bi-jia-net\BackEnd\skzh\skzh-admin\src\main\java\com\skzh\web\util\GraphMapUtils.java`

**核心方法**:
```java
// 判断点是否在多边形内
public static boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> polygon)
```

---

## 五、文件路径处理

### 5.1 路径转换逻辑

**配置文件**: `application.yml`
```yaml
skzh:
  # 文件上传路径（Windows）
  profile: F:/skzh/uploadPath
  # 文件上传路径（Linux）
  # profile: /storage/bijia/uploadPath
```

**转换规则**:
```java
// 本地路径 → HTTP路径
String filePath = SkzhConfig.getUploadPath();  // F:/skzh/uploadPath
String prefixUrl = serverConfig.getUrl();      // http://111.15.191.198:8066

if (image.indexOf(filePath) >= 0) {
    image = image.replace(filePath, prefixUrl + "/profile/upload");
}
```

**示例**:
- 本地路径: `F:/skzh/uploadPath/2024/03/15/xxx.png`
- HTTP路径: `http://111.15.191.198:8066/profile/upload/2024/03/15/xxx.png`

---

## 六、前端调用示例

### 6.1 数据检索

```javascript
import { listSatellite1 } from '@/api/map/satellite'

// 查询参数
const queryParams = {
  pageNum: 1,
  pageSize: 10,
  province: '山东省',
  city: '青岛市',
  satelliteTypes: ['高分系列', '资源系列'],
  startCollectTime: '2024-01-01 00:00:00',
  endCollectTime: '2024-12-31 23:59:59'
}

// 调用接口
listSatellite1(queryParams).then(response => {
  console.log('总条数:', response.total)
  console.log('数据列表:', response.rows)
  
  response.rows.forEach(item => {
    console.log('卫星类型:', item.satelliteType)
    console.log('传感器:', item.sensorType)
    console.log('采集时间:', item.collectTime)
    console.log('缩略图:', item.image)
    console.log('源文件:', item.sourcefilepath)
  })
})
```

### 6.2 自定义区域检索

```javascript
// 自定义多边形区域
const queryParams = {
  pageNum: 1,
  pageSize: 10,
  points: [
    '36.123456789012,120.123456789012',
    '36.234567890123,120.234567890123',
    '36.345678901234,120.345678901234',
    '36.123456789012,120.123456789012'  // 闭合
  ],
  satelliteTypes: ['高分系列'],
  startCollectTime: '2024-01-01 00:00:00',
  endCollectTime: '2024-12-31 23:59:59'
}

listSatellite1(queryParams).then(response => {
  // 处理返回数据
})
```

---

## 七、相关代码文件清单

### 7.1 后端代码

| 文件路径 | 说明 |
|---------|------|
| `BackEnd/skzh/skzh-admin/src/main/java/com/skzh/web/controller/map/SatelliteController.java` | 卫星数据控制器 |
| `BackEnd/skzh/skzh-biz/src/main/java/com/skzh/map/service/ISatelliteService.java` | 卫星服务接口 |
| `BackEnd/skzh/skzh-biz/src/main/java/com/skzh/map/service/impl/SatelliteServiceImpl.java` | 卫星服务实现 |
| `BackEnd/skzh/skzh-biz/src/main/java/com/skzh/map/mapper/SatelliteMapper.java` | 卫星数据Mapper |
| `BackEnd/skzh/skzh-biz/src/main/resources/mapper/map/SatelliteMapper.xml` | MyBatis映射文件 |
| `BackEnd/skzh/skzh-admin/src/main/java/com/skzh/web/controller/answer/AnswerController.java` | 智能问答控制器 |
| `BackEnd/skzh/skzh-admin/src/main/java/com/skzh/web/util/GraphMapUtils.java` | 图形工具类（区域判断） |
| `BackEnd/skzh/skzh-biz/src/main/java/com/skzh/map/domain/Satellite.java` | 卫星实体类 |

### 7.2 前端代码

| 文件路径 | 说明 |
|---------|------|
| `FrontEnd/src/views/Home.vue` | Home 页面主文件 |
| `FrontEnd/src/api/map/satellite.js` | 卫星数据API |
| `FrontEnd/src/utils/request.js` | Axios请求封装 |

### 7.3 配置文件

| 文件路径 | 说明 |
|---------|------|
| `BackEnd/skzh/skzh-admin/src/main/resources/application.yml` | 应用配置 |
| `FrontEnd/.env.development` | 开发环境配置 |
| `FrontEnd/vite.config.js` | Vite配置（代理） |

---

## 八、联调注意事项

### 8.1 环境配置

**后端服务**:
- 服务地址: `http://localhost:8066`
- 文件上传路径: `F:/skzh/uploadPath`
- Redis地址: `localhost:6379`

**前端服务**:
- 开发环境API前缀: `/dev-api`
- 代理目标: `http://localhost:8066`

### 8.2 Vite 代理配置

在 `FrontEnd/vite.config.js` 中配置：

```javascript
{
  server: {
    proxy: {
      '/dev-api': {
        target: 'http://localhost:8066',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev-api/, '')
      }
    }
  }
}
```

### 8.3 常见问题

#### 问题1: 检索结果为空

**排查步骤**:
1. 检查数据库中是否有符合条件的数据
2. 确认 satellite_type 字段值与前端传递的值一致
3. 检查时间格式是否正确（yyyy-MM-dd HH:mm:ss）
4. 查看后端日志确认 SQL 语句

#### 问题2: 自定义区域检索不准确

**排查步骤**:
1. 确认 points 数组格式正确（lat,lng）
2. 检查中心点坐标是否已计算
3. 验证射线法算法逻辑
4. 测试已知点在多边形内的判断结果

#### 问题3: 图片无法加载

**排查步骤**:
1. 检查 image 字段的路径转换是否正确
2. 确认文件服务器可访问
3. 检查本地文件路径是否存在
4. 验证 serverConfig.getUrl() 返回值

#### 问题4: 分页数据不正确

**排查步骤**:
1. 检查 pageNum 和 pageSize 是否正确传递
2. 确认 PageHelper 分页插件配置
3. 查看 total 字段值是否准确
4. 检查 SQL 是否被分页拦截

---

## 九、性能优化建议

### 9.1 数据库优化

1. **添加索引**:
   ```sql
   -- 卫星类型索引
   CREATE INDEX idx_satellite_type ON satellite(satellite_type);
   
   -- 采集时间索引
   CREATE INDEX idx_collect_time ON satellite(collect_time);
   
   -- 省份城市索引
   CREATE INDEX idx_province_city ON satellite(province, city);
   
   -- 中心点坐标索引（用于空间查询优化）
   CREATE INDEX idx_center_lat_lng ON satellite(centerlatitude, centerlongitude);
   ```

2. **分区表**: 如果数据量超过百万级，建议按采集时间分区

### 9.2 查询优化

1. **缓存策略**: 对热门查询结果进行 Redis 缓存
2. **懒加载**: 图片资源采用懒加载方式
3. **分页查询**: 限制单次查询结果数量，避免一次性加载过多数据

### 9.3 空间查询优化

1. **预计算中心点**: 数据入库时即计算并存储中心点坐标
2. **空间索引**: 使用 MySQL 的空间数据类型和索引（POINT、SPATIAL INDEX）
3. **范围过滤**: 先用外接矩形过滤，再精确判断

---

## 十、扩展功能接口

### 10.1 其他相关接口（未在 Home 页面直接使用）

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| `/map/satellite/list` | GET | 卫星数据列表（管理后台使用） |
| `/map/satellite` | POST | 新增卫星数据 |
| `/map/satellite` | PUT | 修改卫星数据 |
| `/map/satellite/{ids}` | DELETE | 删除卫星数据 |
| `/map/satellite/export` | POST | 导出卫星数据 |

### 10.2 AI 处理相关接口

项目中还包含多种 AI 处理功能（浒苔检测、马尾藻检测、海岸线提取等），这些功能通过 WebSocket 异步处理，具体实现参考：

- `hutaiRouter.java` - 浒苔检测
- `maweizaoRouter.java` - 马尾藻检测
- `haianxianRouter.java` - 海岸线提取
- `yangzhiquRouter.java` - 养殖区检测
- `redtideRouter.java` - 赤潮检测
- `mapRouter.java` - 专题图生成

---

## 十一、测试数据准备

### 11.1 插入测试数据示例

```sql
INSERT INTO satellite (
  satellite_type, sensor_type, collect_time, province, city, region,
  leftup_latitude, leftup_longitude, leftdown_latitude, leftdown_longitude,
  rightup_latitude, rightup_longitude, rightdown_latitude, rightdown_longitude,
  centerlatitude, centerlongitude,
  image, sourcefilepath, sourcefilename, resolution, title
) VALUES (
  '高分系列', 'PMS', '2024-03-15 10:30:00', '山东省', '青岛市', '胶州湾区域',
  36.123456789012, 120.123456789012, 35.987654321098, 120.234567890123,
  36.234567890123, 120.345678901234, 36.098765432109, 120.456789012345,
  36.111111111111, 120.222222222222,
  'F:/skzh/uploadPath/2024/03/15/test.png',
  'F:/skzh/uploadPath/2024/03/15/test.tif',
  'GF6_PMS_20240315.tif',
  '2米',
  '胶州湾高分六号影像'
);
```

### 11.2 卫星类型枚举值

根据前端代码，支持的卫星类型包括：
- `高分系列`
- `资源系列`
- `吉林1号`
- `无人机`

---

## 十二、联系方式

### 技术支持
- **项目负责人**: [待补充]
- **后端开发**: [待补充]
- **前端开发**: [待补充]
- **项目仓库**: [待补充]

### 相关文档
- 项目 README: `README.md`
- 数据库脚本: `BackEnd/skzh/sql/bijia.sql`
- 登录注册接口文档: `登录注册功能前后端联调交接文档.md`

---

## 附录

### A. 完整请求示例（curl）

```bash
# 省市检索
curl -X GET "http://localhost:8066/map/satellite/list1?province=山东省&city=青岛市&satelliteTypes=高分系列&startCollectTime=2024-01-01%2000:00:00&endCollectTime=2024-12-31%2023:59:59&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 自定义区域检索
curl -X GET "http://localhost:8066/map/satellite/list1?points=36.123456789012,120.123456789012&points=36.234567890123,120.234567890123&points=36.345678901234,120.345678901234&satelliteTypes=高分系列&startCollectTime=2024-01-01%2000:00:00&endCollectTime=2024-12-31%2023:59:59&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### B. SQL 查询示例

```sql
-- 基础查询（省市检索）
SELECT * FROM satellite
WHERE province = '山东省'
  AND city = '青岛市'
  AND satellite_type IN ('高分系列', '资源系列')
  AND collect_time >= '2024-01-01 00:00:00'
  AND collect_time <= '2024-12-31 23:59:59'
ORDER BY collect_time DESC, create_time DESC
LIMIT 10 OFFSET 0;

-- 查询统计
SELECT satellite_type, COUNT(*) as count
FROM satellite
WHERE province = '山东省'
GROUP BY satellite_type;

-- 时间范围查询
SELECT DATE(collect_time) as date, COUNT(*) as count
FROM satellite
WHERE collect_time >= '2024-01-01'
GROUP BY DATE(collect_time)
ORDER BY date DESC;
```

---

**文档结束**

如有疑问，请及时沟通反馈！🚀
