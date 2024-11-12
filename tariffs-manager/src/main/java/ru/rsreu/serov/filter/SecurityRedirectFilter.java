package ru.rsreu.serov.filter;

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
import ru.rsreu.serov.entity.RoleEnum;
import ru.rsreu.serov.entity.User;

@Order(1)
@WebFilter(urlPatterns = {"/admin/*", "/editor/*"})
public class SecurityRedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(false);

        String indexPath = "/";
        // if session ended, when user was on page and send command to controller
        if (session == null) {
            httpResponse.sendRedirect(String.format("%s%s", httpRequest.getContextPath(), indexPath));
            return;
            // if user logged out and returned to page and send command to controller
        } else {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                httpResponse.sendRedirect(String.format("%s%s", httpRequest.getContextPath(), indexPath));
                return;
            } else {
                RoleEnum role = RoleEnum.findRoleByName(user.getRole().getName());
                String mainPage = role.getMainPage();
                //if requested page not allowed to this users role, redirect him to his role main page
                if (!httpRequest.getRequestURI().contains(mainPage)) {
                    httpResponse.sendRedirect(String.format("%s%s", httpRequest.getContextPath(), mainPage));
                    return;
                }

            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
