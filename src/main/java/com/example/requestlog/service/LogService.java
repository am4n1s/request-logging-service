package com.example.requestlog.service;

import com.example.requestlog.dto.LogRequestDto;
import com.example.requestlog.entity.RequestLog;
import com.example.requestlog.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogService {

    private final RequestLogRepository repository;

    public List<RequestLog> getLogs(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public List<RequestLog> getLogsByUser(String userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    /** Кэшируется на 10 секунд (см. CacheConfig), поэтому значение может слегка отставать. */
    @Cacheable("logCount")
    public long getCount() {
        return repository.count();
    }

    public List<RequestLog> getTop10ByDuration() {
        return repository.findTop10ByOrderByDurationDesc();
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<RequestLog>> getLogsAsync(Pageable pageable) {
        return CompletableFuture.completedFuture(getLogs(pageable));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<RequestLog>> getLogsByUserAsync(String userId, Pageable pageable) {
        return CompletableFuture.completedFuture(getLogsByUser(userId, pageable));
    }

    @Async("logExecutor")
    @Transactional
    public void saveAsync(RequestLog log) {
        repository.save(log);
    }

    @Transactional
    public void createLog(LogRequestDto dto) {
        RequestLog log = new RequestLog();
        log.setPath(dto.path());
        log.setMethod(dto.method());
        log.setStatus(dto.status());
        log.setDuration(dto.duration());
        log.setUserId(dto.userId());
        repository.save(log);
    }
}
