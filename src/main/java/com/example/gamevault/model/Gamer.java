package com.example.gamevault.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gamer")
public class Gamer extends Person {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "gamer_id")
    private List<VideoGame> reservedVideoGames;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "gamer_id")
    private List<VideoGame> reservedPurchases;

    private int totalCredits;

    public Gamer(String name, String username, String email, String password) {
        super(name, username, email, password);
        reservedVideoGames = new ArrayList<>();
        totalCredits = 100;
    }

    @Override
    public String getRole() {
        return "GAMER";
    }

    public void addToReservedVideoGames(VideoGame videoGame) {
        reservedVideoGames.add(videoGame);
    }

    public void removeFromReservedVideoGames(VideoGame videoGame) {
        reservedVideoGames.remove(videoGame);
    }

    public void addToPurchasedVideoGames(VideoGame videoGame) {
        reservedVideoGames.add(videoGame);
    }

    public void removeFromPurchasedVideoGames(VideoGame videoGame) {
        reservedVideoGames.remove(videoGame);
    }

    public void increaseCredits(int amount) {
        totalCredits += amount;
    }

    public void decreaseCredits(int amount) {
        totalCredits -= amount;
    }

}
