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
    private List<Purchase> purchaseHistory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<Reservation> reservationHistory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<Cancellation> cancellationHistory;

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

    public void addPurchaseTransaction(Purchase purchase) {
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        purchaseHistory.add(0, purchase);
    }

    public void addCancelTransaction(Cancellation cancellation) {
        if (cancellationHistory == null) {
            cancellationHistory = new ArrayList<>();
        }
        cancellationHistory.add(0, cancellation);
    }

    public void addReservationTransaction(Reservation reservation) {
        if (reservationHistory == null) {
            reservationHistory = new ArrayList<>();
        }
        reservationHistory.add(0, reservation);
    }

    public void removeReservationTransaction(Reservation reservation) {
        reservationHistory.remove(reservation);
    }

    @Override
    public String toString() {
        return "Gamer{" +
                "purchases=" + purchaseHistory.size() +
                ", reservations=" + reservationHistory.size() +
                ", cancellations=" + cancellationHistory.size() +
                ", totalCredits=" + totalCredits +
                '}';
    }

}
