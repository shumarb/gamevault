package com.example.gamevault.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseTest {

    @Test
    void createPurchaseTransaction_success() {
        Gamer gamer = new Gamer("Ali Hassan", "alihassan", "ali_hassan@gmail.com", "QQaa123!");
        Transaction purchaseTransaction = new Purchase("FIFA 19", "EA Sports", 2, 20, gamer);
        assertEquals(purchaseTransaction.getTitle(), "FIFA 19");
        assertEquals(purchaseTransaction.getCreator(), "EA Sports");
        assertEquals(purchaseTransaction.getQuantity(), 2);
        assertEquals(purchaseTransaction.getCost(), 20);
    }

}
