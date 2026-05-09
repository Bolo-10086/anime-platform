# 论文截图清单

建议按照下面顺序截图，便于论文中展示系统功能。

## 前台功能截图

1. 首页：`/home`
2. 登录页：`/login`
3. 注册页：`/register`
4. 动漫库：`/anime`
5. 动漫库分类、标签、年份组合筛选效果
6. 动漫详情页：`/anime/1`
7. 动漫详情页中的收藏、评分、评论区域
8. 资讯中心：`/news`
9. 资讯详情页：`/news/1`
10. 个人中心：`/profile`

## 后台功能截图

1. 后台统计看板：`/admin/stats`
2. 后台动漫管理：`/admin/anime`
3. 新增或编辑动漫弹窗
4. 分类管理：`/admin/category`
5. 标签管理：`/admin/tag`
6. 资讯管理：`/admin/news`
7. 评论管理：`/admin/comment`
8. 用户管理：`/admin/user`

## 接口测试截图

可以使用 Apifox 截图：

1. 登录接口：`POST /api/auth/login`
2. 动漫列表接口：`GET /api/anime/list`
3. 分类列表接口：`GET /api/category/list`
4. 标签列表接口：`GET /api/tag/list`
5. 动漫详情接口：`GET /api/anime/detail/1`
6. 发表评论接口：`POST /api/user/anime/1/comments`
7. 后台统计接口：`GET /api/admin/stats/overview`
8. 后台用户列表接口：`GET /api/admin/user/list`

## 数据库截图

可以使用 DBeaver 截图：

1. `sys_user`
2. `sys_role`
3. `anime_info`
4. `anime_category`
5. `anime_tag`
6. `anime_comment`
7. `anime_favorite`
8. `anime_rating`
9. `news_info`
