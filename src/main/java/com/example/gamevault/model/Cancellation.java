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
public class Cancellation extends Transaction {
    private String latestPurchaseDate;
    private double creditsPaid;
    private double creditsToPay;
    private String dateOfCancellation;
    private String reasonOfCancellation = "Manual Cancellation";

    public Cancellation(Reservation reservation) {
        super(reservation.getGamerId(), reservation.getVideoGameId(), reservation.getTotalQuantity(), reservation.getTotalCost());
        this.creditsPaid = reservation.getCreditsPaid();
        this.creditsToPay = reservation.getCreditsToPay();
        this.latestPurchaseDate = reservation.getLatestPurchaseDate();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.dateOfCancellation = dateTimeFormatter.format(localDateTime);
    }

}
