package com.mhj.anime.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhj.anime.common.ApiResult;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.service.SysUserService;
import com.mhj.anime.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final SysUserService sysUserService;

    public AuthInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper, SysUserService sysUserService) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.sysUserService = sysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            writeUnauthorized(response, "请先登录");
            return false;
        }

        try {
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            Object userIdObject = claims.get("userId");
            Long userId = Long.valueOf(String.valueOf(userIdObject));
            SysUser user = sysUserService.getById(userId);
            if (user == null || user.getStatus() == null || user.getStatus() != 1) {
                writeUnauthorized(response, "账号已被禁用或不存在");
                return false;
            }
            request.setAttribute("currentUserId", userId);
            request.setAttribute("currentUsername", claims.getSubject());
            request.setAttribute("currentRoles", claims.get("roles"));
            return true;
        } catch (Exception exception) {
            writeUnauthorized(response, "登录状态已失效，请重新登录");
            return false;
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResult.fail(401, message)));
    }
}
