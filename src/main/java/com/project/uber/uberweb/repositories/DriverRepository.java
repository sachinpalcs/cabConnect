package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.RideRequest;
import com.project.uber.uberweb.entities.User;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ST_Distance(point1, point2)
// ST_DWithin(point1, 10000)
//
//@Repository
//public interface DriverRepository extends JpaRepository<Driver, Long> {
//
//    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
//            "FROM driver d " +
//            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
//            "ORDER BY distance " +
//            "LIMIT 10", nativeQuery = true)
//    List<Driver> findTenNearestDrivers(Point pickUpLocation);
//
//
//    @Query(value = "SELECT d.* " +
//            "FROM driver d " +
//            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 15000) " +
//            "ORDER BY d.rating DESC " +
//            "LIMIT 10", nativeQuery = true)
//    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);
//
//    Optional<Driver> findByUser(User user);
//}

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "ORDER BY distance " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(@Param("pickupLocation") Point pickUpLocation);

    @Query(value = "SELECT d.* " +
            "FROM driver d " +
            "WHERE d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 15000) " +
            "ORDER BY d.rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(@Param("pickupLocation") Point pickupLocation);

    Optional<Driver> findByUser(User user);


    // --- THIS IS THE NEW QUERY ---
    @Query(value = "SELECT rr.* FROM ride_request rr " +
            "JOIN ride_request_potential_drivers rrpd ON rr.id = rrpd.ride_request_id " +
            "WHERE rrpd.driver_id = :driverId AND rr.ride_request_statues = :status",
            nativeQuery = true)
    List<RideRequest> findPendingRideRequestsForDriver(
            @Param("driverId") Long driverId,
            @Param("status") String status
    );

//    @Query("SELECT rr FROM RideRequest rr JOIN rr.potentialDrivers d WHERE d.id = :driverId AND rr.rideRequestStatues = :status")
//    List<RideRequest> findPendingRideRequestsForDriver(@Param("driverId") Long driverId, @Param("status") RideRequestStatues status);
}