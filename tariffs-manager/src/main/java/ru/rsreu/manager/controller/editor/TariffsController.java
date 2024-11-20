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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.message.MessagePropertiesSource;
import ru.rsreu.manager.service.implementation.CompanyService;
import ru.rsreu.manager.service.implementation.TariffService;

@Controller
@RequestMapping("/editor")
public class TariffsController {

    private final TariffService tariffService;

    private final CompanyService companyService;

    private final MessagePropertiesSource messagePropertiesSource;

    public TariffsController(
        TariffService tariffService,
        CompanyService companyService,
        MessagePropertiesSource messagePropertiesSource
    ) {
        this.tariffService = tariffService;
        this.companyService = companyService;
        this.messagePropertiesSource = messagePropertiesSource;
    }

    @RequestMapping({"", "/tariffs"})
    public String showTariffsPage(Model model) {
        model.addAttribute("tariffs", tariffService.findAll());
        return "/editor/tariffs";
    }

    @GetMapping("/showEditTariffPage")
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
                    messagePropertiesSource.getMessage("binding.result.template"), "tariff"
                ), bindingResult);
            }
        }
        model.addAttribute("companies", companyService.findAll());
        return "/editor/tariffPage";
    }

    @PostMapping("/saveTariff")
    public String saveTariff(
        @Valid @ModelAttribute("tariff") Tariff tariff,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("tariff", tariff);
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            return "redirect:/editor/showTariffPage";
        }

        if (tariffService.processUpdateTransactional(tariff)) {
            return "redirect:/editor/tariffs";
        } else {
            redirectAttributes.addFlashAttribute("tariff", tariff);
            redirectAttributes.addFlashAttribute(
                "errorUniqueNameText",
                "Тариф с таким наименованием, компанией и способом создания уже существует"
            );
            return "redirect:/editor/showTariffPage";
        }

    }
}
