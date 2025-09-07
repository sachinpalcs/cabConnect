package com.project.uber.uberweb.services;

import com.project.uber.uberweb.dto.DriverDto;
import com.project.uber.uberweb.dto.SignupDto;
import com.project.uber.uberweb.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    String refreshToken(String refreshToken);
}
