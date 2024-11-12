package com.example.gamevault.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CancelTransaction extends Transaction {
    private String latestPurchaseDate;
    private double creditsPaid;
    private double creditsToPay;
    private String dateOfCancellation;
    private String reasonOfCancellation = "Manual Cancellation";

    public CancelTransaction(String title, String creator, int quantity, double reservationCost, double creditsPaid, double creditsToPay, String latestPurchaseDate, Gamer gamer) {
        super(title, creator, quantity, reservationCost, gamer);
        this.creditsPaid = creditsPaid;
        this.creditsToPay = creditsToPay;
        this.latestPurchaseDate = latestPurchaseDate;
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.dateOfCancellation = dateTimeFormatter.format(localDateTime);
    }

}
