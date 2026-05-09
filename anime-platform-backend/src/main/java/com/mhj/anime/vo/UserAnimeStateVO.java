package com.mhj.anime.vo;

import java.math.BigDecimal;

public class UserAnimeStateVO {

    private Boolean favorite;
    private BigDecimal rating;

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
