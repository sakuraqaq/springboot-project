package com.sakura.component;


import com.alibaba.fastjson.JSONArray;
import com.sakura.web.annotation.NoLoginRequired;
import com.sakura.web.session.RedisSession;
import com.sakura.web.session.SessionUser;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class URLPathMatchingFilter implements HandlerInterceptor {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession(true);
        List<String> list = null;
        boolean contains = false;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethodAnnotation(NoLoginRequired.class) != null || handlerMethod.getBeanType().getAnnotation(NoLoginRequired.class) != null) {
            return true;
        }

        if (session instanceof RedisSession) {
            if (!((RedisSession) session).getSessionUser().isLogin()) {
                response.sendError(401);
                return false;
            }
            SessionUser sessionUser = ((RedisSession) session).getSessionUser();
            String permission = sessionUser.get(sessionUser.getUserId() + "permission");
            list = JSONArray.parseArray(permission, String.class);
        }

        for (String url : list) {
            if(antPathMatcher.match(url,request.getRequestURI())){
                contains = true;
                break;
            }
        }

        if (!contains) {
            response.sendError(403);
            return false;
        }
        return false;
    }
}
