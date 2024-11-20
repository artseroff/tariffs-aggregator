package ru.rsreu.manager.controller;

import jakarta.annotation.PostConstruct;
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

@Controller
public class UserController {

    public static final String REDIRECT_USER_TARIFFS = "redirect:/user/tariffs";
    public static final String TARIFFS_ATTR = "tariffs";
    public static final String BINDING_RESULT_ERRORS = "bindingResultErrors";
    public static final String TARIFF_FILTER_ATTR = "tariffFilter";
    private final TariffService tariffService;
    private final CompanyService companyService;
    private final MessagePropertiesSource messagePropertiesSource;
    private String bindingResultErrorsFilterAttr;

    public UserController(
        TariffService tariffService,
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @PostConstruct
    public void initBindingResultErrorsFilterAttr() {
        bindingResultErrorsFilterAttr = String.format("%s.%s",
            messagePropertiesSource.getMessage("binding.result.template"), TARIFF_FILTER_ATTR
        );
    }

    @RequestMapping("/")
    public String initialize() {
        return REDIRECT_USER_TARIFFS;
    }

    @RequestMapping({"user", "/user/tariffs"})
    public String showTariffsPage(
        @ModelAttribute("tariffFilter") TariffFilter tariffFilter,
        @ModelAttribute(TARIFFS_ATTR) ArrayList<Tariff> tariffs,
        Model model
    ) {
        if (tariffs.isEmpty() && (!tariffFilter.isCurrent())) {
            model.addAttribute(TARIFFS_ATTR, tariffService.findAll());
        }

        if (tariffFilter == null) {
            model.addAttribute(TARIFF_FILTER_ATTR, new TariffFilter());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute(BINDING_RESULT_ERRORS) != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute(BINDING_RESULT_ERRORS);
                model.addAttribute(bindingResultErrorsFilterAttr, bindingResult);
            }
        }

        return "/user/tariffs";
    }

    @PostMapping("/user/applyFilter")
    public String applyFilter(
        @Valid @ModelAttribute(TARIFF_FILTER_ATTR) TariffFilter tariffFilter,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(TARIFF_FILTER_ATTR, tariffFilter);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_ERRORS, bindingResult);
            return REDIRECT_USER_TARIFFS;
        }
        if (!tariffFilter.validRanges()) {
            redirectAttributes.addFlashAttribute(TARIFF_FILTER_ATTR, tariffFilter);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_ERRORS, bindingResult);
            redirectAttributes.addFlashAttribute(
                "wrongRanges",
                "Максимальная граница диапазона фильтрации должна быть больше минимальной!"
            );
            return REDIRECT_USER_TARIFFS;
        }
        List<Tariff> tariffList = tariffService.findAll();
        tariffList = (tariffList.stream().filter(tariffFilter::applyFilter).collect(Collectors.toList()));
        tariffFilter.setCurrent(true);
        redirectAttributes.addFlashAttribute(TARIFFS_ATTR, tariffList);
        redirectAttributes.addFlashAttribute(TARIFF_FILTER_ATTR, tariffFilter);
        return REDIRECT_USER_TARIFFS;
    }

    @PostMapping("/user/deleteFilter")
    public String deleteFilter(
        @ModelAttribute(TARIFF_FILTER_ATTR) TariffFilter tariffFilter,
        RedirectAttributes redirectAttributes
    ) {
        tariffFilter.setCurrent(false);
        redirectAttributes.addFlashAttribute(TARIFF_FILTER_ATTR, tariffFilter);
        return REDIRECT_USER_TARIFFS;
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
