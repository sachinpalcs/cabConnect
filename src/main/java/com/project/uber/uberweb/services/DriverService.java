package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.*;
import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface DriverService {

    RideDto acceptRide(Long rideRequestId);

    RideDto cancelRide(Long rideId);

    RideDto startRide(Long rideId, String otp);

    RideDto endRide(Long rideId);


    RiderDto rateRider(Long rideId, BigDecimal rating);

    DriverDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability(Driver driver, boolean available);

    Driver createNewDriver(Driver driver);

    DriverDto updateAvailability(Boolean available);

    DriverDto updateLocation(DriverLocationDto locationDto);

    List<RideRequestDto> getPendingRequests();

//    add new method to get ride details
    RideDto getRideDetails(Long rideId);
}
