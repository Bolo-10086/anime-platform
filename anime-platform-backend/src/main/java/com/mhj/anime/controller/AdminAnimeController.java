package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.AnimeInfo;
import com.mhj.anime.service.AnimeInfoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/anime")
public class AdminAnimeController {

    private final AnimeInfoService animeInfoService;

    public AdminAnimeController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeInfo>> list(@RequestAttribute("currentRoles") List<String> roles,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long tagId,
                                           @RequestParam(required = false) Integer releaseYear,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false) String region,
                                           @RequestParam(required = false) String type) {
        requireAdmin(roles);
        return ApiResult.success(animeInfoService.listAnime(keyword, categoryId, tagId, releaseYear, status, region, type));
    }

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeInfo> create(@RequestAttribute("currentRoles") List<String> roles,
                                       @RequestBody AnimeInfo animeInfo) {
        requireAdmin(roles);
        return ApiResult.success(animeInfoService.createAnime(animeInfo));
    }

    @PutMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeInfo> update(@RequestAttribute("currentRoles") List<String> roles,
                                       @PathVariable Long id,
                                       @RequestBody AnimeInfo animeInfo) {
        requireAdmin(roles);
        return ApiResult.success(animeInfoService.updateAnime(id, animeInfo));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> delete(@RequestAttribute("currentRoles") List<String> roles,
                                  @PathVariable Long id) {
        requireAdmin(roles);
        animeInfoService.deleteAnime(id);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
