# PROJECT_CONTEXT

## 基本信息

毕设题目：基于 SpringBoot 的在线动漫信息平台系统的设计与实现

正式项目路径：

```text
C:\Users\asus\Desktop\anime-platform
```

旧项目 `wangzhan` 已归档到：

```text
C:\Users\asus\Desktop\anime-platform-archive\wangzhan-archive-20260509
```

## 技术栈

- 后端：SpringBoot + MyBatis Plus + MySQL + RESTful API + JWT + BCrypt
- 前端：Vue 2 + Element UI + Vue Router + ECharts + Axios
- 构建：后端 Maven，前端 npm

## 本机环境

- JDK 21：`C:\Program Files\Microsoft\jdk-21.0.7.6-hotspot`
- Maven 3.6.1
- Node.js 20.20.2
- npm 10.8.2
- MySQL 8.4.8，服务名 `MySQL84`，端口 `3306`

## 数据库

```text
数据库名：anime_platform
用户名：anime_user
密码：Anime_2026_Mhj@520
```

SpringBoot 数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/anime_platform?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: anime_user
    password: Anime_2026_Mhj@520
```

## 当前进度

- 已完成登录注册、JWT 鉴权、角色权限控制。
- 已完成前台首页、动漫库、动漫详情、资讯中心、个人中心。
- 已扩展近十年真实热门动漫数据约 30 部，并配套真实封面。
- 已完成主分类 + 多标签体系，支持组合筛选。
- 已完成评论、收藏、评分和个人互动记录。
- 已完成后台统计、动漫、分类、标签、资讯、评论、用户管理。

## 默认账号

```text
admin / admin123
demo / demo123
```
