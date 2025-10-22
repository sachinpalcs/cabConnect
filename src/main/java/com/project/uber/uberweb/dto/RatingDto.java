package com.project.uber.uberweb.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingDto {
    private Long rideId;
    private BigDecimal rating;
}
