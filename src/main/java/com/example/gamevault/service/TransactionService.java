package com.example.gamevault.service;

import com.example.gamevault.exception.ReservationTransactionNotFoundException;
import com.example.gamevault.model.*;
import com.example.gamevault.repository.CancellationRepository;
import com.example.gamevault.repository.PurchaseRepository;
import com.example.gamevault.repository.ReservationRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CancellationRepository cancellationRepository;

    public Purchase createPurchaseTransaction(Gamer gamer, VideoGame videoGame, int quantity) {
        double videoGameCost = videoGame.getCredits();
        double totalCost = videoGameCost * (double) quantity;
        logger.info("Currently at successfulPurchase method. Gamer: {}, VideoGame: {}, Quantity: {}, TotalCost: {}", gamer, videoGame, quantity, totalCost);
        Purchase purchase = new Purchase(videoGame.getTitle(), videoGame.getCreator(), quantity, totalCost, gamer);
        purchaseRepository.save(purchase);
        logger.info("Created and saved Purchase ({}) for Gamer: {}", purchase.toString(), gamer.toString());
        return purchase;
    }

    public Reservation createReservationTransaction(Gamer gamer, VideoGame videoGame, int quantity) {
        double totalCost = videoGame.getCredits() * (double) quantity;
        logger.info("Currently at successfulReservation method. Gamer: {}, VideoGame: {}, Quantity: {}, TotalCost: {}", gamer, videoGame, quantity, totalCost);
        Reservation reservation = new Reservation(videoGame.getTitle(), videoGame.getCreator(), quantity, totalCost, gamer);
        reservationRepository.save(reservation);
        logger.info("Created and saved Reservation ({}) for Gamer: {}", reservation, gamer.toString());
        return reservation;
    }

    public Reservation getReservationTransaction(long id) throws ReservationTransactionNotFoundException {
        logger.info("Currently at getReservationTransaction method, finding Reservation with id {}", id);
        if (reservationRepository.findById(id).isPresent()) {
            Reservation reservation = reservationRepository.findById(id).get();
            logger.info("Found Reservation: {}", reservation.toString());
            return reservation;
        }
        throw new ReservationTransactionNotFoundException();
    }

    public Cancellation createCancelTransaction(Gamer gamer, Reservation reservation) {
        logger.info("Currently at createCancelTransaction method. Cancellation for Gamer ({}) involving Reservation: {}", reservation.toString(), gamer.toString());
        Cancellation cancellation = new Cancellation(
                reservation.getTitle(),
                reservation.getCreator(),
                reservation.getQuantity(),
                reservation.getCost(),
                reservation.getCreditsPaid(),
                reservation.getCreditsToPay(),
                reservation.getLatestPurchaseDate(),
                gamer);
        saveCancelTransaction(cancellation);
        logger.info("Created Cancellation, saved into CancellationRepository: {}", cancellation.toString());
        return cancellation;
    }

    public void saveCancelTransaction(Cancellation cancellation) {
        logger.info("Saving Cancellation ({}) into CancellationRepository.", cancellation.toString());
        cancellationRepository.save(cancellation);
    }

    public void deleteReservationTransaction(Reservation reservation) {
        logger.info("Deleting Reservation ({}) from ReservationRepository.", reservation.toString());
        reservationRepository.delete(reservation);
        logger.info("Deleted aforementioned Reservation from ReservationRepository.");
    }

}
