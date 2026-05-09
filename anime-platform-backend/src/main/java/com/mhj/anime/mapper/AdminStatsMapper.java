package com.mhj.anime.mapper;

import com.mhj.anime.vo.NameValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminStatsMapper {

    @Select("SELECT COUNT(*) FROM anime_info")
    Integer countAnime();

    @Select("SELECT COUNT(*) FROM sys_user")
    Integer countUser();

    @Select("SELECT COUNT(*) FROM anime_comment")
    Integer countComment();

    @Select("SELECT COUNT(*) FROM anime_favorite")
    Integer countFavorite();

    @Select("SELECT COUNT(*) FROM anime_rating")
    Integer countRating();

    @Select("SELECT COALESCE(category_name, '未分类') AS name, COUNT(*) AS value FROM anime_info GROUP BY category_name ORDER BY value DESC")
    List<NameValueVO> selectCategoryStats();

    @Select("SELECT title AS name, view_count AS value FROM anime_info ORDER BY view_count DESC, id DESC LIMIT 8")
    List<NameValueVO> selectTopViewedAnime();
}
