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
    public Purchase(long gamerId, long videoGameId, int quantity, double totalCost) {
        super(gamerId, videoGameId, quantity, totalCost);
    }

}
