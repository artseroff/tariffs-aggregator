package ru.rsreu.manager.controller.editor;

import jakarta.annotation.PostConstruct;
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
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.CompanyService;
import ru.rsreu.manager.service.implementation.TariffService;

@Controller
@RequestMapping("/editor")
public class TariffsController {

    public static final String TARIFF_ID_PARAM = "tariffId";
    public static final String TARIFF_ATTR = "tariff";
    public static final String COMPANIES_ATTR = "companies";
    public static final String EDITOR_TARIFF_PAGE = "/editor/tariffPage";
    public static final String REDIRECT_EDITOR = "redirect:/editor";
    public static final String BINDING_RESULT_ERRORS_ATTR = "bindingResultErrors";
    public static final String REDIRECT_EDITOR_SHOW_TARIFF_PAGE = "redirect:/editor/showTariffPage";
    private final TariffService tariffService;
    private final CompanyService companyService;
    private final MessagePropertiesSource messagePropertiesSource;
    private String bindingResultErrorsTariffAttr;

    public TariffsController(
        TariffService tariffService,
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @PostConstruct
    public void initBindingResultErrorsTariffAttr() {
        bindingResultErrorsTariffAttr = String.format("%s.%s",
            messagePropertiesSource.getMessage("binding.result.template"), TARIFF_ATTR
        );
    }

    @GetMapping({"", "/tariffs"})
    public String showTariffsPage(Model model) {
        model.addAttribute("tariffs", tariffService.findAll());
        return "/editor/tariffs";
    }

    @GetMapping("/showEditTariffPage")
    public String showEditTariffPage(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter(TARIFF_ID_PARAM));
        Tariff tariff = tariffService.findById(id);
        model.addAttribute(TARIFF_ATTR, tariff);
        model.addAttribute(COMPANIES_ATTR, companyService.findAll());
        return EDITOR_TARIFF_PAGE;
    }

    @PostMapping("/deleteTariff")
    public String deleteTariff(HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter(TARIFF_ID_PARAM));
        tariffService.deleteById(id);
        return REDIRECT_EDITOR;
    }

    @GetMapping("/showTariffPage")
    public String showTariffPage(Model model, @ModelAttribute(TARIFF_ATTR) Tariff tariff) {
        if (tariff == null) {
            model.addAttribute(TARIFF_ATTR, new Tariff());
        } else {
            // Если метод открыт не на добавление или были ошибки биндинга
            if (model.getAttribute(BINDING_RESULT_ERRORS_ATTR) != null) {
                BindingResult bindingResult = (BindingResult) model.getAttribute(BINDING_RESULT_ERRORS_ATTR);
                model.addAttribute(bindingResultErrorsTariffAttr, bindingResult);
            }
        }
        model.addAttribute(COMPANIES_ATTR, companyService.findAll());
        return EDITOR_TARIFF_PAGE;
    }

    @PostMapping("/saveTariff")
    public String saveTariff(
        @Valid @ModelAttribute(TARIFF_ATTR) Tariff tariff,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(TARIFF_ATTR, tariff);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_ERRORS_ATTR, bindingResult);
            return REDIRECT_EDITOR_SHOW_TARIFF_PAGE;
        }

        if (tariffService.processUpdateTransactional(tariff)) {
            return REDIRECT_EDITOR;
        } else {
            redirectAttributes.addFlashAttribute(TARIFF_ATTR, tariff);
            redirectAttributes.addFlashAttribute(
                "errorUniqueNameText",
                "Тариф с таким наименованием, компанией и способом создания уже существует"
            );
            return REDIRECT_EDITOR_SHOW_TARIFF_PAGE;
        }

    }
}
