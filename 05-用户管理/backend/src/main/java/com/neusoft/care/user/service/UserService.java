package com.neusoft.care.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.user.entity.SysRole;
import com.neusoft.care.user.entity.SysUser;
import com.neusoft.care.user.mapper.SysRoleMapper;
import com.neusoft.care.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;

    public List<SysUser> listUsers() {
        return sysUserMapper.selectWithRole();
    }

    public List<SysRole> listRoles() {
        return sysRoleMapper.selectList(null);
    }

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

    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }

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

    public SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }
}
