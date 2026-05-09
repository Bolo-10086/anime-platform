package com.mhj.anime.controller;

import com.mhj.anime.common.ApiResult;
import com.mhj.anime.dto.StatusRequest;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.mapper.SysRoleMapper;
import com.mhj.anime.mapper.SysUserMapper;
import com.mhj.anime.service.SysUserService;
import com.mhj.anime.vo.AdminUserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminUserController {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserService sysUserService;

    public AdminUserController(SysUserMapper sysUserMapper,
                               SysRoleMapper sysRoleMapper,
                               SysUserService sysUserService) {
        this.sysUserMapper = sysUserMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserService = sysUserService;
    }

    @GetMapping(value = "/api/admin/user/list", produces = "application/json;charset=UTF-8")
    public ApiResult<List<AdminUserVO>> list(@RequestAttribute("currentRoles") List<String> roles,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) Integer status) {
        requireAdmin(roles);
        List<AdminUserVO> users = sysUserMapper.selectAdminUsers(keyword, status);
        users.forEach(user -> user.setRoles(sysRoleMapper.selectRoleCodesByUserId(user.getId())));
        return ApiResult.success(users);
    }

    @PutMapping(value = "/api/admin/user/{id}/status", produces = "application/json;charset=UTF-8")
    public ApiResult<Void> updateStatus(@RequestAttribute("currentRoles") List<String> roles,
                                        @PathVariable Long id,
                                        @RequestBody StatusRequest request) {
        requireAdmin(roles);
        if (id == null || id == 1L) {
            throw new IllegalArgumentException("系统管理员账号不能被禁用");
        }
        if (request == null || (request.getStatus() != 0 && request.getStatus() != 1)) {
            throw new IllegalArgumentException("账号状态只能为启用或禁用");
        }
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setStatus(request.getStatus());
        sysUserService.updateById(user);
        return ApiResult.success(null);
    }

    private void requireAdmin(List<String> roles) {
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("无管理员权限");
        }
    }
}
