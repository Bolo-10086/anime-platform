package com.mhj.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mhj.anime.entity.AnimeComment;
import com.mhj.anime.vo.AdminCommentVO;
import com.mhj.anime.vo.CommentVO;
import com.mhj.anime.vo.UserCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnimeCommentMapper extends BaseMapper<AnimeComment> {

    @Select("SELECT c.id, c.anime_id, c.user_id, u.username, u.nickname, c.content, c.create_time " +
            "FROM anime_comment c LEFT JOIN sys_user u ON u.id = c.user_id " +
            "WHERE c.anime_id = #{animeId} AND c.status = 1 " +
            "ORDER BY c.create_time DESC, c.id DESC")
    List<CommentVO> selectCommentList(@Param("animeId") Long animeId);

    @Select("SELECT c.id AS comment_id, a.id AS anime_id, a.title, a.cover_image, c.content, c.create_time " +
            "FROM anime_comment c INNER JOIN anime_info a ON a.id = c.anime_id " +
            "WHERE c.user_id = #{userId} AND c.status = 1 " +
            "ORDER BY c.create_time DESC, c.id DESC")
    List<UserCommentVO> selectUserComments(@Param("userId") Long userId);

    @Select("<script>" +
            "SELECT c.id, c.anime_id, a.title AS anime_title, c.user_id, u.username, u.nickname, c.content, c.status, c.create_time " +
            "FROM anime_comment c " +
            "INNER JOIN anime_info a ON a.id = c.anime_id " +
            "INNER JOIN sys_user u ON u.id = c.user_id " +
            "WHERE 1 = 1 " +
            "<if test='keyword != null and keyword != \"\"'> " +
            "AND (a.title LIKE CONCAT('%', #{keyword}, '%') OR u.username LIKE CONCAT('%', #{keyword}, '%') OR u.nickname LIKE CONCAT('%', #{keyword}, '%') OR c.content LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if> " +
            "<if test='animeId != null'> AND c.anime_id = #{animeId} </if> " +
            "<if test='userId != null'> AND c.user_id = #{userId} </if> " +
            "<if test='status != null'> AND c.status = #{status} </if> " +
            "ORDER BY c.create_time DESC, c.id DESC" +
            "</script>")
    List<AdminCommentVO> selectAdminComments(@Param("keyword") String keyword,
                                             @Param("animeId") Long animeId,
                                             @Param("userId") Long userId,
                                             @Param("status") Integer status);
}
