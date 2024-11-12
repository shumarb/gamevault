package com.example.gamevault.repository;

import com.example.gamevault.model.ReservationTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTransactionRepository extends JpaRepository<ReservationTransaction, Long> {

}
