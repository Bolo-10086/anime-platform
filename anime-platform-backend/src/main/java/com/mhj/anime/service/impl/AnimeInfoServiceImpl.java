package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.AnimeInfo;
import com.mhj.anime.entity.AnimeTag;
import com.mhj.anime.mapper.AnimeInfoMapper;
import com.mhj.anime.service.AnimeInfoService;
import com.mhj.anime.service.AnimeTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AnimeInfoServiceImpl extends ServiceImpl<AnimeInfoMapper, AnimeInfo> implements AnimeInfoService {

    private final AnimeTagService animeTagService;

    public AnimeInfoServiceImpl(AnimeTagService animeTagService) {
        this.animeTagService = animeTagService;
    }

    @Override
    public List<AnimeInfo> listAnime(String keyword,
                                     Long categoryId,
                                     Long tagId,
                                     Integer releaseYear,
                                     String status,
                                     String region,
                                     String type) {
        LambdaQueryWrapper<AnimeInfo> wrapper = new LambdaQueryWrapper<AnimeInfo>()
                .orderByDesc(AnimeInfo::getScore)
                .orderByDesc(AnimeInfo::getReleaseYear)
                .orderByDesc(AnimeInfo::getViewCount)
                .orderByDesc(AnimeInfo::getId);

        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(query -> query
                    .like(AnimeInfo::getTitle, value)
                    .or()
                    .like(AnimeInfo::getOriginalTitle, value)
                    .or()
                    .like(AnimeInfo::getCategoryName, value)
                    .or()
                    .like(AnimeInfo::getTagNames, value)
                    .or()
                    .like(AnimeInfo::getDescription, value));
        }
        if (categoryId != null) {
            wrapper.eq(AnimeInfo::getCategoryId, categoryId);
        }
        if (tagId != null) {
            AnimeTag tag = animeTagService.getById(tagId);
            if (tag != null && StringUtils.hasText(tag.getName())) {
                wrapper.like(AnimeInfo::getTagNames, tag.getName());
            }
        }
        if (releaseYear != null) {
            wrapper.eq(AnimeInfo::getReleaseYear, releaseYear);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(AnimeInfo::getStatus, status.trim());
        }
        if (StringUtils.hasText(region)) {
            wrapper.eq(AnimeInfo::getRegion, region.trim());
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(AnimeInfo::getType, type.trim());
        }
        return list(wrapper);
    }

    @Override
    public AnimeInfo detail(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("动漫ID不能为空");
        }
        AnimeInfo animeInfo = getById(id);
        if (animeInfo == null) {
            throw new IllegalArgumentException("动漫不存在");
        }
        animeInfo.setViewCount(animeInfo.getViewCount() == null ? 1 : animeInfo.getViewCount() + 1);
        updateById(animeInfo);
        return animeInfo;
    }

    @Override
    public AnimeInfo createAnime(AnimeInfo animeInfo) {
        validateAnime(animeInfo);
        if (animeInfo.getViewCount() == null) {
            animeInfo.setViewCount(0);
        }
        save(animeInfo);
        return animeInfo;
    }

    @Override
    public AnimeInfo updateAnime(Long id, AnimeInfo animeInfo) {
        if (id == null) {
            throw new IllegalArgumentException("动漫ID不能为空");
        }
        validateAnime(animeInfo);
        if (getById(id) == null) {
            throw new IllegalArgumentException("动漫不存在");
        }
        animeInfo.setId(id);
        updateById(animeInfo);
        return getById(id);
    }

    @Override
    public void deleteAnime(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("动漫ID不能为空");
        }
        if (!removeById(id)) {
            throw new IllegalArgumentException("动漫不存在");
        }
    }

    private void validateAnime(AnimeInfo animeInfo) {
        if (animeInfo == null || !StringUtils.hasText(animeInfo.getTitle())) {
            throw new IllegalArgumentException("动漫名称不能为空");
        }
        if (!StringUtils.hasText(animeInfo.getCategoryName())) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (!StringUtils.hasText(animeInfo.getCoverImage())) {
            throw new IllegalArgumentException("封面图片不能为空");
        }
        if (animeInfo.getReleaseYear() != null && (animeInfo.getReleaseYear() < 1900 || animeInfo.getReleaseYear() > 2100)) {
            throw new IllegalArgumentException("年份范围不合理");
        }
        BigDecimal score = animeInfo.getScore();
        if (score != null && (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("评分必须在0到10之间");
        }
    }
}
