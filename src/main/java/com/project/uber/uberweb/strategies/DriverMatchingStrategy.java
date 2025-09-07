package com.project.uber.uberweb.strategies;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
