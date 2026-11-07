package com.example.requestlog.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MetricService {

    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer requestTimer;

    public MetricService(MeterRegistry registry) {
        this.requestCounter = Counter.builder("http_requests_total")
                .description("Total HTTP requests")
                .register(registry);
        this.errorCounter = Counter.builder("http_requests_errors_total")
                .description("Total HTTP error requests")
                .register(registry);
        this.requestTimer = Timer.builder("http_request_duration")
                .description("HTTP request duration")
                .register(registry);
    }

    public void incrementRequest() {
        requestCounter.increment();
    }

    public void incrementError() {
        errorCounter.increment();
    }

    public void recordDuration(long durationInMillis) {
        requestTimer.record(durationInMillis, TimeUnit.MILLISECONDS);
    }

    public Map<String, Double> getLatencyStats() {
        return Map.of(
                "count", (double) requestTimer.count(),
                "mean", requestTimer.mean(TimeUnit.MILLISECONDS),
                "max", requestTimer.max(TimeUnit.MILLISECONDS));
    }
}
