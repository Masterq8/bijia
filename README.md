# BiJiaNet - 海洋海岸带遥感影像智能解译云平台

![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-1.8-orange)
![Spring%20Boot](https://img.shields.io/badge/Spring%20Boot-2.5.15-brightgreen)
![Vue](https://img.shields.io/badge/Vue-3-success)
![Vite](https://img.shields.io/badge/Vite-5.3.2-purple)

## 📋 项目介绍

**BiJiaNet** 是一个开源的海洋海岸带遥感影像智能解译云平台，基于**Spring Boot + Vue 3 + Vite** 构建。平台集成了先进的遥感影像处理和AI智能解译功能，支持海岸线、绿潮、湿地、赤潮等海洋海岸要素的快速识别与提取。

### 核心特性

✨ **智能遥感解译**
- 海岸线自动提取
- 绿潮（浒苔）范围识别（16m和2m两种精度）
- 海水养殖区斑块识别
- 滨海湿地信息提取
- 赤潮疑似区域识别
- 马尾藻分布提取（16m和2m精度）
- 土地覆盖多地物智能分类
- 多时相变化检测

🗺️ **交互式地图**
- 基于Leaflet的高性能地图引擎
- 支持多种影像源与瓦片图层
- 动态地理信息标记与可视化
- 自定义区域绘制（多边形、矩形、矢量图上传）
- 初始定位到黄渤海区域，支持缩放与比例尺控件
- 影像快速预览与定位功能

🎨 **现代化UI/UX**
- 深色/浅色双主题适配
- 响应式设计支持多分辨率
- 流畅的动画与过渡效果
- Element Plus组件库集成

🔐 **企业级权限管理**
- 基于角色的访问控制（RBAC）
- 用户/角色/权限三层管理
- 数据资源权限隔离
- 操作日志与审计追踪

⚙️ **完整的后端服务**
- RESTful API设计
- 多模块服务架构
- 定时任务调度（Quartz）
- 缓存加速（Redis）
- 数据持久化（MySQL 8.4）

---

## 🏗️ 技术栈

### 后端
| 技术 | 版本 | 描述 |
|------|------|------|
| Java | 1.8 | 编程语言 |
| Spring Boot | 2.5.15 | 应用框架 |
| Maven | 3.9.9 | 项目构建 |
| MySQL | 8.4 | 数据库 |
| Redis | Latest | 缓存服务 |
| Quartz | - | 定时任务 |

### 前端
| 技术 | 版本 | 描述 |
|------|------|------|
| Vue | 3 | 前端框架 |
| Vite | 5.3.2 | 构建工具 |
| Element Plus | - | UI组件库 |
| Pinia | - | 状态管理 |
| Leaflet | - | 地图库 |
| Axios | - | HTTP客户端 |

---

## 📦 项目结构

```
bi-jia-net/
├── BackEnd/                          # 后端项目
│   └── skzh/                         # Spring Boot多模块项目
│       ├── pom.xml                  # 父POM
│       ├── skzh-admin/              # 后端启动模块（8066端口）
│       ├── skzh-biz/                # 业务逻辑层
│       ├── skzh-common/             # 公共工具类
│       ├── skzh-framework/          # 框架基础
│       ├── skzh-generator/          # 代码生成器
│       ├── skzh-system/             # 系统核心模块
│       ├── skzh-quartz/             # 定时任务模块
│       └── sql/
│           └── bijia.sql            # 数据库初始化脚本
├── FrontEnd/                         # 前端项目
│   ├── package.json                 # 依赖配置
│   ├── vite.config.js               # Vite配置
│   ├── index.html                   # 入口HTML
│   ├── src/
│   │   ├── main.js                  # 应用入口
│   │   ├── App.vue                  # 根组件
│   │   ├── router/                  # 路由定义
│   │   ├── store/                   # Pinia状态管理
│   │   ├── components/              # 可复用组件
│   │   │   ├── Map/                 # 地图组件
│   │   │   ├── Breadcrumb/          # 面包屑
│   │   │   └── ...
│   │   ├── views/                   # 页面组件
│   │   │   ├── Home.vue             # 遥感数据检索（主页）
│   │   │   ├── aiProcess.vue        # AI工具箱与处理
│   │   │   ├── login.vue            # 登录页
│   │   │   └── ...
│   │   ├── api/                     # API接口定义
│   │   ├── utils/                   # 工具函数
│   │   ├── assets/                  # 静态资源
│   │   │   └── styles/
│   │   │       └── theme.scss       # 全局主题变量
│   │   ├── directive/               # 自定义指令
│   │   ├── plugins/                 # 插件配置
│   │   └── permission.js            # 权限控制
│   ├── public/                       # 公共资源
│   ├── bin/                          # 辅助脚本
│   │   ├── build.bat                # 生产构建脚本
│   │   ├── run-web.bat              # 开发运行脚本
│   │   └── package.bat              # 打包脚本
│   └── SampleView/                  # 功能截图示例
├── README.md                         # 本文档
└── .gitignore                        # Git忽略规则
```

---

## 🚀 快速开始

### 环境要求

- **Java**: JDK 1.8 或更高版本
- **Node.js**: 16+ 或 pnpm 8+
- **MySQL**: 8.4 或兼容版本
- **Redis**: 最新版本
- **Maven**: 3.9.9 或更高版本

### 后端启动

1. **数据库初始化**
```bash
# 使用MySQL客户端导入初始化脚本
mysql -uroot -proot < BackEnd/skzh/sql/bijia.sql
```

2. **启动Redis服务**
```bash
# Windows用户
redis-server.exe redis.windows.conf

# Linux/Mac用户
redis-server
```

3. **构建后端项目**
```bash
cd BackEnd/skzh
mvn clean install -DskipTests
```

4. **运行Spring Boot应用**
```bash
# 方法1：使用Maven插件
mvn -pl skzh-admin org.springframework.boot:spring-boot-maven-plugin:2.5.15:run -DskipTests

# 方法2：直接运行JAR
java -jar skzh-admin/target/bijia-boot.jar
```

后端服务将启动在 **http://localhost:8066**

### 前端启动

1. **安装依赖**
```bash
cd FrontEnd

# 使用npm
npm install

# 或使用pnpm（推荐）
pnpm install
```

2. **启动开发服务器**
```bash
# 使用npm
npm run dev

# 或使用pnpm
pnpm dev
```

3. **访问应用**
打开浏览器访问 **http://localhost:5173**

### 登录凭证

创建的管理员用户：
- **用户名**: rootadmin
- **密码**: 123456
- **权限**: 超级管理员（可访问所有功能和菜单）

---

## 📝 核心功能模块

### 1. 遥感数据检索与管理（Home.vue）
- 支持按卫星、传感器、时间范围检索公开数据
- 自定义区域绘制（多边形/矩形）或GeoJSON上传
- 本地影像上传与解析
- 数据快速预览与位置定位
- 快速日期选择（近3个月/6个月/1年等）

### 2. AI遥感智能解译工具（aiProcess.vue）
- 工具选择与激活
- 批量影像处理
- 任务状态跟踪
- 要素提取结果查看

### 3. 交互式地图（Map.vue）
- 初始定位：黄渤海中心（37.5, 120），缩放级别7
- 可交互的影像覆盖图层
- 自定义绘制工具（GeomAn）
- 缩放控件与比例尺（右下角）
- 动态标记与信息弹窗

### 4. 用户权限管理
- 用户创建与删除
- 角色绑定与权限分配
- 菜单权限隔离
- 操作日志记录

### 5. 主题系统
- 深色/浅色主题切换
- 全局CSS变量驱动（`FrontEnd/src/assets/styles/theme.scss`）
- 实时主题应用与本地存储

---

## 🎯 最近更新（v1.0）

### 前端优化
- ✅ **主题适配**: 修复白色主题下AI工具页样式，全面使用全局主题变量
- ✅ **界面优化**: 删除遥感数据页顶部重复按钮，优化工具卡片边框（1.8px，深色#4c4d4f、浅色#dcdfe6）
- ✅ **布局调整**: 调整遥感数据框体宽度与下方卡片对齐，解决右侧被挡问题
- ✅ **地图功能**: 初始定位到黄渤海中心(37.5,120)，添加缩放控件和比例尺控件
- ✅ **菜单清理**: 删除aboutUs菜单（前端路由、数据库菜单、角色绑定）

### 后端优化
- ✅ **权限管理**: 插入最高权限用户(rootadmin)并关联所有菜单权限
- ✅ **数据初始化**: 优化数据库初始化脚本

---

## 🔧 配置指南

### 后端配置（application.yml）
```yaml
# 数据库连接
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/skzh
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

# Redis连接
redis:
  host: localhost
  port: 6379

# 服务端口
server:
  port: 8066
```

### 前端代理配置（vite.config.js）
```javascript
export default {
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8066',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
}
```

### 主题变量自定义
编辑 `FrontEnd/src/assets/styles/theme.scss`：

```scss
// 深色主题
[data-theme="dark"] {
  --bg-primary: #0f172a;
  --text-primary: #f1f5f9;
  --tool-card-border: #4c4d4f;
  // ...更多变量
}

// 浅色主题
[data-theme="light"] {
  --bg-primary: #ffffff;
  --text-primary: #1e293b;
  --tool-card-border: #dcdfe6;
  // ...更多变量
}
```

---

## 📚 API文档

### 主要接口端点

#### 用户相关
```
POST   /api/login              # 用户登录
GET    /api/user/info          # 获取用户信息
POST   /api/logout             # 用户登出
```

#### 遥感数据
```
GET    /api/satellite/list     # 获取卫星数据列表
POST   /api/satellite/search   # 搜索数据
POST   /api/upload             # 上传影像
```

#### AI处理
```
POST   /api/ai/order/create    # 创建AI处理任务
GET    /api/ai/order/{id}      # 获取任务详情
GET    /api/ai/results/{id}    # 获取处理结果
```

更多API详情见 `BackEnd/skzh/skzh-biz/src/main/java/com/skzh/controller/`

---

## 🐛 常见问题

### Q: 启动后端时显示Redis连接失败
**A:** 确保Redis服务已启动。Windows用户可使用：
```bash
redis-server.exe redis.windows.conf
```

### Q: 前端DEV模式跨域报错
**A:** 检查 `vite.config.js` 中的代理配置，确保 `target` 指向正确的后端地址（默认 `http://localhost:8066`）

### Q: 打包后为空白页
**A:** 检查 `index.html` 中的base路径，确保与部署路径一致

### Q: 某些菜单选项消失或权限不足
**A:** 需要以admin或rootadmin用户登录，检查 `sys_role_menu` 表中该角色的菜单关联

### Q: 地图瓦片不显示
**A:** 检查网络连接，确保高德地图API可访问（`FrontEnd/src/components/Map/Map.vue` 中的TileLayer配置）

---

## 📄 许可证

本项目采用 **MIT License**，详见 [LICENSE](LICENSE) 文件

---

## 👥 贡献指南

欢迎提交Issue和Pull Request！

### 提交步骤
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 提交规范
- 使用清晰的commit message
- 一个commit解决一个问题
- 大改动前先开Issue讨论

---

## 📞 联系方式

- 📧 Email: 2362104471@qq.com
- 🐙 GitHub: [Masterq8/bijia](https://github.com/Masterq8/bijia)
- 🌐 项目主页: [BiJiaNet](https://github.com/Masterq8/bijia)

---

**最后更新**: 2026年5月13日  
**当前版本**: v1.0.0  
**维护状态**: 🟢 活跃开发
