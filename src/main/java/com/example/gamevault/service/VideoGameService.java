package com.example.gamevault.service;

import com.example.gamevault.exception.InsufficientVideoGameQuantityException;
import com.example.gamevault.exception.VideoGameNotFoundException;
import com.example.gamevault.model.ReservationTransaction;
import com.example.gamevault.model.VideoGame;
import com.example.gamevault.repository.VideoGameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoGameService {
    private static final Logger logger = LogManager.getLogger(VideoGameService.class);

    @Autowired
    private VideoGameRepository videoGameRepository;

    public List<VideoGame> getAllVideoGames() {
        logger.info("Retrieving all video games in the database.");
        return videoGameRepository.findAll();
    }

    public VideoGame getVideoGame(long id) {
        logger.info("Search attempt of VideoGame object with id {}", id);
        return videoGameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found."));
    }

    public boolean hasSufficientQuantityForTransaction(VideoGame videoGame, int quantity) throws InsufficientVideoGameQuantityException {
        logger.info("Check if Video Game has sufficient quantity for a transaction.");
        logger.info("VideoGame: {}, Quantity: {}", videoGame.toString(), quantity);
        if (videoGame.getQuantity() >= quantity) {
            logger.info("Video Game has sufficient quantity for transaction.");
            return true;
        }
        logger.error("Video Game has insufficient quantity for transaction.");
        throw new InsufficientVideoGameQuantityException();
    }

    public void updateVideoGameQuantity(VideoGame videoGame, int quantity, String transactionType) {
        logger.info("Updating VideoGame ({}) for TransactionType ({}) involving Quantity ({})", videoGame.toString(), quantity, transactionType);
        int updatedQuantity;
        if (transactionType.equals("purchase") || transactionType.equals("reservation")) {
            updatedQuantity = videoGame.getQuantity() - quantity;
            if (updatedQuantity == 0) {
                logger.info("Removing VideoGame({}) because its quantity after transaction is 0.", videoGame.toString());
                videoGameRepository.delete(videoGame);
            } else {
                logger.info("Transaction Type ({}): Decrease VideoGame quantity by {}", transactionType, quantity);
                videoGame.setQuantity(videoGame.getQuantity() - quantity);
                logger.info("Updated VideoGame: {}", videoGame);
                videoGameRepository.save(videoGame);
            }
        }
    }

    public double getVideoGameCost(Long gameId) {
        VideoGame videoGame = getVideoGame(gameId);
        return videoGame.getCredits();
    }

    public VideoGame getVideoGame(String title) throws VideoGameNotFoundException {
        logger.info("Currently at getVideoGame method. Title: {}", title);
        Optional<VideoGame> videoGameOptional = videoGameRepository.findByTitle(title);
        if (videoGameOptional.isPresent()) {
            VideoGame videoGame = videoGameOptional.get();
            logger.info("Found VideoGame with title {}: {}", videoGame.toString(), title);
            return videoGame;
        }
        throw new VideoGameNotFoundException();
    }

    public void increaseVideoGameQuantity(ReservationTransaction reservationTransaction) throws VideoGameNotFoundException {
        String title = reservationTransaction.getTitle();
        int quantity = reservationTransaction.getQuantity();
        logger.info("Currently at increaseVideoGameQuality method. Title: {}, Quantity: {}", title, quantity);
        logger.info("Finding VideoGame with title: {}", title);
        if (videoGameRepository.findByTitle(title).isEmpty()) {
            logger.info("Video game not in catalogue. Create one and add it in.");
            VideoGame videoGame = new VideoGame(title, reservationTransaction.getCreator(), reservationTransaction.getQuantity(), reservationTransaction.getCost());
            logger.info("Created and saved videoGame: {}", videoGame.toString());
            videoGameRepository.save(videoGame);

        } else {
            VideoGame videoGame = getVideoGame(title);
            logger.info("Increasing quantity. Before: {}.", videoGame.toString());
            int currentQuantity = videoGame.getQuantity();
            int updatedQuantity = currentQuantity + quantity;
            videoGame.setQuantity(updatedQuantity);
            logger.info("Increased quantity. After: {}.", videoGame.toString());
        }

    }
}
