package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.AnimeCategory;
import com.mhj.anime.service.AnimeCategoryService;
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
public class CategoryController {

    private final AnimeCategoryService animeCategoryService;

    public CategoryController(AnimeCategoryService animeCategoryService) {
        this.animeCategoryService = animeCategoryService;
    }

    @GetMapping(value = "/api/category/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeCategory>> publicList() {
        return ApiResult.success(animeCategoryService.listCategory());
    }

    @GetMapping(value = "/api/admin/category/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AnimeCategory>> adminList(@RequestAttribute("currentRoles") List<String> roles) {
        requireAdmin(roles);
        return ApiResult.success(animeCategoryService.listCategory());
    }

    @PostMapping(value = "/api/admin/category", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeCategory> create(@RequestAttribute("currentRoles") List<String> roles,
                                           @RequestBody AnimeCategory category) {
        requireAdmin(roles);
        return ApiResult.success(animeCategoryService.createCategory(category));
    }

    @PutMapping(value = "/api/admin/category/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<AnimeCategory> update(@RequestAttribute("currentRoles") List<String> roles,
                                           @PathVariable Long id,
                                           @RequestBody AnimeCategory category) {
        requireAdmin(roles);
        return ApiResult.success(animeCategoryService.updateCategory(id, category));
    }

    @DeleteMapping(value = "/api/admin/category/{id}", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> delete(@RequestAttribute("currentRoles") List<String> roles,
                                  @PathVariable Long id) {
        requireAdmin(roles);
        animeCategoryService.deleteCategory(id);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
