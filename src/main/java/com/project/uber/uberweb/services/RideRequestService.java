package com.project.uber.uberweb.services;

import com.project.uber.uberweb.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
