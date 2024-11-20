package ru.rsreu.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.domain.TariffFilter;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.CompanyService;
import ru.rsreu.manager.service.implementation.TariffService;
import ru.rsreu.manager.service.implementation.UserService;

@Controller
public class UserController {

    private final TariffService tariffService;

    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public UserController(
        TariffService tariffService,
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @RequestMapping("/")
    public String initialize() {
        return "redirect:/user/tariffs";
    }

    @RequestMapping({"user", "/user/tariffs"})
    public String showTariffsPage(
        @ModelAttribute("tariffFilter") TariffFilter tariffFilter,
        @ModelAttribute("tariffs") ArrayList<Tariff> tariffs,
        Model model
    ) {
        if (tariffs.isEmpty() && (!tariffFilter.isCurrent())) {
            model.addAttribute("tariffs", tariffService.findAll());
        }

        if (tariffFilter == null) {
            model.addAttribute("tariffFilter", new TariffFilter());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute("bindingResultErrors") != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s",
                    messagePropertiesSource.getMessage("binding.result.template"), "tariffFilter"
                ), bindingResult);
            }
        }

        return "/user/tariffs";
    }

    @PostMapping("/user/applyFilter")
    public String applyFilter(
        @Valid @ModelAttribute("tariffFilter") TariffFilter tariffFilter,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/user/tariffs";
        }
        if (!tariffFilter.validRanges()) {
            redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            redirectAttributes.addFlashAttribute(
                "wrongRanges",
                "Максимальная граница диапазона фильтрации должна быть больше минимальной!"
            );
            return "redirect:/user/tariffs";
        }
        List<Tariff> tariffList = tariffService.findAll();
        tariffList = (tariffList.stream().filter(tariffFilter::applyFilter).collect(Collectors.toList()));
        tariffFilter.setCurrent(true);
        redirectAttributes.addFlashAttribute("tariffs", tariffList);
        redirectAttributes.addFlashAttribute("tariffFilter", tariffFilter);
        return "redirect:/user/tariffs";
    }

    @PostMapping("/user/deleteFilter")
    public String deleteFilter(
        @ModelAttribute("tariffFilter") TariffFilter tariffFilter,
        RedirectAttributes redirectAttributes
    ) {
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

}
