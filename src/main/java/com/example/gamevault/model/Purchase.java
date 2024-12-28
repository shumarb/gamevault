package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Purchase extends Transaction {
    public Purchase(String title, String creator, int quantityBought, double totalCost, Gamer gamer) {
        super(title, creator, quantityBought, totalCost, gamer);
    }

}
