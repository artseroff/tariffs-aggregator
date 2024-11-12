package ru.rsreu.manager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.entity.Company;
import ru.rsreu.manager.entity.Tariff;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.CompanyService;
import ru.rsreu.manager.service.implementation.TariffService;

@Controller
@RequestMapping("/editor")
public class EditorController {

    private final TariffService tariffService;

    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public EditorController(TariffService tariffService, CompanyService companyService, MessagePropertiesSource messagePropertiesSource) {
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @RequestMapping({"", "/tariffs"})
    public String showTariffsPage(Model model) {
        model.addAttribute("tariffs", tariffService.findAll());
        return "/editor/tariffs";
    }


    @RequestMapping("/showEditTariffPage")
    public String showEditTariffPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("tariffId"));
        Tariff tariff = tariffService.findById(id);
        model.addAttribute("tariff", tariff);
        model.addAttribute("companies", companyService.findAll());
        return "/editor/tariffPage";
    }

    @PostMapping("/deleteTariff")
    public String deleteTariff(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("tariffId"));
        tariffService.deleteById(id);
        return "redirect:/editor/tariffs";
    }

    @RequestMapping("/showTariffPage")
    public String showTariffPage(Model model, @ModelAttribute("tariff") Tariff tariff) {
        if (tariff == null) {
            model.addAttribute("tariff", new Tariff());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute("bindingResultErrors") != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s",
                        messagePropertiesSource.getMessage("binding.result.template"), "tariff"), bindingResult);
            }
        }
        model.addAttribute("companies", companyService.findAll());
        return "/editor/tariffPage";
    }

    @PostMapping("/saveTariff")
    public String saveTariff(@Valid @ModelAttribute("tariff") Tariff tariff,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("tariff", tariff);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/editor/showTariffPage";
        }

        if (tariffService.isNewNameOfTariffWithIdUnique(tariff.getName(), tariff.getId())) {
            tariffService.add(tariff);
            return "redirect:/editor/tariffs";
        } else {
            redirectAttributes.addFlashAttribute("tariff", tariff);
            redirectAttributes.addFlashAttribute("errorUniqueNameText", "Тариф с таким наименованием уже существует");
            return "redirect:/editor/showTariffPage";
        }

    }

    @RequestMapping("/companies")
    public String showCompaniesPage(Model model) {
        model.addAttribute("companies", companyService.findAll());
        return "/editor/companies";
    }


    @RequestMapping("/showEditCompanyPage")
    public String showEditCompanyPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("companyId"));
        Company company = companyService.findById(id);
        model.addAttribute("company", company);
        return "/editor/companyPage";
    }

    @PostMapping("/deleteCompany")
    public String deleteCompany(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("companyId"));
        companyService.deleteById(id);
        return "redirect:/editor/companies";
    }

    @RequestMapping("/showCompanyPage")
    public String showCompanyPage(Model model, @ModelAttribute("company") Company company) {
        if (company == null) {
            model.addAttribute("company", new Company());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute("bindingResultErrors") != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
                model.addAttribute(String.format("%s.%s",
                        messagePropertiesSource.getMessage("binding.result.template"), "company"), bindingResult);
            }
        }

        return "/editor/companyPage";
    }

    @PostMapping("/saveCompany")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("company", company);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/editor/showCompanyPage";
        }

        if (companyService.isNewNameOfCompanyWithIdUnique(company.getName(), company.getId())) {
            companyService.add(company);
            return "redirect:/editor/companies";
        } else {
            redirectAttributes.addFlashAttribute("company", company);
            redirectAttributes.addFlashAttribute("errorUniqueNameText", "Компания с таким наименованием уже существует");
            return "redirect:/editor/showCompanyPage";
        }

    }
}
