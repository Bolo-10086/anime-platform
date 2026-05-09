package com.mhj.anime.dto;

import java.math.BigDecimal;

public class RatingRequest {

    private BigDecimal rating;

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
