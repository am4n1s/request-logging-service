package com.nurzhigit.amanov.qazcodetask.controller;

import com.nurzhigit.amanov.qazcodetask.dto.LogRequestDto;
import com.nurzhigit.amanov.qazcodetask.entity.RequestLog;
import com.nurzhigit.amanov.qazcodetask.service.LogService;
import com.nurzhigit.amanov.qazcodetask.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final MetricService metricService;

    @GetMapping
    public List<RequestLog> getAll() {
        return logService.getLogs();
    }

    @GetMapping("/user/{userId}")
    public List<RequestLog> getByUser(@PathVariable String userId) {
        return logService.getLogsByUser(userId);
    }

    @GetMapping("/count")
    public long count() {
        return logService.getCount();
    }
    @GetMapping("/async")
    public CompletableFuture<List<RequestLog>> getAsyncLogs() {
        return logService.getLogsAsync();
    }
    @GetMapping("/async/user/{userId}")
    public CompletableFuture<List<RequestLog>> getAsyncUser(@PathVariable String userId) {
        return logService.getLogsByUserAsync(userId);
    }
    @GetMapping("/metrics/latency")
    public Map<String, Double> getLatency() {
        return metricService.getLatencyStats();
    }
    @PostMapping("/log")
    @PreAuthorize("hasRole('ADMIN')")
    public void create(@Valid @RequestBody LogRequestDto dto) {
        logService.createLog(dto);
    }

}
