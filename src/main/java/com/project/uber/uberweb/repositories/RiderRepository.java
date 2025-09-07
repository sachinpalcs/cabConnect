package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.Rider;
import com.project.uber.uberweb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByUser(User user);
}
