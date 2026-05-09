package com.mhj.anime.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mhj.anime.entity.SysUser;
import com.mhj.anime.mapper.SysUserMapper;
import com.mhj.anime.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
