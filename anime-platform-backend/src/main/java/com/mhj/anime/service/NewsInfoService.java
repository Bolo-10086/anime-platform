package com.mhj.anime.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mhj.anime.entity.NewsInfo;

import java.util.List;

public interface NewsInfoService extends IService<NewsInfo> {

    List<NewsInfo> listPublished(String keyword);

    List<NewsInfo> listAll(String keyword, Integer status);

    NewsInfo detail(Long id);

    NewsInfo createNews(NewsInfo newsInfo, Long authorId);

    NewsInfo updateNews(Long id, NewsInfo newsInfo);

    void deleteNews(Long id);
}
