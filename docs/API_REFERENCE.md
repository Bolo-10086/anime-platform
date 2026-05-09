# 接口说明

## 认证模块

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | 用户注册 | 公开 |
| POST | `/api/auth/login` | 用户登录 | 公开 |
| GET | `/api/auth/profile` | 当前用户资料 | 登录 |

登录后请求头：

```text
Authorization: Bearer <token>
```

## 动漫与分类模块

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| GET | `/api/anime/list` | 动漫列表，支持关键词、分类、标签、年份、状态、地区、类型筛选 | 公开 |
| GET | `/api/anime/detail/{id}` | 动漫详情 | 公开 |
| GET | `/api/category/list` | 主分类列表 | 公开 |
| GET | `/api/tag/list` | 标签列表 | 公开 |

`/api/anime/list` 可选参数：

```text
keyword, categoryId, tagId, releaseYear, status, region, type
```

## 用户互动模块

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| GET | `/api/anime/{id}/comments` | 动漫评论列表 | 公开 |
| GET | `/api/user/anime/{id}/state` | 当前用户收藏评分状态 | 登录 |
| POST | `/api/user/anime/{id}/favorite` | 收藏动漫 | 登录 |
| DELETE | `/api/user/anime/{id}/favorite` | 取消收藏 | 登录 |
| POST | `/api/user/anime/{id}/rating` | 提交评分 | 登录 |
| POST | `/api/user/anime/{id}/comments` | 发表评论 | 登录 |
| GET | `/api/user/favorites` | 我的收藏 | 登录 |
| GET | `/api/user/ratings` | 我的评分 | 登录 |
| GET | `/api/user/comments` | 我的评论 | 登录 |

## 资讯模块

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| GET | `/api/news/list` | 资讯列表 | 公开 |
| GET | `/api/news/detail/{id}` | 资讯详情 | 公开 |

## 后台管理模块

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| GET | `/api/admin/stats/overview` | 后台统计看板 | 管理员 |
| GET | `/api/admin/anime/list` | 后台动漫列表 | 管理员 |
| POST | `/api/admin/anime` | 新增动漫 | 管理员 |
| PUT | `/api/admin/anime/{id}` | 编辑动漫 | 管理员 |
| DELETE | `/api/admin/anime/{id}` | 删除动漫 | 管理员 |
| GET | `/api/admin/category/list` | 后台分类列表 | 管理员 |
| POST | `/api/admin/category` | 新增分类 | 管理员 |
| PUT | `/api/admin/category/{id}` | 编辑分类 | 管理员 |
| DELETE | `/api/admin/category/{id}` | 删除分类 | 管理员 |
| GET | `/api/admin/tag/list` | 后台标签列表 | 管理员 |
| POST | `/api/admin/tag` | 新增标签 | 管理员 |
| PUT | `/api/admin/tag/{id}` | 编辑标签 | 管理员 |
| DELETE | `/api/admin/tag/{id}` | 删除标签 | 管理员 |
| GET | `/api/admin/news/list` | 后台资讯列表 | 管理员 |
| POST | `/api/admin/news` | 新增资讯 | 管理员 |
| PUT | `/api/admin/news/{id}` | 编辑资讯 | 管理员 |
| DELETE | `/api/admin/news/{id}` | 删除资讯 | 管理员 |
| GET | `/api/admin/comment/list` | 后台评论列表 | 管理员 |
| PUT | `/api/admin/comment/{id}/status` | 显示/隐藏评论 | 管理员 |
| GET | `/api/admin/user/list` | 后台用户列表 | 管理员 |
| PUT | `/api/admin/user/{id}/status` | 启用/禁用用户 | 管理员 |
