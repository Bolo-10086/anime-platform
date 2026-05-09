# AnimeHub 在线动漫信息平台

毕设题目：基于 SpringBoot 的在线动漫信息平台系统的设计与实现

## 项目路径

```text
C:\Users\asus\Desktop\anime-platform
```

后续开发、运行、截图、打包和提交均以该目录为准。

## 项目结构

```text
anime-platform/
├─ anime-platform-backend      SpringBoot 后端
├─ anime-platform-frontend     Vue 前端
├─ docs                        接口、测试、模块和截图材料
├─ PROJECT_CONTEXT.md          项目上下文
└─ README.md                   项目说明
```

## 技术栈

- 后端：SpringBoot、MyBatis Plus、MySQL、RESTful API、JWT、BCrypt
- 前端：Vue 2、Element UI、Vue Router、ECharts、Axios
- 构建：Maven、npm
- 数据库：MySQL 8

## 已实现功能

- 用户注册、登录、JWT 鉴权、管理员和普通用户角色区分
- 首页、动漫库、动漫详情、资讯中心、资讯详情、个人中心
- 近十年真实热门动漫数据，包含封面、横幅图、分类、标签、评分、简介、正版入口和官方预告入口
- 主分类和多标签筛选，支持关键词、分类、标签、年份、状态、地区、类型组合查询
- 评论、收藏、评分和个人互动记录
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
/admin/anime      动漫管理
/admin/category   分类管理
/admin/tag        标签管理
/admin/news       资讯管理
/admin/comment    评论管理
/admin/user       用户管理
```

## 验证命令

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-backend
mvn package
```

```powershell
cd C:\Users\asus\Desktop\anime-platform\anime-platform-frontend
npm run build
```
