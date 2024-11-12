package com.example.gamevault.service;

import com.example.gamevault.exception.ReservationTransactionNotFoundException;
import com.example.gamevault.model.*;
import com.example.gamevault.repository.CancelTransactionRepository;
import com.example.gamevault.repository.PurchaseTransactionRepository;
import com.example.gamevault.repository.ReservationTransactionRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    @Autowired
    private PurchaseTransactionRepository purchaseTransactionRepository;

    @Autowired
    private ReservationTransactionRepository reservationTransactionRepository;

    @Autowired
    private CancelTransactionRepository cancelTransactionRepository;

    public PurchaseTransaction createPurchaseTransaction(Gamer gamer, VideoGame videoGame, int quantity) {
        double videoGameCost = videoGame.getCredits();
        double totalCost = videoGameCost * (double) quantity;
        logger.info("Currently at successfulPurchase method. Gamer: {}, VideoGame: {}, Quantity: {}, TotalCost: {}", gamer, videoGame, quantity, totalCost);
        PurchaseTransaction purchaseTransaction = new PurchaseTransaction(videoGame.getTitle(), videoGame.getCreator(), quantity, totalCost, gamer);
        purchaseTransactionRepository.save(purchaseTransaction);
        logger.info("Created and saved PurchaseTransaction ({}) for Gamer: {}", purchaseTransaction.toString(), gamer.toString());
        return purchaseTransaction;
    }

    public ReservationTransaction createReservationTransaction(Gamer gamer, VideoGame videoGame, int quantity) {
        double totalCost = videoGame.getCredits() * (double) quantity;
        logger.info("Currently at successfulReservation method. Gamer: {}, VideoGame: {}, Quantity: {}, TotalCost: {}", gamer, videoGame, quantity, totalCost);
        ReservationTransaction reservationTransaction = new ReservationTransaction(videoGame.getTitle(), videoGame.getCreator(), quantity, totalCost, gamer);
        reservationTransactionRepository.save(reservationTransaction);
        logger.info("Created and saved ReservationTransaction ({}) for Gamer: {}", reservationTransaction, gamer.toString());
        return reservationTransaction;
    }

    public ReservationTransaction getReservationTransaction(long id) throws ReservationTransactionNotFoundException {
        logger.info("Currently at getReservationTransaction method, finding ReservationTransaction with id {}", id);
        if (reservationTransactionRepository.findById(id).isPresent()) {
            ReservationTransaction reservationTransaction = reservationTransactionRepository.findById(id).get();
            logger.info("Found ReservationTransaction: {}", reservationTransaction.toString());
            return reservationTransaction;
        }
        throw new ReservationTransactionNotFoundException();
    }

    public CancelTransaction createCancelTransaction(Gamer gamer, ReservationTransaction reservationTransaction) {
        logger.info("Currently at createCancelTransaction method. CancelTransaction for Gamer ({}) involving ReservationTransaction: {}", reservationTransaction.toString(), gamer.toString());
        CancelTransaction cancelTransaction = new CancelTransaction(
                reservationTransaction.getTitle(),
                reservationTransaction.getCreator(),
                reservationTransaction.getQuantity(),
                reservationTransaction.getCost(),
                reservationTransaction.getCreditsPaid(),
                reservationTransaction.getCreditsToPay(),
                reservationTransaction.getLatestPurchaseDate(),
                gamer);
        saveCancelTransaction(cancelTransaction);
        logger.info("Created CancelTransaction, saved into CancelTransactionRepository: {}", cancelTransaction.toString());
        return cancelTransaction;
    }

    public void saveCancelTransaction(CancelTransaction cancelTransaction) {
        logger.info("Saving CancelTransaction ({}) into CancelTransactionRepository.", cancelTransaction.toString());
        cancelTransactionRepository.save(cancelTransaction);
    }

    public void deleteReservationTransaction(ReservationTransaction reservationTransaction) {
        logger.info("Deleting ReservationTransaction ({}) from ReservationTransactionRepository.", reservationTransaction.toString());
        reservationTransactionRepository.delete(reservationTransaction);
        logger.info("Deleted aforementioned ReservationTransaction from ReservationTransactionRepository.");
    }

}
