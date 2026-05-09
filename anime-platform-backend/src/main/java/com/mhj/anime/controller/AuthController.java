package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.dto.LoginRequest;
import com.mhj.anime.dto.RegisterRequest;
import com.mhj.anime.service.AuthService;
import com.mhj.anime.vo.LoginResponse;
import com.mhj.anime.vo.UserInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public ApiResult<LoginResponse> register(@RequestBody RegisterRequest request) {
        return ApiResult.success(authService.register(request));
    }

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ApiResult<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResult.success(authService.login(request));
    }

    @GetMapping(value = "/profile", produces = "application/json;charset=UTF-8")
    public ApiResult<UserInfoVO> profile(@RequestAttribute("currentUserId") Long currentUserId) {
        return ApiResult.success(authService.profile(currentUserId));
    }
}
