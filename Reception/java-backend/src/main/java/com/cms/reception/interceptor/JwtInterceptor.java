package com.cms.reception.interceptor;

import com.cms.reception.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 如果是登录或注册请求，直接放行
        if (requestURI.contains("/auth/login") || requestURI.contains("/auth/register")) {
            return true;
        }

        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            
            // 验证token
            if (jwtUtil.validateToken(token)) {
                // 获取用户ID并设置到request属性中
                Long userId = jwtUtil.getUserIdFromToken(token);
                request.setAttribute("userId", userId);
                return true;
            }
        }

        // 未通过验证
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
} 