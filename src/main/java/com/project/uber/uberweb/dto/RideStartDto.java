package com.project.uber.uberweb.dto;

import com.project.uber.uberweb.entities.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideStartDto {

    private String otp;
}
