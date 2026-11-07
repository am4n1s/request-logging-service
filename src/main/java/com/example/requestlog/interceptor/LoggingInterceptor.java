package com.example.requestlog.interceptor;

import com.example.requestlog.entity.RequestLog;
import com.example.requestlog.service.LogService;
import com.example.requestlog.service.MetricService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = LoggingInterceptor.class.getName() + ".startTime";

    private final LogService logService;
    private final MetricService metricService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // для async-запросов preHandle вызывается повторно: не затираем исходное время старта
        if (request.getAttribute(START_TIME) == null) {
            request.setAttribute(START_TIME, System.nanoTime());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Object start = request.getAttribute(START_TIME);
        long duration = start instanceof Long s
                ? TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - s)
                : 0;
        int status = response.getStatus();
        if (ex != null && status < 400) {
            status = 500;
        }

        // метрики обновляем в первую очередь: сбой записи в БД их не затрагивает
        metricService.incrementRequest();
        if (status >= 400) {
            metricService.incrementError();
        }
        metricService.recordDuration(duration);

        try {
            RequestLog entry = new RequestLog();
            entry.setPath(request.getRequestURI());
            entry.setMethod(request.getMethod());
            entry.setStatus(status);
            entry.setDuration(duration);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                entry.setUserId(auth.getName());
            }
            logService.saveAsync(entry);
        } catch (Exception e) {
            log.warn("Failed to log request {} {}", request.getMethod(), request.getRequestURI(), e);
        }
    }
}
