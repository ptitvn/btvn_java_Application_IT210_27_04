package com.example.todo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
public class SessionController {

    private final MessageSource messageSource;

    public SessionController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/welcome")
    public String showWelcomeForm(HttpSession session) {
        if (session.getAttribute("ownerName") != null) {
            return "redirect:/";
        }
        return "welcome";
    }

    @PostMapping("/welcome")
    public String saveOwner(@RequestParam String ownerName,
                            HttpSession session,
                            Model model,
                            Locale locale) {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            String error = messageSource.getMessage("error.owner.empty", null, locale);
            model.addAttribute("error", error);
            return "welcome";
        }
        session.setAttribute("ownerName", ownerName.trim());
        return "redirect:/";
    }
}