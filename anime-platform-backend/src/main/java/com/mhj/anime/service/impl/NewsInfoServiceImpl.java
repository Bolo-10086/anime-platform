package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.NewsInfo;
import com.mhj.anime.mapper.NewsInfoMapper;
import com.mhj.anime.service.NewsInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsInfoServiceImpl extends ServiceImpl<NewsInfoMapper, NewsInfo> implements NewsInfoService {

    @Override
    public List<NewsInfo> listPublished(String keyword) {
        return list(buildWrapper(keyword, 1));
    }

    @Override
    public List<NewsInfo> listAll(String keyword, Integer status) {
        return list(buildWrapper(keyword, status));
    }

    @Override
    public NewsInfo detail(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("资讯ID不能为空");
        }
        NewsInfo newsInfo = getById(id);
        if (newsInfo == null || newsInfo.getStatus() == null || newsInfo.getStatus() != 1) {
            throw new IllegalArgumentException("资讯不存在");
        }
        newsInfo.setViewCount(newsInfo.getViewCount() == null ? 1 : newsInfo.getViewCount() + 1);
        updateById(newsInfo);
        return newsInfo;
    }

    @Override
    public NewsInfo createNews(NewsInfo newsInfo, Long authorId) {
        validate(newsInfo);
        newsInfo.setAuthorId(authorId);
        if (newsInfo.getStatus() == null) {
            newsInfo.setStatus(1);
        }
        if (newsInfo.getViewCount() == null) {
            newsInfo.setViewCount(0);
        }
        if (newsInfo.getPublishTime() == null) {
            newsInfo.setPublishTime(LocalDateTime.now());
        }
        save(newsInfo);
        return newsInfo;
    }

    @Override
    public NewsInfo updateNews(Long id, NewsInfo newsInfo) {
        if (id == null || getById(id) == null) {
            throw new IllegalArgumentException("资讯不存在");
        }
        validate(newsInfo);
        newsInfo.setId(id);
        updateById(newsInfo);
        return getById(id);
    }

    @Override
    public void deleteNews(Long id) {
        if (id == null || !removeById(id)) {
            throw new IllegalArgumentException("资讯不存在");
        }
    }

    private LambdaQueryWrapper<NewsInfo> buildWrapper(String keyword, Integer status) {
        LambdaQueryWrapper<NewsInfo> wrapper = new LambdaQueryWrapper<NewsInfo>()
                .orderByDesc(NewsInfo::getPublishTime)
                .orderByDesc(NewsInfo::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(query -> query
                    .like(NewsInfo::getTitle, value)
                    .or()
                    .like(NewsInfo::getSummary, value)
                    .or()
                    .like(NewsInfo::getContent, value));
        }
        if (status != null) {
            wrapper.eq(NewsInfo::getStatus, status);
        }
        return wrapper;
    }

    private void validate(NewsInfo newsInfo) {
        if (newsInfo == null || !StringUtils.hasText(newsInfo.getTitle())) {
            throw new IllegalArgumentException("资讯标题不能为空");
        }
        if (!StringUtils.hasText(newsInfo.getSummary())) {
            throw new IllegalArgumentException("资讯摘要不能为空");
        }
        if (!StringUtils.hasText(newsInfo.getContent())) {
            throw new IllegalArgumentException("资讯内容不能为空");
        }
    }
}
