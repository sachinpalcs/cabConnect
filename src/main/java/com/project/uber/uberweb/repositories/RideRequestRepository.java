package com.project.uber.uberweb.repositories;

import com.project.uber.uberweb.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
}
