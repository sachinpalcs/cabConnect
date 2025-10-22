package com.project.uber.uberweb.entities;

import com.project.uber.uberweb.entities.enums.PaymentMethod;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "idx_ride_request_rider", columnList = "rider_id")
        })
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point pickUpLocation;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point dropOffLocation;


    @CreationTimestamp
    private LocalDateTime requestedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideRequestStatues rideRequestStatues;

    private BigDecimal fare;

    // ADD other necessary fields and relationships as needed
    @ManyToMany
    @JoinTable(
            name = "ride_request_potential_drivers",
            joinColumns = @JoinColumn(name = "ride_request_id"),
            inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<Driver> potentialDrivers;
}
