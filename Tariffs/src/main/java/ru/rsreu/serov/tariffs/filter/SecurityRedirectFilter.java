package ru.rsreu.serov.tariffs.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.rsreu.serov.tariffs.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = {"/admin/*"})
public class SecurityRedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(false);
        //String command = httpRequest.getParameter("command");
        String indexPath = "/";
        // if session ended, when user was on page and send command to controller
        if (session == null) {
            httpResponse.sendRedirect(String.format("%s%s", httpRequest.getContextPath(), indexPath));
            return;
            // if user logged out and returned back to page and send command to controller
        } else {
            User user = (User) session.getAttribute("user");
            if (user == null) {

                    httpResponse.sendRedirect(String.format("%s%s", httpRequest.getContextPath(), indexPath));
                    return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
