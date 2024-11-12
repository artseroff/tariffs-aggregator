package ru.rsreu.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.entity.User;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.RoleService;
import ru.rsreu.manager.service.implementation.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final MessagePropertiesSource messagePropertiesSource;

    public AdminController(UserService userService,
                           RoleService roleService, MessagePropertiesSource messagePropertiesSource) {
        this.userService = userService;
        this.roleService = roleService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @RequestMapping({"", "/users"})
    public String showUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/admin/users";
    }

    @RequestMapping("/showEditUserPage")
    public String showEditUserPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("userId"));
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/admin/editUserPage";
    }

    @GetMapping("/userPageWithErrors")
    public String showUserPageWithErrors(Model model, @ModelAttribute("user") User user) {
        // если были ошибки биндинга
        if (model.getAttribute("bindingResultErrors") != null) {
            BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
            model.addAttribute(String.format("%s.%s",
                    messagePropertiesSource.getMessage("binding.result.template"), "user"), bindingResult);
        }
        return "/admin/editUserPage";
    }


    @PostMapping("/editUser")
    public String saveEditedUser(@Valid @ModelAttribute("user") User user,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/admin/userPageWithErrors";
        }

        if (userService.isNewLoginOfUserWithIdUnique(user.getLogin(), user.getId())) {
            userService.update(user);
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errorUniqueLoginText", "Пользователь с таким логином уже существует");
            return "redirect:/admin/userPageWithErrors";
        }

    }

    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("userId"));
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    // Этот метот как для пост, так и гет после редиректа
    @RequestMapping("/showAddUserPage")
    public String showAddUserPage(Model model, @ModelAttribute("user") User user) {
        if (user == null) {
            model.addAttribute("user", new User());
        } else {
            // Если метод открыт не в первый раз и были ошибки биндинга
            if (model.getAttribute("bindingResultErrors") != null) {
                BindingResult bindingResult = (BindingResult) model
                        .getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s",
                        messagePropertiesSource.getMessage("binding.result.template"), "user"), bindingResult);
            }

        }
        model.addAttribute("roles", roleService.findAll());
        return "admin/addUserPage";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/admin/showAddUserPage";
        }

        if (userService.isNewLoginOfUserWithIdUnique(user.getLogin(), user.getId())) {
            userService.add(user);
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errorUniqueLoginText", "Пользователь с таким логином уже существует");
            return "redirect:/admin/showAddUserPage";
        }

    }

}
