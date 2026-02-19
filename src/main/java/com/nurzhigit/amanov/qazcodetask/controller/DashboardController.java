package com.nurzhigit.amanov.qazcodetask.controller;

import com.nurzhigit.amanov.qazcodetask.entity.RequestLog;
import com.nurzhigit.amanov.qazcodetask.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final LogService logService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<RequestLog> top = logService.getTop10ByDuration();
        model.addAttribute("logs", top);
        return "dashboard";
    }
}

