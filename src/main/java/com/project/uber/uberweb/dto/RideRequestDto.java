package com.project.uber.uberweb.dto;


import com.project.uber.uberweb.entities.enums.PaymentMethod;
import com.project.uber.uberweb.entities.enums.RideRequestStatues;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RideRequestDto {

    private Long id;


    private PointDto pickUpLocation;
    private PointDto dropOffLocation;
    private LocalDateTime requestedTime;

    private RiderDto rider;

    private Double fare;
    private PaymentMethod paymentMethod;
    private RideRequestStatues rideRequestStatues;
}
