package com.web.mvc;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
@WebFilter("/*")
public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        request.setCharacterEncoding("UTF-8"); response.setCharacterEncoding("UTF-8");
        response.setHeader("X-Content-Type-Options", "nosniff");
        String path = request.getServletPath();
        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("userId") != null;
        boolean admin = path.startsWith("/admin");
        boolean privatePath = admin || (path.startsWith("/myPage") || path.equals("/account.jsp")) || path.equals("/edit.do") || path.equals("/messages.do")
            || path.equals("/message-compose.jsp") || path.equals("/sendMessage.do") || path.startsWith("/editAdopt")
            || path.equals("/deleteAdopt.do") || path.equals("/adoption-create.jsp")
            || (path.equals("/adopt") && "POST".equals(request.getMethod()));
        if (privatePath && !loggedIn) { response.sendRedirect(request.getContextPath() + "/auth.jsp"); return; }
        if (admin && !Boolean.TRUE.equals(session.getAttribute("isAdmin"))) { response.sendError(403); return; }
        if ("POST".equals(request.getMethod())) {
            String origin = request.getHeader("Origin");
            String expected = request.getScheme() + "://" + request.getServerName()
                + ((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
            if ("cross-site".equals(request.getHeader("Sec-Fetch-Site")) || (origin != null && !origin.equals(expected))) { response.sendError(403); return; }
        }
        chain.doFilter(req, res);
    }
}
