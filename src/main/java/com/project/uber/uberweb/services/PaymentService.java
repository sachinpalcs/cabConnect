package com.project.uber.uberweb.services;

import com.project.uber.uberweb.entities.Payment;
import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.enums.PaymentStatues;


public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatues status);

}
