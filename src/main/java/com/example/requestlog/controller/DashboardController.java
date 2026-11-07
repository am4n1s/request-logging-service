package com.example.requestlog.controller;

import com.example.requestlog.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final LogService logService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("logs", logService.getTop10ByDuration());
        return "dashboard";
    }
}
