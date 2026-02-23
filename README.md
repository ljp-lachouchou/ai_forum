# AIForum Android

## 简介

AIForum 是一个使用现代 Android 技术栈构建的论坛应用，支持帖子发布、评论、搜索、匿名树洞等功能。

本项目致力于为用户提供一个简洁、高效的社区交流平台。通过采用最新的 Android 开发技术栈和 Clean Architecture 架构设计，AIForum 具备良好的可维护性和扩展性。应用支持离线阅读、实时推送通知、个性化心情主题等特色功能，为用户带来流畅的使用体验。

## 技术栈

### 核心技术

- **Kotlin 2.3.0**
  - Google 官方推荐的 Android 开发语言
  - 提供空安全、扩展函数、协程等现代语言特性
  - 与 Java 100% 兼容，代码简洁且高效

- **Jetpack Compose**
  - Google 推出的现代声明式 UI 工具包
  - 使用 Kotlin 编写 UI，大幅简化视图层代码
  - 支持预览、动画和自定义组件
  - 减少样板代码，提高开发效率

- **Material 3**
  - Google 最新 Material Design 设计规范
  - 提供动态配色、自适应布局等现代设计特性
  - 内置丰富的组件库和主题系统
  - 支持大屏设备和折叠屏适配

- **Hilt**
  - Google 官方的 Android 依赖注入库
  - 基于 Dagger 构建，提供编译时依赖注入
  - 简化模块间依赖管理
  - 支持 ViewModel、WorkManager 等 Android 组件的自动注入

### 架构与模式

- **Clean Architecture**
  - 分层架构设计，将代码按职责划分为领域层、数据层和展示层
  - 核心业务逻辑与外部依赖解耦，提高代码可测试性
  - 单向数据流，确保状态管理的一致性
  - 支持依赖反转，便于单元测试和功能扩展

- **MVVM (Model-View-ViewModel)**
  - 分离 UI 逻辑与业务逻辑
  - ViewModel 管理 UI 状态，处理数据转换
  - View 负责渲染和用户交互
  - 通过 LiveData 或 StateFlow 实现数据观察

- **模块化设计**
  - 按功能划分模块，每个模块独立编译和测试
  - 核心模块提供通用能力，功能模块按业务隔离
  - 支持按需加载，减少应用包体积
  - 便于多人协作开发和功能复用

### 数据与持久化

- **Room**
  - Google 官方的 SQLite 数据库封装
  - 提供编译时 SQL 检查，避免运行时错误
  - 支持 LiveData、Flow 和 RxJava 集成
  - 提供数据库迁移方案，简化版本升级

- **DataStore**
  - Google 推荐的轻量级数据存储方案
  - 支持 Key-Value 存储（Preferences）和类型安全存储（Proto）
  - 事务性操作，保证数据一致性
  - 替代传统的 SharedPreferences

- **Paging 3**
  - 分页数据加载解决方案
  - 支持无限滚动和列表分页
  - 内置缓存机制，提升用户体验
  - 与 Compose、RecyclerView 无缝集成

- **Ktor**
  - 由 JetBrains 开发的轻量级 HTTP 客户端
  - 支持 Kotlin 协程，异步请求简洁高效
  - 插件化设计，支持日志、序列化、缓存等扩展
  - 支持 WebSocket 和双向通信

### 后端服务

- **Supabase**
  - 开源的 Firebase 替代方案
  - 提供 PostgreSQL 数据库、身份认证、实时订阅等服务
  - 支持行级安全策略，数据安全可控
  - 提供 RESTful API 和 GraphQL 接口

- **Jiguang (极光推送)**
  - 国内主流的移动推送服务
  - 支持高并发推送，送达率高
  - 提供自定义消息、通知栏样式等丰富功能
  - 支持用户标签和精准推送

### 开发与测试

- **JUnit**
  - Java/Kotlin 单元测试框架
  - 支持 Parameterized Tests、Repeated Tests 等高级特性
  - 与 Mockito、Robolectric 等工具无缝集成

- **Robolectric**
  - Android 单元测试框架
  - 在 JVM 上运行测试，无需真实设备或模拟器
  - 支持 Android SDK API 调用
  - 测试运行速度快，适合持续集成

- **Roborazzi**
  - Compose UI 截图测试工具
  - 捕获 Composable 组件的渲染结果
  - 支持视觉回归测试，确保 UI 样式正确
  - 易于集成到 CI/CD 流程中

## 项目结构

```
AIForum/
├── app/                      # 主应用模块
├── build-logic/               # Gradle 约定插件
├── sync/                      # 同步模块
└── core/                      # 共享库
    ├── analytics/            # 分析追踪
    ├── common/               # 通用工具
    ├── database/             # Room 数据库
    ├── datastore/            # DataStore 设置
    ├── designsystem/         # 设计系统
    ├── domain/               # 领域层用例
    ├── model/                # 数据模型
    ├── network/              # 网络层
    ├── navigation/           # 导航设置
    └── ui/                   # 通用 UI 组件

功能模块:
├── Home/                     # 首页信息流
├── Login/                    # 登录认证
├── Me/                       # 个人中心
├── Profile/                  # 用户资料
├── Post/                     # 帖子详情
├── PostCreate/               # 创建帖子
├── Search/                   # 搜索功能
├── Treehole/                 # 匿名树洞
└── TreeholeCreate/           # 创建树洞
```

## 主要功能

- 📱 **首页信息流** - 浏览最新帖子
- 💬 **帖子互动** - 发帖、评论、点赞、收藏
- 🔍 **搜索功能** - 搜索帖子或用户
- 🌳 **匿名树洞** - 发布匿名内容
- 👤 **个人中心** - 用户资料和设置
- 🎨 **心情主题** - 根据心情动态切换主题
- 📨 **推送通知** - 极光推送支持
- 📝 **Markdown 支持** - 富文本内容

## 开始使用

### 环境要求
- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK API 34+

### 构建项目

```bash
# 克隆项目
git clone https://github.com/your-username/AIForum.git
cd AIForum

# 使用 Android Studio 打开项目
# 或使用命令行构建
./gradlew assembleDebug
```

### 运行应用

1. 使用 Android Studio 打开项目
2. 连接 Android 设备或启动模拟器
3. 点击 Run 按钮或运行命令：
   ```bash
   ./gradlew installDebug
   ```

## 依赖配置

项目使用 Gradle Version Catalogs 管理依赖，主要版本：
- Kotlin: 2.3.0
- Compose BOM: 2025.09.01
- Room: 2.8.3
- Hilt: 2.57.2
- Ktor: 3.3.3

## 架构设计

### 整体架构

项目采用 **Clean Architecture** 设计思想，将应用划分为三层架构，各层之间通过接口通信，实现松耦合和高内聚：

```
┌─────────────────────────────────────────────────────────┐
│                    展示层 (Presentation)                  │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  Jetpack Compose UI  │  ViewModels  │  Navigation   │ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────┬──────────────────────────────────┘
                       │ ↑
                       │ │ 依赖反转
                       ↓ │
┌──────────────────────┴──────────────────────────────────┐
│                    领域层 (Domain)                        │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  Use Cases (用例)  │  Entities (实体)  │  Repositories│ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────┬──────────────────────────────────┘
                       │ ↑
                       │ │ 依赖反转
                       ↓ │
┌──────────────────────┴──────────────────────────────────┐
│                    数据层 (Data)                          │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  Repository Impl  │  Room DB  │  Ktor Network      │ │
│  └─────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

### 层级职责

#### 1. 展示层 (Presentation Layer)

**职责**：负责 UI 渲染和用户交互，展示应用状态

**组成**：
- **Jetpack Compose UI** - 使用声明式方式构建用户界面
- **ViewModels** - 管理 UI 状态，处理用户交互逻辑
- **Navigation** - 处理页面导航和路由

**特点**：
- 不直接访问数据源，通过 ViewModel 与领域层交互
- UI 状态由 ViewModel 持有，通过 StateFlow 或 LiveData 暴露
- 响应用户操作，调用对应的 Use Case

#### 2. 领域层 (Domain Layer)

**职责**：封装核心业务逻辑，不依赖任何外部框架

**组成**：
- **Use Cases (用例)** - 表示单一业务操作，如"获取帖子列表"、"发布评论"等
- **Entities (实体)** - 业务领域对象，包含核心业务规则
- **Repository Interfaces** - 数据访问接口，由领域层定义，数据层实现

**特点**：
- 纯 Kotlin 代码，不依赖 Android SDK 或第三方库
- 业务逻辑集中管理，易于单元测试
- 通过接口与数据层解耦，可灵活替换数据源

#### 3. 数据层 (Data Layer)

**职责**：处理数据获取、缓存和持久化，为领域层提供数据

**组成**：
- **Repository Implementations** - 实现 Repository 接口，协调不同数据源
- **Local Data Sources** - Room 数据库、DataStore 等本地存储
- **Remote Data Sources** - Ktor 网络请求、Supabase API 调用

**特点**：
- 实现领域层定义的 Repository 接口
- 处理数据转换和映射
- 实现缓存策略，优化性能

### 数据流向

```
用户操作 → UI → ViewModel → Use Case → Repository → 数据源
                                                    ↓
                                              网络请求 / 本地查询
                                                    ↓
                                              数据转换 / 缓存
                                                    ↓
Use Case ← Repository ← 业务逻辑处理
         ↓
    返回结果
         ↓
   ViewModel 更新状态
         ↓
     UI 重新渲染
```

### 关键设计原则

1. **依赖反转 (Dependency Inversion)**
   - 高层模块不依赖低层模块，都依赖抽象
   - 领域层定义接口，数据层实现接口

2. **单一职责 (Single Responsibility)**
   - 每个类/模块只负责一个功能
   - Use Case 只做一件事，Repository 只负责数据

3. **开闭原则 (Open/Closed)**
   - 对扩展开放，对修改关闭
   - 通过接口实现多态，避免修改现有代码

4. **单一数据源 (Single Source of Truth)**
   - Repository 作为数据的唯一入口
   - 状态在 ViewModel 中集中管理

### 模块化设计

项目按功能和职责划分为多个模块：

| 模块类型 | 说明 | 依赖关系 |
|---------|------|---------|
| Core 模块 | 提供通用能力，供所有模块使用 | 不依赖业务模块 |
| Feature 模块 | 按功能划分的业务模块 | 依赖 Core 和其他 Feature API |
| API/Impl | 功能模块分为 API 接口和 Impl 实现 | Impl 依赖 API，其他模块依赖 API |

这种设计支持：
- 按需加载功能模块，减少应用启动时间
- 独立开发和测试各个功能
- 灵活组合和替换功能模块

## 测试

```bash
# 运行单元测试
./gradlew test

# 运行 UI 测试
./gradlew connectedAndroidTest

# 运行 Roborazzi 截图测试
./gradlew verifyRoborazziDebug
```

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request


## 联系方式

如有问题或建议，欢迎提交 Issue 或 Pull Request。
