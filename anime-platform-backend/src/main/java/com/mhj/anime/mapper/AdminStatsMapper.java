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

    @Select("SELECT CAST(release_year AS CHAR) AS name, COUNT(*) AS value FROM anime_info WHERE release_year IS NOT NULL GROUP BY release_year ORDER BY release_year")
    List<NameValueVO> selectYearStats();

    @Select("SELECT score_range AS name, COUNT(*) AS value FROM (" +
            "SELECT CASE " +
            "WHEN score >= 9 THEN '9.0以上' " +
            "WHEN score >= 8.5 THEN '8.5-8.9' " +
            "WHEN score >= 8 THEN '8.0-8.4' " +
            "ELSE '8.0以下' END AS score_range, " +
            "CASE WHEN score >= 9 THEN 1 WHEN score >= 8.5 THEN 2 WHEN score >= 8 THEN 3 ELSE 4 END AS sort_order " +
            "FROM anime_info WHERE score IS NOT NULL) t GROUP BY score_range, sort_order ORDER BY sort_order")
    List<NameValueVO> selectScoreStats();

    @Select("SELECT title AS name, view_count AS value FROM anime_info ORDER BY view_count DESC, id DESC LIMIT 8")
    List<NameValueVO> selectTopViewedAnime();

    @Select("SELECT a.title AS name, COUNT(f.id) AS value FROM anime_info a " +
            "LEFT JOIN anime_favorite f ON f.anime_id = a.id " +
            "GROUP BY a.id, a.title ORDER BY value DESC, a.view_count DESC, a.id DESC LIMIT 8")
    List<NameValueVO> selectTopFavoriteAnime();

    @Select("SELECT title AS name, view_count AS value FROM news_info WHERE status = 1 ORDER BY view_count DESC, id DESC LIMIT 8")
    List<NameValueVO> selectTopViewedNews();
}
