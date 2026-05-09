package com.mhj.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.vo.AdminUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("<script>" +
            "SELECT u.id, u.username, u.nickname, u.email, u.phone, u.status, u.create_time, " +
            "(SELECT COUNT(*) FROM anime_comment c WHERE c.user_id = u.id) AS comment_count, " +
            "(SELECT COUNT(*) FROM anime_favorite f WHERE f.user_id = u.id) AS favorite_count, " +
            "(SELECT COUNT(*) FROM anime_rating r WHERE r.user_id = u.id) AS rating_count " +
            "FROM sys_user u " +
            "WHERE 1 = 1 " +
            "<if test='keyword != null and keyword != \"\"'> " +
            "AND (u.username LIKE CONCAT('%', #{keyword}, '%') OR u.nickname LIKE CONCAT('%', #{keyword}, '%') OR u.email LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if> " +
            "<if test='status != null'> AND u.status = #{status} </if> " +
            "ORDER BY u.create_time DESC, u.id DESC" +
            "</script>")
    List<AdminUserVO> selectAdminUsers(@Param("keyword") String keyword, @Param("status") Integer status);
}
