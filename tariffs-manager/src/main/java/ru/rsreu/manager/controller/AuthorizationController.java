package ru.rsreu.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.domain.RoleEnum;
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.service.exception.AlreadyAuthorizedUserException;
import ru.rsreu.manager.service.implementation.UserService;

@Controller
public class AuthorizationController {
    public static final String REDIRECT = "redirect:/";
    public static final String REDIRECT_LOGIN = "redirect:/login";
    public static final String ERROR_TEXT_ATTR = "errorText";
    private final UserService userService;

    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
        @RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
        HttpServletRequest request, RedirectAttributes redirectAttributes
    ) {
        User foundUser;
        try {
            foundUser = userService.authorizeUserWithLoginAndPassword(login, password);
        } catch (AlreadyAuthorizedUserException e) {
            redirectAttributes.addFlashAttribute(ERROR_TEXT_ATTR, "Вы уже авторизованы!");
            return REDIRECT_LOGIN;
        }
        if (foundUser == null) {
            redirectAttributes.addFlashAttribute(ERROR_TEXT_ATTR, "Неверный логин или пароль!");
            return REDIRECT_LOGIN;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", foundUser);

        RoleEnum roleEnum = RoleEnum.findRoleByName(foundUser.getRole().getName());
        session.setAttribute("roleEnum", roleEnum);

        return REDIRECT + roleEnum.getMainPage();
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return REDIRECT;
    }
}
