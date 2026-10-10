package com.neusoft.care.auth;

import com.neusoft.care.user.entity.SysUser;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private final Map<String, SysUser> tokens = new ConcurrentHashMap<>();

    public String issue(SysUser user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokens.put(token, user);
        return token;
    }

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
