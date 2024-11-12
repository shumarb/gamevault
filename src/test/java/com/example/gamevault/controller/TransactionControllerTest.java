package com.example.gamevault.controller;

import com.example.gamevault.exception.InsufficientCreditsForTransactionException;
import com.example.gamevault.exception.InsufficientVideoGameQuantityException;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.PurchaseTransaction;
import com.example.gamevault.model.ReservationTransaction;
import com.example.gamevault.model.VideoGame;
import com.example.gamevault.service.GamerService;
import com.example.gamevault.service.TransactionService;
import com.example.gamevault.service.VideoGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionControllerTest {

    @Mock
    private GamerService gamerService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private VideoGameService videoGameService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private TransactionController transactionController;

    private Gamer gamer;
    private VideoGame videoGame1;
    private VideoGame videoGame2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gamer = new Gamer("Syed Ali", "syedali123", "syedAli@gmail.com", "MMMaaa12");
        videoGame1 = new VideoGame("FIFA 20", "EA Sports", 4, 20);
        videoGame2 = new VideoGame("FIFA Street", "EA Sports", 2, 200);
    }

    @Test
    void buyVideoGame_unsuccessfulDueToInsufficientQuantity() throws InsufficientVideoGameQuantityException {
        int quantity = 15;
        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(1L)).thenReturn(videoGame1);
        when(videoGameService.hasSufficientQuantityForTransaction(videoGame1, quantity)).thenThrow(InsufficientVideoGameQuantityException.class);
        String result = transactionController.buyVideoGame(1L, quantity, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "Unsuccessful purchase - Insufficient video games available.");
        assertEquals("redirect:/gamer/home", result);
    }

    @Test
    void buyVideoGame_unsuccessfulDueToInsufficientCredits() throws InsufficientCreditsForTransactionException {
        int quantity = 1;
        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(2L)).thenReturn(videoGame2);
        when(gamerService.canAffordTransaction(gamer, videoGame2, quantity, "purchase")).thenThrow(InsufficientCreditsForTransactionException.class);
        String result = transactionController.buyVideoGame(2L, quantity, redirectAttributes);
        assertEquals("redirect:/gamer/home", result);
    }

    @Test
    void buyVideoGame_success() throws InsufficientVideoGameQuantityException, InsufficientCreditsForTransactionException {
        int quantity = 1;
        PurchaseTransaction purchaseTransaction = new PurchaseTransaction(videoGame2.getTitle(), videoGame2.getCreator(), 1, videoGame2.getCredits(), gamer);

        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(2L)).thenReturn(videoGame2);
        when(videoGameService.hasSufficientQuantityForTransaction(videoGame2, quantity)).thenReturn(true);
        when(gamerService.canAffordTransaction(gamer, videoGame2, quantity, "purchase")).thenReturn(true);
        when(transactionService.createPurchaseTransaction(gamer, videoGame2, quantity)).thenReturn(purchaseTransaction);
        doNothing().when(videoGameService).updateVideoGameQuantity(videoGame2, quantity, "purchase");
        doNothing().when(gamerService).deductCredits(gamer, videoGame2, quantity, "purchase");

        String result = transactionController.buyVideoGame(2L, quantity, redirectAttributes);
        verify(gamerService).getCurrentGamer();
        verify(videoGameService).getVideoGame(2L);
        verify(videoGameService).hasSufficientQuantityForTransaction(videoGame2, quantity);
        verify(gamerService).canAffordTransaction(gamer, videoGame2, quantity, "purchase");
        verify(videoGameService).updateVideoGameQuantity(videoGame2, quantity, "purchase");
        verify(transactionService).createPurchaseTransaction(gamer, videoGame2, quantity);
        verify(gamerService).deductCredits(gamer, videoGame2, quantity, "purchase");
        verify(gamerService).addPurchaseTransactionForGamer(gamer, purchaseTransaction);
        verify(redirectAttributes).addFlashAttribute("success", "Successful purchase.");
        assertEquals("redirect:/gamer/home", result);
    }

    @Test
    void reserveVideoGame_unsuccessfulDueToInsufficientQuantity() throws InsufficientVideoGameQuantityException {
        int quantity = 15;
        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(1L)).thenReturn(videoGame1);
        when(videoGameService.hasSufficientQuantityForTransaction(videoGame1, quantity)).thenThrow(InsufficientVideoGameQuantityException.class);
        String result = transactionController.reserveVideoGame(1L, quantity, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "Unsuccessful purchase - Insufficient video games available.");
        assertEquals("redirect:/gamer/home", result);
    }

    @Test
    void reserveVideoGame_unsuccessfulDueToInsufficientCredits() throws InsufficientCreditsForTransactionException {
        int quantity = 1;
        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(2L)).thenReturn(videoGame2);
        when(gamerService.canAffordTransaction(gamer, videoGame2, quantity, "purchase")).thenThrow(InsufficientCreditsForTransactionException.class);
        String result = transactionController.reserveVideoGame(2L, quantity, redirectAttributes);
        assertEquals("redirect:/gamer/home", result);
    }

    @Test
    void reserveVideoGame_success() throws InsufficientVideoGameQuantityException, InsufficientCreditsForTransactionException {
        int quantity = 1;
        ReservationTransaction reservationTransaction = new ReservationTransaction(videoGame2.getTitle(), videoGame2.getCreator(), 1, videoGame2.getCredits(), gamer);

        when(gamerService.getCurrentGamer()).thenReturn(gamer);
        when(videoGameService.getVideoGame(2L)).thenReturn(videoGame2);
        when(videoGameService.hasSufficientQuantityForTransaction(videoGame2, quantity)).thenReturn(true);
        when(gamerService.canAffordTransaction(gamer, videoGame2, quantity, "purchase")).thenReturn(true);
        when(transactionService.createReservationTransaction(gamer, videoGame2, quantity)).thenReturn(reservationTransaction);
        doNothing().when(videoGameService).updateVideoGameQuantity(videoGame2, quantity, "reservation");
        doNothing().when(gamerService).deductCredits(gamer, videoGame2, quantity, "reservation");

        String result = transactionController.reserveVideoGame(2L, quantity, redirectAttributes);
        verify(gamerService).getCurrentGamer();
        verify(videoGameService).getVideoGame(2L);
        verify(videoGameService).hasSufficientQuantityForTransaction(videoGame2, quantity);
        verify(gamerService).canAffordTransaction(gamer, videoGame2, quantity, "reservation");
        verify(videoGameService).updateVideoGameQuantity(videoGame2, quantity, "reservation");
        verify(transactionService).createReservationTransaction(gamer, videoGame2, quantity);
        verify(gamerService).deductCredits(gamer, videoGame2, quantity, "reservation");
        verify(gamerService).addReservationTransactionForGamer(gamer, reservationTransaction);
        verify(redirectAttributes).addFlashAttribute("success", "Successful reservation.");
        assertEquals("redirect:/gamer/home", result);
    }

}
