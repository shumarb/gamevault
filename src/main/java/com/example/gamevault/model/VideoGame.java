package com.example.gamevault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "video_games")
@NoArgsConstructor
@Getter
@ToString
public class VideoGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String title;

    private String creator;

    @Setter
    private int quantity;

    private double credits;

    public VideoGame(String title, String creator, int quantity, double credits) {
        this.title = title;
        this.creator = creator;
        this.quantity = quantity;
        this.credits = credits;
    }

}
