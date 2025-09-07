package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.Payment;
import com.project.uber.uberweb.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
