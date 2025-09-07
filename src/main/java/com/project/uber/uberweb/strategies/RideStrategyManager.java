package com.project.uber.uberweb.strategies;


import com.project.uber.uberweb.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.project.uber.uberweb.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.project.uber.uberweb.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import com.project.uber.uberweb.strategies.impl.RiderFareDefaultFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {


    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RiderFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(BigDecimal riderRating) {
        if (riderRating.compareTo(new BigDecimal("4.8")) >= 0) {
            return highestRatedDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy() {

//        6PM to 9PM is SURGE TIME
        LocalTime surgeStartTime = LocalTime.of(18, 0);
        LocalTime surgeEndTime = LocalTime.of(21, 0);
        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if (isSurgeTime) {
            return surgePricingFareCalculationStrategy;
        } else {
            return defaultFareCalculationStrategy;
        }
    }

}
