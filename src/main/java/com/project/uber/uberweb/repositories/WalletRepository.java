package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.User;
import com.project.uber.uberweb.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);

}
