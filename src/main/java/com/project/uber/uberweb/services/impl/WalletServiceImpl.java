package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.entities.Ride;
import com.project.uber.uberweb.entities.User;
import com.project.uber.uberweb.entities.Wallet;
import com.project.uber.uberweb.entities.WalletTransaction;
import com.project.uber.uberweb.entities.enums.TransactionMethod;
import com.project.uber.uberweb.entities.enums.TransactionType;
import com.project.uber.uberweb.exceptions.ResourceNotFoundException;
import com.project.uber.uberweb.repositories.WalletRepository;
import com.project.uber.uberweb.services.WalletService;
import com.project.uber.uberweb.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, BigDecimal amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance().add(amount));

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, BigDecimal amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance().subtract(amount));

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

//        walletTransactionService.createNewWalletTransaction(walletTransaction);

        wallet.getTransactions().add(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return null;
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with id: " + user.getId()));
    }
}
