package com.mhj.anime.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserRatingVO {

    private Long ratingId;
    private Long animeId;
    private String title;
    private String coverImage;
    private BigDecimal rating;
    private BigDecimal animeScore;
    private LocalDateTime updateTime;

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Long getAnimeId() {
        return animeId;
    }

    public void setAnimeId(Long animeId) {
        this.animeId = animeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public BigDecimal getAnimeScore() {
        return animeScore;
    }

    public void setAnimeScore(BigDecimal animeScore) {
        this.animeScore = animeScore;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
