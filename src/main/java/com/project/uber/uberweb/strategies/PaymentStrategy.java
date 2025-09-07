package com.project.uber.uberweb.strategies;

import com.project.uber.uberweb.entities.Payment;

import java.math.BigDecimal;

public interface PaymentStrategy {
    BigDecimal PLATFORM_COMMISSION = new BigDecimal("0.3");

    void processPayment(Payment payment);
}
