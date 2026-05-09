package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.AnimeInfo;
import com.mhj.anime.service.AnimeInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anime")
public class AnimeInfoController {

    private final AnimeInfoService animeInfoService;

    public AnimeInfoController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeInfo>> list(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long tagId,
                                           @RequestParam(required = false) Integer releaseYear,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false) String region,
                                           @RequestParam(required = false) String type) {
        return ApiResult.success(animeInfoService.listAnime(keyword, categoryId, tagId, releaseYear, status, region, type));
    }

    @GetMapping(value = "/detail/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeInfo> detail(@PathVariable Long id) {
        return ApiResult.success(animeInfoService.detail(id));
    }
}
