package com.project.uber.uberweb.strategies.impl;

import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.services.DistanceService;
import com.project.uber.uberweb.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;
    private static final BigDecimal SURGE_FACTOR = new BigDecimal("2");

    @Override
    public BigDecimal calculateFare(RideRequest rideRequest) {

        BigDecimal distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),
                rideRequest.getDropOffLocation());
        return distance.multiply(RIDE_FARE_MULTIPLIER).multiply(SURGE_FACTOR);
    }
}
