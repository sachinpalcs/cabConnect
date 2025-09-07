package com.project.uber.uberweb.services;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.enums.RideStatues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride, RideStatues rideStatues);

    Page<Ride> getAllRidesOfRider(Rider rideId, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driverId, PageRequest pageRequest);

}

