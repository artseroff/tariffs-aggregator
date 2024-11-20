package ru.rsreu.manager.controller.editor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    public static final String COMPANY_ID_PARAM = "companyId";
    public static final String COMPANY_ATTR = "company";
    public static final String EDITOR_COMPANY_PAGE = "/editor/companyPage";
    public static final String REDIRECT_EDITOR_COMPANIES = "redirect:/editor/companies";
    public static final String REDIRECT_EDITOR_SHOW_COMPANY_PAGE = "redirect:/editor/showCompanyPage";
    public static final String BINDING_RESULT_ERRORS_ATTR = "bindingResultErrors";
    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public CompanyController(
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @GetMapping("/companies")
    public String showCompaniesPage(Model model) {
        model.addAttribute("companies", companyService.findAll());
        return "/editor/companies";
    }

    @GetMapping("/showEditCompanyPage")
    public String showEditCompanyPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter(COMPANY_ID_PARAM));
        Company company = companyService.findById(id);
        model.addAttribute(COMPANY_ATTR, company);
        return EDITOR_COMPANY_PAGE;
    }

    @PostMapping("/deleteCompany")
    public String deleteCompany(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        long id = Long.parseLong(request.getParameter(COMPANY_ID_PARAM));
        try {
            companyService.deleteById(id);
        } catch (EntityHasOrphansException e) {
            redirectAttributes.addFlashAttribute(COMPANY_ID_PARAM, id);
            redirectAttributes.addFlashAttribute(
                "errorHasOrphansNameText",
                e.getMessage()
            );
        }
        return REDIRECT_EDITOR_COMPANIES;
    }

    @GetMapping("/showCompanyPage")
    public String showCompanyPage(Model model, @ModelAttribute(COMPANY_ATTR) Company company) {
        if (company == null) {
            model.addAttribute(COMPANY_ATTR, new Company());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute(BINDING_RESULT_ERRORS_ATTR) != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute(BINDING_RESULT_ERRORS_ATTR);
                model.addAttribute(String.format("%s.%s",
                    messagePropertiesSource.getMessage("binding.result.template"), COMPANY_ATTR
                ), bindingResult);
            }
        }

        return EDITOR_COMPANY_PAGE;
    }

    @PostMapping("/saveCompany")
    public String saveCompany(
        @Valid @ModelAttribute(COMPANY_ATTR) Company company,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(COMPANY_ATTR, company);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_ERRORS_ATTR, bindingResult);
            return REDIRECT_EDITOR_SHOW_COMPANY_PAGE;
        }

        if (companyService.processUpdateTransactional(company)) {
            return REDIRECT_EDITOR_COMPANIES;
        } else {
            redirectAttributes.addFlashAttribute(COMPANY_ATTR, company);
            redirectAttributes.addFlashAttribute(
                "errorUniqueNameText",
                "Компания с таким наименованием уже существует"
            );
            return REDIRECT_EDITOR_SHOW_COMPANY_PAGE;
        }

    }
}
