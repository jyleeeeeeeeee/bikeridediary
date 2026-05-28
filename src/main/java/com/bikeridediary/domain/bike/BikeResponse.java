package com.bikeridediary.domain.bike;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for bike information.
 */
public record BikeResponse(
        UUID id,
        String manufacturerName,
        String modelName,
        Integer year,
        BikeCategory category,
        Integer totalMileageKm,
        boolean isRepresentative,
        LocalDate purchasedAt,
        String photoUrl,
        String memo,
        LocalDateTime createdAt
) {

    public static BikeResponse from(Bike bike) {
        return new BikeResponse(
                bike.getId(),
                bike.getManufacturerName(),
                bike.getModelName(),
                bike.getYear(),
                bike.getCategory(),
                bike.getTotalMileageKm(),
                bike.isRepresentative(),
                bike.getPurchasedAt(),
                bike.getPhotoUrl(),
                bike.getMemo(),
                bike.getCreatedAt()
        );
    }
}
