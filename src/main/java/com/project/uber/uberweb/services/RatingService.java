package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Ride;

import java.math.BigDecimal;

public interface RatingService {

    DriverDto rateDriver(Ride ride, BigDecimal rating);

    RiderDto rateRider(Ride ride, BigDecimal rating);

    void createNewRating(Ride ride);
}
