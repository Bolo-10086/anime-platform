package com.mhj.anime.service;

import com.mhj.anime.dto.LoginRequest;
import com.mhj.anime.dto.RegisterRequest;
import com.mhj.anime.vo.LoginResponse;
import com.mhj.anime.vo.UserInfoVO;

public interface AuthService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserInfoVO profile(Long userId);
}
