# 接口清单

## 认证接口

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| GET | `/api/auth/profile` | 获取当前登录用户信息 |

## 前台接口

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| GET | `/api/anime/list` | 动漫列表，支持关键词、分类、标签、年份、状态、地区、类型筛选 |
| GET | `/api/anime/detail/{id}` | 动漫详情 |
| GET | `/api/anime/{animeId}/comments` | 动漫评论列表 |
| GET | `/api/anime/{animeId}/interaction-summary` | 动漫互动统计 |
| GET | `/api/category/list` | 分类列表 |
| GET | `/api/tag/list` | 标签列表 |
| GET | `/api/news/list` | 资讯列表 |
| GET | `/api/news/detail/{id}` | 资讯详情 |

## 用户互动接口

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| GET | `/api/user/anime/{animeId}/state` | 当前用户对动漫的收藏和评分状态 |
| POST | `/api/user/anime/{animeId}/comments` | 发表评论 |
| DELETE | `/api/user/comments/{commentId}` | 删除本人评论 |
| POST | `/api/user/anime/{animeId}/favorite` | 收藏动漫 |
| DELETE | `/api/user/anime/{animeId}/favorite` | 取消收藏 |
| POST | `/api/user/anime/{animeId}/rating` | 评分 |
| GET | `/api/user/favorites` | 我的收藏 |
| GET | `/api/user/ratings` | 我的评分 |
| GET | `/api/user/comments` | 我的评论 |

## 后台接口

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| GET | `/api/admin/stats/overview` | 后台统计看板 |
| GET/POST/PUT/DELETE | `/api/admin/anime/**` | 动漫管理 |
| GET/POST/PUT/DELETE | `/api/admin/category/**` | 分类管理 |
| GET/POST/PUT/DELETE | `/api/admin/tag/**` | 标签管理 |
| GET/POST/PUT/DELETE | `/api/admin/news/**` | 资讯管理 |
| GET/PUT | `/api/admin/comment/**` | 评论管理 |
| GET/PUT | `/api/admin/user/**` | 用户管理 |

后台接口需要管理员角色，普通用户不能访问。

## 统计接口返回内容

`/api/admin/stats/overview` 返回后台首页需要的数据：

- 基础数量：动漫、用户、评论、收藏、评分。
- 分类占比：各主分类动漫数量。
- 年份分布：不同年份动漫数量。
- 评分分布：按评分区间统计作品数量。
- 浏览排行：动漫浏览量排行。
- 收藏排行：动漫收藏量排行。
- 资讯排行：资讯浏览量排行。
