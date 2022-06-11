package ru.rsreu.serov.tariffs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.serov.tariffs.entity.RoleEnum;
import ru.rsreu.serov.tariffs.entity.Tariff;
import ru.rsreu.serov.tariffs.entity.TariffFilter;
import ru.rsreu.serov.tariffs.entity.User;
import ru.rsreu.serov.tariffs.message.MessagePropertiesSource;
import ru.rsreu.serov.tariffs.service.implementation.CompanyService;
import ru.rsreu.serov.tariffs.service.implementation.TariffService;
import ru.rsreu.serov.tariffs.service.implementation.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;

    private final TariffService tariffService;

    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public UserController(UserService userService, TariffService tariffService, CompanyService companyService, MessagePropertiesSource messagePropertiesSource) {
        this.userService = userService;
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @RequestMapping("/")
    public String initialize() {
        return "redirect:/user/tariffs";
    }

    @RequestMapping({"user", "/user/tariffs"})
    public String showTariffsPage(@ModelAttribute("tariffFilter") TariffFilter tariffFilter, @ModelAttribute("tariffs") ArrayList<Tariff> tariffs, Model model) {
        if (tariffs.isEmpty() && (!tariffFilter.isCurrent())) {
            model.addAttribute("tariffs", tariffService.findAll());
        }

        // компании
        /////
        if (tariffFilter == null) {
            model.addAttribute("tariffFilter", new TariffFilter());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute("bindingResultErrors") != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s", messagePropertiesSource.getMessage("binding.result.template"), "tariffFilter"), bindingResult);
            }
        }
        /////
        return "/user/tariffs";
    }

    @PostMapping("/user/applyFilter")
    public String applyFilter(@Valid @ModelAttribute("tariffFilter") TariffFilter tariffFilter, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/user/tariffs";
        }
        if (!tariffFilter.validRanges()) {
            redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            redirectAttributes.addFlashAttribute("wrongRanges", "Максимальная граница диапазона фильтрации должна быть больше минимальной!");
            return "redirect:/user/tariffs";
        }
        List<Tariff> tariffList = tariffService.findAll();
        tariffList = (tariffList.stream().filter((tariff) -> {
            return tariffFilter.applyFilter(tariff);
        }).collect(Collectors.toList()));
        tariffFilter.setCurrent(true);
        redirectAttributes.addFlashAttribute("tariffs", tariffList);
        redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
        return "redirect:/user/tariffs";
    }


    @PostMapping("/user/deleteFilter")
    public String deleteFilter(@ModelAttribute("tariffFilter") TariffFilter tariffFilter, RedirectAttributes redirectAttributes) {
        tariffFilter.setCurrent(false);
        redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
        return "redirect:/user/tariffs";
    }

    @RequestMapping("/user/companies")
    public String showCompaniesPage(Model model) {
        model.addAttribute("companies", companyService.findAll());
        return "/user/companies";
    }

    @GetMapping("/user/showTariffPage")
    public String showTariffPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("tariffId"));
        Tariff tariff = tariffService.findById(id);
        model.addAttribute("tariff", tariff);
        return "/user/tariffPage";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                        HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String page;
        User foundUser = userService.getByLoginAndPassword(login, password);
        if (foundUser != null) {
            //Если не авторизован
            if (!foundUser.isAuthorized()) {

                //изменить статус на авторизован
                userService.updateUserAuthorizationStatus(foundUser.getId(), true);
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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }


}
