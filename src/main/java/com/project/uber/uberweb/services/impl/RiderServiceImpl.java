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
import com.project.uber.uberweb.services.DriverService;
import com.project.uber.uberweb.services.RatingService;
import com.project.uber.uberweb.services.RideService;
import com.project.uber.uberweb.services.RiderService;
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

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatues(RideRequestStatues.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(BigDecimal.valueOf(fare));

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> drivers = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);
        return modelMapper.map(savedRideRequest, RideRequestDto.class);
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

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
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

}
