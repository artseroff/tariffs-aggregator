package ru.rsreu.manager.controller.editor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.exception.EntityHasOrphansException;
import ru.rsreu.manager.service.implementation.CompanyService;

@Controller
@RequestMapping("/editor")
public class CompanyController {

    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public CompanyController(
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
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
    public String deleteCompany(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        long id = Long.parseLong(request.getParameter("companyId"));
        try {
            companyService.deleteById(id);
        } catch (EntityHasOrphansException e) {
            redirectAttributes.addFlashAttribute("companyId", id);
            redirectAttributes.addFlashAttribute(
                "errorHasOrphansNameText",
                e.getMessage()
            );
            return "redirect:/editor/companies";
        }
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
                    messagePropertiesSource.getMessage("binding.result.template"), "company"
                ), bindingResult);
            }
        }

        return "/editor/companyPage";
    }

    @PostMapping("/saveCompany")
    public String saveCompany(
        @Valid @ModelAttribute("company") Company company,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("company", company);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/editor/showCompanyPage";
        }

        if (companyService.processUpdateTransactional(company)) {
            return "redirect:/editor/companies";
        } else {
            redirectAttributes.addFlashAttribute("company", company);
            redirectAttributes.addFlashAttribute(
                "errorUniqueNameText",
                "Компания с таким наименованием уже существует"
            );
            return "redirect:/editor/showCompanyPage";
        }

    }
}
