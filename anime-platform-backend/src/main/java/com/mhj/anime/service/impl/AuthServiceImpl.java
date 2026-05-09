package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mhj.anime.dto.LoginRequest;
import com.mhj.anime.dto.RegisterRequest;
import com.mhj.anime.entity.SysRole;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.entity.SysUserRole;
import com.mhj.anime.mapper.SysRoleMapper;
import com.mhj.anime.service.AuthService;
import com.mhj.anime.service.SysRoleService;
import com.mhj.anime.service.SysUserRoleService;
import com.mhj.anime.service.SysUserService;
import com.mhj.anime.util.JwtUtil;
import com.mhj.anime.vo.LoginResponse;
import com.mhj.anime.vo.UserInfoVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "USER";

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleMapper sysRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(SysUserService sysUserService,
                           SysRoleService sysRoleService,
                           SysUserRoleService sysUserRoleService,
                           SysRoleMapper sysRoleMapper,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysUserRoleService = sysUserRoleService;
        this.sysRoleMapper = sysRoleMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse register(RegisterRequest request) {
        validateRegister(request);
        String username = request.getUsername().trim();
        boolean exists = sysUserService.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)) > 0;
        if (exists) {
            throw new IllegalArgumentException("用户名已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname().trim() : username);
        user.setEmail(StringUtils.hasText(request.getEmail()) ? request.getEmail().trim() : null);
        user.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone().trim() : null);
        user.setStatus(1);
        sysUserService.save(user);

        SysRole role = getRole(DEFAULT_ROLE);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        sysUserRoleService.save(userRole);

        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("请输入用户名和密码");
        }
        SysUser user = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername().trim())
                .last("LIMIT 1"));
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new IllegalArgumentException("账号已被禁用");
        }
        return buildLoginResponse(user);
    }

    @Override
    public UserInfoVO profile(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("请先登录");
        }
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new IllegalArgumentException("账号已被禁用");
        }
        return buildUserInfo(user);
    }

    private void validateRegister(RegisterRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername())) {
            throw new IllegalArgumentException("请输入用户名");
        }
        if (!StringUtils.hasText(request.getPassword()) || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于6位");
        }
    }

    private SysRole getRole(String roleCode) {
        SysRole role = sysRoleService.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, roleCode)
                .last("LIMIT 1"));
        if (role == null) {
            throw new IllegalArgumentException("系统角色未初始化");
        }
        return role;
    }

    private LoginResponse buildLoginResponse(SysUser user) {
        LoginResponse response = new LoginResponse();
        UserInfoVO userInfo = buildUserInfo(user);
        response.setUserInfo(userInfo);
        response.setToken(jwtUtil.generateToken(user, userInfo.getRoles()));
        return response;
    }

    private UserInfoVO buildUserInfo(SysUser user) {
        List<String> roles = sysRoleMapper.selectRoleCodesByUserId(user.getId());
        if (roles == null || roles.isEmpty()) {
            roles = Collections.singletonList(DEFAULT_ROLE);
        }
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setStatus(user.getStatus());
        userInfo.setRoles(roles);
        return userInfo;
    }
}
