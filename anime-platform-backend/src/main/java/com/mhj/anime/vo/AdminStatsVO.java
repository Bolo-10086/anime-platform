package com.mhj.anime.vo;

import java.util.List;

public class AdminStatsVO {

    private Integer animeCount;
    private Integer userCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Integer ratingCount;
    private List<NameValueVO> categoryStats;
    private List<NameValueVO> topViewedAnime;

    public Integer getAnimeCount() {
        return animeCount;
    }

    public void setAnimeCount(Integer animeCount) {
        this.animeCount = animeCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<NameValueVO> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(List<NameValueVO> categoryStats) {
        this.categoryStats = categoryStats;
    }

    public List<NameValueVO> getTopViewedAnime() {
        return topViewedAnime;
    }

    public void setTopViewedAnime(List<NameValueVO> topViewedAnime) {
        this.topViewedAnime = topViewedAnime;
    }
}
