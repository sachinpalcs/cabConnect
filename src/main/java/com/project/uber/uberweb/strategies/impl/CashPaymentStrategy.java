package com.project.uber.uberweb.strategies.impl;

import com.project.uber.uberweb.entities.Driver;
import com.project.uber.uberweb.entities.Payment;
import com.project.uber.uberweb.entities.enums.PaymentStatues;
import com.project.uber.uberweb.entities.enums.TransactionMethod;
import com.project.uber.uberweb.repositories.PaymentRepository;
import com.project.uber.uberweb.services.WalletService;
import com.project.uber.uberweb.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//Rider -> 100
//Driver -> 70 Deduct 30Rs from Driver's wallet

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {


    private final WalletService walletService;
    private final PaymentRepository paymentRepository;


    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        BigDecimal platformCommission = payment.getAmount().multiply(PLATFORM_COMMISSION);

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatues(PaymentStatues.CONFIRMED);
        paymentRepository.save(payment);
    }
}


//10 ratingsCount -> 4.0
//new rating 4.6
//updated rating
//new rating 44.6/11 -> 4.05
