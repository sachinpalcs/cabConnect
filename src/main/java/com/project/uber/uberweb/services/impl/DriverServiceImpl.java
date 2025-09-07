package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RideDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.entities.User;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import com.project.uber.uberweb.entities.enums.RideStatues;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.repositories.DriverRepository;
import com.project.uber.uberweb.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {


    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;


    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if (!rideRequest.getRideRequestStatues().equals(RideRequestStatues.PENDING)) {
            throw new RuntimeException("RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatues());
        }

        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

        Driver savedDriver = updateDriverAvailability(currentDriver, false);

        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {

        Ride ride = rideService.getRideById(rideId);

        Driver driver = getCurrentDriver();
        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if (!ride.getRideStatues().equals(RideStatues.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatues());
        }

        rideService.updateRideStatus(ride, RideStatues.CANCELLED);
        updateDriverAvailability(driver, true);

        return modelMapper.map(ride, RideDto.class);
    }


    @Override
    public RideDto startRide(Long rideId, String otp) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if (!ride.getRideStatues().equals(RideStatues.CONFIRMED)) {
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status: " + ride.getRideStatues());
        }

        if (!otp.equals(ride.getOtp())) {
            throw new RuntimeException("Otp is not valid, otp: " + otp);
        }

        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatues.ONGOING);

        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if (!ride.getRideStatues().equals(RideStatues.ONGOING)) {
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: " + ride.getRideStatues());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatues.ENDED);
        updateDriverAvailability(driver, true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, BigDecimal rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if (!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver is not the owner of this Ride");
        }

        if (!ride.getRideStatues().equals(RideStatues.ENDED)) {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: " + ride.getRideStatues());
        }

        return ratingService.rateRider(ride, rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }


    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not associated with user with " +
                        "id " + user.getId()));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
