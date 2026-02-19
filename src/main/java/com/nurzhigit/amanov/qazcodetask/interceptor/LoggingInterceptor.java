package com.nurzhigit.amanov.qazcodetask.interceptor;

import com.nurzhigit.amanov.qazcodetask.entity.RequestLog;
import com.nurzhigit.amanov.qazcodetask.service.LogService;
import com.nurzhigit.amanov.qazcodetask.service.MetricService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private final LogService logService;
    private final MetricService metricService;
    public LoggingInterceptor(LogService logService, MetricService metricService) {
        this.logService = logService;
        this.metricService = metricService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    @Override
    public void afterCompletion (HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler,
                                 Exception ex) {
        long duration = 0;
        try {
            long startTime = (Long) request.getAttribute("startTime");
            duration = System.currentTimeMillis() - startTime;
            RequestLog log = new RequestLog();
            log.setPath(request.getRequestURI());
            log.setMethod(request.getMethod());
            log.setStatus(response.getStatus());
            log.setDuration(duration);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                log.setUserId(auth.getName());
            }
            logService.saveAsync(log);
        } catch (Exception e) {
            System.err.println("Failed to log request:" + e.getMessage());
        }
        metricService.incrementRequest();
        if (response.getStatus() >= 400) {
            metricService.incrementError();
        }
        metricService.recordDuration(duration);
    }
}
