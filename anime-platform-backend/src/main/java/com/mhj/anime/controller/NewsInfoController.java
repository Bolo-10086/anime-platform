package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.NewsInfo;
import com.mhj.anime.service.NewsInfoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsInfoController {

    private final NewsInfoService newsInfoService;

    public NewsInfoController(NewsInfoService newsInfoService) {
        this.newsInfoService = newsInfoService;
    }

    @GetMapping(value = "/api/news/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<NewsInfo>> publicList(@RequestParam(required = false) String keyword) {
        return ApiResult.success(newsInfoService.listPublished(keyword));
    }

    @GetMapping(value = "/api/news/detail/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<NewsInfo> detail(@PathVariable Long id) {
        return ApiResult.success(newsInfoService.detail(id));
    }

    @GetMapping(value = "/api/admin/news/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<NewsInfo>> adminList(@RequestAttribute("currentRoles") List<String> roles,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Integer status) {
        requireAdmin(roles);
        return ApiResult.success(newsInfoService.listAll(keyword, status));
    }

    @PostMapping(value = "/api/admin/news", produces = "application/json;charset=UTF-8")
    public ApiResult<NewsInfo> create(@RequestAttribute("currentRoles") List<String> roles,
                                      @RequestAttribute("currentUserId") Long currentUserId,
                                      @RequestBody NewsInfo newsInfo) {
        requireAdmin(roles);
        return ApiResult.success(newsInfoService.createNews(newsInfo, currentUserId));
    }

    @PutMapping(value = "/api/admin/news/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<NewsInfo> update(@RequestAttribute("currentRoles") List<String> roles,
                                      @PathVariable Long id,
                                      @RequestBody NewsInfo newsInfo) {
        requireAdmin(roles);
        return ApiResult.success(newsInfoService.updateNews(id, newsInfo));
    }

    @DeleteMapping(value = "/api/admin/news/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> delete(@RequestAttribute("currentRoles") List<String> roles,
                                  @PathVariable Long id) {
        requireAdmin(roles);
        newsInfoService.deleteNews(id);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
