package com.example.gamevault.repository;

import com.example.gamevault.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTransactionRepository extends JpaRepository<Reservation, Long> {

}
