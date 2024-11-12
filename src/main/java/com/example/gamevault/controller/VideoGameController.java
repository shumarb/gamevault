package com.example.gamevault.controller;

import com.example.gamevault.model.VideoGame;
import com.example.gamevault.repository.VideoGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class VideoGameController {

    @Autowired
    private VideoGameRepository videoGameRepository;

    public List<VideoGame> getAllVideoGames() {
        return videoGameRepository.findAll();
    }

}
