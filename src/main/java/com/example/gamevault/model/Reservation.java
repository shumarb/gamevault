package com.example.gamevault.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reservation extends Transaction {
    private String latestPurchaseDate;
    private double creditsPaid;
    private double creditsToPay;

    public Reservation(long gamerId, long videoGameId, int totalQuantity, double reservationCost) {
        super(gamerId, videoGameId, totalQuantity, reservationCost);
        creditsPaid = roundToTwoDecimalPlaces(0.2 * reservationCost);
        creditsToPay = roundToTwoDecimalPlaces(reservationCost - creditsPaid);
        LocalDateTime transactionDateTime = LocalDateTime.parse(super.getTransactionDateTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime latestPurchaseDateTime = transactionDateTime.plusHours(48);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.latestPurchaseDate = dateTimeFormatter.format(latestPurchaseDateTime);
    }

    private double roundToTwoDecimalPlaces(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(value));
    }

}
