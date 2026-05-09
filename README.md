# AnimeHub 在线动漫信息平台

毕设题目：基于 SpringBoot 的在线动漫信息平台系统的设计与实现

## 正式项目路径

```text
C:\Users\asus\Desktop\anime-platform
```

旧项目目录 `wangzhan` 已归档到：

```text
C:\Users\asus\Desktop\anime-platform-archive\wangzhan-archive-20260509
```

后续开发、运行、截图、打包提交都以 `anime-platform` 为准。

## 项目结构

```text
anime-platform/
├─ anime-platform-backend      SpringBoot 后端
├─ anime-platform-frontend     Vue 前端
├─ docs                        接口、测试、模块说明
├─ PROJECT_CONTEXT.md          项目上下文
└─ README.md                   项目说明
```

## 技术栈

- 后端：SpringBoot + MyBatis Plus + MySQL + RESTful API + JWT + BCrypt
- 前端：Vue 2 + Element UI + Vue Router + ECharts + Axios
- 构建：Maven + npm
- 数据库：MySQL 8

## 已实现功能

- 登录注册、JWT 认证、管理员/普通用户角色区分
- 首页、动漫库、动漫详情、资讯中心、个人中心
- 近十年真实热门动漫数据约 30 部，包含真实封面、分类、标签、评分、简介、正版入口和预告入口
- 主分类 + 多标签筛选，支持关键词、分类、标签、年份、状态、地区、类型组合查询
- 评论、收藏、评分、个人互动记录
- 后台统计看板、动漫管理、分类管理、标签管理、资讯管理、评论管理、用户管理

## 默认账号

```text
管理员：admin / admin123
普通用户：demo / demo123
```

## 后端启动

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-backend
$env:JAVA_HOME='C:\Program Files\Microsoft\jdk-21.0.7.6-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run
```

后端地址：

```text
http://localhost:8080
```

## 前端启动

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-frontend
npm install
npm run dev
```

前端地址：

```text
http://localhost:5173
```

## 常用页面

```text
/home             首页
/anime            动漫库
/anime/:id        动漫详情
/news             资讯中心
/news/:id         资讯详情
/profile          个人中心
/admin/stats      后台统计看板
/admin/anime      后台动漫管理
/admin/category   后台分类管理
/admin/tag        后台标签管理
/admin/news       后台资讯管理
/admin/comment    后台评论管理
/admin/user       后台用户管理
```

## 验证命令

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-backend
$env:JAVA_HOME='C:\Program Files\Microsoft\jdk-21.0.7.6-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn package
```

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-frontend
npm run build
```
