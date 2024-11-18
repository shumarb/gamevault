package com.example.gamevault.repository;

import com.example.gamevault.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseTransactionRepository extends JpaRepository<Purchase, Long> {

}
