package ru.rsreu.manager.controller;

import jakarta.annotation.PostConstruct;
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
import ru.rsreu.manager.domain.User;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.RoleService;
import ru.rsreu.manager.service.implementation.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String USER_ID_PARAM = "userId";
    public static final String USER_ATTR = "user";
    public static final String EDIT_USER_PAGE_PATH = "/admin/editUserPage";
    public static final String BINDING_RESULT_ERRORS_ATTR = "bindingResultErrors";
    public static final String REDIRECT_ADMIN = "redirect:/admin";
    private final UserService userService;
    private final RoleService roleService;
    private final MessagePropertiesSource messagePropertiesSource;
    private String bindingResultErrorsUserAttr;

    public AdminController(
        UserService userService,
        RoleService roleService, MessagePropertiesSource messagePropertiesSource
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @PostConstruct
    public void initBindingResultErrorsUserAttr() {
        bindingResultErrorsUserAttr = String.format("%s.%s",
            messagePropertiesSource.getMessage("binding.result.template"), USER_ATTR
        );
    }

    @RequestMapping({"", "/users"})
    public String showUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/admin/users";
    }

    @RequestMapping("/showEditUserPage")
    public String showEditUserPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter(USER_ID_PARAM));
        User user = userService.findById(id);
        model.addAttribute(USER_ATTR, user);
        return EDIT_USER_PAGE_PATH;
    }

    @GetMapping("/userPageWithErrors")
    public String showUserPageWithErrors(Model model, @ModelAttribute(USER_ATTR) User user) {
        // если были ошибки биндинга
        if (model.getAttribute(BINDING_RESULT_ERRORS_ATTR) != null) {
            BindingResult bindingResult = (BindingResult) model.getAttribute(BINDING_RESULT_ERRORS_ATTR);
            model.addAttribute(bindingResultErrorsUserAttr, bindingResult);
        }
        return EDIT_USER_PAGE_PATH;
    }

    @PostMapping("/editUser")
    public String saveEditedUser(
        @Valid @ModelAttribute("USER_ATTR") User user,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        String errorView = "redirect:/admin/userPageWithErrors";
        return saveUser(user, bindingResult, redirectAttributes, errorView);
    }

    private String saveUser(
        User user,
        BindingResult bindingResult, RedirectAttributes redirectAttributes, String errorPage
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_ERRORS_ATTR, bindingResult);
            redirectAttributes.addFlashAttribute(USER_ATTR, user);
            return errorPage;
        }

        if (userService.processUpdateTransactional(user)) {
            return REDIRECT_ADMIN;
        } else {
            redirectAttributes.addFlashAttribute(USER_ATTR, user);
            redirectAttributes.addFlashAttribute("errorUniqueLoginText", "Пользователь с таким логином уже существует");
            return errorPage;
        }
    }

    @PostMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter(USER_ID_PARAM));
        userService.deleteById(id);
        return REDIRECT_ADMIN;
    }

    // Этот метот как для пост, так и гет после редиректа
    @RequestMapping("/showAddUserPage")
    public String showAddUserPage(Model model, @ModelAttribute(USER_ATTR) User user) {
        if (user == null) {
            model.addAttribute(USER_ATTR, new User());
        } else {
            // Если метод открыт не в первый раз и были ошибки биндинга
            if (model.getAttribute(BINDING_RESULT_ERRORS_ATTR) != null) {
                BindingResult bindingResult = (BindingResult) model
                    .getAttribute(BINDING_RESULT_ERRORS_ATTR);
                model.addAttribute(bindingResultErrorsUserAttr, bindingResult);
            }

        }
        model.addAttribute("roles", roleService.findAll());
        return "admin/addUserPage";
    }

    @PostMapping("/addUser")
    public String addUser(
        @Valid @ModelAttribute(USER_ATTR) User user,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        String errorView = "redirect:/admin/showAddUserPage";
        return saveUser(user, bindingResult, redirectAttributes, errorView);

    }

}
