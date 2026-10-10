package com.neusoft.care.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.auth.TokenStore;
import com.neusoft.care.auth.dto.LoginRequest;
import com.neusoft.care.auth.dto.LoginResponse;
import com.neusoft.care.auth.dto.RegisterRequest;
import com.neusoft.care.user.entity.SysRole;
import com.neusoft.care.user.entity.SysUser;
import com.neusoft.care.user.mapper.SysRoleMapper;
import com.neusoft.care.user.mapper.SysUserMapper;
import com.neusoft.care.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final TokenStore tokenStore;

    public LoginResponse login(LoginRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));
        if (user == null || !request.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new IllegalArgumentException("账号已禁用");
        }
        return buildResponse(user);
    }

    public LoginResponse register(RegisterRequest request) {
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getRealName())) {
            throw new IllegalArgumentException("用户名和姓名不能为空");
        }
        Long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, request.getUsername()));
        if (count != null && count > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setRole(StringUtils.hasText(request.getRole()) ? request.getRole() : "nurse");
        user.setStatus(1);
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(request.getPassword());
        } else {
            user.setPassword(UserService.defaultPasswordFromPhone(request.getPhone()));
        }
        sysUserMapper.insert(user);
        return buildResponse(user);
    }

    public void logout(String token) {
        tokenStore.revoke(token);
    }

    public LoginResponse current(String token) {
        SysUser user = tokenStore.get(token);
        if (user == null) {
            throw new IllegalArgumentException("未登录或登录已过期");
        }
        return buildResponse(user);
    }

    private LoginResponse buildResponse(SysUser user) {
        String token = tokenStore.issue(user);
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRealName(user.getRealName());
        resp.setRole(user.getRole());
        SysRole role = sysRoleMapper.selectOne(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, user.getRole()));
        resp.setRoleName(role != null ? role.getRoleName() : user.getRole());
        return resp;
    }
}
