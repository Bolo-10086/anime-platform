package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.AnimeTag;
import com.mhj.anime.service.AnimeTagService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {

    private final AnimeTagService animeTagService;

    public TagController(AnimeTagService animeTagService) {
        this.animeTagService = animeTagService;
    }

    @GetMapping(value = "/api/tag/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeTag>> publicList() {
        return ApiResult.success(animeTagService.listTag());
    }

    @GetMapping(value = "/api/admin/tag/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeTag>> adminList(@RequestAttribute("currentRoles") List<String> roles) {
        requireAdmin(roles);
        return ApiResult.success(animeTagService.listTag());
    }

    @PostMapping(value = "/api/admin/tag", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeTag> create(@RequestAttribute("currentRoles") List<String> roles,
                                      @RequestBody AnimeTag tag) {
        requireAdmin(roles);
        return ApiResult.success(animeTagService.createTag(tag));
    }

    @PutMapping(value = "/api/admin/tag/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeTag> update(@RequestAttribute("currentRoles") List<String> roles,
                                      @PathVariable Long id,
                                      @RequestBody AnimeTag tag) {
        requireAdmin(roles);
        return ApiResult.success(animeTagService.updateTag(id, tag));
    }

    @DeleteMapping(value = "/api/admin/tag/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> delete(@RequestAttribute("currentRoles") List<String> roles,
                                  @PathVariable Long id) {
        requireAdmin(roles);
        animeTagService.deleteTag(id);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
