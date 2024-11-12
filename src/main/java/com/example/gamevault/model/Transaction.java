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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gamer_id")
    private Gamer gamer;

    private String title;

    private String creator;

    private int quantity;

    private double cost;

    private String transactionDateTime;

    public Transaction(String title, String creator, int quantity, double cost, Gamer gamer) {
        this.title = title;
        this.creator = creator;
        this.quantity = quantity;
        this.cost = cost;
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.transactionDateTime = dateTimeFormatter.format(localDateTime);
        this.gamer = gamer;
    }

}
