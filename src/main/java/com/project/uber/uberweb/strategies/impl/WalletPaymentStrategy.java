package com.project.uber.uberweb.strategies.impl;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Payment;
import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.enums.PaymentStatues;
import com.project.uber.uberweb.entities.enums.TransactionMethod;
import com.project.uber.uberweb.repositories.PaymentRepository;
import com.project.uber.uberweb.services.WalletService;
import com.project.uber.uberweb.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


//Rider had 232, Driver had 500
//Ride cost is 100, commission = 30
//Rider -> 232-100 = 132
//Driver -> 500 + (100 - 30) = 570

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {


    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        BigDecimal driversCut = payment.getAmount().subtract(BigDecimal.ONE.subtract(PLATFORM_COMMISSION));

        walletService.addMoneyToWallet(driver.getUser(),
                driversCut, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatues(PaymentStatues.CONFIRMED);
        paymentRepository.save(payment);
    }
}
