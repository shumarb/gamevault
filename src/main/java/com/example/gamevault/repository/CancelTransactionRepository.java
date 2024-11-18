package com.example.gamevault.repository;

import com.example.gamevault.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelTransactionRepository extends JpaRepository<Cancellation, Long> {

}
