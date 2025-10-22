package com.project.uber.uberweb.dto;


import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDto {

    private Long id;
    private UserDto user;
    private BigDecimal rating;
}
