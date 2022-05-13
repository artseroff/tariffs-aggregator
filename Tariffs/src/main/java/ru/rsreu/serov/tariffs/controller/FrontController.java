package ru.rsreu.serov.tariffs.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.serov.tariffs.entity.RoleEnum;
import ru.rsreu.serov.tariffs.entity.Tariff;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.repository.RoleRepository;
import ru.rsreu.serov.tariffs.repository.TariffRepository;
import ru.rsreu.serov.tariffs.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class FrontController {

    @Autowired
    TariffRepository tariffRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String initialize() {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value="username") String username, @RequestParam(value="password") String password,
                        HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String page;
        User foundUser = userRepository.getUserByLogin(username);

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            //Если не авторизован
            if (!foundUser.isAuthorized()) {

                //изменить статус на авторизован
                userRepository.updateUserAuthorizationStatus(foundUser.getId(), true);
                foundUser.setAuthorized(true);
                HttpSession session = request.getSession();
                session.setAttribute("user", foundUser);

                if (foundUser.getRole().getName().equals(RoleEnum.ADMINISTRATOR.getName())) {
                    page = "redirect:/admin/users";
                } else {
                    page = "redirect:/editor/tariffs";
                }

            } else {
                redirectAttributes.addFlashAttribute("errorText", "Вы уже авторизованы!");
                page = "redirect:/login";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorText", "Неверный логин или пароль!");
            page = "redirect:/login";
        }

        return page;
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "/admin/users";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @RequestMapping("/third")
    public String goThird() {
        return "third";
    }

    @RequestMapping("/admin/showEditUserPage")
    public String showEditUserPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("userId"));
        User user = userRepository.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("oldLogin", user.getLogin());
        return "/admin/editUserPage";
    }

    @RequestMapping("/admin/user")
    public String showUserPageWithErrors(Model model, @ModelAttribute("user") User user, @ModelAttribute("bindingResultErrors") BindingResult bindingResult) {
        // если были ошибки биндинга
       /* if (model.getAttribute("bindingResultErrors")!=null) {
            List<ObjectError> listErrors = (List<ObjectError>) model.getAttribute("bindingResultErrors");
            assert listErrors != null;
            for (ObjectError err: listErrors) {
                bindingResult.addError(err);
            }
        }*/

        model.addAttribute("org.springframework.validation.BindingResult.user", bindingResult);
        return "/admin/editUserPage";
    }


    @PostMapping("/admin/editUser")
    public String saveEditedUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("oldLogin") String oldLogin) {
        if (bindingResult.hasErrors()) {

            // org.springframework.validation.BindingResult.имяформы
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/admin/user";
        }

        // Если были изменения в логине
        if (!oldLogin.equals(user.getLogin())){
            List<User> users = userRepository.findUsersByLogin(user.getLogin());
            if (users.isEmpty()) {
                userRepository.save(user);
                return "redirect:/admin/users";
            } else {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("errorUniqueLoginText", "Пользователь с таким логином уже существует");
                return "redirect:/admin/user";
            }
        } else {
            userRepository.save(user);
            return "redirect:/admin/users";
        }

    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("userId"));
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }


    @RequestMapping("/admin/showAddUserPage")
    public String addUser(Model model, @ModelAttribute("user") User user) {
        if (user==null) {
            model.addAttribute("user", new User());
        } else {
            // Если метод открыт не в первый раз и были ошибки биндинга
            if (model.getAttribute("bindingResultErrors")!=null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            }

        }

        model.addAttribute("roles", roleRepository.findAll());
        return "admin/addUserPage";
    }
/*
    @RequestMapping("/admin/addUserPage")
    public String showAddUserPage(Model model) {
        return "admin/addUserPage";
    }*/

    @PostMapping("/admin/addUser")
    public String saveAddedUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, @RequestParam("oldLogin") String oldLogin) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            //redirectAttributes.addFlashAttribute("roles", roleRepository.findAll());
            // org.springframework.validation.BindingResult.имяформы
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/admin/showAddUserPage";
        }

        // Если были изменения в логине
        if (!oldLogin.equals(user.getLogin())) {
            List<User> users = userRepository.findUsersByLogin(user.getLogin());
            if (users.isEmpty()) {
                userRepository.save(user);
                return "redirect:/admin/users";
            } else {
                redirectAttributes.addFlashAttribute("user", user);
                //redirectAttributes.addFlashAttribute("roles", roleRepository.findAll());
                redirectAttributes.addFlashAttribute("errorUniqueLoginText", "Пользователь с таким логином уже существует");
                return "redirect:/admin/showAddUserPage";
            }
        } else {
            userRepository.save(user);
            return "redirect:/admin/users";
        }

    }

}
