package com.example.gamevault.controller;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.*;
import com.example.gamevault.service.GamerService;
import com.example.gamevault.service.TransactionService;
import com.example.gamevault.service.VideoGameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionController {
    private static final Logger logger = LogManager.getLogger(TransactionController.class);

    @Autowired
    private GamerService gamerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private VideoGameService videoGameService;

    @PostMapping("/gamer/buy")
    public String buyVideoGame(@RequestParam("gameId") Long gameId,
                               @RequestParam("quantity") int quantity,
                               RedirectAttributes redirectAttributes) {

        try {
            Gamer gamer = gamerService.getCurrentGamer();
            VideoGame videoGame = videoGameService.getVideoGame(gameId);
            logger.info("Purchase attempt. Gamer: {}, Video Game: {}, Quantity: {}", gamer.toString(), videoGame.toString(), quantity);
            if (videoGameService.hasSufficientQuantityForTransaction(videoGame, quantity) && gamerService.canAffordTransaction(gamer, videoGame, quantity, "purchase")) {
                videoGameService.updateVideoGameQuantity(videoGame, quantity,"purchase");
                Purchase purchase = transactionService.createPurchaseTransaction(gamer, videoGame, quantity);
                gamerService.deductCredits(gamer, videoGame, quantity, "purchase");
                gamerService.addPurchaseToGamerPurchaseHistory(gamer, purchase);
                logger.info("Successful purchase: {}. Redirection to Gamer Home page with success message displayed.", purchase.toString());
                redirectAttributes.addFlashAttribute("success", "Successful purchase.");
            }

        } catch (InsufficientVideoGameQuantityException e) {
            e.printStackTrace();
            logger.error("Unsuccessful purchase due to insufficient video game quantity. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful purchase - Insufficient video games available.");

        } catch (InsufficientCreditsForTransactionException e) {
            e.printStackTrace();
            logger.error("Unsuccessful purchase due to insufficient credits. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful purchase - Insufficient credits to purchase video games in quantity specified.");

        } catch (UnavailableVideoGame e) {
            e.printStackTrace();
            logger.error("Unsuccessful purchase due to unavailable video game. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful purchase - Video Game specified is unavailable.");

        }  catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Unsuccessful purchase due to unexpected error. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred. Please try again later.");
        }

        return "redirect:/gamer/home";
    }

    @PostMapping("/gamer/reserve")
    public String reserveVideoGame(@RequestParam("gameId") Long gameId,
                                   @RequestParam("quantity") int quantity,
                                   RedirectAttributes redirectAttributes) {

        try {
            Gamer gamer = gamerService.getCurrentGamer();
            VideoGame videoGame = videoGameService.getVideoGame(gameId);
            logger.info("Reservation attempt. Gamer: {}, Video Game: {}, Quantity: {}", gamer.toString(), videoGame.toString(), quantity);

            if (videoGameService.hasSufficientQuantityForTransaction(videoGame, quantity) && gamerService.canAffordTransaction(gamer, videoGame, quantity, "reservation")) {
                videoGameService.updateVideoGameQuantity(videoGame, quantity,"reservation");
                Reservation reservation = transactionService.createReservationTransaction(gamer, videoGame, quantity);
                gamerService.deductCredits(gamer, videoGame, quantity, "reservation");
                gamerService.addReservationToGamerReservationHistory(gamer, reservation);
                logger.info("Successful reservation: {}. Redirection to Gamer Home page with success message displayed.", reservation.toString());
                redirectAttributes.addFlashAttribute("success", "Successful reservation.");
            }

        } catch (InsufficientVideoGameQuantityException e) {
            e.printStackTrace();
            logger.error("Unsuccessful reservation due to insufficient video game quantity. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful reservation - Insufficient video games available.");

        } catch (InsufficientCreditsForTransactionException e) {
            e.printStackTrace();
            logger.error("Unsuccessful reservation due to insufficient credits. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful reservation - Insufficient credits to purchase video games in quantity specified.");

        } catch (UnavailableVideoGame e) {
            e.printStackTrace();
            logger.error("Unsuccessful reservation due to unavailable video game. Redirection to Gamer with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unsuccessful reservation - Video Game specified is unavailable.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Unsuccessful reservation due to unexpected error. Redirection to Gamer Home page with error message displayed.");
            redirectAttributes.addFlashAttribute("error", "Unexpected error occurred. Please try again later.");
        }

        return "redirect:/gamer/home";
    }

    @PostMapping("/gamer/reservations/cancel")
    public String cancelReservation(@RequestParam("reservationId") Long reservationId, Model model) {
        try {
            Gamer gamer = gamerService.getCurrentGamer();
            Reservation reservation = transactionService.getReservationTransaction(reservationId);
            logger.info("Cancellation of Reservation ({}) for Gamer ({})", gamer.toString(), reservation.toString());
            Cancellation cancellation = transactionService.createCancelTransaction(gamer, reservation);
            gamerService.addCancellationToGamerCancellationHistory(gamer, cancellation);
            gamerService.removeReservationFromGamerReservationHistory(gamer, reservation);
            videoGameService.increaseVideoGameQuantity(reservation);
            transactionService.deleteReservationTransaction(reservation);
            model.addAttribute("success", "Successful cancellation.");

        } catch (ReservationTransactionNotFoundException e) {
            e.printStackTrace();
            logger.error("Cannot find reservationTransaction. Cancellation voided.");
            model.addAttribute("error", "Order to cancel cannot be found.");

        } catch (VideoGameNotFoundException e) {
            e.printStackTrace();
            logger.error("Cannot find VideoGame to update. Cancellation voided.");
            model.addAttribute("error", "Video Game specified in order cannot be found.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Unexpected error occurred.");
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        }

        return "gamer-reservations";
    }

    @PostMapping("/gamer/reservations/buy")
    public String completePurchaseOfReservation(@RequestParam("reservationId") Long reservationTransactionId, Model model) {
        Gamer gamer = gamerService.getCurrentGamer();

        try {
            Reservation reservation = transactionService.getReservationTransaction(reservationTransactionId);
            logger.info("Purchase of Reservation ({}) for Gamer ({}).", reservation.toString(), gamer.toString());
            VideoGame videoGame = videoGameService.getVideoGame(reservation.getTitle());
            if (gamerService.canAffordTransaction(gamer, videoGame, reservation.getQuantity(), "complete purchase of reservation")) {
                Purchase purchase = transactionService.createPurchaseTransaction(gamer, videoGame, reservation.getQuantity());
                gamerService.deductCredits(gamer, videoGame, reservation.getQuantity(), "complete purchase of reservation");
                gamerService.addPurchaseToGamerPurchaseHistory(gamer, purchase);
                gamerService.removeReservationFromGamerReservationHistory(gamer, reservation);
                logger.info("Successful purchase of reservation game/games: {}. Redirection to Gamer Home page with success message displayed.", purchase.toString());
                model.addAttribute("success", "Successful purchase");
                model.addAttribute("purchases", gamer.getPurchaseHistory());
            }

        } catch (ReservationTransactionNotFoundException e) {
            e.printStackTrace();
            logger.error("Cannot find reservationTransaction. Purchase voided.");
            model.addAttribute("error", "Unsuccessful purchase - Reservation not found.");

        } catch (VideoGameNotFoundException e) {
            e.printStackTrace();
            logger.error("Cannot find VideoGame. Purchase voided.");
            model.addAttribute("error", "Unsuccessful purchase - Video Game in Reservation not found.");

        } catch (InsufficientCreditsForTransactionException e) {
            e.printStackTrace();
            logger.error("Gamer ({}) has insufficient credits to complete payment of Reservation. Purchase voided.", gamer.toString());
            model.addAttribute("error", "Unsuccessful purchase - Insufficient funds for payment of total payable credits.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal("Unexpected error occurred. Purchase voided.");
            model.addAttribute("error", "Unexpected error occurred. Please try again later.");
        }

        return "gamer-reservations";
    }

}
