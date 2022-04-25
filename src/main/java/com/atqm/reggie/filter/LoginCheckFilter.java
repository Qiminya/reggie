package com.atqm.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.atqm.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1.获取本次请求的uri
        String uri = request.getRequestURI();

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        // 2.判断路径是否可行
        boolean check = check(urls, uri);
        if (check){
            // 匹配 放行
            filterChain.doFilter(request, response);
            return ;
        }
        log.info("检查的请求,{}", request.getRequestURI());
        // 3.开始做登录检查
        if (request.getSession().getAttribute("employee") != null){
            // 已登录放行
            filterChain.doFilter(request, response);
            return;
        }
        // 未登录配合前端拦截器
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配
     * @param urls
     * @param requestURI
     * @return 匹配成功返回true
     */
    public boolean check(String[] urls, String requestURI){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
