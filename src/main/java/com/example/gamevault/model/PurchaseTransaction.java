package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PurchaseTransaction extends Transaction {
    public PurchaseTransaction(String title, String creator, int quantityBought, double totalCost, Gamer gamer) {
        super(title, creator, quantityBought, totalCost, gamer);
    }

}
