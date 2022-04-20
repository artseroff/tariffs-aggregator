package ru.rsreu.serov.tariffs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontController {

    @Autowired
    UserRepository userRepository;

    List<String> listLogins = new ArrayList<>();

    @RequestMapping("/")
    public String init() {

        return "index";
    }
/*
    // в command передаем path
    @RequestMapping("/**")
    public String callCommand(HttpServletRequest request) {

        return command(request);
    }

    public String command(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (path.equals("/addLogin")) {
            String login = request.getParameter("login");
            listLogins.add(login);
            return "redirect:/greeting";
        } else if (path.equals("/greeting")) {
            request.setAttribute("listLogins", listLogins);
            return "greeting";
        } else {
            return "third";
        }
    }*/

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("1", "1");
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam(value="username") String username, @RequestParam(value="password") String password,
                        HttpServletRequest request) {
        String page = "";
        User foundUser = userRepository.getUserByUsername(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            //Если не авторизован
            if (!foundUser.isAuthorized()) {

                //изменить статус на авторизован
                userRepository.updateUserAuthorizationStatus(foundUser.getId(), true);

                boolean b = request.getSession(false) == null;
                HttpSession session = request.getSession();
                b = session.getAttribute("user") == null;
                session.setAttribute("user", foundUser);

                page = "redirect:/admin/users";
            } else {
                model.addAttribute("errorText", "Вы уже авторизованы!");
                page = "/login";
            }
        } else {
            model.addAttribute("errorText", "Неверный логин или пароль!");
            page = "/login";
        }

        return page;
    }

    @RequestMapping("/admin/users")
    public String showUsers(Model model, HttpServletRequest request) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "/admin/users";
    }

    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        request.getSession().invalidate();
        return "/index";
    }


}
