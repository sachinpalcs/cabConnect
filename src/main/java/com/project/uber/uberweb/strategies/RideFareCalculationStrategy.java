package com.project.uber.uberweb.strategies;


import com.project.uber.uberweb.entities.RideRequest;

import java.math.BigDecimal;


public interface RideFareCalculationStrategy {

    BigDecimal RIDE_FARE_MULTIPLIER = new BigDecimal("10");

    BigDecimal calculateFare(RideRequest rideRequest);
}
