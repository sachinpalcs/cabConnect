package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RideDto;
import com.project.uber.uberweb.dto.RideRequestDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);


    RideRequestDto cancelRideRequest(Long rideRequestId);


    DriverDto rateDriver(Long rideId, BigDecimal rating);

    RideDto getRideDetails(Long rideId);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);


    Rider createNewRider(User user);

    Rider getCurrentRider();

//    new method to rideRequests of rider
    RideRequestDto getRideRequestDetails(Long rideRequestId);
}
