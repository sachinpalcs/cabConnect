package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RideDto;
import com.project.uber.uberweb.dto.RideRequestDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RideDto cancelRide(Long rideId);


    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RideDto> getAllMyRides(PageRequest pageRequest);


    Rider createNewRider(User user);

    Rider getCurrentRider();
}
