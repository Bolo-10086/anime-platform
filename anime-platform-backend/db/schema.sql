CREATE DATABASE IF NOT EXISTS anime_platform
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'anime_user'@'localhost' IDENTIFIED BY 'Anime_2026_Mhj@520';
GRANT ALL PRIVILEGES ON anime_platform.* TO 'anime_user'@'localhost';
FLUSH PRIVILEGES;

USE anime_platform;

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
  nickname VARCHAR(50) NULL COMMENT '昵称',
  avatar VARCHAR(500) NULL COMMENT '头像',
  email VARCHAR(100) NULL COMMENT '邮箱',
  phone VARCHAR(30) NULL COMMENT '手机号',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0禁用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS sys_user_role (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS anime_category (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  name VARCHAR(50) NOT NULL COMMENT '分类名称',
  description VARCHAR(255) NULL COMMENT '分类描述',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫主分类表';

CREATE TABLE IF NOT EXISTS anime_tag (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  name VARCHAR(50) NOT NULL COMMENT '标签名称',
  description VARCHAR(255) NULL COMMENT '标签描述',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_tag_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫标签表';

CREATE TABLE IF NOT EXISTS anime_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '动漫ID',
  title VARCHAR(100) NOT NULL COMMENT '动漫名称',
  original_title VARCHAR(150) NULL COMMENT '原名',
  cover_image VARCHAR(500) NULL COMMENT '封面图',
  category_id BIGINT NULL COMMENT '分类ID',
  category_name VARCHAR(50) NULL COMMENT '分类名称',
  region VARCHAR(60) NULL COMMENT '地区',
  type VARCHAR(30) NULL COMMENT '作品类型',
  tag_names VARCHAR(300) NULL COMMENT '标签名称，逗号分隔',
  release_year INT NULL COMMENT '年份',
  status VARCHAR(30) NULL COMMENT '状态',
  episodes INT NULL COMMENT '集数',
  score DECIMAL(3,1) NULL COMMENT '评分',
  description TEXT NULL COMMENT '简介',
  source_name VARCHAR(100) NULL COMMENT '正版来源平台',
  watch_url VARCHAR(500) NULL COMMENT '正版观看入口',
  trailer_url VARCHAR(500) NULL COMMENT '官方预告入口',
  view_count INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_title (title),
  KEY idx_category_id (category_id),
  KEY idx_release_year (release_year),
  KEY idx_status (status),
  KEY idx_region (region),
  KEY idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫信息表';

CREATE TABLE IF NOT EXISTS anime_comment (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  anime_id BIGINT NOT NULL COMMENT '动漫ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  content VARCHAR(1000) NOT NULL COMMENT '评论内容',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1显示，0隐藏',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_comment_anime (anime_id),
  KEY idx_comment_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫评论表';

CREATE TABLE IF NOT EXISTS anime_favorite (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  anime_id BIGINT NOT NULL COMMENT '动漫ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_anime_favorite (user_id, anime_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫收藏表';

CREATE TABLE IF NOT EXISTS anime_rating (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  anime_id BIGINT NOT NULL COMMENT '动漫ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  rating DECIMAL(3,1) NOT NULL COMMENT '评分',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_anime_rating (user_id, anime_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动漫评分表';

CREATE TABLE IF NOT EXISTS news_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '资讯ID',
  title VARCHAR(150) NOT NULL COMMENT '标题',
  cover_image VARCHAR(500) NULL COMMENT '封面图',
  summary VARCHAR(500) NULL COMMENT '摘要',
  content TEXT NULL COMMENT '正文内容',
  author_id BIGINT NULL COMMENT '发布人ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1发布，0草稿',
  view_count INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  publish_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_news_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资讯表';

-- 角色、默认账号、分类、标签和约30部动漫数据由 SpringBoot 启动时的 DataInitializer 自动补齐。
