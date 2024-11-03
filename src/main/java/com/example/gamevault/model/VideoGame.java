package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video_games")
@NoArgsConstructor
@Getter
public class VideoGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;
    private String companyName;
    private int yearOfPublication;
    private int quantity;
    private int credits;

    public VideoGame(String title, String companyName, int yearOfPublication, int quantity, int credits) {
        this.title = title;
        this.companyName = companyName;
        this.yearOfPublication = yearOfPublication;
        this.quantity = quantity;
        this.credits = credits;
    }

    public void decreaseQuantityByOne() {
        quantity--;
    }

    public void increaseQuantityByOne() {
        quantity++;
    }

}
