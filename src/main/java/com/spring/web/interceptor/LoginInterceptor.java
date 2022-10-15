package com.spring.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws IOException {
        String uri = request.getRequestURI();
        if (uri.contains("login") || uri.contains("register")) {
            return true;
        } else {
            if (request.getSession().getAttribute("username") != null) {
                // 已经登陆直接放行
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        }
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception ex) {

    }
}
