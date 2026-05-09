package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.service.AdminStatsService;
import com.mhj.anime.vo.AdminStatsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping(value = "/overview", produces = "application/json;charset=UTF-8")
    public ApiResult<AdminStatsVO> overview(@RequestAttribute("currentRoles") List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
        return ApiResult.success(adminStatsService.overview());
    }
}
