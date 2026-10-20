package com.web.mvc;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/adopt", "/adoptForm.jsp"})
public class LoginFilter implements Filter {
	
    // 필터 초기화 메서드
    public void init(FilterConfig filterConfig) throws ServletException {}

    // 요청을 필터링하는 메서드
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 세션이 없거나 userId 속성이 없으면 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.setContentType("text/html; charset=UTF-8");
            httpResponse.getWriter().println("<script>alert('로그인이 필요합니다. 로그인 페이지로 이동합니다.'); location.href='loginForm.jsp';</script>");
        } else {
            // 세션이 유효하면 요청을 다음 필터로 전달
            chain.doFilter(request, response);
        }
    }

    // 필터 종료 메서드
    public void destroy() {}
}
