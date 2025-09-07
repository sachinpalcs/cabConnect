package com.project.uber.uberweb.strategies;


import com.project.uber.uberweb.entities.enums.PaymentMethod;
import com.project.uber.uberweb.strategies.impl.CashPaymentStrategy;
import com.project.uber.uberweb.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }
}
