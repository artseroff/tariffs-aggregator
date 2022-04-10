package ru.rsreu.serov.tariffs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FrontController {

    List<String> listLogins = new ArrayList<>();

    @RequestMapping("/")
    public String init() {

        return "index";
    }
/*
    // в command передаем path
    @RequestMapping("/**")
    public String callCommand(HttpServletRequest request) {

        return command(request);
    }

    public String command(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (path.equals("/addLogin")) {
            String login = request.getParameter("login");
            listLogins.add(login);
            return "redirect:/greeting";
        } else if (path.equals("/greeting")) {
            request.setAttribute("listLogins", listLogins);
            return "greeting";
        } else {
            return "third";
        }
    }*/

    @RequestMapping("/addLogin")
    public String addLogin(@RequestParam(value="login", required=false, defaultValue="login") String login, HttpServletRequest request) {
        listLogins.add(login);
        return "redirect:/greeting";
    }

    @RequestMapping("/greeting")
    public String toGreeting(Model model, HttpServletRequest request) {
        String s1 = request.getRequestURI();
        model.addAttribute("listLogins", listLogins);

        return "/greeting";
    }

    @RequestMapping("/third")
    public String toThird( String name, Model model, HttpServletRequest  request) {

        return "third";
    }

}
