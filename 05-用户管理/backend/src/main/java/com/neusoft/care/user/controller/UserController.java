package com.neusoft.care.user.controller;

import com.neusoft.care.common.Result;
import com.neusoft.care.user.entity.SysRole;
import com.neusoft.care.user.entity.SysUser;
import com.neusoft.care.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public Result<List<SysUser>> list() {
        return Result.ok(userService.listUsers());
    }

    @GetMapping("/roles")
    public Result<List<SysRole>> roles() {
        return Result.ok(userService.listRoles());
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysUser user) {
        userService.saveUser(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.ok();
    }

    @PostMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.ok();
    }
}
