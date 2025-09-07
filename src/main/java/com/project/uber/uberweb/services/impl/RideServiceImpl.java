package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import com.project.uber.uberweb.entities.enums.RideStatues;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.repositories.RideRepository;
import com.project.uber.uberweb.services.RideRequestService;
import com.project.uber.uberweb.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {


    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;


    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {

        rideRequest.setRideRequestStatues(RideRequestStatues.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setRideStatues(RideStatues.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatues rideStatus) {

        ride.setRideStatues(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider, pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver, pageRequest);
    }


    private String generateRandomOTP() {
        Random random = new Random();
        int otpInt = random.nextInt(10000);  //0 to 9999
        return String.format("%04d", otpInt);
    }
}
