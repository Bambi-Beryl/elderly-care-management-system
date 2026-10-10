package com.neusoft.care.auth;

import com.neusoft.care.user.entity.SysUser;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Token 存储器（内存版）
 * <p>
 * 功能：
 * <ul>
 *     <li>生成随机登录 Token</li>
 *     <li>保存 Token -> 用户信息映射</li>
 *     <li>根据 Token 获取用户</li>
 *     <li>注销/移除 Token</li>
 * </ul>
 * 线程安全：使用 ConcurrentHashMap 存储
 * 适用场景：单服务、简单登录态管理
 *
 * @author 你的名字
 * @since 2026-06-01
 */
@Component
public class TokenStore {
    /**
     * 保存 Token 与 用户信息的映射
     * key：token 字符串
     * value：当前登录用户对象
     */
    private final Map<String, SysUser> tokens = new ConcurrentHashMap<>();
    /**
     * 创建并保存用户的登录 Token
     *
     * @param user 登录用户对象
     * @return 生成的 token 字符串
     */
    public String issue(SysUser user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokens.put(token, user);
        return token;
    }
    /**
     * 根据 token 获取用户信息
     *
     * @param token 登录令牌
     * @return 存在则返回用户对象；不存在返回 null
     */
    public SysUser get(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return tokens.get(token);
    }

    public void revoke(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }
}
