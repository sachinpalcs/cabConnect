package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Ride;

public interface RatingService {

    DriverDto rateDriver(Ride ride, Integer rating);

    RiderDto rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);
}
