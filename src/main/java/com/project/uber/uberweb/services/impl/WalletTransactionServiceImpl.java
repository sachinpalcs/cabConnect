package com.project.uber.uberweb.services.impl;

import com.project.uber.uberweb.entities.WalletTransaction;
import com.project.uber.uberweb.repositories.WalletTransactionRepository;
import com.project.uber.uberweb.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {


    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);

    }
}
