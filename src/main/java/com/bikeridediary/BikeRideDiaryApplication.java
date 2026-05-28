package com.bikeridediary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing          // createdAt, updatedAt 자동 관리
@EnableScheduling           // 소모품 알림 배치 스케줄러
public class BikeRideDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeRideDiaryApplication.class, args);
    }
}
