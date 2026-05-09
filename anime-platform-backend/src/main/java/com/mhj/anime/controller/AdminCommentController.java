package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.dto.StatusRequest;
import com.mhj.anime.entity.AnimeComment;
import com.mhj.anime.mapper.AnimeCommentMapper;
import com.mhj.anime.vo.AdminCommentVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminCommentController {

    private final AnimeCommentMapper animeCommentMapper;

    public AdminCommentController(AnimeCommentMapper animeCommentMapper) {
        this.animeCommentMapper = animeCommentMapper;
    }

    @GetMapping(value = "/api/admin/comment/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AdminCommentVO>> list(@RequestAttribute("currentRoles") List<String> roles,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Long animeId,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) Integer status) {
        requireAdmin(roles);
        return ApiResult.success(animeCommentMapper.selectAdminComments(keyword, animeId, userId, status));
    }

    @PutMapping(value = "/api/admin/comment/{id}/status", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> updateStatus(@RequestAttribute("currentRoles") List<String> roles,
                                        @PathVariable Long id,
                                        @RequestBody StatusRequest request) {
        requireAdmin(roles);
        if (request == null || (request.getStatus() != 0 && request.getStatus() != 1)) {
            throw new IllegalArgumentException("评论状态只能为显示或隐藏");
        }
        AnimeComment comment = animeCommentMapper.selectById(id);
        if (comment == null) {
            throw new IllegalArgumentException("评论不存在");
        }
        comment.setStatus(request.getStatus());
        animeCommentMapper.updateById(comment);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
