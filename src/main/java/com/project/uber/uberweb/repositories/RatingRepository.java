package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Rating;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRider(Rider rider);

    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}
