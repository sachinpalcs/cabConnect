package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.RiderDto;
import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Rating;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.exceptions.RuntimeConflictException;
import com.project.uber.uberweb.repositories.DriverRepository;
import com.project.uber.uberweb.repositories.RatingRepository;
import com.project.uber.uberweb.repositories.RiderRepository;
import com.project.uber.uberweb.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;


    @Override
    public DriverDto rateDriver(Ride ride, BigDecimal rating) {
        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getId()));

        if (ratingObj.getDriverRating() != null)
            throw new RuntimeConflictException("Driver has already been rated, cannot rate again");

        ratingObj.setDriverRating(rating);
        ratingRepository.save(ratingObj);

//        BigDecimal newRating = ratingRepository.findByDriver(driver)
//                .stream()
//                .mapToDouble(Rating::getDriverRating)
//                .average().orElse(0.0);

        List<BigDecimal> ratings = ratingRepository.findByDriver(driver)
                .stream()
                .map(Rating::getDriverRating)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal newRating = ratings.isEmpty() ? BigDecimal.ZERO :
                ratings.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);

        driver.setRating(newRating);

        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, BigDecimal rating) {
        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for ride with id: " + ride.getId()));
        if (ratingObj.getRiderRating() != null)
            throw new RuntimeConflictException("Rider has already been rated, cannot rate again");

        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);

//        BigDecimal newRating = ratingRepository.findByRider(rider)
//                .stream()
//                .mapToDouble(Rating::getRiderRating)
//                .average().orElse(0.0);

        List<BigDecimal> ratings = ratingRepository.findByRider(rider)
                .stream()
                .map(Rating::getRiderRating)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal newRating = ratings.isEmpty() ? BigDecimal.ZERO :
                ratings.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);


        rider.setRating(newRating);

        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
