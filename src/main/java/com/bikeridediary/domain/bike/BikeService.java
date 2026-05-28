package com.bikeridediary.domain.bike;

import com.bikeridediary.domain.user.User;
import com.bikeridediary.domain.user.UserRepository;
import com.bikeridediary.global.exception.BusinessException;
import com.bikeridediary.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BikeService {

    private final BikeRepository bikeRepository;
    private final UserRepository userRepository;

    /**
     * Get all active bikes for the user, ordered by representative desc, then created date desc.
     */
    public List<BikeResponse> getMyBikes(UUID userId) {
        verifyUserExists(userId);
        return bikeRepository.findByUserIdAndDeletedAtIsNullOrderByIsRepresentativeDescCreatedAtDesc(userId)
                .stream()
                .map(BikeResponse::from)
                .toList();
    }

    /**
     * Get a specific bike by ID (must be owned by the user).
     */
    public BikeResponse getBike(UUID bikeId, UUID userId) {
        Bike bike = bikeRepository.findByIdAndDeletedAtIsNull(bikeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BIKE_NOT_FOUND));

        verifyBikeOwnership(bike, userId);
        return BikeResponse.from(bike);
    }

    /**
     * Create a new bike for the user.
     * If it's the first bike, automatically set it as representative.
     */
    @Transactional
    public BikeResponse createBike(BikeCreateRequest request, UUID userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Bike bike = Bike.create(
                user,
                request.manufacturerName(),
                request.modelName(),
                request.year(),
                request.category(),
                request.totalMileageKm()
        );

        // 첫 번째 바이크면 대표로 설정
        long bikeCount = bikeRepository.findByUserIdAndDeletedAtIsNullOrderByIsRepresentativeDescCreatedAtDesc(userId)
                .size();
        if (bikeCount == 0) {
            bike.setRepresentative(true);
        }

        Bike saved = bikeRepository.save(bike);
        return BikeResponse.from(saved);
    }

    /**
     * Update an existing bike.
     */
    @Transactional
    public BikeResponse updateBike(UUID bikeId, BikeUpdateRequest request, UUID userId) {
        Bike bike = bikeRepository.findByIdAndDeletedAtIsNull(bikeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BIKE_NOT_FOUND));

        verifyBikeOwnership(bike, userId);

        bike.update(
                request.manufacturerName(),
                request.modelName(),
                request.year(),
                request.category(),
                request.totalMileageKm(),
                request.purchasedAt(),
                request.memo()
        );

        return BikeResponse.from(bikeRepository.save(bike));
    }

    /**
     * Soft delete a bike (set deleted_at).
     */
    @Transactional
    public void deleteBike(UUID bikeId, UUID userId) {
        Bike bike = bikeRepository.findByIdAndDeletedAtIsNull(bikeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BIKE_NOT_FOUND));

        verifyBikeOwnership(bike, userId);

        // 대표 바이크면 플래그 해제
        if (bike.isRepresentative()) {
            bike.setRepresentative(false);
        }

        bike.delete();
        bikeRepository.save(bike);
    }

    /**
     * Set a bike as the representative bike.
     * Clears the representative flag from all other bikes of the user.
     */
    @Transactional
    public BikeResponse setRepresentative(UUID bikeId, UUID userId) {
        Bike bike = bikeRepository.findByIdAndDeletedAtIsNull(bikeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BIKE_NOT_FOUND));

        verifyBikeOwnership(bike, userId);

        bikeRepository.clearRepresentative(userId);
        bike.setRepresentative(true);

        return BikeResponse.from(bikeRepository.save(bike));
    }

    // ============ Helper methods ============

    private void verifyUserExists(UUID userId) {
        userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private void verifyBikeOwnership(Bike bike, UUID userId) {
        if (!bike.isOwner(userId)) {
            throw new BusinessException(ErrorCode.BIKE_ACCESS_DENIED);
        }
    }
}
