package com.example.requestlog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "request_log", indexes = {
        @Index(name = "idx_request_log_user_id", columnList = "user_id"),
        @Index(name = "idx_request_log_duration", columnList = "duration")
})
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String path;

    @Column(length = 10)
    private String method;

    private int status;

    private long duration;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
