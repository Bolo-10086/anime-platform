package com.mhj.anime.service.impl;

import com.mhj.anime.mapper.AdminStatsMapper;
import com.mhj.anime.service.AdminStatsService;
import com.mhj.anime.vo.AdminStatsVO;
import org.springframework.stereotype.Service;

@Service
public class AdminStatsServiceImpl implements AdminStatsService {

    private final AdminStatsMapper adminStatsMapper;

    public AdminStatsServiceImpl(AdminStatsMapper adminStatsMapper) {
        this.adminStatsMapper = adminStatsMapper;
    }

    @Override
    public AdminStatsVO overview() {
        AdminStatsVO stats = new AdminStatsVO();
        stats.setAnimeCount(adminStatsMapper.countAnime());
        stats.setUserCount(adminStatsMapper.countUser());
        stats.setCommentCount(adminStatsMapper.countComment());
        stats.setFavoriteCount(adminStatsMapper.countFavorite());
        stats.setRatingCount(adminStatsMapper.countRating());
        stats.setCategoryStats(adminStatsMapper.selectCategoryStats());
        stats.setYearStats(adminStatsMapper.selectYearStats());
        stats.setScoreStats(adminStatsMapper.selectScoreStats());
        stats.setTopViewedAnime(adminStatsMapper.selectTopViewedAnime());
        stats.setTopFavoriteAnime(adminStatsMapper.selectTopFavoriteAnime());
        stats.setTopViewedNews(adminStatsMapper.selectTopViewedNews());
        return stats;
    }
}
