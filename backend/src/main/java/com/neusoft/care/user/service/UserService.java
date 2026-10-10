package com.neusoft.care.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.user.entity.SysRole;
import com.neusoft.care.user.entity.SysUser;
import com.neusoft.care.user.mapper.SysRoleMapper;
import com.neusoft.care.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 用户服务类
 * 负责系统用户、角色的管理业务
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    /**
     * 查询所有用户（带角色信息）
     */
    public List<SysUser> listUsers() {
        return sysUserMapper.selectWithRole();
    }

    public List<SysRole> listRoles() {
        return sysRoleMapper.selectList(null);
    }
    /**
     * 新增/更新用户
     * 新增时自动设置默认密码和状态
     */
    public void saveUser(SysUser user) {
        if (user.getId() == null) {
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                user.setPassword(defaultPasswordFromPhone(user.getPhone()));
            }
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            sysUserMapper.insert(user);
        } else {
            SysUser existing = sysUserMapper.selectById(user.getId());
            if (existing != null && (user.getPassword() == null || user.getPassword().isBlank())) {
                user.setPassword(existing.getPassword());
            }
            sysUserMapper.updateById(user);
        }
    }
    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }
    /**
     * 重置密码为手机号后6位
     */
    public void resetPassword(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user != null) {
            user.setPassword(defaultPasswordFromPhone(user.getPhone()));
            sysUserMapper.updateById(user);
        }
    }

    public static String defaultPasswordFromPhone(String phone) {
        if (phone != null && phone.length() >= 6) {
            return phone.substring(phone.length() - 6);
        }
        return "123456";
    }

    /**
     * 根据用户名查询用户（登录用）
     */
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }
}
