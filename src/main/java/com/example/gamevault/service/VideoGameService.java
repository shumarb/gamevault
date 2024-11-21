/**
 * Service class responsible for managing video game-related operations.
 * It interacts with the `VideoGameRepository` to persist and retrieve video games data and handles various exceptions related
 * to a transaction involving one more video games.
 */
package com.example.gamevault.service;

import com.example.gamevault.exception.InsufficientVideoGameQuantityException;
import com.example.gamevault.exception.UnavailableVideoGameException;
import com.example.gamevault.exception.VideoGameNotFoundException;
import com.example.gamevault.model.Reservation;
import com.example.gamevault.model.VideoGame;
import com.example.gamevault.repository.VideoGameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoGameService {

    /**
     * Logger to monitor operational flow and facilitate troubleshooting.
     */
    private static final Logger logger = LogManager.getLogger(VideoGameService.class);

    /**
     * {@link VideoGameRepository} for managing {@link VideoGame} entities.
     */
    @Autowired
    private VideoGameRepository videoGameRepository;

    /**
     * Gets a video game by its identification number (id).
     * @param id The identification number of the video game.
     * @return {@link VideoGame} with specified identified id
     * @throws UnavailableVideoGameException if id of video game cannot be found in Video Game Repository.
     */
    public VideoGame getVideoGame(long id) throws UnavailableVideoGameException {
        logger.info("Search attempt of VideoGame object with id {}", id);
        VideoGame videoGame;
        Optional<VideoGame> videoGameOptional = videoGameRepository.findById(id);
        if (videoGameOptional.isPresent()) {
            videoGame = videoGameOptional.get();
            logger.info("Found, and returning VideoGame with id {}: {}", id, videoGame.toString());
        }
        logger.error("No VideoGame with id of {} found.", id);
        throw new UnavailableVideoGameException();
    }

    /**
     * Checks if specified quantity of video game is available for a transaction.
     * @param videoGame The video game involved in a transaction.
     * @param quantity  The quantity involved in a transaction.
     * @return {@code true} if the quantity of video game is available for a transaction.
     * @throws InsufficientVideoGameQuantityException if the specified quantity of video game is unavailable for a transaction.
     */
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

    /**
     * Updates a video game's quantity by the specified quantity after a transaction.
     * @param videoGame The video game involved in a transaction.
     * @param quantity  The quantity of video game involved in a transaction.
     * @param transactionType The type of transaction (purchase/reservation).
     */
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

    public void increaseVideoGameQuantity(Reservation reservation) throws VideoGameNotFoundException {
        String title = reservation.getTitle();
        int quantity = reservation.getQuantity();
        logger.info("Currently at increaseVideoGameQuality method. Title: {}, Quantity: {}", title, quantity);
        logger.info("Finding VideoGame with title: {}", title);
        if (videoGameRepository.findByTitle(title).isEmpty()) {
            logger.info("Video game not in catalogue. Create one and add it in.");
            VideoGame videoGame = new VideoGame(title, reservation.getCreator(), reservation.getQuantity(), reservation.getCost());
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
