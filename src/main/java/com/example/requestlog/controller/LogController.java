package com.example.requestlog.controller;

import com.example.requestlog.dto.LogRequestDto;
import com.example.requestlog.entity.RequestLog;
import com.example.requestlog.service.LogService;
import com.example.requestlog.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/** Доступ ко всем эндпоинтам ограничен ролью ADMIN в SecurityConfig. */
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final MetricService metricService;

    @GetMapping
    public List<RequestLog> getAll(
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return logService.getLogs(pageable);
    }

    @GetMapping("/user/{userId}")
    public List<RequestLog> getByUser(
            @PathVariable String userId,
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return logService.getLogsByUser(userId, pageable);
    }

    @GetMapping("/count")
    public long count() {
        return logService.getCount();
    }

    @GetMapping("/async")
    public CompletableFuture<List<RequestLog>> getAsyncLogs(
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return logService.getLogsAsync(pageable);
    }

    @GetMapping("/async/user/{userId}")
    public CompletableFuture<List<RequestLog>> getAsyncUser(
            @PathVariable String userId,
            @PageableDefault(size = 50, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return logService.getLogsByUserAsync(userId, pageable);
    }

    @GetMapping("/metrics/latency")
    public Map<String, Double> getLatency() {
        return metricService.getLatencyStats();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody LogRequestDto dto) {
        logService.createLog(dto);
    }
}
