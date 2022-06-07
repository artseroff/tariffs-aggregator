package ru.rsreu.serov.tariffs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rsreu.serov.tariffs.entity.Tariff;
import ru.rsreu.serov.tariffs.message.MessagePropertiesSource;
import ru.rsreu.serov.tariffs.service.implementation.CompanyService;
import ru.rsreu.serov.tariffs.service.implementation.TariffService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    /*@GetMapping("/showEditTariffPageWithErrors")
    public String showEditTariffPageWithErrors(Model model, @ModelAttribute("tariff") Tariff tariff) {
        if (model.getAttribute("bindingResultErrors") != null) {
            BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResultErrors");
            model.addAttribute(String.format("%s.%s", messagePropertiesSource.getMessage("binding.result.template"), "tariff"), bindingResult);
        }
        *//*model.addAttribute("companies", companyService.findAll());*//*
        return "/editor/addTariffPage";
    }


    @PostMapping("/editTariff")
    public String saveEditedTariff(@Valid @ModelAttribute("tariff") Tariff tariff, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bindingResultErrors", bindingResult);
            redirectAttributes.addFlashAttribute("tariff", tariff);
            return "redirect:/editor/showEditTariffPageWithErrors";
        }

        if (tariffService.isNewNameOfTariffWithIdUnique(tariff.getName(), tariff.getId())) {
            tariffService.update(tariff);
            return "redirect:/editor/tariffs";
        } else {
            redirectAttributes.addFlashAttribute("tariff", tariff);
            redirectAttributes.addFlashAttribute("errorUniqueNameText", "Тариф с таким именем уже существует");
            return "redirect:/editor/showEditTariffPageWithErrors";
        }

    }*/

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
                model.addAttribute(String.format("%s.%s", messagePropertiesSource.getMessage("binding.result.template"), "tariff"), bindingResult);
            }
        }
        model.addAttribute("companies", companyService.findAll());
        return "/editor/tariffPage";
    }

    @PostMapping("/saveTariff")
    public String saveTariff(@Valid @ModelAttribute("tariff") Tariff tariff, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("errorUniqueNameText", "Тариф с таким именем уже существует");
            return "redirect:/editor/showTariffPage";
        }

    }

}
