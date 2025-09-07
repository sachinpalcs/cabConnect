package com.project.uber.uberweb.dto;

import com.project.uber.uberweb.entities.enums.PaymentMethod;
import com.project.uber.uberweb.entities.enums.RideStatues;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class RideDto {


    private Long id;
    private PointDto pickUpLocation;
    private PointDto dropOffLocation;

    private LocalDateTime createdTime;
    private RiderDto rider;
    private DriverDto driver;
    private PaymentMethod paymentMethod;

    private RideStatues rideStatues;

    private String otp;

    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
