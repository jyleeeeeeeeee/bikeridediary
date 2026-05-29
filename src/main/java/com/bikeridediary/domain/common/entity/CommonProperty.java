package com.bikeridediary.domain.common.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class CommonProperty {

    // 등록 일시
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // 삭제 일시 (소프트 삭제)
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
