package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@Getter
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long gamerId;

    private long videoGameId;

    private int totalQuantity;

    private double totalCost;

    private String transactionDateTime;

    public Transaction(long gamerId, long videoGameId, int totalQuantity, double totalCost) {
        this.gamerId = gamerId;
        this.videoGameId = videoGameId;
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.transactionDateTime = dateTimeFormatter.format(localDateTime);
    }

}
