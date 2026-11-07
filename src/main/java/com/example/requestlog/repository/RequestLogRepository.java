package com.example.requestlog.repository;

import com.example.requestlog.entity.RequestLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    List<RequestLog> findByUserId(String userId, Pageable pageable);

    List<RequestLog> findTop10ByOrderByDurationDesc();
}
