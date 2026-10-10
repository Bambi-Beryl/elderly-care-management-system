package com.neusoft.care.common;

import com.neusoft.care.auth.TokenStore;
import com.neusoft.care.user.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenStore tokenStore;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        SysUser user = tokenStore.get(token);
        if (user == null) {
            throw new IllegalArgumentException("请先登录");
        }
        request.setAttribute("loginUser", user);
        return true;
    }
}
