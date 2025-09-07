package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.entities.Payment;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.enums.PaymentStatues;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.repositories.PaymentRepository;
import com.project.uber.uberweb.services.PaymentService;
import com.project.uber.uberweb.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;


    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride with id: " + ride.getId()));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatues(PaymentStatues.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatues status) {
        payment.setPaymentStatues(status);
        paymentRepository.save(payment);
    }
}
