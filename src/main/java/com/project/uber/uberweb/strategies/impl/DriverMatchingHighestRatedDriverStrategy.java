package com.project.uber.uberweb.strategies.impl;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.repositories.DriverRepository;
import com.project.uber.uberweb.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional()
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {


    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickUpLocation());

    }
}
