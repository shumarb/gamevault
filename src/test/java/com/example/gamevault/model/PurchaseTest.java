package com.example.gamevault.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseTest {

    @Test
    void createPurchaseTransaction_success() {
        long gamerId = 1L;
        long videoGameId = 2L;
        int totalQuantity = 2;
        double totalCost = 20;
        Transaction purchase = new Purchase(gamerId, videoGameId, totalQuantity, totalCost);
        assertEquals(2, purchase.getTotalQuantity());
        assertEquals(20, purchase.getTotalCost());
    }

}
