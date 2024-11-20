package ru.rsreu.manager.controller.servlet.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import ru.rsreu.manager.domain.RoleEnum;
import ru.rsreu.manager.domain.User;

@Order(1)
@WebFilter(urlPatterns = {"/admin/*", "/editor/*"})
public class SecurityRedirectFilter implements Filter {
    private static final String INDEX_PATH = "/";
    private static final String COMBINE_FORMAT = "%s%s";

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(false);

        // if session ended, when user was on page and send command to controller
        if (session == null) {
            httpResponse.sendRedirect(String.format(COMBINE_FORMAT, httpRequest.getContextPath(), INDEX_PATH));
            return;
        }
        // if user logged out and returned to page and send command to controller
        User user = (User) session.getAttribute("user");
        if (user == null) {
            httpResponse.sendRedirect(String.format(COMBINE_FORMAT, httpRequest.getContextPath(), INDEX_PATH));
        } else {
            RoleEnum role = (RoleEnum) session.getAttribute("roleEnum");
            String mainPage = role.getMainPage();
            //if requested page not allowed to this users role, redirect him to his role main page
            if (!httpRequest.getRequestURI().contains(mainPage)) {
                httpResponse.sendRedirect(String.format(COMBINE_FORMAT, httpRequest.getContextPath(), mainPage));
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }

    }
}
