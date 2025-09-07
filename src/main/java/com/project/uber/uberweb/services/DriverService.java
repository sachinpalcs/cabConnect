package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RideDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

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
}
