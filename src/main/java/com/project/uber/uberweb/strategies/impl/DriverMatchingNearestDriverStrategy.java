package com.project.uber.uberweb.strategies.impl;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.repositories.DriverRepository;
import com.project.uber.uberweb.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        return driverRepository.findTenNearestDrivers(rideRequest.getPickUpLocation());
    }
}
