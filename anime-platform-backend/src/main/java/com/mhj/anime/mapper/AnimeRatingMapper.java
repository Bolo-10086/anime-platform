package com.mhj.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mhj.anime.entity.AnimeRating;
import com.mhj.anime.vo.UserRatingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AnimeRatingMapper extends BaseMapper<AnimeRating> {

    @Select("SELECT ROUND(AVG(rating), 1) FROM anime_rating WHERE anime_id = #{animeId}")
    BigDecimal selectAverageRating(@Param("animeId") Long animeId);

    @Select("SELECT r.id AS rating_id, a.id AS anime_id, a.title, a.cover_image, " +
            "r.rating, a.score AS anime_score, r.update_time " +
            "FROM anime_rating r INNER JOIN anime_info a ON a.id = r.anime_id " +
            "WHERE r.user_id = #{userId} " +
            "ORDER BY r.update_time DESC, r.id DESC")
    List<UserRatingVO> selectUserRatings(@Param("userId") Long userId);
}
