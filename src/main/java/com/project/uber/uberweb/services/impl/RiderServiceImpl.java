package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RideDto;
import com.project.uber.uberweb.dto.RideRequestDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.*;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import com.project.uber.uberweb.entities.enums.RideStatues;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.repositories.RideRequestRepository;
import com.project.uber.uberweb.repositories.RiderRepository;
import com.project.uber.uberweb.services.*;
import com.project.uber.uberweb.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;
    private final RideRequestService rideRequestService;

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatues(RideRequestStatues.PENDING);
        rideRequest.setRider(rider);

        BigDecimal fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        //ADD FOR DRIVER MATCH
        savedRideRequest.setPotentialDrivers(drivers);
        RideRequest updatedRideRequest = rideRequestRepository.save(savedRideRequest);
        return modelMapper.map(updatedRideRequest, RideRequestDto.class);


//        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }


    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if (!rider.equals(ride.getRider())) {
            throw new RuntimeException(("Rider does not own this ride with id: " + rideId));
        }

        if (!ride.getRideStatues().equals(RideStatues.CONFIRMED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatues());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatues.CANCELLED);
        driverService.updateDriverAvailability(ride.getDriver(), true);

        // ADD OR DRIVER MATCH
//        rideRequest.getPotentialDrivers().clear();

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RideRequestDto cancelRideRequest(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        if (rideRequest.getRideRequestStatues().equals(RideRequestStatues.CANCELLED)) {
            throw new RuntimeException("RideRequest is already cancelled, status is " + rideRequest.getRideRequestStatues());
        }

        if (rideRequest.getRideRequestStatues().equals(RideRequestStatues.CONFIRMED)) {
            throw new RuntimeException("RideRequest is confirmed, It can cancel by cancelRide status is " + rideRequest.getRideRequestStatues());
        }

        rideRequest.setRideRequestStatues(RideRequestStatues.CANCELLED);
        rideRequest.getPotentialDrivers().clear();
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, BigDecimal rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if (!rider.equals(ride.getRider())) {
            throw new RuntimeException("Rider is not the owner of this Ride");
        }

        if (!ride.getRideStatues().equals(RideStatues.ENDED)) {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: " + ride.getRideStatues());
        }

        return ratingService.rateDriver(ride, rating);
    }

    @Override
    public RideDto getRideDetails(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if (!rider.getId().equals(ride.getRider().getId())) {
            throw new RuntimeException("Rider is not the owner of this Ride");
        }

        return modelMapper.map(ride, RideDto.class);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();
        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(
                ride -> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(BigDecimal.valueOf(0.0))
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(
                "Rider not associated with user with id: " + user.getId()
        ));
    }

//    new method to rideRequests of rider
    @Override
    public RideRequestDto getRideRequestDetails(Long rideRequestId) {
        Rider rider = getCurrentRider();
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if (!rider.getId().equals(rideRequest.getRider().getId())) {
            throw new RuntimeException("Rider is not the owner of this Ride Request");
        }


        return modelMapper.map(rideRequest, RideRequestDto.class);
    }

}
