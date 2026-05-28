package com.bikeridediary.domain.bike;

import com.bikeridediary.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bikes")
@RequiredArgsConstructor
public class BikeController {

    private final BikeService bikeService;

    /**
     * GET /api/v1/bikes
     * Get all bikes for the authenticated user
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BikeResponse>>> getMyBikes(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        List<BikeResponse> bikes = bikeService.getMyBikes(userId);
        return ResponseEntity.ok(ApiResponse.ok(bikes));
    }

    /**
     * GET /api/v1/bikes/{bikeId}
     * Get a specific bike by ID
     */
    @GetMapping("/{bikeId}")
    public ResponseEntity<ApiResponse<BikeResponse>> getBike(
            @PathVariable UUID bikeId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BikeResponse bike = bikeService.getBike(bikeId, userId);
        return ResponseEntity.ok(ApiResponse.ok(bike));
    }

    /**
     * POST /api/v1/bikes
     * Create a new bike
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BikeResponse>> createBike(
            @Valid @RequestBody BikeCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BikeResponse bike = bikeService.createBike(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(bike));
    }

    /**
     * PUT /api/v1/bikes/{bikeId}
     * Update an existing bike
     */
    @PutMapping("/{bikeId}")
    public ResponseEntity<ApiResponse<BikeResponse>> updateBike(
            @PathVariable UUID bikeId,
            @Valid @RequestBody BikeUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BikeResponse bike = bikeService.updateBike(bikeId, request, userId);
        return ResponseEntity.ok(ApiResponse.ok(bike));
    }

    /**
     * DELETE /api/v1/bikes/{bikeId}
     * Soft delete a bike
     */
    @DeleteMapping("/{bikeId}")
    public ResponseEntity<ApiResponse<Void>> deleteBike(
            @PathVariable UUID bikeId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        bikeService.deleteBike(bikeId, userId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * PATCH /api/v1/bikes/{bikeId}/representative
     * Set a bike as the representative bike
     */
    @PatchMapping("/{bikeId}/representative")
    public ResponseEntity<ApiResponse<BikeResponse>> setRepresentative(
            @PathVariable UUID bikeId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        BikeResponse bike = bikeService.setRepresentative(bikeId, userId);
        return ResponseEntity.ok(ApiResponse.ok(bike));
    }
}
