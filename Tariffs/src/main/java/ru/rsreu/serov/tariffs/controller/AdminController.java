package ru.rsreu.serov.tariffs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.serov.tariffs.entity.RoleEnum;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.message.MessagePropertiesSource;
import ru.rsreu.serov.tariffs.service.implementation.RoleService;
import ru.rsreu.serov.tariffs.service.implementation.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final MessagePropertiesSource messagePropertiesSource;

    public AdminController(UserService userService, RoleService roleService, MessagePropertiesSource messagePropertiesSource) {
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
       /* if (model.getAttribute("bindingResultErrors")!=null) {
            List<ObjectError> listErrors = (List<ObjectError>) model.getAttribute("bindingResultErrors");
            assert listErrors != null;
            for (ObjectError err: listErrors) {
                bindingResult.addError(err);
            }
        }*/
        if (model.getAttribute("bindingResultErrors") != null) {
            BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
            model.addAttribute(String.format("%s.%s", messagePropertiesSource.getMessage("binding.result.template"), "user"), bindingResult);
        }
        return "/admin/editUserPage";
    }


    @PostMapping("/editUser")
    public String saveEditedUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s", messagePropertiesSource.getMessage("binding.result.template"), "user"), bindingResult);
            }

        }
        model.addAttribute("roles", roleService.findAll());
        return "admin/addUserPage";
    }

    @PostMapping("/addUser")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
