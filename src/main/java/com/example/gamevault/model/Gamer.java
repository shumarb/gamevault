package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "gamer")
@Getter
@Setter
public class Gamer extends Person {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<PurchaseTransaction> purchaseHistory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<ReservationTransaction> reservationHistory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<CancelTransaction> cancellationHistory;

    private double totalCredits;

    public Gamer(String name, String username, String email, String password) {
        super(name, username, email, password);
        purchaseHistory = new ArrayList<>();
        
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalCredits = Double.parseDouble(decimalFormat.format(100)); // Default number of credits
    }

    @Override
    public String getRole() {
        return "GAMER";
    }

    public void addPurchaseTransaction(PurchaseTransaction purchaseTransaction) {
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        purchaseHistory.add(0, purchaseTransaction);
    }

    public void addCancelTransaction(CancelTransaction cancelTransaction) {
        if (cancellationHistory == null) {
            cancellationHistory = new ArrayList<>();
        }
        cancellationHistory.add(0, cancelTransaction);
    }

    public void addReservationTransaction(ReservationTransaction reservationTransaction) {
        if (reservationHistory == null) {
            reservationHistory = new ArrayList<>();
        }
        reservationHistory.add(0, reservationTransaction);
    }

    public void removeReservationTransaction(ReservationTransaction reservationTransaction) {
        reservationHistory.remove(reservationTransaction);
    }

    @Override
    public String toString() {
        return "Gamer{" +
                "purchaseHistory=" + purchaseHistory +
                ", reservationHistory=" + reservationHistory +
                ", cancellationHistory=" + cancellationHistory +
                ", totalCredits=" + totalCredits +
                '}';
    }

}
