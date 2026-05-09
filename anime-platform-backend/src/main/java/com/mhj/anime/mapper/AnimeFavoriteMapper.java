package com.mhj.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mhj.anime.entity.AnimeFavorite;
import com.mhj.anime.vo.UserFavoriteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnimeFavoriteMapper extends BaseMapper<AnimeFavorite> {

    @Select("SELECT f.id AS favorite_id, a.id AS anime_id, a.title, a.cover_image, a.category_name, " +
            "a.score, a.source_name, a.watch_url, f.create_time " +
            "FROM anime_favorite f INNER JOIN anime_info a ON a.id = f.anime_id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.create_time DESC, f.id DESC")
    List<UserFavoriteVO> selectUserFavorites(@Param("userId") Long userId);
}
