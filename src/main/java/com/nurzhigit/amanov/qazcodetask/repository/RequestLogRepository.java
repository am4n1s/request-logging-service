package com.nurzhigit.amanov.qazcodetask.repository;

import com.nurzhigit.amanov.qazcodetask.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    List<RequestLog> findByUserId(String userId);

    @Query("SELECT COUNT(r) FROM RequestLog r")
    long countAllLogs();
}

