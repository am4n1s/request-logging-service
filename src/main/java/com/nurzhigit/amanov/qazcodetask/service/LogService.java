package com.nurzhigit.amanov.qazcodetask.service;

import com.nurzhigit.amanov.qazcodetask.dto.LogRequestDto;
import com.nurzhigit.amanov.qazcodetask.entity.RequestLog;
import com.nurzhigit.amanov.qazcodetask.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final RequestLogRepository repository;

    public List<RequestLog> getLogs() {
        return repository.findAll();
    }

    public List<RequestLog> getLogsByUser(String userId) {
        return repository.findByUserId(userId);
    }

    @Cacheable("logCount")
    public long getCount() {
        return repository.count();
    }
    @Transactional
    @CacheEvict(value = "logCount", allEntries = true)
    public void save(RequestLog log) {
        repository.save(log);
    }
    @Async("logExecutor")
    @CacheEvict(value = "logCount", allEntries = true)
    public void saveAsync(RequestLog log) {
        repository.save(log);
    }
    @Async
    public CompletableFuture<List<RequestLog>> getLogsAsync() {
        List<RequestLog> logs = repository.findAll();
        return CompletableFuture.completedFuture(logs);
    }
    public List<RequestLog> getTop10ByDuration() {
        return repository
                .findAll(PageRequest.of(0, 10, Sort.by("duration").descending()))
                .getContent();
    }

    @Async("logExecutor")
    public CompletableFuture<List<RequestLog>> getLogsByUserAsync(String userId) {
        return CompletableFuture.completedFuture(repository.findByUserId(userId));
    }
    @Transactional
    @CacheEvict(value = "logCount", allEntries = true)
    public void createLog(LogRequestDto dto) {
        RequestLog log = new RequestLog();
        log.setPath(dto.getPath());
        log.setMethod(dto.getMethod());
        log.setStatus(dto.getStatus());
        log.setDuration(dto.getDuration());
        log.setUserId(dto.getUserId());
        repository.save(log);
    }

}
