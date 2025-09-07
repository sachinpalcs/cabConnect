package com.project.uber.uberweb.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDto {

    private Long id;
    private UserDto user;
    private Double rating;
}
