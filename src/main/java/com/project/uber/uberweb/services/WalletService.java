package com.project.uber.uberweb.services;

import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.User;
import com.project.uber.uberweb.entities.Wallet;
import com.project.uber.uberweb.entities.enums.TransactionMethod;

import java.math.BigDecimal;

public interface WalletService {

    Wallet addMoneyToWallet(User user, BigDecimal amount,
                            String transactionId, Ride ride,
                            TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, BigDecimal amount,
                                 String transactionId, Ride ride,
                                 TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
