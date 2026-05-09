package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.SysRole;
import com.mhj.anime.mapper.SysRoleMapper;
import com.mhj.anime.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
