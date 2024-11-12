package com.example.gamevault.configuration;

import com.example.gamevault.exception.*;
import com.example.gamevault.model.Gamer;
import com.example.gamevault.model.Person;
import com.example.gamevault.model.VideoGame;
import com.example.gamevault.repository.GamerRepository;
import com.example.gamevault.repository.VideoGameRepository;
import com.example.gamevault.service.GamerService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitialStartupDataPopulation {
    private static final Logger logger = LogManager.getLogger(InitialStartupDataPopulation.class);

    @Autowired
    private VideoGameRepository videoGameRepository;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private GamerService gamerService;

    @PostConstruct
    public void initialize() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        initializeGamers();
        initializeVideoGames();
    }

    @PostConstruct
    private void initializeGamers() throws InvalidNameException, InvalidEmailAddressException, UnavailableEmailAddressException, InvalidPasswordException, InvalidUsernameException, UnavailableUsernameException {
        if (gamerRepository.count() == 0) {
            List<Gamer> initialGamers = new ArrayList<>();
            Person gamer1 = gamerService.register("Sam Tan", "samtan95", "samtan@gmail.com", "ZZZzzz12");
            Person gamer2 = gamerService.register("Ali Hassan", "alihassan1", "alihassan@gmail.com", "ZZZzzz12");
            initialGamers.add((Gamer) gamer1);
            initialGamers.add((Gamer) gamer2);
            gamerRepository.saveAll(initialGamers);
            logger.info("2 gamers created at start of application if there are no gamers at start of application.");
            logger.info("{}", gamer1.toString());
            logger.info("{}", gamer2.toString());
        }
    }

    @PostConstruct
    private void initializeVideoGames() {
        if (videoGameRepository.count() == 0) {
            List<VideoGame> initialVideoGames = new ArrayList<> ();
            VideoGame videoGame1 = new VideoGame("FIFA 20", "EA Sports", 15, 20);
            VideoGame videoGame2 = new VideoGame("Pro Evolution Soccer 2019", "Konami", 12, 10);
            VideoGame videoGame3 = new VideoGame("Spider-man 2", "Treyarch", 14, 4);
            VideoGame videoGame4 = new VideoGame("WWE 2K23", "Visual Concepts", 1, 10);
            initialVideoGames.add(videoGame1);
            initialVideoGames.add(videoGame2);
            initialVideoGames.add(videoGame3);
            initialVideoGames.add(videoGame4);
            videoGameRepository.saveAll(initialVideoGames);
            logger.info("4 video games created if there are no video games at start of application.");
            logger.info("{}", videoGame1.toString());
            logger.info("{}", videoGame2.toString());
            logger.info("{}", videoGame3.toString());
            logger.info("{}", videoGame4.toString());
        }
    }

}
