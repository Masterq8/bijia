from pathlib import Path


ROOT = Path(r"d:\code\bi-jia-net-master\bi-jia-net-master\BackEnd\skzh")
OUT = ROOT / "file_specification.md"


ROOT_FILES = {
    "pom.xml": ("父工程 Maven 配置", "定义 7 个子模块、统一依赖版本和编译参数。"),
    "README.md": ("项目简介", "介绍项目名称、版本与技术栈概览。"),
    "LICENSE": ("授权文件", "声明项目使用的开源许可。"),
    ".keep": ("占位文件", "用于保留空目录。"),
}


SPECIAL = {
    "skzh-admin/src/main/java/com/skzh/SkzhApplication.java": ("Spring Boot 启动入口", "启动整个后端应用。"),
    "skzh-admin/src/main/java/com/skzh/SkzhServletInitializer.java": ("WAR 部署入口", "用于外部容器部署时初始化 SpringBoot 应用。"),
    "skzh-admin/src/main/java/com/skzh/config/AsyncConfig.java": ("异步配置", "配置异步线程执行能力。"),
    "skzh-admin/src/main/java/com/skzh/config/WebSocketConfig.java": ("WebSocket 配置", "注册 WebSocket 服务端端点。"),
    "skzh-admin/src/main/java/com/skzh/kit/haianxianRouter.java": ("海岸线检测流程", "封装海岸线提取任务的 Python 调用与结果回写。"),
    "skzh-admin/src/main/java/com/skzh/kit/hutai2Router.java": ("互采二代检测流程", "互采检测的另一套 Python 驱动封装。"),
    "skzh-admin/src/main/java/com/skzh/kit/hutaiRouter.java": ("互采/绿潮检测流程", "查询卫星数据、创建订单、生成 XML 配置并启动 Python 处理。"),
    "skzh-admin/src/main/java/com/skzh/kit/mapRouter.java": ("专题图生成流程", "根据专题图请求生成订单并调用 ArcGIS Python 脚本。"),
    "skzh-admin/src/main/java/com/skzh/kit/maweizao2Router.java": ("马尾藻二代检测流程", "马尾藻二代任务的外部程序封装。"),
    "skzh-admin/src/main/java/com/skzh/kit/maweizaoRouter.java": ("马尾藻检测流程", "驱动马尾藻检测外部程序并处理输出。"),
    "skzh-admin/src/main/java/com/skzh/kit/redtideRouter.java": ("赤潮检测流程", "赤潮检测任务的 Python/命令行编排。"),
    "skzh-admin/src/main/java/com/skzh/kit/yangzhiquRouter.java": ("养殖区检测流程", "养殖区识别任务的外部脚本驱动。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/ai/AiController.java": ("AI 数据列表接口", "查询与 AI 处理相关的卫星数据并修正图片访问地址。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/ai/AiProcessController.java": ("AI 任务入口", "按订单 type 分发到不同检测流程，并通过 WebSocket 通知完成状态。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/answer/AnswerController.java": ("智能问答接口", "管理 token 缓存并把用户输入转发给外部问答服务。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/common/CaptchaController.java": ("验证码接口", "生成字符/算术验证码并缓存验证码答案。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/common/CommonController.java": ("通用文件接口", "实现本地文件上传、下载和资源下载。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/common/MinioController.java": ("MinIO 文件接口", "封装 MinIO 上传、下载、删除。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/common/SeaweedFsController.java": ("SeaweedFS 文件接口", "封装 SeaweedFS 上传、下载、删除。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/map/DataInfoController.java": ("基础地图数据接口", "按字典类型查询地图相关基础数据。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/map/SatelliteController.java": ("卫星数据接口", "卫星影像查询、分页与导出。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/monitor/CacheController.java": ("缓存监控接口", "查看/清理 Redis 缓存。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/monitor/ServerController.java": ("服务器监控接口", "返回 CPU、内存、JVM、磁盘等运行信息。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/monitor/SysLogininforController.java": ("登录日志接口", "查询、导出、删除和清空登录日志。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/monitor/SysOperlogController.java": ("操作日志接口", "查询、导出、删除和清空操作日志。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/monitor/SysUserOnlineController.java": ("在线用户接口", "列出在线会话并支持强制下线。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/order/OrderInfoController.java": ("订单信息接口", "列出订单、下载结果文件、切换是否展示。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/orderManagement/OrderManagementController.java": ("订单管理接口", "订单 CRUD、导出、批量导出与专题图生成入口。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/orderManagement/OrderWebSocket.java": ("订单 WebSocket 推送", "向前端实时推送订单状态更新。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/record/RecordInfoController.java": ("我的数据接口", "支持查询、批量新增、压缩包解压导入和导出。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/system/SysLoginController.java": ("登录控制器", "登录、获取用户信息和路由菜单。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/system/SysProfileController.java": ("个人中心接口", "个人资料、头像、密码、邮箱/短信验证码找回密码。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/system/SysRegisterController.java": ("注册控制器", "用户注册入口。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/system/SysUserController.java": ("用户管理接口", "用户列表、导入导出、授权角色、重置密码等。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/thematicMap/ThematicMapController.java": ("专题图接口", "专题图 CRUD、图片类型列表与代码查询。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/visits/VisitController.java": ("访问统计接口", "返回每日访问量统计。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/websocket/ServerEncoder.java": ("WebSocket 编码器", "把 Java 对象编码成可传输消息。"),
    "skzh-admin/src/main/java/com/skzh/web/controller/websocket/WebSocketServer.java": ("WebSocket 服务端", "维护会话并支持单发/群发消息。"),
    "skzh-admin/src/main/java/com/skzh/web/core/config/SwaggerConfig.java": ("Swagger 配置", "配置接口文档、鉴权头和基础信息。"),
    "skzh-admin/src/main/java/com/skzh/web/util/GraphMapUtils.java": ("几何工具", "计算中心点和点是否在多边形内。"),
    "skzh-admin/src/main/java/com/skzh/web/util/SmsUtil.java": ("短信工具", "初始化阿里云短信客户端。"),

    "skzh-biz/src/main/java/com/skzh/aiProcess/domain/BackInfo.java": ("返回结果对象", "承载外部流程状态与附加信息。"),
    "skzh-biz/src/main/java/com/skzh/aiProcess/domain/LockInfo.java": ("文件锁对象", "用于文件锁和随机读写文件句柄封装。"),
    "skzh-biz/src/main/java/com/skzh/aiProcess/domain/zhuantituVo.java": ("专题图入参对象", "用于专题图生成请求传参。"),
    "skzh-biz/src/main/java/com/skzh/aiProcess/service/OrdersService.java": ("旧版 AI 服务接口", "当前文件已注释，属于旧版专题图/AI 流程接口草稿。"),
    "skzh-biz/src/main/java/com/skzh/aiProcess/service/impl/OrdersServiceImpl.java": ("历史 AI 流程实现", "当前文件已注释，属于旧版专题图/AI 任务实现草稿。"),
    "skzh-biz/src/main/java/com/skzh/collection/domain/CollectionInfo.java": ("收藏实体", "承载收藏表数据。"),
    "skzh-biz/src/main/java/com/skzh/collection/mapper/CollectionInfoMapper.java": ("收藏 Mapper", "定义收藏表数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/collection/service/ICollectionInfoService.java": ("收藏业务接口", "定义收藏业务契约。"),
    "skzh-biz/src/main/java/com/skzh/collection/service/impl/CollectionInfoServiceImpl.java": ("收藏业务实现", "收藏信息 CRUD，并自动写入时间戳。"),
    "skzh-biz/src/main/java/com/skzh/map/domain/DataInfo.java": ("基础数据实体", "承载地图基础字典数据。"),
    "skzh-biz/src/main/java/com/skzh/map/domain/Satellite.java": ("卫星实体", "承载卫星影像数据。"),
    "skzh-biz/src/main/java/com/skzh/map/dto/MapPoint.java": ("地图点对象", "用于地图几何计算。"),
    "skzh-biz/src/main/java/com/skzh/map/mapper/DataInfoMapper.java": ("地图基础数据 Mapper", "定义基础数据查询。"),
    "skzh-biz/src/main/java/com/skzh/map/mapper/SatelliteMapper.java": ("卫星 Mapper", "定义卫星影像数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/map/service/DataInfoService.java": ("地图基础数据业务接口", "定义基础数据查询契约。"),
    "skzh-biz/src/main/java/com/skzh/map/service/ISatelliteService.java": ("卫星业务接口", "定义卫星业务契约。"),
    "skzh-biz/src/main/java/com/skzh/map/service/impl/DataInfoServiceImpl.java": ("地图基础数据业务实现", "按字典类型查询基础字典数据。"),
    "skzh-biz/src/main/java/com/skzh/map/service/impl/SatelliteServiceImpl.java": ("卫星业务实现", "卫星影像 CRUD、批量查询、中心点更新。"),
    "skzh-biz/src/main/java/com/skzh/order/domain/OrderInfo.java": ("订单信息实体", "承载订单基础信息。"),
    "skzh-biz/src/main/java/com/skzh/order/domain/OrderManagement.java": ("订单管理实体", "承载订单管理相关字段。"),
    "skzh-biz/src/main/java/com/skzh/order/mapper/OrderInfoMapper.java": ("订单信息 Mapper", "定义订单信息数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/order/mapper/OrderManagementMapper.java": ("订单管理 Mapper", "定义订单管理数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/order/service/OrderInfoService.java": ("订单信息业务接口", "定义订单信息查询契约。"),
    "skzh-biz/src/main/java/com/skzh/order/service/OrderManagementService.java": ("订单管理业务接口", "定义订单管理契约。"),
    "skzh-biz/src/main/java/com/skzh/order/service/impl/OrderInfoServiceImpl.java": ("订单信息业务实现", "查询订单、主页列表与展示状态更新。"),
    "skzh-biz/src/main/java/com/skzh/order/service/impl/OrderManagementServiceImpl.java": ("订单管理业务实现", "生成订单号、补全用户信息、设置价格并写入订单。"),
    "skzh-biz/src/main/java/com/skzh/record/domain/RecordInfo.java": ("我的数据实体", "承载记录/我的数据表数据。"),
    "skzh-biz/src/main/java/com/skzh/record/mapper/RecordInfoMapper.java": ("我的数据 Mapper", "定义我的数据数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/record/service/IRecordInfoService.java": ("我的数据业务接口", "定义我的数据契约。"),
    "skzh-biz/src/main/java/com/skzh/record/service/impl/RecordInfoServiceImpl.java": ("我的数据业务实现", "记录信息 CRUD、批量保存和时间戳维护。"),
    "skzh-biz/src/main/java/com/skzh/thematic/domain/Thematic.java": ("专题图实体", "承载专题图数据。"),
    "skzh-biz/src/main/java/com/skzh/thematic/mapper/ThematicMapper.java": ("专题图 Mapper", "定义专题图数据库操作。"),
    "skzh-biz/src/main/java/com/skzh/thematic/service/ThematicService.java": ("专题图业务接口", "定义专题图业务契约。"),
    "skzh-biz/src/main/java/com/skzh/thematic/service/impl/ThematicServiceImpl.java": ("专题图业务实现", "专题图查询、类型下拉、增删改。"),
    "skzh-biz/src/main/java/com/skzh/visits/domain/visits.java": ("访问统计实体", "承载访问统计数据。"),
    "skzh-biz/src/main/java/com/skzh/visits/mapper/visitsMapper.java": ("访问统计 Mapper", "定义访问统计数据库查询。"),
    "skzh-biz/src/main/java/com/skzh/visits/service/VisitService.java": ("访问统计业务接口", "定义访问统计契约。"),
    "skzh-biz/src/main/java/com/skzh/visits/service/impl/VisitServiceImpl.java": ("访问统计业务实现", "查询每日访问量。"),

    "skzh-common/src/main/java/com/skzh/common/config/MinioConfig.java": ("MinIO 配置", "绑定 MinIO 连接配置。"),
    "skzh-common/src/main/java/com/skzh/common/config/SkzhConfig.java": ("项目配置绑定", "读取项目名、版本、上传路径、验证码类型等。"),
    "skzh-common/src/main/java/com/skzh/common/config/serializer/SensitiveJsonSerializer.java": ("敏感字段序列化器", "对敏感字段进行脱敏序列化。"),
    "skzh-common/src/main/java/com/skzh/common/core/controller/BaseController.java": ("控制器基类", "提供分页、排序、统一返回和登录用户获取。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/AjaxResult.java": ("统一 Ajax 返回对象", "统一封装 code/msg/data 与 success/error/warn。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/BaseEntity.java": ("基础实体", "封装创建/更新/备注/参数等通用字段。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/R.java": ("通用响应对象", "封装 code/msg/data 风格返回体。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/TreeEntity.java": ("树形实体", "封装树结构父子关系。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/TreeSelect.java": ("树选择对象", "把部门/菜单树转换成前端树选择结构。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/model/LoginBody.java": ("登录入参对象", "封装用户名、密码、验证码等。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/model/LoginUser.java": ("登录用户模型", "承载认证后的用户、权限和 token 信息。"),
    "skzh-common/src/main/java/com/skzh/common/core/domain/model/RegisterBody.java": ("注册入参对象", "封装注册表单数据。"),
    "skzh-common/src/main/java/com/skzh/common/core/page/PageDomain.java": ("分页请求对象", "承载页码、每页数量与排序字段。"),
    "skzh-common/src/main/java/com/skzh/common/core/page/TableDataInfo.java": ("表格分页返回对象", "封装 total、rows、code、msg。"),
    "skzh-common/src/main/java/com/skzh/common/core/page/TableSupport.java": ("分页辅助类", "从请求中解析分页参数。"),
    "skzh-common/src/main/java/com/skzh/common/core/redis/RedisCache.java": ("Redis 工具", "封装 String/List/Set/Hash 缓存操作。"),
    "skzh-common/src/main/java/com/skzh/common/core/text/CharsetKit.java": ("字符集工具", "提供字符集处理辅助。"),
    "skzh-common/src/main/java/com/skzh/common/core/text/Convert.java": ("转换工具", "提供基础类型转换能力。"),
    "skzh-common/src/main/java/com/skzh/common/core/text/StrFormatter.java": ("字符串格式化工具", "支持占位符格式化。"),
    "skzh-common/src/main/java/com/skzh/common/constant/CacheConstants.java": ("缓存常量", "定义 Redis key 前缀。"),
    "skzh-common/src/main/java/com/skzh/common/constant/Constants.java": ("系统常量", "定义通用常量与权限常量。"),
    "skzh-common/src/main/java/com/skzh/common/constant/GenConstants.java": ("生成器常量", "定义代码生成相关常量。"),
    "skzh-common/src/main/java/com/skzh/common/constant/HttpStatus.java": ("HTTP 状态常量", "定义成功、失败、警告等状态码。"),
    "skzh-common/src/main/java/com/skzh/common/constant/ScheduleConstants.java": ("调度常量", "定义 Quartz 任务常量。"),
    "skzh-common/src/main/java/com/skzh/common/constant/UserConstants.java": ("用户常量", "定义用户唯一性、密码等常量。"),

    "skzh-common/src/main/java/com/skzh/common/enums/BusinessStatus.java": ("业务状态枚举", "定义操作成功/失败等状态。"),
    "skzh-common/src/main/java/com/skzh/common/enums/BusinessType.java": ("业务类型枚举", "定义新增、修改、删除、导出等类型。"),
    "skzh-common/src/main/java/com/skzh/common/enums/DataSourceType.java": ("数据源类型枚举", "定义主/从/动态数据源类型。"),
    "skzh-common/src/main/java/com/skzh/common/enums/DesensitizedType.java": ("脱敏类型枚举", "定义手机号、邮箱等脱敏方式。"),
    "skzh-common/src/main/java/com/skzh/common/enums/HttpMethod.java": ("HTTP 方法枚举", "定义 GET/POST/PUT/DELETE 等方法。"),
    "skzh-common/src/main/java/com/skzh/common/enums/LimitType.java": ("限流类型枚举", "定义按 IP、用户等限流方式。"),
    "skzh-common/src/main/java/com/skzh/common/enums/OperatorType.java": ("操作类型枚举", "定义其他、后台、移动端等操作源。"),
    "skzh-common/src/main/java/com/skzh/common/enums/UserStatus.java": ("用户状态枚举", "定义正常、停用等状态。"),

    "skzh-common/src/main/java/com/skzh/common/exception/GlobalException.java": ("全局异常", "表示业务层统一异常。"),
    "skzh-common/src/main/java/com/skzh/common/exception/DemoModeException.java": ("演示模式异常", "用于限制演示环境的写操作。"),
    "skzh-common/src/main/java/com/skzh/common/exception/ServiceException.java": ("服务异常", "业务层抛出的统一异常。"),
    "skzh-common/src/main/java/com/skzh/common/exception/UtilException.java": ("工具异常", "工具类异常基类。"),
    "skzh-common/src/main/java/com/skzh/common/exception/base/BaseException.java": ("基础异常", "异常体系的基础类。"),
    "skzh-common/src/main/java/com/skzh/common/exception/file/FileException.java": ("文件异常", "文件处理异常基类。"),
    "skzh-common/src/main/java/com/skzh/common/exception/file/FileNameLengthLimitExceededException.java": ("文件名长度超限异常", "文件名过长时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/file/FileSizeLimitExceededException.java": ("文件大小超限异常", "文件过大时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/file/FileUploadException.java": ("文件上传异常", "文件上传失败时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/file/InvalidExtensionException.java": ("非法扩展名异常", "文件后缀不允许时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/job/TaskException.java": ("任务异常", "Quartz 调度任务异常。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/BlackListException.java": ("黑名单异常", "黑名单用户访问时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/CaptchaException.java": ("验证码异常", "验证码错误时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/CaptchaExpireException.java": ("验证码过期异常", "验证码过期时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/UserException.java": ("用户异常", "用户模块异常基类。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/UserNotExistsException.java": ("用户不存在异常", "用户名不存在时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/UserPasswordNotMatchException.java": ("密码不匹配异常", "登录密码错误时抛出。"),
    "skzh-common/src/main/java/com/skzh/common/exception/user/UserPasswordRetryLimitExceedException.java": ("密码重试超限异常", "密码输入错误次数过多时抛出。"),

    "skzh-common/src/main/java/com/skzh/common/filter/PropertyPreExcludeFilter.java": ("属性过滤器", "用于序列化时排除指定字段。"),
    "skzh-common/src/main/java/com/skzh/common/filter/RepeatableFilter.java": ("可重复读过滤器", "包装请求以支持重复读取请求体。"),
    "skzh-common/src/main/java/com/skzh/common/filter/RepeatedlyRequestWrapper.java": ("重复请求包装器", "支持请求体多次读取。"),
    "skzh-common/src/main/java/com/skzh/common/filter/XssFilter.java": ("XSS 过滤器", "拦截并过滤 XSS 风险输入。"),
    "skzh-common/src/main/java/com/skzh/common/filter/XssHttpServletRequestWrapper.java": ("XSS 请求包装器", "对请求参数做 XSS 处理。"),

    "skzh-common/src/main/java/com/skzh/common/utils/Arith.java": ("算术工具", "提供高精度算术运算。"),
    "skzh-common/src/main/java/com/skzh/common/utils/DateUtils.java": ("日期工具", "处理日期格式化、解析与时间戳。"),
    "skzh-common/src/main/java/com/skzh/common/utils/DesensitizedUtil.java": ("脱敏工具", "对手机号、邮箱等做脱敏处理。"),
    "skzh-common/src/main/java/com/skzh/common/utils/DictUtils.java": ("字典工具", "从缓存或服务中取字典。"),
    "skzh-common/src/main/java/com/skzh/common/utils/ExceptionUtil.java": ("异常工具", "统一异常栈处理。"),
    "skzh-common/src/main/java/com/skzh/common/utils/HttpClientUtil.java": ("HTTP 客户端工具", "封装 HTTP 请求发送。"),
    "skzh-common/src/main/java/com/skzh/common/utils/LogUtils.java": ("日志工具", "处理日志输出与包装。"),
    "skzh-common/src/main/java/com/skzh/common/utils/MessageUtils.java": ("消息工具", "国际化消息获取。"),
    "skzh-common/src/main/java/com/skzh/common/utils/PageUtils.java": ("分页工具", "辅助 PageHelper 分页。"),
    "skzh-common/src/main/java/com/skzh/common/utils/SecurityUtils.java": ("安全工具", "获取登录用户、校验密码、角色和权限。"),
    "skzh-common/src/main/java/com/skzh/common/utils/ServletUtils.java": ("Servlet 工具", "获取请求/响应对象。"),
    "skzh-common/src/main/java/com/skzh/common/utils/StringUtils.java": ("字符串工具", "通用字符串处理与判空方法。"),
    "skzh-common/src/main/java/com/skzh/common/utils/Threads.java": ("线程工具", "统一线程池/异步执行辅助。"),
    "skzh-common/src/main/java/com/skzh/common/utils/bean/BeanUtils.java": ("Bean 工具", "对象属性拷贝。"),
    "skzh-common/src/main/java/com/skzh/common/utils/bean/BeanValidators.java": ("校验工具", "Bean 校验辅助。"),
    "skzh-common/src/main/java/com/skzh/common/utils/file/FileTypeUtils.java": ("文件类型工具", "判断文件真实类型。"),
    "skzh-common/src/main/java/com/skzh/common/utils/file/FileUploadUtils.java": ("文件上传工具", "做大小/后缀校验并生成安全文件名。"),
    "skzh-common/src/main/java/com/skzh/common/utils/file/FileUtils.java": ("文件工具", "文件读写、下载与删除。"),
    "skzh-common/src/main/java/com/skzh/common/utils/file/ImageUtils.java": ("图片工具", "图片处理辅助。"),
    "skzh-common/src/main/java/com/skzh/common/utils/file/MimeTypeUtils.java": ("MIME 工具", "定义常见文件 MIME 类型和后缀。"),
    "skzh-common/src/main/java/com/skzh/common/utils/html/EscapeUtil.java": ("HTML 转义工具", "防止 HTML 注入。"),
    "skzh-common/src/main/java/com/skzh/common/utils/html/HTMLFilter.java": ("HTML 过滤工具", "过滤危险 HTML 标签。"),
    "skzh-common/src/main/java/com/skzh/common/utils/http/HttpHelper.java": ("HTTP 辅助工具", "辅助 HTTP 请求。"),
    "skzh-common/src/main/java/com/skzh/common/utils/http/HttpUtils.java": ("HTTP 工具", "HTTP 请求封装。"),
    "skzh-common/src/main/java/com/skzh/common/utils/ip/AddressUtils.java": ("地址工具", "IP 到地址转换。"),
    "skzh-common/src/main/java/com/skzh/common/utils/ip/IpUtils.java": ("IP 工具", "IP 获取与判断。"),
    "skzh-common/src/main/java/com/skzh/common/utils/minio/MinioTemplate.java": ("MinIO 配置模型", "承载 MinIO endpoint、密钥和 bucket。"),
    "skzh-common/src/main/java/com/skzh/common/utils/poi/ExcelHandlerAdapter.java": ("Excel 处理适配器", "辅助 Excel 单元格处理。"),
    "skzh-common/src/main/java/com/skzh/common/utils/poi/ExcelUtil.java": ("Excel 工具", "导入导出 Excel。"),
    "skzh-common/src/main/java/com/skzh/common/utils/reflect/ReflectUtils.java": ("反射工具", "封装反射能力。"),
    "skzh-common/src/main/java/com/skzh/common/utils/sign/Base64.java": ("Base64 工具", "Base64 编码解码。"),
    "skzh-common/src/main/java/com/skzh/common/utils/sign/Md5Utils.java": ("MD5 工具", "MD5 摘要计算。"),
    "skzh-common/src/main/java/com/skzh/common/utils/spring/SpringUtils.java": ("Spring 工具", "静态获取 Spring Bean。"),
    "skzh-common/src/main/java/com/skzh/common/utils/sql/SqlUtil.java": ("SQL 工具", "SQL 过滤与排序字段处理。"),
    "skzh-common/src/main/java/com/skzh/common/utils/uuid/IdUtils.java": ("ID 工具", "生成 UUID/简单 ID。"),
    "skzh-common/src/main/java/com/skzh/common/utils/uuid/Seq.java": ("序列工具", "生成递增序列。"),
    "skzh-common/src/main/java/com/skzh/common/utils/uuid/UUID.java": ("UUID 工具", "UUID 生成器。"),
    "skzh-common/src/main/java/com/skzh/common/xss/Xss.java": ("XSS 注解", "声明需要 XSS 过滤的字段。"),
    "skzh-common/src/main/java/com/skzh/common/xss/XssValidator.java": ("XSS 校验器", "执行 XSS 规则校验。"),

    "skzh-framework/src/main/java/com/skzh/framework/aspectj/DataScopeAspect.java": ("数据权限切面", "拼接数据权限 SQL 条件。"),
    "skzh-framework/src/main/java/com/skzh/framework/aspectj/DataSourceAspect.java": ("动态数据源切面", "根据注解切换数据源。"),
    "skzh-framework/src/main/java/com/skzh/framework/aspectj/LogAspect.java": ("日志切面", "记录操作日志。"),
    "skzh-framework/src/main/java/com/skzh/framework/aspectj/RateLimiterAspect.java": ("限流切面", "对接口进行访问限流。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/ApplicationConfig.java": ("应用配置", "注册通用 Spring 组件。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/CaptchaConfig.java": ("验证码配置", "配置验证码生成器。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/DruidConfig.java": ("Druid 配置", "配置数据源和监控。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/FastJson2JsonRedisSerializer.java": ("Redis 序列化器", "使用 FastJson2 序列化对象。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/FilterConfig.java": ("过滤器配置", "注册 XSS、重复读等过滤器。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/I18nConfig.java": ("国际化配置", "配置消息资源。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/KaptchaTextCreator.java": ("验证码文本生成器", "生成算术验证码文本。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/MyBatisConfig.java": ("MyBatis 配置", "配置分页、别名和插件。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/RedisConfig.java": ("Redis 配置", "配置 RedisTemplate 和序列化。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/ResourcesConfig.java": ("资源配置", "配置静态资源映射。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/SecurityConfig.java": ("安全配置", "配置 Spring Security、JWT 和放行规则。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/ServerConfig.java": ("服务地址配置", "提供服务器基础 URL。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/ThreadPoolConfig.java": ("线程池配置", "配置异步线程池。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/properties/DruidProperties.java": ("Druid 属性", "承载 Druid 连接池参数。"),
    "skzh-framework/src/main/java/com/skzh/framework/config/properties/PermitAllUrlProperties.java": ("放行地址属性", "配置无需认证的 URL。"),
    "skzh-framework/src/main/java/com/skzh/framework/datasource/DynamicDataSource.java": ("动态数据源", "实现主从数据源切换。"),
    "skzh-framework/src/main/java/com/skzh/framework/datasource/DynamicDataSourceContextHolder.java": ("数据源上下文", "持有当前线程数据源标识。"),
    "skzh-framework/src/main/java/com/skzh/framework/interceptor/RepeatSubmitInterceptor.java": ("重复提交拦截器", "防止重复请求。"),
    "skzh-framework/src/main/java/com/skzh/framework/interceptor/impl/SameUrlDataInterceptor.java": ("重复提交实现", "识别同 URL 重复请求。"),
    "skzh-framework/src/main/java/com/skzh/framework/manager/AsyncManager.java": ("异步任务管理器", "管理异步执行任务。"),
    "skzh-framework/src/main/java/com/skzh/framework/manager/ShutdownManager.java": ("关闭管理器", "应用关闭时回收资源。"),
    "skzh-framework/src/main/java/com/skzh/framework/manager/factory/AsyncFactory.java": ("异步工厂", "创建异步任务。"),
    "skzh-framework/src/main/java/com/skzh/framework/security/context/AuthenticationContextHolder.java": ("认证上下文", "保存当前认证对象。"),
    "skzh-framework/src/main/java/com/skzh/framework/security/context/PermissionContextHolder.java": ("权限上下文", "保存当前权限字符串。"),
    "skzh-framework/src/main/java/com/skzh/framework/security/filter/JwtAuthenticationTokenFilter.java": ("JWT 过滤器", "从请求中解析 token 并注入认证上下文。"),
    "skzh-framework/src/main/java/com/skzh/framework/security/handle/AuthenticationEntryPointImpl.java": ("认证入口处理器", "处理未认证请求。"),
    "skzh-framework/src/main/java/com/skzh/framework/security/handle/LogoutSuccessHandlerImpl.java": ("登出成功处理器", "处理退出登录逻辑。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/Server.java": ("服务器运行信息", "封装服务器监控数据。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/server/Cpu.java": ("CPU 信息", "封装 CPU 使用率数据。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/server/Jvm.java": ("JVM 信息", "封装 JVM 运行信息。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/server/Mem.java": ("内存信息", "封装内存使用情况。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/server/Sys.java": ("系统信息", "封装操作系统信息。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/domain/server/SysFile.java": ("磁盘信息", "封装文件系统/磁盘信息。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/exception/GlobalExceptionHandler.java": ("全局异常处理器", "统一处理 Controller 异常。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/PermissionService.java": ("权限服务", "封装权限判断服务。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/SysLoginService.java": ("登录服务", "处理登录认证与 token 生成。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/SysPasswordService.java": ("密码服务", "处理密码校验与重试限制。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/SysPermissionService.java": ("权限服务实现", "构建角色与权限集合。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/SysRegisterService.java": ("注册服务", "处理注册校验与用户创建。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/TokenService.java": ("Token 服务", "管理登录 token 的生成与缓存。"),
    "skzh-framework/src/main/java/com/skzh/framework/web/service/UserDetailsServiceImpl.java": ("用户详情服务", "Spring Security 用户加载实现。"),

    "skzh-generator/src/main/java/com/skzh/generator/config/GenConfig.java": ("生成器配置", "绑定代码生成参数。"),
    "skzh-generator/src/main/java/com/skzh/generator/controller/GenController.java": ("代码生成接口", "读取数据库表结构并驱动代码生成。"),
    "skzh-generator/src/main/java/com/skzh/generator/domain/GenTable.java": ("生成表实体", "承载数据库表元数据。"),
    "skzh-generator/src/main/java/com/skzh/generator/domain/GenTableColumn.java": ("生成列实体", "承载数据库列元数据。"),
    "skzh-generator/src/main/java/com/skzh/generator/mapper/GenTableColumnMapper.java": ("生成列 Mapper", "查询表列信息。"),
    "skzh-generator/src/main/java/com/skzh/generator/mapper/GenTableMapper.java": ("生成表 Mapper", "查询数据库表信息。"),
    "skzh-generator/src/main/java/com/skzh/generator/service/GenTableColumnServiceImpl.java": ("生成列服务实现", "查询和维护列元数据。"),
    "skzh-generator/src/main/java/com/skzh/generator/service/GenTableServiceImpl.java": ("生成表服务实现", "查询和维护表元数据。"),
    "skzh-generator/src/main/java/com/skzh/generator/service/IGenTableColumnService.java": ("生成列业务接口", "定义列元数据契约。"),
    "skzh-generator/src/main/java/com/skzh/generator/service/IGenTableService.java": ("生成表业务接口", "定义表元数据契约。"),
    "skzh-generator/src/main/java/com/skzh/generator/util/GenUtils.java": ("生成工具", "初始化表列信息并转换类名。"),
    "skzh-generator/src/main/java/com/skzh/generator/util/VelocityInitializer.java": ("Velocity 初始化工具", "初始化 Velocity 模板引擎。"),
    "skzh-generator/src/main/java/com/skzh/generator/util/VelocityUtils.java": ("Velocity 工具", "准备生成上下文、模板文件和导入列表。"),

    "skzh-quartz/src/main/java/com/skzh/quartz/config/ScheduleConfig.java": ("调度配置", "初始化 Quartz 调度器。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/controller/SysJobController.java": ("定时任务控制器", "定时任务 CRUD 与执行控制。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/controller/SysJobLogController.java": ("定时任务日志控制器", "定时任务日志查询与清理。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/domain/SysJob.java": ("定时任务实体", "承载任务定义。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/domain/SysJobLog.java": ("定时任务日志实体", "承载任务执行日志。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/mapper/SysJobLogMapper.java": ("任务日志 Mapper", "定义任务日志数据库操作。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/mapper/SysJobMapper.java": ("任务 Mapper", "定义任务数据库操作。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/service/ISysJobLogService.java": ("任务日志业务接口", "定义任务日志契约。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/service/ISysJobService.java": ("任务业务接口", "定义任务调度契约。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/service/impl/SysJobLogServiceImpl.java": ("任务日志业务实现", "查询、记录和清理任务日志。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/service/impl/SysJobServiceImpl.java": ("任务业务实现", "任务初始化、暂停、恢复等。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/task/RyTask.java": ("示例任务", "提供无参/单参/多参的调度测试方法。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/task/SubjectTokenTask.java": ("定时 token 刷新任务", "定期重新获取外部服务 token。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/AbstractQuartzJob.java": ("Quartz 抽象任务", "封装任务执行前后逻辑。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/CronUtils.java": ("Cron 工具", "校验和计算 Cron 表达式。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/EmailUtils.java": ("邮件工具", "发送邮件验证码。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/JobInvokeUtil.java": ("任务调用工具", "反射调用任务方法。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/QuartzDisallowConcurrentExecution.java": ("禁止并发执行任务", "任务执行包装类。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/QuartzJobExecution.java": ("普通任务执行器", "任务执行包装类。"),
    "skzh-quartz/src/main/java/com/skzh/quartz/util/ScheduleUtils.java": ("调度工具", "创建 Quartz 任务、处理 misfire 和白名单校验。"),

    "skzh-system/src/main/java/com/skzh/system/domain/SysCache.java": ("缓存视图对象", "承载缓存分类与键值信息。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysConfig.java": ("系统参数实体", "承载系统参数配置。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysLogininfor.java": ("登录日志实体", "承载登录记录。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysNotice.java": ("通知公告实体", "承载通知公告信息。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysOperLog.java": ("操作日志实体", "承载操作日志。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysPost.java": ("岗位实体", "承载岗位数据。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysRoleDept.java": ("角色部门关联实体", "承载角色与部门关系。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysRoleMenu.java": ("角色菜单关联实体", "承载角色与菜单关系。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysUserOnline.java": ("在线用户实体", "承载在线会话信息。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysUserPost.java": ("用户岗位关联实体", "承载用户与岗位关系。"),
    "skzh-system/src/main/java/com/skzh/system/domain/SysUserRole.java": ("用户角色关联实体", "承载用户与角色关系。"),
    "skzh-system/src/main/java/com/skzh/system/domain/vo/MetaVo.java": ("路由元信息对象", "承载路由标题、图标和缓存标识。"),
    "skzh-system/src/main/java/com/skzh/system/domain/vo/RouterVo.java": ("路由对象", "承载前端路由结构。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysConfigMapper.java": ("系统参数 Mapper", "定义系统参数数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysDeptMapper.java": ("部门 Mapper", "定义部门数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysDictDataMapper.java": ("字典数据 Mapper", "定义字典数据数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysDictTypeMapper.java": ("字典类型 Mapper", "定义字典类型数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysLogininforMapper.java": ("登录日志 Mapper", "定义登录日志数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysMenuMapper.java": ("菜单 Mapper", "定义菜单数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysNoticeMapper.java": ("通知公告 Mapper", "定义通知公告数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysOperLogMapper.java": ("操作日志 Mapper", "定义操作日志数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysPostMapper.java": ("岗位 Mapper", "定义岗位数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysRoleDeptMapper.java": ("角色部门 Mapper", "定义角色部门关系数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysRoleMapper.java": ("角色 Mapper", "定义角色数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysRoleMenuMapper.java": ("角色菜单 Mapper", "定义角色菜单关系数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysUserMapper.java": ("用户 Mapper", "定义用户数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysUserPostMapper.java": ("用户岗位 Mapper", "定义用户岗位关系数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/mapper/SysUserRoleMapper.java": ("用户角色 Mapper", "定义用户角色关系数据库操作。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysConfigService.java": ("系统参数业务接口", "定义系统参数契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysDeptService.java": ("部门业务接口", "定义部门契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysDictDataService.java": ("字典数据业务接口", "定义字典数据契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysDictTypeService.java": ("字典类型业务接口", "定义字典类型契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysLogininforService.java": ("登录日志业务接口", "定义登录日志契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysMenuService.java": ("菜单业务接口", "定义菜单契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysNoticeService.java": ("通知公告业务接口", "定义通知公告契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysOperLogService.java": ("操作日志业务接口", "定义操作日志契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysPostService.java": ("岗位业务接口", "定义岗位契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysRoleService.java": ("角色业务接口", "定义角色契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysUserOnlineService.java": ("在线用户业务接口", "定义在线用户契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/ISysUserService.java": ("用户业务接口", "定义用户契约。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysConfigServiceImpl.java": ("系统参数业务实现", "参数查询、缓存与更新。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysDeptServiceImpl.java": ("部门业务实现", "部门树、树选择和角色部门关系。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysDictDataServiceImpl.java": ("字典数据业务实现", "字典数据查询、缓存和维护。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysDictTypeServiceImpl.java": ("字典类型业务实现", "字典类型查询、缓存和维护。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysLogininforServiceImpl.java": ("登录日志业务实现", "登录日志记录与清理。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysMenuServiceImpl.java": ("菜单业务实现", "菜单树、权限点和路由构建。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysNoticeServiceImpl.java": ("通知公告业务实现", "通知公告查询与维护。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysOperLogServiceImpl.java": ("操作日志业务实现", "操作日志记录与清理。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysPostServiceImpl.java": ("岗位业务实现", "岗位查询、唯一性校验与删除限制。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysRoleServiceImpl.java": ("角色业务实现", "角色查询和授权数据构建。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysUserOnlineServiceImpl.java": ("在线用户业务实现", "根据 token/IP/用户名查询在线会话。"),
    "skzh-system/src/main/java/com/skzh/system/service/impl/SysUserServiceImpl.java": ("用户业务实现", "用户查询、注册、授权、唯一性校验和数据权限控制。"),
}


MODULE_INTRO = {
    ".github": "GitHub/工具链相关目录，当前主要是 java-upgrade 记录和脚本。",
    ".idea": "IntelliJ IDEA 项目和数据源配置，不属于业务代码。",
    ".vscode": "VS Code 工作区设置。",
    "skzh-admin": "Web 启动模块：Spring Boot 主应用、控制器、外部流程编排与资源文件。",
    "skzh-biz": "业务域模块：订单、卫星、收藏、专题图、记录、访问统计、AI 相关领域对象和服务。",
    "skzh-common": "公共基础模块：统一返回、工具类、常量、异常、注解、实体与 Redis/文件工具。",
    "skzh-framework": "框架核心模块：AOP、Security、动态数据源、拦截器、全局处理、服务支撑。",
    "skzh-generator": "代码生成模块：数据库元数据读取、Velocity 模板和生成控制器。",
    "skzh-quartz": "定时任务模块：Quartz 任务、任务控制器、调度与执行工具。",
    "skzh-system": "系统基础模块：用户、角色、菜单、部门、参数、字典、日志等通用系统能力。",
    "sql": "数据库初始化脚本与建表数据。",
}


EXT_DESC = {
    ".xml": ("配置/映射文件", "定义 SQL 映射、框架配置或模板相关 XML。"),
    ".yml": ("YAML 配置文件", "承载 SpringBoot 或子模块配置。"),
    ".yaml": ("YAML 配置文件", "承载 SpringBoot 或子模块配置。"),
    ".properties": ("属性配置文件", "保存资源或插件属性。"),
    ".txt": ("文本资源", "启动横幅或说明文本。"),
    ".md": ("Markdown 文档", "项目文档或说明。"),
    ".jar": ("第三方依赖包", "运行时引入的外部库。"),
    ".jpg": ("静态资源图片", "地图/示意类静态资源。"),
    ".terrain": ("地图地形资源", "地图地形数据文件。"),
    ".sql": ("数据库脚本", "建表、初始化和测试数据脚本。"),
    ".json": ("JSON 配置", "工具或流程配置记录。"),
    ".sh": ("Shell 脚本", "工具链脚本。"),
    ".ps1": ("PowerShell 脚本", "工具链脚本。"),
    ".gitignore": ("Git 忽略配置", "定义忽略规则。"),
}


def role_and_core(rel: str, path: Path):
    if rel in ROOT_FILES:
        return ROOT_FILES[rel]
    if rel in SPECIAL:
        return SPECIAL[rel]

    if path.name == "pom.xml":
        return ("模块构建配置", "声明模块依赖、打包插件和运行时库。")

    suffix = path.suffix.lower()
    if suffix in EXT_DESC:
        return EXT_DESC[suffix]

    parts = rel.split("/")
    if ".github" in parts:
        return ("仓库工具文件", "辅助升级/记录/脚本，不参与业务运行。")
    if ".idea" in parts:
        return ("IDE 配置文件", "用于 IntelliJ 数据源、编码和编译设置。")
    if ".vscode" in parts:
        return ("编辑器配置", "用于 VS Code 设置。")
    if "target" in parts:
        return ("构建产物", "Maven 编译输出，不属于源代码。")

    if path.suffix == ".java":
        name = path.stem
        if name.endswith("Controller"):
            return ("REST 控制器", "对外暴露 HTTP 接口。")
        if name.endswith("ServiceImpl"):
            return ("业务实现类", "封装具体业务处理逻辑。")
        if name.endswith("Service") or (name.startswith("I") and "Service" in name):
            return ("业务接口", "定义业务契约。")
        if name.endswith("Mapper"):
            return ("MyBatis Mapper 接口", "声明数据库访问方法。")
        if name.endswith("Config"):
            return ("配置类", "提供 Spring/框架配置。")
        if name.endswith("Util") or name.endswith("Utils"):
            return ("工具类", "提供通用辅助能力。")
        if name.endswith("Task"):
            return ("任务类", "给定时调度或后台流程调用。")
        if name.endswith("Vo") or name.endswith("VO"):
            return ("视图/入参对象", "用于接口入参或返回封装。")
        if name.endswith("Dto") or name.endswith("DTO"):
            return ("数据传输对象", "用于模块间/接口间传递轻量数据。")
        return ("实体/模型类", "承载数据库字段或业务模型。")

    return ("其他文件", "按文件名承担相应业务、配置或资源职责。")


def build_section(title: str, intro: str, files):
    out = [f"## {title}", "", intro, ""]
    for path in files:
        rel = path.relative_to(ROOT).as_posix()
        role, core = role_and_core(rel, path)
        out.append(f"- **{rel}**")
        out.append(f"  - 作用：{role}。")
        out.append(f"  - 核心逻辑：{core}")
    out.append("")
    return out


def main():
    lines = []
    lines += [
        "# skzh 后端项目文件说明书",
        "",
        "> 说明：本文档按目录逐文件说明作用与核心逻辑；`target/` 为构建产物，不展开逐个类文件。",
        "",
        "## 1. 项目总览",
        "",
        "- 技术栈：Spring Boot + MyBatis + Quartz + Redis + WebSocket + Swagger + MinIO/SeaweedFS + Python 外部流程。",
        "- 形态：Maven 多模块后端工程，`skzh-admin` 是启动入口，其余模块提供公共能力、系统基础、业务逻辑、定时任务和代码生成。",
        "- 关键业务：卫星影像管理、订单管理、专题图生成、AI/外部脚本任务、收藏与我的数据、系统管理。",
        "",
        "## 2. 模块依赖关系",
        "",
        "- `skzh-admin` 依赖 `framework` / `quartz` / `generator` / `biz` / `system` / `common`，负责对外暴露接口。",
        "- `skzh-biz` 依赖 `common`、`system`，承载业务域对象与业务服务。",
        "- `skzh-framework` 提供安全、AOP、动态数据源、异常处理、Web 支撑。",
        "- `skzh-system` 负责用户、角色、菜单、部门、字典、日志等通用系统能力。",
        "- `skzh-quartz` 负责定时任务调度。",
        "- `skzh-generator` 负责代码生成。",
        "- `skzh-common` 提供全局复用组件。",
        "",
        "## 3. 根目录文件",
        "",
    ]

    root_files = [ROOT / name for name in ["pom.xml", "README.md", "LICENSE", ".keep"] if (ROOT / name).exists()]
    lines += build_section("根目录", "项目根目录下的基础文件。", root_files)

    for module in [".github", ".idea", ".vscode", "skzh-admin", "skzh-biz", "skzh-common", "skzh-framework", "skzh-generator", "skzh-quartz", "skzh-system", "sql"]:
        base = ROOT / module
        if not base.exists():
            continue
        if module in {".github", ".idea", ".vscode", "sql"}:
            files = sorted([p for p in base.rglob("*") if p.is_file()], key=lambda p: p.relative_to(ROOT).as_posix())
        else:
            files = sorted([p for p in base.rglob("*") if p.is_file() and "target" not in p.parts], key=lambda p: p.relative_to(ROOT).as_posix())
        lines += build_section(module, MODULE_INTRO[module], files)

    OUT.write_text("\n".join(lines), encoding="utf-8")


if __name__ == "__main__":
    main()
