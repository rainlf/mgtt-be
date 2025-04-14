package com.rainlf.mgttbe.infra.db.dataobj;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "mgtt_api_monitor")
@Data
public class ApiMonitorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String uri;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "class_method", nullable = false, length = 255)
    private String classMethod;

    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "cost_time", nullable = false)
    private Integer costTime;

    @Column(name = "has_exception", nullable = false)
    private Boolean hasException = false;

    @Column(name = "exception_stack", columnDefinition = "LONGTEXT")
    private String exceptionStack;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
}