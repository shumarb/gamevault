/**
 * Gamer class for user of the application purchasing, reserving video games, and cancelling these reservations.
 * This class extends from {@link Person} class, with these additional attributes:
 * - Purchase history
 * - Reservation history
 * - Cancellation history
 * - Total Credits
 */
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

    /**
     * The list of purchases made by a gamer, comprising {@link Purchase} entities when a Gamer buys one or more {@link VideoGame}.
     * This list is fetched eagerly and cascades all operations.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<Purchase> purchaseHistory;

    /**
     * The list of reservations made by a gamer, comprising {@link Reservation} entities when a Gamer reserves one or more {@link VideoGame}.
     * This list is fetched eagerly and cascades all operations.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<Reservation> reservationHistory;

    /**
     * The list of cancellations made by a gamer, comprising {@link Cancellation} entities when a Gamer cancels one or more {@link Reservation}.
     * This list is fetched eagerly and cascades all operations.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gamer")
    private List<Cancellation> cancellationHistory;

    /**
     * The total credits of a Gamer in his/her account.
     * Set to a default value of 100 upon instantiation.
     */
    private double totalCredits;

    /**
     * Constructor to instantiate a Gamer.
     * Initialises the lists of purchase, reservation, and cancellation histories.
     * Total credits is set to a default value of 100.
     *
     * @param name      The name of the Gamer.
     * @param username  The username of the Gamer.
     * @param email     The email address of the Gamer.
     * @param password  The password of the Gamer.
     */
    public Gamer(String name, String username, String email, String password) {
        super(name, username, email, password);
        purchaseHistory = new ArrayList<>();
        reservationHistory = new ArrayList<>();
        cancellationHistory = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalCredits = Double.parseDouble(decimalFormat.format(100)); // Default number of credits
    }

    /**
     * Returns the role of the {@link Person}.
     * @return The role of the Gamer.
     */
    @Override
    public String getRole() {
        return "GAMER";
    }

    /**
     * Adds a {@link Purchase} entity to a Gamer's purchase history at the start of the list.
     * @param purchase The {@link Purchase} to add.
     */
    public void addPurchaseTransaction(Purchase purchase) {
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        purchaseHistory.add(0, purchase);
    }

    /**
     * Adds a {@link Cancellation} entity to a Gamer's cancellation history at the start of the list.
     * @param cancellation The {@link Cancellation} to add.
     */
    public void addCancelTransaction(Cancellation cancellation) {
        if (cancellationHistory == null) {
            cancellationHistory = new ArrayList<>();
        }
        cancellationHistory.add(0, cancellation);
    }

    /**
     * Adds a {@link Reservation} entity to a Gamer's reservation history at the start of the list.
     * @param reservation The {@link Cancellation} to add.
     */
    public void addReservationTransaction(Reservation reservation) {
        if (reservationHistory == null) {
            reservationHistory = new ArrayList<>();
        }
        reservationHistory.add(0, reservation);
    }

    /**
     * Removes a {@link Reservation} entity from a Gamer's reservation history.
     * @param reservation The {@link Cancellation} to remove.
     */
    public void removeReservationTransaction(Reservation reservation) {
        reservationHistory.remove(reservation);
    }

    /**
     * Returns a String representation of a Gamer.
     * @return A string representation of a Gamer.
     */
    @Override
    public String toString() {
        return "Gamer{" +
                ", totalCredits=" + totalCredits +
                '}';
    }

}
